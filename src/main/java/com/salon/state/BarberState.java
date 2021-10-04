package com.salon.state;

import com.salon.model.Customer;

public class BarberState {
  private boolean occupied;
  private long startTime;
  private long endTime;
  private Customer customer;

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public void setOccupied(boolean occupied) {
    this.occupied = occupied;
  }

  public BarberState() {
    reset();
  }

  public void reset() {
    occupied = false;
    startTime = 0;
    endTime = 0;
    customer = null;
  }


}
