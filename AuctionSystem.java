import java.util.Scanner;

public class AuctionSystem {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // VEHICLE DETAILS
        System.out.print("Enter vehicle registration number: ");
        String regNo = input.nextLine();

        System.out.print("Enter vehicle cost: ");
        double vehicleCost = input.nextDouble();

        System.out.print("Enter total expenses incurred: ");
        double expenses = input.nextDouble();

        System.out.print("Enter total deposits paid: ");
        double deposits = input.nextDouble();

        // BIDDERS
        System.out.print("Enter bid from bidder 1: ");
        double bid1 = input.nextDouble();

        System.out.print("Enter bid from bidder 2: ");
        double bid2 = input.nextDouble();

        System.out.print("Enter bid from bidder 3: ");
        double bid3 = input.nextDouble();

        // FIND HIGHEST BID
        double highestBid = bid1;

        if (bid2 > highestBid) {
            highestBid = bid2;
        }
        if (bid3 > highestBid) {
            highestBid = bid3;
        }

        // CALCULATIONS
        double totalCost = vehicleCost + expenses;
        double balance = highestBid - deposits;
        double profitOrLoss = deposits - totalCost;
        double finalProfitOrLoss = highestBid - totalCost;

        // OUTPUT
        System.out.println("\nVEHICLE DETAILS");
        System.out.println("Registration Number: " + regNo);
        System.out.println("Vehicle Cost: " + vehicleCost);
        System.out.println("Expenses: " + expenses);

        System.out.println("\nAUCTION RESULTS");
        System.out.println("Highest Bid: " + highestBid);
        System.out.println("Balance Left: " + balance);

        System.out.println("\nPROFIT / LOSS");
        System.out.println("Profit/Loss before clearing balance: " + profitOrLoss);
        System.out.println("Profit/Loss after clearing balance: " + finalProfitOrLoss);

        input.close();
    }
}