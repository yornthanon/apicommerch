package com.springboot.relationship.Controller;

import com.springboot.relationship.DTO.RequestDTO.UserRequestDTO;
import com.springboot.relationship.DTO.ResponeDTO.UserResponseDTO;
import com.springboot.relationship.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO responseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> responseDTOList = userService.getAllUsers();
        return ResponseEntity.ok(responseDTOList);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {
        UserResponseDTO responseDTO = userService.getUserById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO responseDTO = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User has been deleted successfully.");
    }
}

