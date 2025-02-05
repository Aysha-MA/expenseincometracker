package com.project.statistics.exception;

public class NegativeBalanceException extends RuntimeException {
	public NegativeBalanceException(String message) {
		super(message);
	}
}
