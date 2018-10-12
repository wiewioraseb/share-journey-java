package com.swcorp.edutech.repositories;

import com.swcorp.edutech.model.Ride;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RestResource(exported = false)
public interface RideRepository extends CrudRepository<Ride, Long> {
    List<Ride> findByDriver_Id(Long driverId);
}
