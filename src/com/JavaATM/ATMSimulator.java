package com.JavaATM;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * ATM Simulator - Main Entry Point Topics: OOP, Scanner, Loops, Conditionals,
 * Static methods
 */
public class ATMSimulator {
	// ─── GLOBAL ACCOUNT LIST (ArrayList - grows dynamically) ─────────────────
	static ArrayList<Account> accounts = new ArrayList<>();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		// Pre-loaded demo accounts
		accounts.add(new Account("ACC001", "Ravi Kumar", "1234", 25000.00));
		accounts.add(new Account("ACC002", "Priya Sharma", "5678", 52300.00));

		boolean running = true;

		while (running) {

			printBanner();
			showWelcomeMenu();

			System.out.print("\n  Your choice: ");
			String choice = scanner.nextLine().trim();

			switch (choice) {
			case "1":
				Account loggedIn = login();
				if (loggedIn != null) {
					showMainMenu(loggedIn);
				}
				break;

			case "2":
				createAccount();
				break;

			case "3":
				running = false;
				System.out.println("\n  Thank you for using JavaBank ATM. Goodbye!");
				break;

			default:
				System.out.println("\n  [ERROR] Invalid choice. Please enter 1, 2, or 3.");
			}

			if (running && !choice.equals("3")) {
				System.out.print("\n  Return to main screen? (yes/no): ");
				String cont = scanner.nextLine().trim().toLowerCase();
				if (cont.equals("no") || cont.equals("n")) {
					running = false;
					System.out.println("\n  Thank you for using JavaBank ATM. Goodbye!");
				}
			}
		}

