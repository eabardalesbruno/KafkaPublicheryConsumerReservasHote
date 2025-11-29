package pe.edu.galaxy.apps.domain.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class DuplicateZipCodeExceptionMapper implements ExceptionMapper<DuplicateZipCodeException> {
    @Override
    public Response toResponse(DuplicateZipCodeException exception) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "ZipCode duplicado");
        error.put("message", exception.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }
}
