package com.poseidoncapitalsolutions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
    
    @NotBlank(message = "Username is mandatory")
    String username,

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()])[A-Za-z\\d@$!%*?&#^()]{8,}$",
        message = "Le mot de passe doit contenir au moins une majuscule, un chiffre et un symbole"
    )
    String rawPassword,

    @NotBlank(message = "FullName is mandatory")
    String fullname,

    @NotBlank(message = "Role is mandatory")
    String role
) {}
