
import java.util.Random;

class SupermarketSimulation {

    int numSuper;
    int numExp;
    int numStandLines;
    double arrivalRate;
    int maxItems;
    int maxSimTime;

    CheckoutCounter superexpress;
    CheckoutCounter[] express;
    CheckoutCounter[] standard;

    int currentTime;

    public SupermarketSimulation(int numSuper, int numExp, int numStandLines, double arrivalRate, int maxItems, int maxSimTime) {
        this.numSuper = numSuper;
        this.numExp = numExp;
        this.numStandLines = numStandLines;
        this.arrivalRate = arrivalRate;
        this.maxItems = maxItems;
        this.maxSimTime = maxSimTime;

        this.superexpress = new CheckoutCounter();
        this.express = new CheckoutCounter[2];
        this.express[0] = new CheckoutCounter();
        this.express[1] = new CheckoutCounter();
        this.standard = new CheckoutCounter[numStandLines];

        for (int i = 0; i < numStandLines; i++) {
            standard[i] = new CheckoutCounter();
        }

        this.currentTime = 0;
    }

    public void runSimulation() {
        Random rand = new Random();

        while (currentTime < maxSimTime) {
            // Simulate customer arrivals
            double arrivalProbability = rand.nextDouble();
            System.out.println("current time" + currentTime + "arrival probability" + arrivalProbability);
            if (arrivalProbability < (arrivalRate * (1.0/ 3600.0))) {
                int items = rand.nextInt(maxItems) + 1;
                Customer customer = new Customer(items, currentTime);
                System.out.println("Customer Arrived - Items: " + items + ", Arrival Time: " + currentTime);

                // Assign customer to the appropriate checkout counter
                if (items <= numSuper) {
                    assignCustomerToCounter(superexpress, customer);
                } else if (items <= numExp) {
                    assignCustomerToCounter(express[0], express[1], customer);
                } else {
                    int shortestStandardLineIndex = findShortestStandardLine();
                    assignCustomerToCounter(standard[shortestStandardLineIndex], customer);
                }
            }

            // Process customers at each checkout counter
            superexpress.processCustomers(currentTime);
            express[0].processCustomers(currentTime);
            express[1].processCustomers(currentTime);
            for (CheckoutCounter standLine : standard) {
                standLine.processCustomers(currentTime);
            }

            currentTime++;
        }

        // Print statistics
        printStatistics(superexpress, "Superexpress");
        printStatistics(express[0], "Express 1");
        printStatistics(express[1], "Express 2");
        for (int i = 0; i < numStandLines; i++) {
            printStatistics(standard[i], "Standard " + (i + 1));
        }
        printOverallStatistics();
    }

    private void assignCustomerToCounter(CheckoutCounter counter, Customer customer) {
        counter.addCustomer(customer, currentTime);
    }

    private void assignCustomerToCounter(CheckoutCounter counter1, CheckoutCounter counter2, Customer customer) {
        // Assign to the counter with the shorter line
        if (counter1.line.size() <= counter2.line.size()) {
            counter1.addCustomer(customer, currentTime);
        } else {
            counter2.addCustomer(customer, currentTime);
        }
    }

    private int findShortestStandardLine() {
        int shortestLineIndex = 0;
        int minLength = standard[0].line.size();

        for (int i = 1; i < numStandLines; i++) {
            if (standard[i].line.size() < minLength) {
                minLength = standard[i].line.size();
                shortestLineIndex = i;
            }
        }

        return shortestLineIndex;
    }

    private void printStatistics(CheckoutCounter counter, String counterType) {
        System.out.println(counterType + " Statistics:");
        System.out.println("  Average waiting time: " + calculateAverage(counter.totalWaitTime, counter.totalCustomers));
        System.out.println("  Maximum line length: " + counter.maxLength);
        System.out.println("  Customers per hour: " + calculateRate(counter.totalCustomers));
        System.out.println("  Items processed per hour: " + calculateRate(counter.totalItemsProcessed));
        System.out.println("  Average free time: " + calculateAverage(counter.totalFreeTime, currentTime - 1));
        System.out.println();
    }

    private void printOverallStatistics() {
        System.out.println("Overall Statistics:");
        int totalCustomers = superexpress.totalCustomers + express[0].totalCustomers
                + express[1].totalCustomers;
        int totalItemsProcessed = superexpress.totalItemsProcessed + express[0].totalItemsProcessed
                + express[1].totalItemsProcessed;

        System.out.println("  Average waiting time: " + calculateAverage(
                superexpress.totalWaitTime + express[0].totalWaitTime + express[1].totalWaitTime,
                totalCustomers));
        System.out.println("  Maximum line length: " + Math.max(superexpress.maxLength,
                Math.max(express[0].maxLength, express[1].maxLength)));
        System.out.println("  Customers per hour: " + calculateRate(totalCustomers));
        System.out.println("  Items processed per hour: " + calculateRate(totalItemsProcessed));
        System.out.println("  Average free time: " + calculateAverage(
                superexpress.totalFreeTime + express[0].totalFreeTime + express[1].totalFreeTime,
                currentTime - 1));
    }

    private double calculateAverage(int total, int count) {
        return count == 0 ? 0 : (double) total / count;
    }

    private double calculateRate(int count) {
        return (double) count / currentTime * 3600;
    }
}
