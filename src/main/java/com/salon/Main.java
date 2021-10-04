package com.salon;

import com.salon.model.Customer;
import com.salon.model.Salon;
import com.salon.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {



    public static void main(String[] args) {
//        List<Customer> statusCustomerList = new ArrayList();
//        statusCustomerList.addAll(customerList);
        try {
            List<Customer> customerList = null;
            if (args.length > 0) {
                Scanner scanner = new Scanner();
                customerList = scanner.parseCustomers(args[0]);
            } else {
                customerList = generateCustomers();
            }
            Salon salon = new Salon("Smart Cuts");
            salon.processDay(customerList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Customer> generateCustomers() {
        List<Customer> customerList = new ArrayList();
        int customerIndex = 1;
        Random random = new Random();
        while (customerIndex <= 300) {
            Customer customer = new Customer("" + customerIndex);
            customer.setArrivalTime(random.nextInt(60*8*60));
            customer.setTimeTaken(random.nextInt(20*60));
            customerIndex++;
            customerList.add(customer);
        }
        return customerList;
    }
}
