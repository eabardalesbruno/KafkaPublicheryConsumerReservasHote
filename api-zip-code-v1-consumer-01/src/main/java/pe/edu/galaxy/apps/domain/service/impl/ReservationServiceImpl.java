package pe.edu.galaxy.apps.domain.service.impl;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import pe.edu.galaxy.apps.data.model.entites.GuestEntity;
import pe.edu.galaxy.apps.data.model.entites.HotelReservationEntity;
import pe.edu.galaxy.apps.data.model.entites.RoomEntity;
import pe.edu.galaxy.apps.data.repositories.GuestRepository;
import pe.edu.galaxy.apps.data.repositories.HotelReservationRepository;
import pe.edu.galaxy.apps.data.repositories.RoomRepository;
import pe.edu.galaxy.apps.model.HotelReservation;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ReservationServiceImpl implements pe.edu.galaxy.apps.domain.service.ReservationService {
    private static final Logger log = Logger.getLogger(ReservationServiceImpl.class.getName());

    @Inject
    HotelReservationRepository reservationRepository;

    @Inject
    GuestRepository guestRepository;

    @Inject
    RoomRepository roomRepository;

    @Incoming("hotel-reservation-topic")
    public CompletionStage<Void> processReservation(Message<HotelReservation> reservationMessage) {
        var reservation = reservationMessage.getPayload();
        // Generar IDs si no existen
        String guestId = (reservation.getGuest() != null && reservation.getGuest().getGuestId() != null) ? reservation.getGuest().getGuestId().toString() : java.util.UUID.randomUUID().toString();
        String roomId = (reservation.getRoom() != null && reservation.getRoom().getRoomId() != null) ? reservation.getRoom().getRoomId().toString() : java.util.UUID.randomUUID().toString();
        String reservationId = (reservation.getReservationId() != null) ? reservation.getReservationId().toString() : java.util.UUID.randomUUID().toString();

        // Crear entidades
        GuestEntity guestEntity = new GuestEntity();
        guestEntity.setGuestId(guestId);
        guestEntity.setFullName(reservation.getGuest().getFullName() != null ? reservation.getGuest().getFullName().toString() : null);
        guestEntity.setEmail(reservation.getGuest().getEmail() != null ? reservation.getGuest().getEmail().toString() : null);
        guestEntity.setPhone(reservation.getGuest().getPhone() != null ? reservation.getGuest().getPhone().toString() : null);

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomId);
        roomEntity.setRoomType(reservation.getRoom().getRoomType() != null ? reservation.getRoom().getRoomType().toString() : null);
        roomEntity.setCapacity(reservation.getRoom().getCapacity());

        HotelReservationEntity reservationEntity = new HotelReservationEntity();
        reservationEntity.setReservationId(reservationId);
        reservationEntity.setGuestId(guestId);
        reservationEntity.setRoomId(roomId);
        reservationEntity.setCheckIn(java.time.LocalDate.parse(reservation.getCheckIn()));
        reservationEntity.setCheckOut(java.time.LocalDate.parse(reservation.getCheckOut()));
        reservationEntity.setStatus(reservation.getStatus() != null ? reservation.getStatus().toString() : null);
        reservationEntity.setCurrency(reservation.getCurrency() != null ? reservation.getCurrency().toString() : null);
        reservationEntity.setTotalPrice(java.math.BigDecimal.valueOf(reservation.getTotalPrice()));

        return Uni.createFrom().deferred(() ->
            Panache.withTransaction(() ->
                guestRepository.find("guestId", guestId).firstResult()
                    .chain(existingGuest -> {
                        if (existingGuest == null) {
                            return guestRepository.persist(guestEntity);
                        } else {
                            return Uni.createFrom().item(existingGuest);
                        }
                    })
                    .chain(() -> roomRepository.find("roomId", roomId).firstResult())
                    .chain(existingRoom -> {
                        if (existingRoom == null) {
                            return roomRepository.persist(roomEntity);
                        } else {
                            return Uni.createFrom().item(existingRoom);
                        }
                    })
                    .chain(() -> reservationRepository.find("reservationId", reservationId).firstResult())
                    .chain(existingReservation -> {
                        if (existingReservation == null) {
                            return reservationRepository.persist(reservationEntity);
                        } else {
                            return Uni.createFrom().item(existingReservation);
                        }
                    })
            )
        )
        .onFailure().invoke(err -> {
            log.errorf("Error processing reservation %s: %s", reservationId, err.getMessage());
        })
        .onItem().transformToUni(v ->
            Uni.createFrom().completionStage(reservationMessage.ack())
        )
        .onFailure().recoverWithUni(err ->
            Uni.createFrom().completionStage(reservationMessage.nack(err))
        )
        .replaceWithVoid()
        .subscribeAsCompletionStage();
    }
}
