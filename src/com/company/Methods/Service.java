package com.company.Methods;

import java.io.*;
import java.util.*;
import com.company.Objects.Customer;


public class Service implements IService{
    private final static String PATH = System.getProperty("user.dir") + "\\";
    private final static String C_PATH = PATH + "planeSeatings.dat";
    private final static String D_PATH = PATH + "seatingsDisplayer.dat";
    private static Scanner scanner = new Scanner(System.in);
    private final char[] ColumCHAR = { 'A', 'B', 'C', 'D', 'E', 'F' };



    /** Load from Text File */
    public Customer[][] getPlaneSeatings() {
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(C_PATH))) {
            Customer[][] fileCustomers = (Customer[][]) input.readObject();
            input.close();
            return fileCustomers;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            savePlaneSeatings(new Customer[12][6]);
            return getPlaneSeatings();
        }
    }

    /** Save to text file. */
    public void savePlaneSeatings(Customer[][] arrangement) {
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(C_PATH))) {
            output.writeObject(arrangement);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /** Display stored in Random access and binary file */
    public void storePlaneSeatings(char type, int position) {
        try(RandomAccessFile file = new RandomAccessFile(D_PATH,"rwd")) {
            file.seek(position);
            file.write(type);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /** Display Seating and what seats are taken */
    public String displayPlaneSeatingsFromFile(Customer[][] arrangement) {
        String displayer = "";
        for (int i = 0; i < 11; i++) {
            if (i >= 5) {
                displayer += ColumCHAR[i - 5];
                if (i == 7) { displayer += " "; }
            } displayer += " ";
        }
        displayer += "\n";

        for (int x = arrangement.length - 1; x >= 0; x--) {
            displayer += x + 1 + " ";
            for (int i = 0; i < 4 - String.valueOf(x + 1).length(); i++) { displayer += " "; }
            for (int y = 0; y < 6; y++) {
                if (arrangement[x][y] != null) { displayer += arrangement[x][y].getClassType() + " "; }
                else { displayer += "# "; }
                if (y == 2) { displayer += "  "; }
            }
            displayer += "\n";
        }
        return displayer;
    }

    /** display all customer from the file and the new ones in order of name */
    public void displayAllCustomers(Customer[] customers) {

        System.out.println("Customers: ");
        for (int i = 0; i < customers.length; i++) {
            System.out.println("    Name: " + customers[i].getcName() + ".");
            System.out.println("    Customer Type: " + customers[i].getInformation()[1] + ".");
            System.out.println("    Class Type: " + customers[i].getInformation()[2] + ".");
            System.out.println("------------");
        }
    }

    /** Method to find all customers seating and order name asced */
    public Customer[] getAllCustomers(Customer[][] arrangement) {
        List<Customer> customers = new ArrayList<>();
        for (int x = 0; x < arrangement.length; x++) {
            for (int y = 0; y < 6; y++) {
                if (arrangement[x][y] != null) {
                    customers.add(arrangement[x][y]);
                }
            }
        }
        customers.sort(new Comparator<Customer>() {
            @Override public int compare(Customer c1, Customer c2) {
                return c1.getcName().compareToIgnoreCase(c2.getcName());
            }
        });
        return Arrays.copyOf(customers.toArray(), customers.size(), Customer[].class);
    }

    /** Customer maker method*/
    public void allocateSeat() {
        try {
            System.out.print("\n--------------------------");

            //name
            System.out.print("\nName: ");
            String name = scanner.nextLine();

            //adult or child
            System.out.print("Adult or child? (A/C): ");
            char customerType = scanner.nextLine().toUpperCase().charAt(0);
            if (!String.valueOf(customerType).matches("[aAcC]")) { throw new Exception(""); }

            //first, business or economy
            System.out.print("First, business or economy class? (F/B/E): ");
            char classType = scanner.nextLine().toUpperCase().charAt(0);
            if (!String.valueOf(classType).matches("[fFbBeE]")) { throw new Exception(""); }

            //seat preference
            System.out.print("Seat preference? Aisle, middle or window? (A/M/W): ");
            char seatType = scanner.nextLine().toUpperCase().charAt(0);
            if (!String.valueOf(seatType).matches("[aAmMwW]")) { throw new Exception(""); }

            assignSeating(new Customer(name, customerType, classType, seatType));

        } catch (Exception ex) {
            System.out.print("Something bad happened T_T " + ex.getMessage() + "\nPress ENTER to continue");
            scanner.nextLine();
        }
    }

    /** Picks a random slot to give to the customer from the pref*/
    private void assignSeating(Customer customer) {
        Customer[][] arrangement = getPlaneSeatings();
        int selectedRow = -1;
        int selectedColumn = -1;

        //finds out a row and coloum that is empty that fits the selected options
        for (int row = arrangement.length - 1; row >= 0; row--) {
            if ((row <= 1 && customer.getClassType() == 'F') || (row >= 2 && row <= 5 && customer.getClassType() == 'B') || (row >= 6 && customer.getClassType() == 'E')) {
                selectedRow = row;
                if (customer.getSeatType() == 'W') {
                    if (arrangement[row][0] == null) { selectedColumn = 0; }
                    else if (arrangement[row][5] == null) { selectedColumn = 5; }
                }
                else if (customer.getSeatType() == 'M') {
                    if (arrangement[row][1] == null) { selectedColumn = 1; }
                    else if (arrangement[row][4] == null) { selectedColumn = 4; }
                }
                else if (customer.getSeatType() == 'A') {
                    if (arrangement[row][2] == null) { selectedColumn = 2; }
                    else if (arrangement[row][3] == null) { selectedColumn = 3; }
                }
            }
        }

        //seat that the customer gets
        try {
            if (selectedRow == -1 || selectedColumn == -1) { throw new Exception("No seats available for your preference. Sorry."); }
            else {
                arrangement[selectedRow][selectedColumn] = customer;
                savePlaneSeatings(arrangement);
                storePlaneSeatings(customer.getcType(), selectedRow*6 + selectedColumn + 1);
                System.out.println("Customer successfully assigned.");
            }
        } catch (Exception ex) {
            System.out.println("An exception has occurred: " + ex.getMessage());
        } finally {
            System.out.println("Press ENTER to continue...");
            scanner.nextLine();
        }
    }

    /** method to cancer seating */
    public void cancelSeat() {
        Customer[][] arrangement = getPlaneSeatings();
        try {
            //row number
            System.out.print("Select row number: ");
            int selectedRow = scanner.nextInt() - 1;
            if (selectedRow > arrangement.length) { throw new Exception("Row number doesn't exist."); }

            //colum letter
            System.out.print("Select column letter: ");
            char selection = new Scanner(System.in).nextLine().toUpperCase().charAt(0);
            if (!String.valueOf(selection).matches("[aAbBcCdDeEfF]")) { throw new Exception("Column letter doesn't exist."); }
            int selectedColumn = new String(ColumCHAR).indexOf(selection);

            if (arrangement[selectedRow][selectedColumn] != null) {
                arrangement[selectedRow][selectedColumn] = null;
                savePlaneSeatings(arrangement);
                storePlaneSeatings('*', selectedRow*6 + selectedColumn + 1);
                System.out.println("Customer successfully removed.");
            } else {
                System.out.println("No customer in that seat");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\nPress ENTER to continue...");
        } finally {
            new Scanner(System.in).nextLine();
        }
    }

    /** Search Method for the customers*/
    public void search() {
        Customer[][] arrangement = getPlaneSeatings();
        Customer[] customers = getAllCustomers(arrangement);
        String[] names = new String[customers.length];
        for (int i = 0; i < customers.length; i++) { names[i] = customers[i].getcName(); }

        //Name
        System.out.print("Customer Name: ");
        String name = scanner.nextLine();
        int pos = Arrays.binarySearch(names, name);

        //information displayed
        if (pos >= 0) {
            Customer customer = customers[pos];
            System.out.println("--------------");
            System.out.println("Customer found. \nName: " + customer.getInformation()[0] + ". [" + customer.getInformation()[1] + "]\nClass: " + customer.getInformation()[2] + ".\nSeat: " + customer.getInformation()[3] + ".");

            //Where the customer is sitting
            for (int row = 0; row < arrangement.length; row++) {
                for (int col = 0; col < arrangement[row].length; col++) {
                    if (arrangement[row][col] == customer) {
                        System.out.println("Position: " + row + 1 + "-" + ColumCHAR[col]);
                    }
                }
            }
        } else {
            System.out.println("No results found.");
        }
        scanner.nextLine();
    }


}
