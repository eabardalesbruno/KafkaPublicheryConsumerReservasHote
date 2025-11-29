package pe.edu.galaxy.apps.api.dto.response;

public record RoomResponse(
        String roomId,
        String roomType,
        Integer capacity
) {}