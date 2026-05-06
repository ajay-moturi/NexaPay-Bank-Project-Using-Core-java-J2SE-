package com.JavaATM;

/**
 * Account Class - Represents a bank account
 * Topics: OOP, Encapsulation, ArrayList, Methods, Access Modifiers
 */
import java.util.ArrayList;

public class Account {
	// ─── FIELDS (Encapsulation - all private) ────────────────────────────────
	private String accountNumber;
	private String name;
	private String pin;
	private double balance;
	private ArrayList<String> transactionHistory;

	// ─── CONSTRUCTOR ──────────────────────────────────────────────────────────
	public Account(String accountNumber, String name, String pin, double balance) {
		this.accountNumber = accountNumber;
		this.name = name;
		this.pin = pin;
		this.balance = balance;
		this.transactionHistory = new ArrayList<>();
	}

	// ─── PIN VALIDATION ───────────────────────────────────────────────────────
	public boolean validatePin(String enteredPin) {
		return this.pin.equals(enteredPin);
	}

	// ─── DEPOSIT ──────────────────────────────────────────────────────────────
	public void deposit(double amount) {
		balance += amount;
		transactionHistory.add(String.format("%-12s  +Rs. %.2f", "Deposit", amount));
	}

	// ─── WITHDRAW ─────────────────────────────────────────────────────────────
	public boolean withdraw(double amount) {
		if (amount > balance) {
			return false;
		}
		balance -= amount;
		transactionHistory.add(String.format("%-12s  -Rs. %.2f", "Withdrawal", amount));
		return true;
	}

	// ─── TRANSACTION HISTORY ──────────────────────────────────────────────────
	public String[] getTransactionHistory() {
		return transactionHistory.toArray(new String[0]);
	}

	// ─── GETTERS & SETTERS ────────────────────────────────────────────────────
	public String getAccountNumber() {
		return accountNumber;
	}

	public String getName() {
		return name;
	}

	public double getBalance() {
		return balance;
	}

	public void setPin(String newPin) {
		this.pin = newPin;
	}

}
