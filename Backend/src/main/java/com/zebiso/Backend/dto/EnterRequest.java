package com.zebiso.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnterRequest(
        @NotBlank(message = "Nome e obrigatorio")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String name
) {
}
