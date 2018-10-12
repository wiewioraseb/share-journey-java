package com.swcorp.edutech.controller;

import java.net.URI;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.swcorp.edutech.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.swcorp.edutech.dto.TopDriverDTO;
import com.swcorp.edutech.model.Ride;
import com.swcorp.edutech.service.RideService;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;


@RestController
public class RideController {
  private static final Logger LOG = LoggerFactory.getLogger(RideController.class);

  private final RideService rideService;
  private final PersonService personService;

  @Autowired
  public RideController(RideService rideService, PersonService personService) {
    this.rideService = rideService;
    this.personService = personService;
  }

  @PostMapping(path ="/api/ride")
  public ResponseEntity<Ride> createNewRide(@Valid @RequestBody Ride ride, UriComponentsBuilder ucb) throws ParseException {
    LOG.info("Create ride: {}", ride);

    if (ride.getRider().getId().equals(ride.getDriver().getId())) {
      LOG.error("Driver and rider cannot be the same person", ride);
      return ResponseEntity.badRequest().build();
    }

    if (ride.getStartTime().isAfter(ride.getEndTime())) {
      LOG.error("Ride's start time must be before end time", ride);
      return ResponseEntity.badRequest().build();
    }

    Ride rideResult = rideService.save(ride);
    URI locationUri =
            ucb.path("/api/ride/")
                    .path(String.valueOf(rideResult.getId()))
                    .build()
                    .toUri();

    return ResponseEntity.created(locationUri).body(rideResult);
  }

  @GetMapping(path = "/api/ride/{ride-id}")
  public ResponseEntity<Ride> getRideById(@PathVariable(name="ride-id",required=true)Long rideId){
    Ride ride = rideService.findById(rideId);
    if (ride!=null)
      return ResponseEntity.ok(ride);
    return ResponseEntity.notFound().build();
  }

  @GetMapping(path = "/api/top-rides")
  public ResponseEntity<List<TopDriverDTO>> getTopDriver(
      @RequestParam(value="max", defaultValue="5") Long count,
      @RequestParam(value="startTime", required=true) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
      @RequestParam(value="endTime", required=true) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime){
    List<TopDriverDTO> topDrivers = new ArrayList<>();

    topDrivers = personService.getAll().stream()
            .map(person -> {
                Supplier<Stream<Ride>> filteredRidesOfDriverSupplier = () -> rideService.findByDriver_Id(person.getId())
                        .stream()
                        .filter(ride -> ride.getStartTime().isAfter(startTime) && ride.getEndTime().isBefore(endTime));

                Supplier<LongStream> durationOfRidesSupplier = () -> filteredRidesOfDriverSupplier.get().map(ride -> Duration.between(ride.getStartTime(), ride.getEndTime()).toMinutes())
                        .mapToLong(Long::longValue);

                Long totalRideDurationInMinutes = durationOfRidesSupplier.get().sum();
                Long maxRideDurationInMinutes = durationOfRidesSupplier.get()
                        .max()
                        .orElse(0);
                Double averageDistance = filteredRidesOfDriverSupplier.get()
                        .mapToLong(Ride::getDistance)
                        .average()
                        .orElse(0);

                return new TopDriverDTO(
                        person.getName(),
                        person.getEmail(),
                        totalRideDurationInMinutes,
                        maxRideDurationInMinutes,
                        averageDistance);
            })
            .sorted(Comparator.comparing(TopDriverDTO::getTotalRideDurationInMinutes).reversed())
            .limit(count)
            .collect(Collectors.toList());

    return ResponseEntity.ok(topDrivers);
  }
}
