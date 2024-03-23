package com.company.pontoworker.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class UserDTO {

    private Long id;

    @NotBlank(message = "O campo username é obrigatório")
    private String username;

    @NotBlank(message = "O campo password é obrigatório")
    private String password;

    @NotBlank(message = "O campo email é obrigatório")
    private String email;

    @NotBlank(message = "O campo name é obrigatório")
    private String name;

    private LocalDateTime creationDate;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

}
