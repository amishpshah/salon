package com.salon.model;

import com.salon.state.BarberState;
import com.salon.state.SalonState;
import com.salon.util.EventLogger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Salon {
  private List<Barber> firstShift = new ArrayList<Barber>();
  private List<Barber> secondShift = new ArrayList<Barber>();
  private SalonState salonState;
  private String shopName;

  public Salon(String shopName) {
    this.shopName = shopName;
    salonState = new SalonState();
    init();
  }

  public void openShop(long when) {
    EventLogger.log(when, String.format("%s is open for business", shopName));
  }

  public void closeShop(long when) {
    EventLogger.log(when, String.format("%s is closed", shopName));
  }

  public void startShift(long when, Barber barber) {
    salonState.addBarber(when, barber);
    barber.setCurrentShift(true);
  }

  public void endShift(long when, Barber barber) {
    barber.setCurrentShift(false);
  }

  public void leaveChair(long when, Barber barber) {
    salonState.removeBarber(when, barber);
  }


  public void init() {
    firstShift.add(new Barber("Adam"));
    firstShift.add(new Barber("Bob"));
    firstShift.add(new Barber("Charles"));
    firstShift.add(new Barber("David"));

    secondShift.add(new Barber("Fred"));
    secondShift.add(new Barber("Gerald"));
    secondShift.add(new Barber("Howard"));
    secondShift.add(new Barber("James"));

  }



  public void processDay(List<Customer> customers) {
    long when = 0;
    long minute = 60;
    long shiftChange = minute*4*60;
    int secondShiftIndex = 0;
    long closeTime = minute * 60 * 8;
    openShop(when);
    startShift(when, firstShift.get(0));
    startShift(when, firstShift.get(1));
    startShift(when, firstShift.get(2));
    startShift(when, firstShift.get(3));
    long maxTime = (minute * 60 * 8) + 20*minute;
    while (when < maxTime && !salonState.getCurrentShift().isEmpty()) {
      //System.out.println("" + when);
      //customer who are done with their barbar
      salonState.getCustomersWhoAreFinished(when);

      //shift change from first to second to close
      secondShiftIndex = shiftChange(when, secondShiftIndex, closeTime);

      // customer enter event
      customerEnterEventHandler(customers, when, closeTime);

      //assign customer to barbar
      assignCustomerToBarber(when);

      //customers who leave frustrated
      salonState.getCustomersWhoAreLeaving(when);
      when += minute;
      //update minute for each customer
      salonState.updateMinuteForWaitingCustomers();

      // if customer is still waiting they must leave at closing
      if (when == closeTime) {
        for (Customer customer: salonState.getWaitingCustomers()) {
            EventLogger.log(when, String.format("%s leaves cursing", customer.getFullName()));
        }
        salonState.clearWaitingCustomers();
      }

      //assign shift change
      if (when == shiftChange) {
        //Shift change
        Set<Barber> currentBarbers = salonState.getCurrentShift();
        for(Barber barber: currentBarbers) {
          barber.setCurrentShift(false);
        }
        shiftChange = closeTime;
      }

    }
    closeShop(when);
  }

  //Barber is free
  private void assignCustomerToBarber (long when) {
    Barber barber = salonState.getFirstBarberAvailable();
    Iterator<Customer> waitingIter = salonState.getWaitingCustomers().iterator();
    while (barber != null && waitingIter.hasNext()) {
      salonState.moveCustomerToBarber(when, waitingIter.next(), barber);
      barber = salonState.getFirstBarberAvailable();
      waitingIter = salonState.getWaitingCustomers().iterator();
    }
  }

  private void customerEnterEventHandler(List<Customer> customers, long when, long closeTime) {
    Iterator<Customer> custIter = customers.iterator();
    while (custIter.hasNext()) {
      Customer customer = custIter.next();

      if (when >= customer.getArrivalTime()) {
        if (customer.getArrivalTime() >= closeTime) {
          EventLogger.log(when, String.format("%s leaves disappointed", customer.getFullName()));
          continue;
        }
        EventLogger.log(when, String.format("%s entered", customer.getFullName()));
        custIter.remove();

        if (salonState.getTotalCustomerCount() >= 10) {
          EventLogger.log(when, String.format("%s leaves unfulfilled", customer.getFullName()));
        } else {
          salonState.addCustomer(when, customer);
        }
      }
    }
  }

  private int shiftChange(long when, int secondShiftIndex, long closeTime) {
    Set<Barber> currentBarbers = salonState.getCurrentShift();
    List<Barber> endShiftBarbers = new ArrayList<>();
    for(Barber barber: currentBarbers) {
      if (!barber.isCurrentShift() && !barber.getBarberState().isOccupied()) {
        endShiftBarbers.add(barber);
      }
    }
    //remove the barbers

    for(Barber barber: endShiftBarbers) {
      leaveChair(when, barber);
      if (when < closeTime) {
        startShift(when, secondShift.get(secondShiftIndex++));
      }
    }
    return secondShiftIndex;
  }

}
