package pe.edu.galaxy.apps.data.model.entites;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_guest")
public class GuestEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "uuid")
    @org.hibernate.annotations.GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "guest_id", updatable = false, nullable = false)
    public String guestId;
    @Column(name = "full_name")
    public String fullName;
    @Column(name = "email")
    public String email;
    @Column(name = "phone")
    public String phone;
}
