package pe.edu.galaxy.apps.data.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.edu.galaxy.apps.data.model.entites.GuestEntity;



@ApplicationScoped
public class GuestRepository implements PanacheRepositoryBase<GuestEntity , String> {
  }


