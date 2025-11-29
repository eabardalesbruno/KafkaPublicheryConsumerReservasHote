package pe.edu.galaxy.apps.domain.service;

import org.eclipse.microprofile.reactive.messaging.Message;
import pe.edu.galaxy.apps.model.HotelReservation;
import java.util.concurrent.CompletionStage;

public interface ReservationService {
    CompletionStage<Void> processReservation(Message<HotelReservation> reservationMessage);
}