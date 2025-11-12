package com.gs.requalify.service;

import com.gs.requalify.dto.UserDTO;
import com.gs.requalify.model.User;
import com.gs.requalify.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) throw new RuntimeException("Senha não pode ser vazia");
        User existingUser = userRepository.findByUsernameIgnoreCase(user.getUsername()).orElse(null);
        if (existingUser != null) throw new RuntimeException("Email ou senha inválidos");
        userRepository.save(user);
        return user;
    }

    public UserDTO getUserByUsername(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        return new UserDTO(user.getId(), user.getUsername(), user.getName());
    }

}
