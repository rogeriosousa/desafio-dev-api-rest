package com.thebank.bank.entity;

public enum AccountType {
	
	CONTA_CORRENTE(1), 
	CONTA_POUPANCA(2);

	private final int id;
	
	AccountType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}