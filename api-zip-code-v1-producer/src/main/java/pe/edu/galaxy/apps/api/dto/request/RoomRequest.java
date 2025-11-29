package pe.edu.galaxy.apps.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomRequest(
        @NotBlank(message = "El tipo de habitación es obligatorio")
        String roomType,
        @NotNull(message = "La capacidad es obligatoria")
        @Positive(message = "La capacidad debe ser mayor a cero")
        Integer capacity
) {}
