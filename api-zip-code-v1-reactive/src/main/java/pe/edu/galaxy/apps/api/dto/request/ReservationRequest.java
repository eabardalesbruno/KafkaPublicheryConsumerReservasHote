package pe.edu.galaxy.apps.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "El huésped es obligatorio")
        GuestRequest guest,
        @NotNull(message = "La habitación es obligatoria")
        RoomRequest room,
        @NotNull(message = "La fecha de check-in es obligatoria")
        LocalDate checkIn,
        @NotNull(message = "La fecha de check-out es obligatoria")
        LocalDate checkOut,
        @NotBlank(message = "El estado es obligatorio")
        String status,
        @NotNull(message = "El precio total es obligatorio")
        @Positive(message = "El precio total debe ser positivo")
        BigDecimal totalPrice,
        @NotBlank(message = "La moneda es obligatoria")
        String currency
) {}
