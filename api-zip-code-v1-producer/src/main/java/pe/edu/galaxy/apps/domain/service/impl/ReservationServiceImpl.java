package pe.edu.galaxy.apps.domain.service.impl;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import pe.edu.galaxy.apps.api.dto.request.ReservationRequest;
import pe.edu.galaxy.apps.api.dto.response.ReservationResponse;
import pe.edu.galaxy.apps.domain.service.ReservationService;
import pe.edu.galaxy.apps.model.Guest;
import pe.edu.galaxy.apps.model.HotelReservation;
import pe.edu.galaxy.apps.model.Room;
import pe.edu.galaxy.apps.api.dto.response.GuestResponse;
import pe.edu.galaxy.apps.api.dto.response.RoomResponse;

import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@ApplicationScoped
public class ReservationServiceImpl implements ReservationService {

    private final static Logger log = Logger.getLogger(ReservationServiceImpl.class.getName());

    @Channel("hotel-reservation-topic")
    Emitter<HotelReservation> emitter;

    // Almacén en memoria para reservationIds ya registrados
    private static final Set<String> registeredReservations = ConcurrentHashMap.newKeySet();

    @Override
    public Uni<ReservationResponse> save(ReservationRequest request) {
        String reservationId = request.reservationId() != null && !request.reservationId().isBlank()
                ? request.reservationId()
                : UUID.randomUUID().toString();

        // Verificar si el reservationId ya existe
        if (registeredReservations.contains(reservationId)) {
            log.infof("Reserva duplicada detectada para reservationId: %s", reservationId);
            throw new pe.edu.galaxy.apps.domain.exception.DuplicateReservationException(
                "Ya existe una reserva con el reservationId: " + reservationId);
        }
        // Registrar el reservationId en memoria
        registeredReservations.add(reservationId);

        String guestId = UUID.randomUUID().toString();
        String roomId = UUID.randomUUID().toString();

        Guest guest = new Guest(
            guestId,
            request.guest().fullName(),
            request.guest().email(),
            request.guest().phone()
        );

        Room room = new Room(
            roomId,
            request.room().roomType(),
            request.room().capacity() != null ? request.room().capacity() : 0
        );

        HotelReservation avroReservation = new HotelReservation(
            reservationId,
            guest,
            room,
            request.checkIn() != null ? request.checkIn().toString() : "",
            request.checkOut() != null ? request.checkOut().toString() : "",
            request.status() != null ? request.status() : "",
            request.totalPrice() != null ? request.totalPrice().intValue() : 0,
            request.currency() != null ? request.currency() : ""
        );

        GuestResponse guestResponse = new GuestResponse(
            guest.getGuestId(),
            guest.getFullName(),
            guest.getEmail(),
            guest.getPhone()
        );
        RoomResponse roomResponse = new RoomResponse(
            room.getRoomId(),
            room.getRoomType(),
            room.getCapacity()
        );

        return Uni.createFrom().voidItem()
                .invoke(() -> {
                    var message = Message.of(avroReservation)
                            .addMetadata(
                                    OutgoingKafkaRecordMetadata.builder()
                                            .withKey(UUID.randomUUID().toString())
                                            .withHeaders(new RecordHeaders()
                                                    .add("endpointType", "RESERVATION".getBytes(StandardCharsets.UTF_8)))
                                            .build()
                            );
                    emitter.send(message);
                    log.infof("Reservation enviada a Kafka: %s", request);
                })
                .replaceWith(
                        new ReservationResponse(
                                reservationId,
                                guestResponse,
                                roomResponse,
                                request.checkIn(),
                                request.checkOut(),
                                request.status(),
                                request.totalPrice()
                        )
                );
    }
}
