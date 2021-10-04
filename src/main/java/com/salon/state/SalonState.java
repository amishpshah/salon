package com.salon.state;

import com.salon.model.Barber;
import com.salon.model.Customer;
import com.salon.util.EventLogger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import jdk.jfr.Event;

public class SalonState {
  private String name;
  private Set<Barber> currentShift = new HashSet<Barber>();
  private List<Customer> waitingCustomers = new ArrayList<>();
  private List<Customer> servicingCustomers = new ArrayList<>();

  public Set<Barber> getCurrentShift() {
    return currentShift;
  }

  public List<Customer> getWaitingCustomers() {
    return this.waitingCustomers;
  }

  public void clearWaitingCustomers() {
    this.waitingCustomers.clear();
  }

  public int getTotalCustomerCount() {
    return waitingCustomers.size() + servicingCustomers.size();
  }

  public void addCustomer(long when, Customer customer) {
    waitingCustomers.add(customer);
    customer.setWaiting(when);
  }

  public void addBarber(long when, Barber barber) {
    currentShift.add(barber);
    EventLogger.log(when, String.format("%s started shift", barber.getName()));
  }

  public void removeBarber(long when, Barber barber) {
    currentShift.remove(barber);
    EventLogger.log(when, String.format("%s ended shift", barber.getName()));
  }

  public Barber getFirstBarberAvailable() {
    Iterator<Barber> iter = currentShift.iterator();
    while(iter.hasNext()) {
      Barber barber = iter.next();
      if (!barber.getBarberState().isOccupied()) {
        return barber;
      }
    }
    return null;
  }

  public void moveCustomerToBarber(long when, Customer customer, Barber barber) {
    waitingCustomers.remove(customer);
    customer.setWaiting(0);
    BarberState state = barber.getBarberState();
    state.setOccupied(true);
    state.setCustomer(customer);
    state.setStartTime(when);
    state.setEndTime(when + customer.getTimeTaken());
    servicingCustomers.add(customer);
    String event = String.format("%s started cutting %s's hair", barber.getName(), customer.getFullName());
    EventLogger.log(when, event);
  }

  public List<Customer> getCustomersWhoAreFinished(long when) {
    List<Customer> customers = new ArrayList<>();
    Iterator<Barber> barberIter = currentShift.iterator();
    while (barberIter.hasNext()) {
      BarberState barberState = barberIter.next().getBarberState();
      if (barberState.isOccupied() && when >= barberState.getEndTime() ) {
        Customer customer = barberState.getCustomer();
        customers.add(customer);
        barberState.reset();
        servicingCustomers.remove(customer);
        String event = String.format("%s leaves satisfied", customer.getFullName());
        EventLogger.log(when, event);
      }
    }
    return customers;
  }

  public void updateMinuteForWaitingCustomers() {
    Iterator<Customer> iterator = waitingCustomers.iterator();
    while (iterator.hasNext()) {
      Customer customer = iterator.next();
      customer.setWaiting(customer.getWaiting() + 60);
    }
  }

  public List<Customer> getCustomersWhoAreLeaving(long when) {
    List<Customer> leaving = new ArrayList<>();
    Iterator<Customer> iterator = waitingCustomers.iterator();
    while (iterator.hasNext()) {
      Customer customer = iterator.next();
      if (customer.getWaiting() > 30*60*60) {
        iterator.remove();
        leaving.add(customer);
        EventLogger.log(when, String.format("%s leaves frustrated", customer.getFullName()));
      }
    }
    return leaving;
  }


}
