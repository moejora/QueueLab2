import java.util.LinkedList;
import java.util.Queue;

class CheckoutCounter {
    Queue<Customer> line;
    int totalCustomers;
    int totalItemsProcessed;
    int totalWaitTime;
    int maxLength;
    int totalFreeTime;

    public CheckoutCounter() {
        this.line = new LinkedList<>();
        this.totalCustomers = 0;
        this.totalItemsProcessed = 0;
        this.totalWaitTime = 0;
        this.maxLength = 0;
        this.totalFreeTime = 0;
    }

    public void addCustomer(Customer customer, int currentTime) {
        if (line.isEmpty()) {
            customer.waitTime = 0;
        } else {
            customer.waitTime = currentTime - customer.arrivalTime;
        }

        line.add(customer);
        totalCustomers++;
        totalItemsProcessed += customer.items;
        totalWaitTime += customer.waitTime;
        maxLength = Math.max(maxLength, line.size());
    }

    public void processCustomers(int currentTime) {
        if (!line.isEmpty()) {
            Customer currentCustomer = line.poll();
            totalFreeTime += currentTime - (currentCustomer.arrivalTime + currentCustomer.waitTime);
        }
    }
}