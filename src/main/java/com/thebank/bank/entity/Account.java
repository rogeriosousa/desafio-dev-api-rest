package com.thebank.bank.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonPropertyOrder(alphabetic=true)
public class Account {

	@Id
	@JsonProperty(value = "account_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@JsonProperty(value = "person_id")
	private Integer personId;
	
	@JsonProperty(value = "balance")
	private BigDecimal balance;
	
	@JsonProperty(value = "limit_per_day")
	private BigDecimal limitPerDay;
	
	@JsonProperty(value = "is_active")
	private Boolean isActive;

	@JsonProperty(value = "account_type")
	private AccountType accountType;

	private Date createdAt;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getLimitPerDay() {
		return limitPerDay;
	}

	public void setLimitPerDay(BigDecimal limitPerDay) {
		this.limitPerDay = limitPerDay;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

}
