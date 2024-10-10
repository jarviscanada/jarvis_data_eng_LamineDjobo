package ca.jrvs.apps.stockquote.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.Scanner;


public class StockQuoteController {
    private static final Logger logger = LogManager.getLogger(StockQuoteController.class);

    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            logger.info("Initializing Stock Quote App client");

            System.out.println("**********************");

            System.out.println("Welcome to the Stock Quote App! Choose an option:");
            System.out.println("1. View Stock Information");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. Exit");

            System.out.println("**********************");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    viewStockInformation(scanner);
                    break;
                case "2":
                    buyStock(scanner);
                    break;
                case "3":
                    sellStock(scanner);
                    break;
                case "4":
                    logger.info("Exiting Stock Quote App");
                    System.out.println("Thank you for using the Stock Quote App. Goodbye!");
                    return;
                default:
                    logger.warn("Invalid option selected: " + choice);
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
                    break;
            }
        }
    }

    /**
     * View stock information before making a decision.
     */
    private void viewStockInformation(Scanner scanner) {
        logger.info("Fetching stock information");
        System.out.println("Available stocks :");

        // Fetch all available stock quotes from the database
        Iterable<Quote> allQuotes = quoteService.getAllQuotes();

        if (!allQuotes.iterator().hasNext()) {
            System.out.println("No stocks available in the database.");
            return;
        }

        // Display all available stocks
        for (Quote quote : allQuotes) {
            System.out.println("Ticker: " + quote.getTicker() + " | Current Price: " + quote.getPrice());
        }

        // Ask user to input the stock ticker they want to view
        System.out.println("Enter the ticker symbol of the stock to view detailed information:");
        String ticker = scanner.nextLine();

        // Fetch detailed stock information from API
        Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(ticker);

        if (quote.isPresent()) {
            logger.info("Displaying stock information for " + ticker);
            System.out.println("Stock Information for " + ticker + ":");
            System.out.println("Current Price: " + quote.get().getPrice());
            System.out.println("Open: " + quote.get().getOpen());
            System.out.println("High: " + quote.get().getHigh());
            System.out.println("Low: " + quote.get().getLow());
            System.out.println("Volume: " + quote.get().getVolume());

            // Fetch the user's position for this stock
            Optional<Position> position = positionService.getPositionForStock(ticker);
            if (position.isPresent()) {
                double pricePaidPerShare = position.get().getValuePaid() / position.get().getNumOfShares();
                System.out.println("You own " + position.get().getNumOfShares() + " shares.");
                System.out.println("Price paid per share: $" + pricePaidPerShare);
                System.out.println("Total amount paid: $" + position.get().getValuePaid());
            } else {
                logger.warn("No stock for ticker: " + ticker + "found in the portfolio");
                System.out.println("You do not own any shares of this stock.");
            }

        } else {
            logger.warn("No stock information found for ticker: " + ticker);
            System.out.println("No data found for ticker symbol: " + ticker);
        }
    }


    /**
     * Buy stock for a given ticker.
     */
    private void buyStock(Scanner scanner) {
        logger.info("Starting buy stock process");
        // Display all available stocks before asking the user to buy
        System.out.println("Available stocks to buy:");

        // Fetch all available stock quotes from the database
        Iterable<Quote> allQuotes = quoteService.getAllQuotes();

        if (!allQuotes.iterator().hasNext()) {
            System.out.println("No stocks available to buy.");
            return;
        }

        // Display all available stocks
        for (Quote quote : allQuotes) {
            System.out.println("Ticker: " + quote.getTicker() + " | Current Price: " + quote.getPrice() + "\n");
        }

        // Ask user to input the stock ticker they want to buy
        System.out.println("Enter the ticker symbol of the stock you want to buy:");
        String ticker = scanner.nextLine();

        // Use the new method to handle the number of shares input
        int shares = promptForShares(scanner);

        // Ask user to input the price they are willing to pay per share
        System.out.println("Enter the price per share you are willing to pay:");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            logger.error("Error during stock purchase: " + e.getMessage(), e);
            System.out.println("Invalid price. Please enter a valid number.");
            return;
        }

        try {
            // Attempt to buy the stock through the position service
            Position position = positionService.buy(ticker, shares, price);
            System.out.println("You have successfully bought " + shares + " shares of " + ticker + " at $" + price + " per share.");
            System.out.println("Your new position: " + position.getNumOfShares() + " shares, total paid: $" + position.getValuePaid() + "\n");
        } catch (IllegalArgumentException e) {
            logger.error("Error during stock purchase: " + e.getMessage(), e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Example of handling invalid input more gracefully
    private int promptForShares(Scanner scanner) {
        int shares = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter the number of shares you want to buy:");
            try {
                shares = Integer.parseInt(scanner.nextLine());
                if (shares <= 0) {
                    System.out.println("Number of shares must be positive. Please try again.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
        return shares;
    }



    /**
     * Sell all shares of a given ticker.
     */
    private void sellStock(Scanner scanner) {
        logger.info("Starting sell stock process");
        // Ask the user for the stock they want to sell
        System.out.println("Enter the ticker symbol of the stock you want to sell:");
        String ticker = scanner.nextLine();

        // Fetch the user's position for the given ticker
        Optional<Position> position = positionService.getPositionForStock(ticker);

        // If the user does not own the stock, display an error message
        if (!position.isPresent()) {
            System.out.println("You do not own any shares of " + ticker);
            return;
        }

        // Ask for confirmation before selling
        System.out.println("You own " + position.get().getNumOfShares() + " shares of " + ticker + ".");
        System.out.println("Are you sure you want to sell all shares? Type 'yes' to confirm, 'no' to cancel:");
        String confirmation = scanner.nextLine();

        // If the user confirms, proceed with the sale
        if (confirmation.equalsIgnoreCase("yes")) {
            positionService.sell(ticker);
            System.out.println("You have successfully sold all shares of " + ticker);
        } else {
            logger.info("Sale of " + ticker + " canceled.");
            // If the user cancels, display a cancellation message
            System.out.println("Sale of " + ticker + " canceled.");
        }
    }

}
