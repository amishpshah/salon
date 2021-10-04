package com.salon.model;

import java.util.Objects;

public class Customer {
  private String name;
  private long waiting; //in seconds
  private long arrivalTime;
  private long timeTaken;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Customer customer = (Customer) o;
    return Objects.equals(name, customer.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  public long getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(long arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public String getFullName() {
    return "Customer-" + name;
  }
  public long getWaiting() {
    return waiting;
  }

  public void setWaiting(long waiting) {
    this.waiting = waiting;
  }

  public Customer(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public long getTimeTaken() {
    return timeTaken;
  }

  public void setTimeTaken(long timeTaken) {
    this.timeTaken = timeTaken;
  }


}
