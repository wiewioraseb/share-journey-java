package com.swcorp.edutech.dto;

public class TopDriverDTO {
  
  /**
   * Constructor for TopDriverDTO
   * @param name
   * @param email
   * @param totalRideDurationInMinutes
   * @param maxRideDurationInMinutes
   * @param averageDistance
   */
  public TopDriverDTO(String name, 
      String email, 
      Long totalRideDurationInMinutes,
      Long maxRideDurationInMinutes,
      Double averageDistance) {
    this.setName(name);
    this.setEmail(email);
    this.setAverageDistance(averageDistance);
    this.setMaxRideDurationInMinutes(maxRideDurationInMinutes);
    this.setTotalRideDurationInMinutes(totalRideDurationInMinutes);
    
  }
  
  public TopDriverDTO() {
    
  }
  
  private String name;
  
  private String email;
  
  private Long totalRideDurationInMinutes;
  
  private Long maxRideDurationInMinutes;
  
  private Double averageDistance;

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

  public Long getTotalRideDurationInMinutes() {
    return totalRideDurationInMinutes;
  }

  public void setTotalRideDurationInMinutes(Long totalRideDurationInMinutes) {
    this.totalRideDurationInMinutes = totalRideDurationInMinutes;
  }

  public Long getMaxRideDurationInMinutes() {
    return maxRideDurationInMinutes;
  }

  public void setMaxRideDurationInMinutes(Long maxRideDurationInMinutes) {
    this.maxRideDurationInMinutes = maxRideDurationInMinutes;
  }

  public Double getAverageDistance() {
    return averageDistance;
  }

  public void setAverageDistance(Double averageDistance) {
    this.averageDistance = averageDistance;
  }
  
  
    
}
