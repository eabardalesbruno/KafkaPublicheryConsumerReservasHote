package pe.edu.galaxy.apps.domain.service.impl;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import java.util.Map;
import java.util.UUID;
import pe.edu.galaxy.apps.api.dto.request.ReservationRequest;
import pe.edu.galaxy.apps.api.dto.response.GuestResponse;
import pe.edu.galaxy.apps.api.dto.response.ReservationResponse;
import pe.edu.galaxy.apps.api.dto.response.RoomResponse;
import pe.edu.galaxy.apps.data.model.entites.GuestEntity;
import pe.edu.galaxy.apps.data.model.entites.RoomEntity;
import pe.edu.galaxy.apps.data.model.entites.ReservationEntity;
import pe.edu.galaxy.apps.data.repositories.GuestRepository;
import pe.edu.galaxy.apps.data.repositories.RoomRepository;
import pe.edu.galaxy.apps.data.repositories.ReservationRepository;
import pe.edu.galaxy.apps.domain.service.ReservationService;

@ApplicationScoped
public class ReservationServiceImpl implements ReservationService {

    @Inject
    GuestRepository guestRepository;

    @Inject
    RoomRepository roomRepository;

    @Inject
    ReservationRepository reservationRepository;

    @Override
    @WithTransaction
    public Uni<ReservationResponse> save(ReservationRequest request) {
        ReservationEntity reservation = new ReservationEntity();
        reservation.checkIn       = request.checkIn();
        reservation.checkOut      = request.checkOut();
        reservation.status        = request.status();
        reservation.totalPrice    = request.totalPrice();
        reservation.currency      = request.currency();

        GuestEntity guest = new GuestEntity();
        guest.fullName = request.guest().fullName();
        guest.email = request.guest().email();
        guest.phone = request.guest().phone();

        RoomEntity room = new RoomEntity();
        room.roomType = request.room().roomType();
        room.capacity = request.room().capacity();

        return guestRepository.persist(guest)
            .flatMap(g -> {
                reservation.guestId = g.guestId;
                RoomEntity newRoom = new RoomEntity();
                newRoom.roomType = room.roomType;
                newRoom.capacity = room.capacity;
                return roomRepository.persist(newRoom)
                    .map(r -> {
                        reservation.roomId = r.roomId;
                        return new Object[] {g, r};
                    });
            })
            .flatMap(arr -> reservationRepository.persist(reservation)
                .map(x -> {
                    GuestEntity g = (GuestEntity) arr[0];
                    RoomEntity r = (RoomEntity) arr[1];
                    return new ReservationResponse(
                        reservation.reservationId,
                        new GuestResponse(
                            g.guestId,
                            g.fullName,
                            g.email,
                            g.phone
                        ),
                        new RoomResponse(
                            r.roomId,
                            r.roomType,
                            r.capacity
                        ),
                        reservation.checkIn,
                        reservation.checkOut,
                        reservation.status,
                        reservation.totalPrice
                    );
                })
            );
    }
}
