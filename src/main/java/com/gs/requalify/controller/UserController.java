package com.gs.requalify.controller;

import com.gs.requalify.dto.UserDTO;
import com.gs.requalify.dto.UserUpdateDTO;
import com.gs.requalify.model.Credentials;
import com.gs.requalify.model.Token;
import com.gs.requalify.model.User;
import com.gs.requalify.service.AuthService;
import com.gs.requalify.service.TokenService;
import com.gs.requalify.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, TokenService tokenService, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário", description = "Retorna um usuário pelo id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Object> getUser(@Parameter(description = "Id do usuário") @PathVariable Long id) {
        try {
            if (id == null) {
                throw new RuntimeException("Username não pode ser vazio");
            }
            UserDTO user = userService.getUserByUsername(id);
            return ResponseEntity.ok(user);
        }catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado");
        }
    }

    @PostMapping
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário e retorna token JWT")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<Object> create(@RequestBody @Valid User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(tokenService.createToken(savedUser));
        }catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login do usuário", description = "Autentica usuário e retorna token JWT com informações do usuário")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais incorretas")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Object> login(@RequestBody @Valid Credentials credentials){
        try {
            User user = (User) authService.loadUserByUsername(credentials.username());
            if (!passwordEncoder.matches(credentials.password(), user.getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciais incorretas"));
            }
            Token token = tokenService.createToken(user);
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException ex) {
            log.error("Usuário não encontrado: {}", credentials.username());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuário não encontrado"));
        } catch (Exception ex) {
            log.error("Erro no login: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao realizar login"));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Object> updateUser(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário existente", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Object> deleteUser(@Parameter(description = "ID do usuário") @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}