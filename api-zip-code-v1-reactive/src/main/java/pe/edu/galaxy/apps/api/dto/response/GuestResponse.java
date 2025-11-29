// src/main/java/pe/edu/galaxy/apps/api/dto/response/GuestResponse.java
package pe.edu.galaxy.apps.api.dto.response;

public record GuestResponse(
        String guestId,
        String fullName,
        String email,
        String phone
) {}
