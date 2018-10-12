package com.swcorp.edutech.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.swcorp.edutech.model.Ride;
import com.swcorp.edutech.repositories.RideRepository;

@Service
public class RideServiceImpl implements RideService{

  @Autowired
  RideRepository rideRepository;
  
  public Ride save(Ride ride) {
    return rideRepository.save(ride);
  }
  
  public Ride findById(Long rideId) {
    Optional<Ride> optionalRide = rideRepository.findById(rideId);
    if (optionalRide.isPresent()) {
      return optionalRide.get();
    }else return null;
  }

  public List<Ride> findByDriver_Id(Long driverId) {
    return rideRepository.findByDriver_Id(driverId);
  }

}
