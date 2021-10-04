package com.salon.model;
import com.salon.state.BarberState;
import java.util.Objects;

public class Barber {
  private String name;
  private boolean currentShift;
  private BarberState barberState;

  public Barber(String name) {
    this.name = name;
    barberState = new BarberState();
    currentShift = false;
  }


  /**
   * only use name
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Barber barber = (Barber) o;
    return name.equals(barber.name);
  }

  /**
   * only use name
   * @return
   */
  @Override
  public int hashCode() {
    return Objects.hash(name);
  }


  public boolean isCurrentShift() {
    return currentShift;
  }

  public void setCurrentShift(boolean currentShift) {
    this.currentShift = currentShift;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BarberState getBarberState() {
    return barberState;
  }

  public void setBarberState(BarberState barberState) {
    this.barberState = barberState;
  }
}
