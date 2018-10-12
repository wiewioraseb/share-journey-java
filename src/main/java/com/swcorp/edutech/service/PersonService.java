package com.swcorp.edutech.service;

import java.util.List;
import com.swcorp.edutech.model.Person;

public interface PersonService {
  public List<Person> getAll();
  
  public Person save(Person p);
  
  public Person findById(Long personId);
  
}
