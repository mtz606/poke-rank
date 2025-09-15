package com.pokemon.rankings.service;

import com.pokemon.rankings.dto.CreateGroupRequest;
import com.pokemon.rankings.dto.GroupResponse;
import com.pokemon.rankings.dto.GroupDetailResponse;
import com.pokemon.rankings.entity.Group;
import com.pokemon.rankings.entity.GroupMember;
import com.pokemon.rankings.entity.User;
import com.pokemon.rankings.entity.UserCardCollection;
import com.pokemon.rankings.repository.GroupRepository;
import com.pokemon.rankings.repository.GroupMemberRepository;
import com.pokemon.rankings.repository.UserRepository;
import com.pokemon.rankings.repository.UserCardCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @Autowired
    private UserCardCollectionRepository userCardCollectionRepository;
    
    public GroupResponse createGroup(CreateGroupRequest request, String username) {
        try {
            System.out.println("[DEBUG] GroupService.createGroup called with username: " + username);
            
            // Find the user creating the group
            User owner = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("[DEBUG] Found user: " + owner.getUsername());
            
            // Check if group name already exists
            if (groupRepository.findByNameAndIsActiveTrue(request.getName()).isPresent()) {
                throw new RuntimeException("Group name already exists");
            }
            System.out.println("[DEBUG] Group name is available");
            
            // Create the group
            Group group = new Group(request.getName(), request.getDescription(), owner.getUserId());
            group.setGroupId(java.util.UUID.randomUUID().toString());
            System.out.println("[DEBUG] Created group with ID: " + group.getGroupId());
            
            try {
                group = groupRepository.save(group);
                System.out.println("[DEBUG] Group saved successfully");
            } catch (Exception e) {
                System.out.println("[DEBUG] Error saving group: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            
            // Add owner as member with OWNER role
            GroupMember ownerMember = new GroupMember(group.getGroupId(), owner.getUserId(), "OWNER");
            try {
                groupMemberRepository.save(ownerMember);
                System.out.println("[DEBUG] Owner member saved successfully");
            } catch (Exception e) {
                System.out.println("[DEBUG] Error saving owner member: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            
            return createGroupResponseWithMembers(group);
        } catch (Exception e) {
            System.out.println("[DEBUG] GroupService.createGroup failed with exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create group: " + e.getMessage(), e);
        }
    }

    // Helper to populate members in GroupResponse
    private GroupResponse createGroupResponseWithMembers(Group group) {
        GroupResponse response = new GroupResponse(group);
        // Get all members of the group
        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(group.getGroupId());
        // Convert GroupMember to UserSummary
        List<GroupResponse.UserSummary> members = groupMembers.stream()
                .map(member -> {
                    User user = userRepository.findById(member.getUserId()).orElse(null);
                    return user != null ? new GroupResponse.UserSummary(user) : null;
                })
                .filter(member -> member != null)
                .collect(Collectors.toList());
        response.setMembers(members);
        return response;
    }
    
    public List<GroupResponse> getMyGroups(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Find all group memberships for this user
        List<GroupMember> memberships = groupMemberRepository.findByUserId(user.getUserId());
        
        if (memberships.isEmpty()) {
            return List.of();
        }
        
        // Extract group IDs
        List<String> groupIds = memberships.stream()
                .map(GroupMember::getGroupId)
                .collect(Collectors.toList());
        
        // Batch get all groups
        List<Group> groups = groupIds.stream()
                .map(groupId -> groupRepository.findById(groupId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(group -> group.getIsActive())
                .collect(Collectors.toList());
        
        return groups.stream()
                .map(this::createGroupResponseWithMembers)
                .collect(Collectors.toList());
    }
    
    public List<GroupResponse> getAllGroups() {
        List<Group> groups = groupRepository.findByIsActiveTrue();
        return groups.stream()
                .map(this::createGroupResponseWithMembers)
                .collect(Collectors.toList());
    }
    
    public GroupDetailResponse getGroupById(String groupId) {
        System.out.println("[DEBUG] GroupService.getGroupById called with groupId: " + groupId);
        
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        System.out.println("[DEBUG] Found group: " + group.getName() + ", isActive: " + group.getIsActive());
        
        if (!group.getIsActive()) {
            throw new RuntimeException("Group is not active");
        }
        
        GroupResponse groupResponse = createGroupResponseWithMembers(group);
        List<GroupDetailResponse.MemberRanking> leaderboard = calculateLeaderboard(groupId);
        
        System.out.println("[DEBUG] Created GroupDetailResponse with leaderboard size: " + leaderboard.size());
        
        return new GroupDetailResponse(groupResponse, leaderboard);
    }
    
    private List<GroupDetailResponse.MemberRanking> calculateLeaderboard(String groupId) {
        try {
            // Get all members of the group
            List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
            
            List<GroupDetailResponse.MemberRanking> leaderboard = members.stream()
                    .map(member -> {
                        User user = userRepository.findById(member.getUserId()).orElse(null);
                        if (user == null) return null;
                        
                        // Get user's card collection
                        List<UserCardCollection> userCards;
                        try {
                            userCards = userCardCollectionRepository.findByUserId(user.getUserId());
                        } catch (Exception e) {
                            System.out.println("[DEBUG] UserCardCollection table not found, using empty list for user: " + user.getUsername());
                            userCards = new ArrayList<>();
                        }
                        
                        // Calculate total value and card count
                        double totalValue = 0.0;
                        int cardCount = 0;
                        
                        for (UserCardCollection card : userCards) {
                            // For now, we'll use a placeholder value since we don't have real card prices
                            // In a real implementation, you'd fetch card prices from an external API
                            double cardValue = 1.0; // Placeholder: $1 per card
                            totalValue += cardValue * card.getQuantity();
                            cardCount += card.getQuantity();
                        }
                        
                        return new GroupDetailResponse.MemberRanking(user.getUsername(), totalValue, cardCount, 0);
                    })
                    .filter(member -> member != null)
                    .sorted((a, b) -> Double.compare(b.getTotalValue(), a.getTotalValue())) // Sort by value descending
                    .collect(Collectors.toList());
            
            // Add ranks
            for (int i = 0; i < leaderboard.size(); i++) {
                leaderboard.get(i).setRank(i + 1);
            }
            
            return leaderboard;
        } catch (Exception e) {
            System.out.println("[DEBUG] Error calculating leaderboard: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<GroupResponse> searchGroups(String searchTerm) {
        List<Group> groups = groupRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(searchTerm);
        return groups.stream()
                .map(this::createGroupResponseWithMembers)
                .collect(Collectors.toList());
    }
    
    public void addMemberToGroup(String groupId, String username, String memberUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        User member = userRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        // Check if user is the owner of the group
        if (!group.getOwnerId().equals(user.getUserId())) {
            throw new RuntimeException("Only group owner can add members");
        }
        
        // Check if member is already in the group
        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, member.getUserId())) {
            throw new RuntimeException("User is already a member of this group");
        }
        
        // Add member to group
        GroupMember groupMember = new GroupMember(groupId, member.getUserId(), "MEMBER");
        groupMemberRepository.save(groupMember);
    }
    
    public void removeMemberFromGroup(String groupId, String username, String memberUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        User member = userRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        // Check if user is the owner of the group
        if (!group.getOwnerId().equals(user.getUserId())) {
            throw new RuntimeException("Only group owner can remove members");
        }
        
        // Check if member is in the group
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, member.getUserId())) {
            throw new RuntimeException("User is not a member of this group");
        }
        
        // Remove member from group
        groupMemberRepository.deleteByGroupIdAndUserId(groupId, member.getUserId());
    }
    
    public void deleteGroup(String groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user is the owner of the group
        if (!group.getOwnerId().equals(user.getUserId())) {
            throw new RuntimeException("Only group owner can delete the group");
        }
        
        group.setIsActive(false);
        groupRepository.save(group);
        
        // Remove all members from the group
        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        groupMemberRepository.deleteAll(members);
    }
} 