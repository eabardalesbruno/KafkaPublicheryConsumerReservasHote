package pe.edu.galaxy.apps.api.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.galaxy.apps.api.dto.request.ReservationRequest;
import pe.edu.galaxy.apps.domain.service.ReservationService;


@Path("/api/v1/reservation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {

    @Inject
    ReservationService service;

    @POST
    public Uni<Response> create(ReservationRequest request) {
        return service.save(request)
                .map(res -> Response.status(201).entity(res).build());
    }
}
