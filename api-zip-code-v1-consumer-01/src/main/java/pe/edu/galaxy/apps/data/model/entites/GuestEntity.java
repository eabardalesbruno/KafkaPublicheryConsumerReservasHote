package pe.edu.galaxy.apps.data.model.entites;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_guest", schema = "public")
public class GuestEntity {
    @Id
    @Column(name = "guest_id")
    private String guestId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
