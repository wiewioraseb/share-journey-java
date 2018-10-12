package com.swcorp.edutech.controller;

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
import com.swcorp.edutech.model.Person;
import com.swcorp.edutech.repositories.PersonRepository;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private PersonController personController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  PersonRepository personRepository;
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
  }
  
  @Test
  public void shouldBeRegistered() throws Exception {
    HttpEntity<Object> person = getHttpEntity(
        "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\"," 
            + " \"registrationNumber\": \"41DCT\",\"registrationDate\":\"2018-08-08T12:12:12\" }");
    ResponseEntity<Person> response = template.postForEntity(
        "/api/person", person, Person.class);
    //Delete this user
    personRepository.deleteById(response.getBody().getId());
    Assert.assertEquals("test 1", response.getBody().getName());
    Assert.assertEquals(201,response.getStatusCode().value());
  }

  @Test
  public void shouldGetAll() throws Exception {
    HttpEntity<Object> person1 = getHttpEntity(
            "{\"name\": \"test 1\", \"email\": \"test20000000000001@gmail.com\","
                    + " \"registrationNumber\": \"registrationNumber1\",\"registrationDate\":\"2018-08-2T12:12:12\" }");
    ResponseEntity<Person> response1 = template.postForEntity(
            "/api/person", person1, Person.class);

    HttpEntity<Object> person2 = getHttpEntity(
            "{\"name\": \"test 2\", \"email\": \"test20000000000002@gmail.com\","
                    + " \"registrationNumber\": \"registrationNumber2\",\"registrationDate\":\"2018-08-1T12:12:12\" }");
    ResponseEntity<Person> response2 = template.postForEntity(
            "/api/person", person2, Person.class);

    ResponseEntity<List<Person>> responseAllPerson =
            template.exchange("/api/person", HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>(){});

    personRepository.deleteById(response1.getBody().getId());
    personRepository.deleteById(response2.getBody().getId());

    Assert.assertEquals(200,responseAllPerson.getStatusCode().value());
    Assert.assertEquals("registrationNumber1",
            responseAllPerson.getBody().stream()
                    .filter(e -> e.getRegistrationNumber().equals("registrationNumber1"))
                    .findFirst().orElse(new Person())
            .getRegistrationNumber());
    Assert.assertEquals("registrationNumber2",
            responseAllPerson.getBody().stream()
                    .filter(e -> e.getRegistrationNumber().equals("registrationNumber2"))
                    .findFirst().orElse(new Person())
                    .getRegistrationNumber());
  }

  @Test
  public void shouldGetById() throws Exception {
    HttpEntity<Object> person = getHttpEntity(
            "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\","
                    + " \"registrationNumber\": \"51DCT\",\"registrationDate\":\"2018-08-08T12:12:12\" }");
    ResponseEntity<Person> responseCreate = template.postForEntity(
            "/api/person", person, Person.class);

    ResponseEntity<Person> responseGet = template.getForEntity(
            "/api/person/{id}", Person.class, responseCreate.getBody().getId());

    personRepository.deleteById(responseCreate.getBody().getId());
    Assert.assertEquals(responseCreate.getBody().getId(), responseGet.getBody().getId());
    Assert.assertEquals("51DCT", responseGet.getBody().getRegistrationNumber());
    Assert.assertEquals(200,responseGet.getStatusCode().value());
  }

  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }

}
