package com.swcorp.edutech.service;

import com.swcorp.edutech.model.Ride;

import java.util.List;

public interface RideService {
  
  public Ride save(Ride ride);
  
  public Ride findById(Long rideId);
  
  public List<Ride> findByDriver_Id(Long driverId);
}
