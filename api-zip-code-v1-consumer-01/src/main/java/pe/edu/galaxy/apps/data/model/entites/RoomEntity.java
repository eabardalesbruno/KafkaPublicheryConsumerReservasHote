package pe.edu.galaxy.apps.data.model.entites;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_room", schema = "public")
public class RoomEntity {
    @Id
    @Column(name = "room_id")
    private String roomId;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "capacity")
    private Integer capacity;

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}
