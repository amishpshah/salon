package com.salon.util;

import com.salon.model.Customer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class Scanner {

  public static List<Customer> parseCustomers(String file) throws IOException {
    List<Customer> customers = new ArrayList<>();
    String contents = FileUtils.readFileToString(new File(file), "utf8");
    String[] lines = contents.split("\n");
    int customerNumber = 1;
    for (String line: lines) {
      String[] tokens = line.split(";");
      Customer customer = new Customer(customerNumber +"");
      if (tokens.length != 2) {
        continue;
      }
      customer.setArrivalTime(Integer.parseInt(tokens[0]));
      customer.setTimeTaken(Integer.parseInt(tokens[1]));
      customers.add(customer);
      customerNumber++;
    }
    return customers;
  }
}
