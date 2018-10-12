package com.swcorp.edutech.controller;

import com.swcorp.edutech.dto.TopDriverDTO;
import com.swcorp.edutech.model.Person;
import com.swcorp.edutech.model.Ride;
import com.swcorp.edutech.repositories.PersonRepository;
import com.swcorp.edutech.repositories.RideRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RideControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private RideController rideController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  RideRepository rideRepository;

  @Autowired
  PersonRepository personRepository;

  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
  }

  private List<ResponseEntity<Person>> responseList;

  public void prepareDatabaseData() {
    List<Integer> entitiesNo = Arrays.asList(1,2,3,4,5,6);
    List<HttpEntity<Object>> personEntityList =  entitiesNo.stream()
            .map(e -> getHttpEntity("{\"name\": \"test " + e + "\", \"email\": \"test2000000000000" + e + "@gmail.com\","
                    + " \"registrationNumber\": \"registrationNumber" + e + "\",\"registrationDate\":\"2018-08-2T12:12:1" + e + "\" }"))
            .collect(Collectors.toList());

    responseList = personEntityList.stream()
            .map(e -> template.postForEntity(
                    "/api/person", e, Person.class))
            .collect(Collectors.toList());
  }

  public void clearDatabaseData() {
    responseList.forEach(e -> personRepository.deleteById(e.getBody().getId()));
  }

  @Test
  public void shouldBeCreated() {
    HttpEntity<Object> ride = getHttpEntity(
        "{\"startTime\": \"2017-10-05T09:23:24\", "
                + "\"endTime\": \"2017-10-05T12:50:27\","
                + "\"distance\": \"150\","
                + "\"driver\": {\"id\": 1},"
                + "\"rider\": {\"id\": 2}"
                + "}");
    ResponseEntity<Ride> response = template.postForEntity(
        "/api/ride", ride, Ride.class);

    rideRepository.deleteById(response.getBody().getId());
    Assert.assertEquals("2017-10-05T09:23:24", response.getBody().getStartTime().toString());
    Assert.assertEquals(new Long(2), response.getBody().getRider().getId());
    Assert.assertEquals(201, response.getStatusCode().value());
  }

  private static HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }

  @Test
  public void shouldGetById() {
    prepareDatabaseData();
    HttpEntity<Object> ride = getHttpEntity(
            "{\"startTime\": \"2017-11-05T09:23:24\", "
                    + "\"endTime\": \"2017-11-05T12:50:27\","
                    + "\"distance\": \"145\","
                    + "\"driver\": {\"id\": 1},"
                    + "\"rider\": {\"id\": 2}"
                    + "}");
    ResponseEntity<Ride> responseCreate = template.postForEntity(
            "/api/ride", ride, Ride.class);

    ResponseEntity<Ride> responseGet = template.getForEntity(
            "/api/ride/{id}", Ride.class, responseCreate.getBody().getId());

    clearDatabaseData();

    rideRepository.deleteById(responseCreate.getBody().getId());
    Assert.assertEquals(responseCreate.getBody().getId(), responseGet.getBody().getId());
    Assert.assertEquals("2017-11-05T12:50:27", responseGet.getBody().getEndTime().toString());
    Assert.assertEquals(200,responseGet.getStatusCode().value());
  }

  @Test
  public void shouldGet5TopDrivers() {
    prepareDatabaseData();
    int limit = 5;
    ResponseEntity<List<TopDriverDTO>> responseTopDrivers =
            template.exchange("/api/top-rides?max=" + limit + "&startTime=2017-10-04T01:30:00&endTime=2017-10-30T01:25:25",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TopDriverDTO>>(){});

    clearDatabaseData();
    Assert.assertEquals(200,responseTopDrivers.getStatusCode().value());
    Assert.assertEquals(limit, responseTopDrivers.getBody().size());
  }
}
