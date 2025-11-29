package pe.edu.galaxy.apps.data.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import pe.edu.galaxy.apps.data.model.entites.GuestEntity;

@ApplicationScoped
public class GuestRepository implements PanacheRepository<GuestEntity> {
}
