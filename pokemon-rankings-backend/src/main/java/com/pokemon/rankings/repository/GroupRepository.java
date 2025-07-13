package com.pokemon.rankings.repository;

import com.pokemon.rankings.entity.Group;
import com.pokemon.rankings.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    // Find groups by owner
    List<Group> findByOwnerAndIsActiveTrue(User owner);
    
    // Find groups where user is a member (including owner)
    @Query("SELECT g FROM Group g WHERE (g.owner = :user OR :user MEMBER OF g.members) AND g.isActive = true")
    List<Group> findGroupsByUser(@Param("user") User user);
    
    // Find group by name (for uniqueness check)
    Optional<Group> findByNameAndIsActiveTrue(String name);
    
    // Find all active groups
    List<Group> findByIsActiveTrue();
    
    // Find groups by name containing (for search)
    List<Group> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
} 