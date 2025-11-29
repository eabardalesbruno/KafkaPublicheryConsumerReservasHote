package pe.edu.galaxy.apps.api.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import pe.edu.galaxy.apps.api.dto.request.ZipCodeRequest;
import pe.edu.galaxy.apps.domain.service.ZipCodeService;

@Path("/api/v1/zip-code")
public class ZipCodeResource {

	private static final Logger log = Logger.getLogger(ZipCodeResource.class);

	@Inject
	ZipCodeService zipCodeService;

	@POST
	public Uni<Response> create(ZipCodeRequest request) {
		return zipCodeService
				.create(request)
				.map(zipCodeResponse -> {
					boolean exists = false;
					if (zipCodeResponse == null) {
						exists = true;
						zipCodeResponse = new pe.edu.galaxy.apps.api.dto.response.ZipCodeResponse(
							request.zip(),
							request.city(),
							request.county(),
							request.state(),
							request.timezone(),
							request.type()
						);
					}
					// Loguear la respuesta como JSON
					org.jboss.logging.Logger.getLogger(ZipCodeResource.class).infof("Respuesta ZipCode: %s", toJson(zipCodeResponse));
					// Responder siempre con JSON y status adecuado
					if (exists) {
						return Response.ok(new ApiResponseZipCode("El código postal ya existe", zipCodeResponse)).build();
					}
					return Response.status(Response.Status.CREATED).entity(new ApiResponseZipCode("Código postal creado", zipCodeResponse)).build();
				});
	}

    // DTO para respuesta estándar
    public static class ApiResponseZipCode {
        public String mensaje;
        public pe.edu.galaxy.apps.api.dto.response.ZipCodeResponse data;
        public ApiResponseZipCode(String mensaje, pe.edu.galaxy.apps.api.dto.response.ZipCodeResponse data) {
            this.mensaje = mensaje;
            this.data = data;
        }
    }

    // Utilidad para convertir a JSON
    private static String toJson(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    @POST
    @Path("/type-2")
    public Uni<Response> createType2(ZipCodeRequest request) {
        return create(request);
    }
}
