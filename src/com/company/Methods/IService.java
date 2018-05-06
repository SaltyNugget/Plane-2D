package com.company.Methods;

import com.company.Objects.Customer;

public interface IService {
    Customer[][] getPlaneSeatings();
    void savePlaneSeatings(Customer[][] arrangement);
    String displayPlaneSeatingsFromFile(Customer[][] arrangement);
    void storePlaneSeatings(char type, int position);
    Customer[] getAllCustomers(Customer[][] arrangement);

    void allocateSeat();
    void cancelSeat();
    void search();
    void displayAllCustomers(Customer[] customers);
}
