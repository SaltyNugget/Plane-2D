package com.company;

import java.util.InputMismatchException;
import java.util.Scanner;
import com.company.Objects.*;
import com.company.Methods.*;

public class Main {



    //menus and main of the code
    private static IService service = new Service();


    public static void main(String[] args) {
        System.out.println(service.displayPlaneSeatingsFromFile(service.getPlaneSeatings()));
        Customer[] customers = service.getAllCustomers(service.getPlaneSeatings());
        while (true) {
            try {
                System.out.print(
                        "1. Add a Customer" +
                                "\n2. Remove  Customer" +
                                "\n3. Search Customers" +
                                "\n4. Display all" +
                                "\n5. Exit" +
                                "\nInput: ");

                int userInput = new Scanner(System.in).nextInt();

                if (userInput == 1) {
                    service.allocateSeat();
                    customers = service.getAllCustomers(service.getPlaneSeatings());
                } else if (userInput == 2) {
                    System.out.println(service.displayPlaneSeatingsFromFile(service.getPlaneSeatings()));
                    service.cancelSeat();
                } else if (userInput == 3) {
                    service.search();
                } else if (userInput == 4) {
                    service.displayAllCustomers(customers);
                } else if (userInput == 5) {
                    return;
                }
            } catch (InputMismatchException ex) {

            }
        }
    }
}
