package pe.edu.galaxy.apps.data.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

import jakarta.enterprise.context.ApplicationScoped;
import pe.edu.galaxy.apps.data.model.entites.RoomEntity;

@ApplicationScoped
public class RoomRepository implements PanacheRepositoryBase<RoomEntity , String> {
    }
