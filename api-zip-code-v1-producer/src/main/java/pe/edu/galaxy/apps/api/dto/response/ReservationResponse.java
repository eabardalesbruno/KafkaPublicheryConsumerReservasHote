// src/main/java/pe/edu/galaxy/apps/api/dto/response/ReservationResponse.java
package pe.edu.galaxy.apps.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationResponse(
        String reservationId,
        GuestResponse guest,
        RoomResponse room,
        LocalDate checkIn,
        LocalDate checkOut,
        String status,
        BigDecimal totalPrice
) {}
