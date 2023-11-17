import java.util.Scanner;

public class SupermarketSimulationTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of items allowed in the superexpress line:");
        int numSuper = scanner.nextInt();

        System.out.println("Enter the number of items allowed in the express line:");
        int numExp = scanner.nextInt();

        System.out.println("Enter the number of regular lines:");
        int numStandLines = scanner.nextInt();

        System.out.println("Enter the arrival rate of customers per hour:");
        double arrivalRate = scanner.nextDouble();

        System.out.println("Enter the maximum number of items:");
        int maxItems = scanner.nextInt();

        System.out.println("Enter the simulation time in hours:");
        int maxSimTime = scanner.nextInt();

        scanner.close();

        SupermarketSimulation supermarketSimulation = new SupermarketSimulation(
                numSuper, numExp, numStandLines, arrivalRate, maxItems, maxSimTime);
        
        supermarketSimulation.runSimulation();
    }
}