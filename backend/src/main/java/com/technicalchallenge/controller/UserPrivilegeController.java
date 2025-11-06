package com.technicalchallenge.controller;

import com.technicalchallenge.dto.UserPrivilegeDTO;
import com.technicalchallenge.mapper.UserPrivilegeMapper;
import com.technicalchallenge.model.UserPrivilege;
import com.technicalchallenge.service.UserPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/userPrivileges")
public class UserPrivilegeController {
    private static final Logger logger = LoggerFactory.getLogger(UserPrivilegeController.class);

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @Autowired
    private UserPrivilegeMapper userPrivilegeMapper;

    @GetMapping
    public List<UserPrivilegeDTO> getAllUserPrivileges() {
        logger.info("Fetching all user privileges");
        return userPrivilegeService.getAllUserPrivileges().stream()
                .map(userPrivilegeMapper::toDto)
                .toList();
    }

   // @GetMapping("/{id}")
   // public ResponseEntity<UserPrivilegeDTO> getUserPrivilegeById(@PathVariable Long id) {
       // logger.debug("Fetching user privilege by id: {}", id);
       // Optional<UserPrivilege> userPrivilege = userPrivilegeService.getUserPrivilegeById(id);
       // return userPrivilege.map(userPrivilegeMapper::toDto)
         //       .map(ResponseEntity::ok)
           //     .orElseGet(() -> ResponseEntity.notFound().build());
   // }

   @GetMapping("/{userId}/{privilegeId}")
    public ResponseEntity<UserPrivilege> getUserPrivilege(
        @PathVariable Long userId,
        @PathVariable Long privilegeId) {

    return userPrivilegeService.getUserPrivilegeByUserAndPrivilege(userId, privilegeId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}


    @PostMapping
    public ResponseEntity<UserPrivilegeDTO> createUserPrivilege(@Valid @RequestBody UserPrivilegeDTO userPrivilegeDTO) {
        logger.info("Creating new user privilege: {}", userPrivilegeDTO);
        UserPrivilege createdUserPrivilege = userPrivilegeService.saveUserPrivilege(userPrivilegeMapper.toEntity(userPrivilegeDTO));
        return ResponseEntity.created(URI.create("/api/userPrivileges/" + createdUserPrivilege.getUserId()))
                .body(userPrivilegeMapper.toDto(createdUserPrivilege));
    }

   // @DeleteMapping("/{id}")
   // public ResponseEntity<Void> deleteUserPrivilege(@PathVariable Long id) {
       // logger.warn("Deleting user privilege with id: {}", id);
       // userPrivilegeService.deleteUserPrivilege(id);
       // return ResponseEntity.noContent().build();
   // }

   @DeleteMapping("/{userId}/{privilegeId}")
public ResponseEntity<Void> deleteUserPrivilege(
        @PathVariable Long userId,
        @PathVariable Long privilegeId) {

    userPrivilegeService.deleteUserPrivilege(userId, privilegeId);
    return ResponseEntity.noContent().build();
}

}
