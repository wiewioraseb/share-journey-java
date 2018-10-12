package com.swcorp.edutech.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.swcorp.edutech.model.Person;
import com.swcorp.edutech.service.PersonService;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
public class PersonController {

  private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);

  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping(path = "/api/person")
  public ResponseEntity<Person> register(@Valid @RequestBody Person p, UriComponentsBuilder ucb) {
    LOG.info("Register person: {}", p);
    Person personResult = personService.save(p);
    URI locationUri =
            ucb.path("/api/person/")
                    .path(String.valueOf(personResult.getId()))
                    .build()
                    .toUri();
    return ResponseEntity.created(locationUri).body(personResult);
  }
  
  @GetMapping(path = "/api/person")
  public ResponseEntity<List<Person>> getAllPersons() {
    return ResponseEntity.ok(personService.getAll());
  }
  
  @GetMapping(path = "/api/person/{person-id}")
  public ResponseEntity<Person> getPersonById(@PathVariable(name="person-id", required=true)Long personId) {
    Person person = personService.findById(personId);
    if (person != null) {
      return ResponseEntity.ok(person);
    }
    return ResponseEntity.notFound().build();
  }
  
}
