package com.pokemon.rankings.controller;

import com.pokemon.rankings.dto.CreateGroupRequest;
import com.pokemon.rankings.dto.GroupResponse;
import com.pokemon.rankings.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(
            @Valid @RequestBody CreateGroupRequest request,
            Authentication authentication) {
        try {
            GroupResponse response = groupService.createGroup(request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupResponse>> getMyGroups(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<GroupResponse> groups = groupService.getMyGroups(username);
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
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