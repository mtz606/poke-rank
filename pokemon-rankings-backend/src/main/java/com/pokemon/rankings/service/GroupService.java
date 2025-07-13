package com.pokemon.rankings.service;

import com.pokemon.rankings.dto.CreateGroupRequest;
import com.pokemon.rankings.dto.GroupResponse;
import com.pokemon.rankings.entity.Group;
import com.pokemon.rankings.entity.User;
import com.pokemon.rankings.repository.GroupRepository;
import com.pokemon.rankings.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public GroupResponse createGroup(CreateGroupRequest request, String username) {
        // Find the user creating the group
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if group name already exists
        if (groupRepository.findByNameAndIsActiveTrue(request.getName()).isPresent()) {
            throw new RuntimeException("Group name already exists");
        }
        
        // Create the group
        Group group = new Group(request.getName(), request.getDescription(), owner);
        group = groupRepository.save(group);
        
        return new GroupResponse(group);
    }
    
    public List<GroupResponse> getUserGroups(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Group> groups = groupRepository.findGroupsByUser(user);
        return groups.stream()
                .map(GroupResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<GroupResponse> getAllGroups() {
        List<Group> groups = groupRepository.findByIsActiveTrue();
        return groups.stream()
                .map(GroupResponse::new)
                .collect(Collectors.toList());
    }
    
    public GroupResponse getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        if (!group.getIsActive()) {
            throw new RuntimeException("Group is not active");
        }
        
        return new GroupResponse(group);
    }
    
    public List<GroupResponse> searchGroups(String searchTerm) {
        List<Group> groups = groupRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(searchTerm);
        return groups.stream()
                .map(GroupResponse::new)
                .collect(Collectors.toList());
    }
    
    public void addMemberToGroup(Long groupId, String username, String memberUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        User member = userRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        // Check if user is the owner of the group
        if (!group.getOwner().equals(user)) {
            throw new RuntimeException("Only group owner can add members");
        }
        
        // Check if member is already in the group
        if (group.isMember(member)) {
            throw new RuntimeException("User is already a member of this group");
        }
        
        group.addMember(member);
        groupRepository.save(group);
    }
    
    public void removeMemberFromGroup(Long groupId, String username, String memberUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        User member = userRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        // Check if user is the owner of the group
        if (!group.getOwner().equals(user)) {
            throw new RuntimeException("Only group owner can remove members");
        }
        
        // Check if member is in the group
        if (!group.isMember(member)) {
            throw new RuntimeException("User is not a member of this group");
        }
        
        group.removeMember(member);
        groupRepository.save(group);
    }
    
    public void deleteGroup(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user is the owner of the group
        if (!group.getOwner().equals(user)) {
            throw new RuntimeException("Only group owner can delete the group");
        }
        
        group.setIsActive(false);
        groupRepository.save(group);
    }
} 