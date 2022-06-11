package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    // Instantiate an accountService for handling all account related app functions
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);

        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            // on successful login, set the current user in the accountService
            accountService.setCurrentUser(currentUser);
            consoleService.setCurrentUser(currentUser);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        // Print out account balance of currently logged in user
        System.out.println("Your current balance is: " + accountService.getUserBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		// View transfers that were sent or received by user
        consoleService.printCompletedTransfers();
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// Display all users
        consoleService.printAllUsers();
        Long receiverId = consoleService.promptForUserId("Enter id of user you are sending to (0 to cancel): ");
        if (receiverId == 0) {
            return;
        } else if (currentUser.getUser().getId().equals(receiverId)) {
            System.err.println("You can not send money to yourself. Please try again.");
            return;
        }
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to send: ");
        Transfer transfer = new Transfer(currentUser.getUser().getId(), receiverId, TransferType.SEND, TransferStatus.APPROVED, amount);
        if (accountService.sendTransfer(transfer)) {
            System.out.println("Transfer complete.");
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
