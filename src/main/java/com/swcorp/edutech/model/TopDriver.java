package com.swcorp.edutech.model;


import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Entity
@Table(name = "top_driver")
public class TopDriver implements Serializable{

  private static final long serialVersionUID = 601548380514451765L;

  public TopDriver() {}

  public TopDriver(String name, @Email String email, Long minuteTotal, Long maxRideDuration) {
    this.name = name;
    this.email = email;
    this.minuteTotal = minuteTotal;
    this.maxRideDuration = maxRideDuration;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  String name;

  @Email
  @Column(name = "email")
  String email;

  @Column(name = "minute_total")
  Long minuteTotal;

  @Column(name = "max_ride_duration")
  Long maxRideDuration;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getMinuteTotal() {
    return minuteTotal;
  }

  public void setMinuteTotal(Long minuteTotal) {
    this.minuteTotal = minuteTotal;
  }

  public Long getMaxRideDuration() {
    return maxRideDuration;
  }

  public void setMaxRideDuration(Long maxRideDuration) {
    this.maxRideDuration = maxRideDuration;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((minuteTotal == null) ? 0 : minuteTotal.hashCode());
    result = prime * result + ((maxRideDuration == null) ? 0 : maxRideDuration.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TopDriver other = (TopDriver) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (minuteTotal == null) {
      if (other.minuteTotal != null)
        return false;
    } else if (!minuteTotal.equals(other.minuteTotal))
      return false;
    if (maxRideDuration == null) {
      if (other.maxRideDuration != null)
        return false;
    } else if (!maxRideDuration.equals(other.maxRideDuration))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TopDriver [id=" + id + ", name=" + name + ", email=" + email + ", minuteTotal=" + minuteTotal + ", maxRideDuration=" + maxRideDuration + "]";
  }
}
