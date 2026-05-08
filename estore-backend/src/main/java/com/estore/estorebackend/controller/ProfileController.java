package com.estore.estorebackend.controller;

import com.estore.estorebackend.dto.ProfileDTO;
import com.estore.estorebackend.entity.Profile;
import com.estore.estorebackend.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*",
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.PUT,
                        RequestMethod.POST, RequestMethod.OPTIONS})
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        try {
            Profile profile = profileService.getProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileDTO dto) {
        try {
            Profile updated = profileService.updateProfile(userId, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}