package com.springboot.relationship.Service;

import com.springboot.relationship.DTO.RequestDTO.UserRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.UserResponseDTO;
import com.springboot.relationship.Entity.User;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    void deleteUser(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    UserResponseDTO mappToResponseDTO(User user);
}

