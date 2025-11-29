package pe.edu.galaxy.apps.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GuestRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        String fullName,
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico no es válido")
        String email,
        @NotBlank(message = "El teléfono es obligatorio")
        String phone
) {}