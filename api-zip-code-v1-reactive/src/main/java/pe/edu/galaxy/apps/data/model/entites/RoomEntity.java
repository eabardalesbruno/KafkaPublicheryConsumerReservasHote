package pe.edu.galaxy.apps.data.model.entites;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_room")
public class RoomEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "uuid")
    @org.hibernate.annotations.GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "room_id", updatable = false, nullable = false)
    public String roomId;

    @Column(name = "room_type")
    public String roomType;
    public Integer capacity;
}