		scanner.close();
	}

	// ─── WELCOME SCREEN ───────────────────────────────────────────────────────
	static void printBanner() {
		System.out.println("\n╔══════════════════════════════════╗");
		System.out.println("║        NexaPay ATM              ║");
		System.out.println("║   Secure Self-Service Terminal   ║");
		System.out.println("╚══════════════════════════════════╝");
	}

	static void showWelcomeMenu() {
		System.out.println("\n  ┌──────────────────────────────┐");
		System.out.println("  │   1.  Login to my account   │");
		System.out.println("  │   2.  Create new account    │");
		System.out.println("  │   3.  Exit                  │");
		System.out.println("  └──────────────────────────────┘");
		System.out.printf("  Total accounts registered: %d%n", accounts.size());
	}

	// ─── CREATE NEW ACCOUNT ───────────────────────────────────────────────────
	static void createAccount() {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  CREATE NEW ACCOUNT");
		System.out.println("──────────────────────────────────");

		// Get name
		System.out.print("  Enter your full name       : ");
		String name = scanner.nextLine().trim();

		if (name.isEmpty()) {
			System.out.println("  [ERROR] Name cannot be empty.");
			return;
		}

		// Auto-generate unique account number
		String accNo = generateAccountNumber();

		// Get and validate PIN
		System.out.print("  Set a 4-digit PIN          : ");
		String pin = scanner.nextLine().trim();

		if (!pin.matches("\\d{4}")) {
			System.out.println("  [ERROR] PIN must be exactly 4 digits (numbers only).");
			return;
		}

		// Confirm PIN
		System.out.print("  Confirm your PIN           : ");
		String confirmPin = scanner.nextLine().trim();

		if (!pin.equals(confirmPin)) {
			System.out.println("  [ERROR] PINs do not match. Account not created.");
			return;
		}

		// Initial deposit
		System.out.print("  Initial deposit (Rs.)      : ");
		try {
			double initialDeposit = Double.parseDouble(scanner.nextLine().trim());

			if (initialDeposit < 0) {
				System.out.println("  [ERROR] Deposit amount cannot be negative.");
				return;
			}

			// Create and add account to ArrayList
			Account newAccount = new Account(accNo, name, pin, initialDeposit);
			accounts.add(newAccount);

			// Success message
			System.out.println("\n  ╔══════════════════════════════════╗");
			System.out.println("  ║     ACCOUNT CREATED SUCCESS!     ║");
			System.out.println("  ╚══════════════════════════════════╝");
			System.out.printf("  Account Holder  : %s%n", name);
			System.out.printf("  Account Number  : %s%n", accNo);
			System.out.printf("  Opening Balance : Rs. %.2f%n", initialDeposit);
			System.out.println("  ──────────────────────────────────");
			System.out.println("  Please remember your Account Number and PIN.");

		} catch (NumberFormatException e) {
			System.out.println("  [ERROR] Invalid deposit amount. Please enter numbers only.");
		}
	}

	// ─── AUTO GENERATE ACCOUNT NUMBER ─────────────────────────────────────────
	static String generateAccountNumber() {
		// Format: ACC001, ACC002, ACC003 ... ACC999
		int next = accounts.size() + 1;
		return "ACC" + String.format("%03d", next);
	}

	// ─── LOGIN ────────────────────────────────────────────────────────────────
	static Account login() {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  ACCOUNT LOGIN");
		System.out.println("──────────────────────────────────");

		if (accounts.isEmpty()) {
			System.out.println("  [INFO] No accounts exist yet. Please create one first.");
			return null;
		}

		System.out.print("  Account Number : ");
		String accNo = scanner.nextLine().trim();

		Account found = findAccount(accNo);

		if (found == null) {
			System.out.println("  [ERROR] Account number not found.");
			return null;
		}

		int maxAttempts = 3;
		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			System.out.print("  PIN (" + attempt + "/" + maxAttempts + ")      : ");
			String pin = scanner.nextLine().trim();

			if (found.validatePin(pin)) {
				System.out.println("\n  Welcome back, " + found.getName() + "!");
				System.out.println("  Login successful.");
				return found;
			} else {
				int remaining = maxAttempts - attempt;
				if (remaining > 0) {
					System.out.println("  [ERROR] Wrong PIN. " + remaining + " attempt(s) left.");
				}
			}
		}

		System.out.println("\n  [BLOCKED] Too many wrong attempts.");
		System.out.println("  Please contact your bank branch.");
		return null;
	}

	// ─── FIND ACCOUNT IN ARRAYLIST ────────────────────────────────────────────
	static Account findAccount(String accNo) {
		for (Account acc : accounts) {
			if (acc.getAccountNumber().equalsIgnoreCase(accNo)) {
				return acc;
			}
		}
		return null;
	}

	// ─── MAIN MENU (after login) ──────────────────────────────────────────────
	static void showMainMenu(Account acc) {
		boolean inSession = true;

		while (inSession) {
			System.out.println("\n╔══════════════════════════════════╗");
			System.out.println("║           MAIN MENU              ║");
			System.out.println("╠══════════════════════════════════╣");
			System.out.println("║  1.  Check Balance               ║");
			System.out.println("║  2.  Deposit Cash                ║");
			System.out.println("║  3.  Withdraw Cash               ║");
			System.out.println("║  4.  Mini Statement              ║");
			System.out.println("║  5.  Change PIN                  ║");
			System.out.println("║  6.  View All Accounts           ║");
			System.out.println("║  7.  Logout                      ║");
			System.out.println("╚══════════════════════════════════╝");
			System.out.print("  Select option (1-7): ");

			String choice = scanner.nextLine().trim();

			switch (choice) {
			case "1":
				checkBalance(acc);
				break;
			case "2":
				deposit(acc);
				break;
			case "3":
				withdraw(acc);
				break;
			case "4":
				miniStatement(acc);
				break;
			case "5":
				changePin(acc);
				break;
			case "6":
				viewAllAccounts();
				break;
			case "7":
				inSession = false;
				exitSession(acc);
				break;
			default:
				System.out.println("  [ERROR] Invalid option. Enter 1 to 7.");
			}
		}
	}

	// ─── 1. CHECK BALANCE ─────────────────────────────────────────────────────
	static void checkBalance(Account acc) {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  BALANCE ENQUIRY");
		System.out.println("──────────────────────────────────");
		System.out.printf("  Account Holder : %s%n", acc.getName());
		System.out.printf("  Account Number : %s%n", acc.getAccountNumber());
		System.out.printf("  Available Bal  : Rs. %.2f%n", acc.getBalance());
		System.out.println("──────────────────────────────────");
	}

	// ─── 2. DEPOSIT ───────────────────────────────────────────────────────────
	static void deposit(Account acc) {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  DEPOSIT CASH");
		System.out.println("──────────────────────────────────");
		System.out.printf("  Current Balance : Rs. %.2f%n", acc.getBalance());
		System.out.print("  Enter amount    : Rs. ");

		try {
			double amount = Double.parseDouble(scanner.nextLine().trim());

			if (amount <= 0) {
				System.out.println("  [ERROR] Amount must be greater than zero.");
				return;
			}

			acc.deposit(amount);
			System.out.printf("  [SUCCESS] Rs. %.2f deposited successfully.%n", amount);
			System.out.printf("  New Balance     : Rs. %.2f%n", acc.getBalance());

		} catch (NumberFormatException e) {
			System.out.println("  [ERROR] Invalid amount. Please enter numbers only.");
		}
	}

	// ─── 3. WITHDRAW ──────────────────────────────────────────────────────────
	static void withdraw(Account acc) {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  WITHDRAW CASH");
		System.out.println("──────────────────────────────────");
		System.out.printf("  Available Bal   : Rs. %.2f%n", acc.getBalance());
		System.out.print("  Enter amount    : Rs. ");

		try {
			double amount = Double.parseDouble(scanner.nextLine().trim());

			if (amount <= 0) {
				System.out.println("  [ERROR] Amount must be greater than zero.");
				return;
			}

			if (acc.withdraw(amount)) {
				System.out.printf("  [SUCCESS] Rs. %.2f dispensed. Please collect cash.%n", amount);
				System.out.printf("  Remaining Bal   : Rs. %.2f%n", acc.getBalance());
			} else {
				System.out.println("  [FAILED]  Insufficient balance.");
				System.out.printf("  Available Bal   : Rs. %.2f%n", acc.getBalance());
			}

		} catch (NumberFormatException e) {
			System.out.println("  [ERROR] Invalid amount. Please enter numbers only.");
		}
	}

	// ─── 4. MINI STATEMENT ───────────────────────────────────────────────────
	static void miniStatement(Account acc) {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  MINI STATEMENT");
		System.out.println("──────────────────────────────────");
		System.out.printf("  Account : %s (%s)%n", acc.getName(), acc.getAccountNumber());
		System.out.println();

		String[] history = acc.getTransactionHistory();

		if (history.length == 0) {
			System.out.println("  No transactions recorded this session.");
		} else {
			System.out.printf("  %-5s %-14s %s%n", "No.", "Type", "Amount");
			System.out.println("  ─────────────────────────────");
			for (int i = 0; i < history.length; i++) {
				System.out.printf("  %-5d %s%n", (i + 1), history[i]);
			}
		}

		System.out.println("  ─────────────────────────────");
		System.out.printf("  Current Balance : Rs. %.2f%n", acc.getBalance());
		System.out.println("──────────────────────────────────");
	}

	// ─── 5. CHANGE PIN ───────────────────────────────────────────────────────
	static void changePin(Account acc) {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  CHANGE PIN");
		System.out.println("──────────────────────────────────");
		System.out.print("  Enter current PIN  : ");
		String current = scanner.nextLine().trim();

		if (!acc.validatePin(current)) {
			System.out.println("  [ERROR] Wrong current PIN. Change cancelled.");
			return;
		}

		System.out.print("  Enter new PIN      : ");
		String newPin = scanner.nextLine().trim();

		if (!newPin.matches("\\d{4}")) {
			System.out.println("  [ERROR] New PIN must be exactly 4 digits.");
			return;
		}

		System.out.print("  Confirm new PIN    : ");
		String confirmPin = scanner.nextLine().trim();

		if (!newPin.equals(confirmPin)) {
			System.out.println("  [ERROR] PINs do not match. Change cancelled.");
			return;
		}

		acc.setPin(newPin);
		System.out.println("  [SUCCESS] PIN changed successfully.");
		System.out.println("  Use your new PIN for next login.");
	}

	// ─── 6. VIEW ALL ACCOUNTS ─────────────────────────────────────────────────
	static void viewAllAccounts() {
		System.out.println("\n──────────────────────────────────");
		System.out.println("  ALL REGISTERED ACCOUNTS");
		System.out.println("──────────────────────────────────");

		if (accounts.isEmpty()) {
			System.out.println("  No accounts found.");
			return;
		}

		System.out.printf("  %-8s  %-20s  %s%n", "Acc No.", "Name", "Balance");
		System.out.println("  ─────────────────────────────────────");

		// Loop through ArrayList to display all accounts
		for (Account acc : accounts) {
			System.out.printf("  %-8s  %-20s  Rs. %.2f%n", acc.getAccountNumber(), acc.getName(), acc.getBalance());
		}

		System.out.println("  ─────────────────────────────────────");
		System.out.printf("  Total accounts: %d%n", accounts.size());
		System.out.println("──────────────────────────────────");
	}

	// ─── 7. LOGOUT ───────────────────────────────────────────────────────────
	static void exitSession(Account acc) {
		System.out.println("\n╔══════════════════════════════════╗");
		System.out.println("║          SESSION ENDED           ║");
		System.out.printf("║  Goodbye, %-22s║%n", acc.getName() + "!");
		System.out.println("║  Please collect your card.       ║");
		System.out.println("╚══════════════════════════════════╝");
	}

}
