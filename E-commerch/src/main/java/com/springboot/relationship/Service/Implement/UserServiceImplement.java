package com.springboot.relationship.Service.Implement;

import com.springboot.relationship.DTO.RequestDTO.UserRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.UserResponseDTO;
import com.springboot.relationship.Entity.User;
import com.springboot.relationship.Exception.ResourceNotFoundException;
import com.springboot.relationship.Repository.UserRepository;
import com.springboot.relationship.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        validateUserRequest(userRequestDTO);

        // Check if username already exists
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userRequestDTO.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userRequestDTO.getEmail());
        }

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());

        User savedUser = userRepository.save(user);
        return mappToResponseDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mappToResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mappToResponseDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        validateUserRequest(userRequestDTO);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if username is taken by another user
        if (!user.getUsername().equals(userRequestDTO.getUsername())
            && userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userRequestDTO.getUsername());
        }

        // Check if email is taken by another user
        if (!user.getEmail().equals(userRequestDTO.getEmail())
            && userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userRequestDTO.getEmail());
        }

        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());

        User updatedUser = userRepository.save(user);
        return mappToResponseDTO(updatedUser);
    }

    @Override
    public UserResponseDTO mappToResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        return userResponseDTO;
    }

    private void validateUserRequest(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            throw new IllegalArgumentException("User request cannot be null");
        }

        if (userRequestDTO.getUsername() == null || userRequestDTO.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        if (userRequestDTO.getEmail() == null || userRequestDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (userRequestDTO.getPassword() == null || userRequestDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
    }
}

