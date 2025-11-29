package pe.edu.galaxy.apps.domain.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class DuplicateReservationExceptionMapper implements ExceptionMapper<DuplicateReservationException> {
    @Override
    public Response toResponse(DuplicateReservationException exception) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Reserva duplicada");
        error.put("message", exception.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }
}
