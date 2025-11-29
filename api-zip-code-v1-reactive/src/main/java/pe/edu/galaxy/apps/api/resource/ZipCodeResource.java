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
		return zipCodeService.create(request)
				.invoke(result -> {
					var message = result.getValue() ? "ZipCode creado." : "ZipCode ya existente.";
					log.info(message);
				})
				.map(response ->
						Response
								.status(response.getValue() ? Response.Status.CREATED : Response.Status.OK)
								.entity(response.getKey())
								.build())
				.onFailure(RuntimeException.class)
				.recoverWithItem(error ->
						Response.serverError().entity(error.getMessage()).build());
	}
}
