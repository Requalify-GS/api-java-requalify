package com.gs.requalify.service;

import com.gs.requalify.dto.UserDTO;
import com.gs.requalify.dto.UserUpdateDTO;
import com.gs.requalify.model.User;
import com.gs.requalify.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) throw new RuntimeException("Senha não pode ser vazia");
        User existingUser = userRepository.findByUsernameIgnoreCase(user.getUsername()).orElse(null);
        if (existingUser != null) throw new RuntimeException("Email ou senha inválidos");
        userRepository.save(user);
        return user;
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (userUpdateDTO.name() != null && !userUpdateDTO.name().isBlank()) user.setName(userUpdateDTO.name());

        if (userUpdateDTO.password() != null && !userUpdateDTO.password().isEmpty()) user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));

        if (userUpdateDTO.username() != null && !userUpdateDTO.username().isBlank()) {
            User existingUser = userRepository.findByUsernameIgnoreCase(userUpdateDTO.username()).orElse(null);
            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw new RuntimeException("Email já está em uso por outro usuário");
            }
            user.setUsername(userUpdateDTO.username());
        }

        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getName());
    }

    public UserDTO getUserByUsername(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        return new UserDTO(user.getId(), user.getUsername(), user.getName());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        userRepository.delete(user);
    }

}
