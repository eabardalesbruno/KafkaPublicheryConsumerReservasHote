package pe.edu.galaxy.apps.domain.service;

import io.smallrye.mutiny.Uni;
import pe.edu.galaxy.apps.api.dto.request.ReservationRequest;
import pe.edu.galaxy.apps.api.dto.response.ReservationResponse;

public interface ReservationService {
    Uni<ReservationResponse> save(ReservationRequest request);
}