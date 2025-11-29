package pe.edu.galaxy.apps.data.model.entites;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity

@Table(name = "tb_reservation")
public class ReservationEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "uuid")
    @org.hibernate.annotations.GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "reservation_id", updatable = false, nullable = false)
    public String reservationId;

    @Column(name = "guest_id")
    public String guestId;

    @Column(name = "room_id")
    public String roomId;

    @Column(name = "check_in")
    public LocalDate checkIn;
    @Column(name = "check_out")
    public LocalDate checkOut;
    public String status;
    public java.math.BigDecimal totalPrice;
    public String currency;
}
