package com.thebank.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thebank.bank.entity.Account;
import com.thebank.bank.entity.Transaction;
import com.thebank.bank.exception.BusinessException;
import com.thebank.bank.exception.NotFoundException;
import com.thebank.bank.repository.TransactionRepository;

@Service
public class TransactionService {
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * Deposit value into an account
	 * @param transaction
	 * @return
	 * @throws BusinessException
	 * @throws NotFoundException
	 * @throws Exception
	 */
    @Transactional
	public Transaction deposit(Transaction transaction) throws BusinessException, NotFoundException, Exception {
    	Account account = accountService.get(transaction.getAccountId());
    	validateTransaction(transaction, account);
    	
		transaction.setDate(new Date());
		transactionRepository.save(transaction);

		account.setBalance(account.getBalance().add(transaction.getValue()));
		accountService.update(account);
		
		return transaction;
	}
    
    /**
     * Withdraw value of an account
     * @param transaction
     * @return
     * @throws BusinessException
     * @throws NotFoundException
     * @throws Exception
     */
    @Transactional 
	public Transaction withdraw(Transaction transaction) throws BusinessException, NotFoundException, Exception {
		Account account = accountService.get(transaction.getAccountId());
    	validateTransaction(transaction, account);
		
		if(transaction.getValue().compareTo(account.getBalance())>0) {
			throw new BusinessException("Not enough balance available ("+ account.getBalance() +").");
		}
		
		BigDecimal withdrawAmountToday = transactionRepository.getWithdrawSumByaccountIdAndDate(account.getId(), new Date()).abs();
		withdrawAmountToday = withdrawAmountToday.add(transaction.getValue());
		if(withdrawAmountToday.compareTo(account.getLimitPerDay())>0) {
			throw new BusinessException("This transaction exceed the limit per day ("+ account.getLimitPerDay()+").");
		}
	
		transaction.setDate(new Date());
		transaction.setValue(transaction.getValue().negate());
		transactionRepository.save(transaction);

		account.setBalance(account.getBalance().subtract(transaction.getValue()));
		accountService.update(account);
		
		return transaction; 
	}

    /**
     * Retrieve account transactions by Period
     * @param accountId
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
	public List<Transaction> findByAccountIdAndDatePeriod(Integer accountId, Date startDate, Date endDate) throws Exception {
	    List<Transaction> transactions = new ArrayList<>();
	    if(startDate==null) {
	    	startDate=new Date();
	    	endDate=startDate;
	    }
	    transactionRepository.findByAccountIdAndDateBetween(accountId, startDate, endDate).forEach(transactions::add);
	    return transactions;
	}
	
	/**
	 * Validate basic transaction rules
	 * @param transaction
	 * @param account
	 * @throws BusinessException
	 */
	private void validateTransaction(Transaction transaction, Account account) throws BusinessException {
		if(account==null) {
			throw new BusinessException("Account (" + transaction.getAccountId() + ") not found.");
		}
		
		if(!account.isActive()) {
			throw new BusinessException("Account (" + transaction.getAccountId() + ") is locked.");
		}
	}
	
}
