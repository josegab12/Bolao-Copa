package com.zebiso.Backend.service;

import com.zebiso.Backend.dto.UserResponse;
import com.zebiso.Backend.exception.BusinessException;
import com.zebiso.Backend.model.User;
import com.zebiso.Backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse enter(String name) {
        String trimmed = name.trim();
        if (trimmed.length() < 2) {
            throw new BusinessException("Nome deve ter pelo menos 2 caracteres");
        }

        User user = userRepository.findByNameIgnoreCase(trimmed)
                .orElseGet(() -> userRepository.save(new User(trimmed)));

        return mapToResponse(user);
    }

    @Transactional
    public UserResponse updateAvatar(UUID userId, String avatarBase64) {
        User user = findById(userId);
        user.setAvatar(avatarBase64);
        return mapToResponse(user);
    }

    @Transactional
    public UserResponse toggleRankingVisibility(UUID userId) {
        User user = findById(userId);
        user.setHiddenFromRanking(!user.isHiddenFromRanking());
        return mapToResponse(user);
    }

    public java.util.List<UserResponse> listAll() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuario nao encontrado"));
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getAvatar(), user.isHiddenFromRanking());
    }
}
