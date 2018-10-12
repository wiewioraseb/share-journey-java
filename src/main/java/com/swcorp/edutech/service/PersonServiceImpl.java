package com.swcorp.edutech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.swcorp.edutech.model.Person;
import com.swcorp.edutech.repositories.PersonRepository;


@Service
public class PersonServiceImpl implements PersonService{

  @Autowired
  PersonRepository personRepository;

  @Override
  public List<Person> getAll() {
    List<Person> personList = new ArrayList<>();
    personRepository.findAll().forEach(personList::add);
    return personList;
  }
  
  public Person save(Person p) {
    return personRepository.save(p);
  }

  @Override
  public Person findById(Long personId) {
    Optional<Person> dbPerson = personRepository.findById(personId);
    return dbPerson.orElse(null);
  }
  
  

}
