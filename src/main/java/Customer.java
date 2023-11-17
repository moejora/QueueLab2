class Customer {
    int items;
    int arrivalTime;
    int waitTime;
    int processingTime;

    public Customer(int items, int arrivalTime) {
        this.items = items;
        this.arrivalTime = arrivalTime;
        this.waitTime = 0;
        this.processingTime = items * 5; // 5 seconds per item
    }
}