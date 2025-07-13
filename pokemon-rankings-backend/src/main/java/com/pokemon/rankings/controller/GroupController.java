package com.pokemon.rankings.controller;

import com.pokemon.rankings.dto.CreateGroupRequest;
import com.pokemon.rankings.dto.GroupResponse;
import com.pokemon.rankings.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "http://localhost:5173")
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(
            @Valid @RequestBody CreateGroupRequest request,
            Authentication authentication) {
        try {
            GroupResponse group = groupService.createGroup(request, authentication.getName());
            return ResponseEntity.ok(group);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupResponse>> getMyGroups(Authentication authentication) {
        try {
            List<GroupResponse> groups = groupService.getUserGroups(authentication.getName());
            return ResponseEntity.ok(groups);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        try {
            List<GroupResponse> groups = groupService.getAllGroups();
            return ResponseEntity.ok(groups);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable String groupId) {
        try {
            GroupResponse group = groupService.getGroupById(groupId);
            return ResponseEntity.ok(group);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<GroupResponse>> searchGroups(@RequestParam String q) {
        try {
            List<GroupResponse> groups = groupService.searchGroups(q);
            return ResponseEntity.ok(groups);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> addMemberToGroup(
            @PathVariable String groupId,
            @RequestParam String memberUsername,
            Authentication authentication) {
        try {
            groupService.addMemberToGroup(groupId, authentication.getName(), memberUsername);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{groupId}/members")
    public ResponseEntity<Void> removeMemberFromGroup(
            @PathVariable String groupId,
            @RequestParam String memberUsername,
            Authentication authentication) {
        try {
            groupService.removeMemberFromGroup(groupId, authentication.getName(), memberUsername);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable String groupId,
            Authentication authentication) {
        try {
            groupService.deleteGroup(groupId, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 