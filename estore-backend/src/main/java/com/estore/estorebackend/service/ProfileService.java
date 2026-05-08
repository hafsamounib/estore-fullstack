package com.estore.estorebackend.service;

import com.estore.estorebackend.customer.User;
import com.estore.estorebackend.customer.UserRepository;
import com.estore.estorebackend.dto.ProfileDTO;
import com.estore.estorebackend.entity.Profile;
import com.estore.estorebackend.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    // Récupérer le profil d'un utilisateur
    public Profile getProfile(Long userId) {
        return profileRepository.findByUserId(userId)
            .orElseGet(() -> {
                // Si pas de profil, on en crée un vide
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                Profile profile = Profile.builder()
                    .user(user)
                    .phone("")
                    .address("")
                    .city("")
                    .country("")
                    .build();
                return profileRepository.save(profile);
            });
    }

    // Mettre à jour le profil
    public Profile updateProfile(Long userId, ProfileDTO dto) {
        Profile profile = profileRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                return Profile.builder().user(user).build();
            });

        profile.setPhone(dto.getPhone());
        profile.setAddress(dto.getAddress());
        profile.setCity(dto.getCity());
        profile.setCountry(dto.getCountry());

        return profileRepository.save(profile);
    }
}