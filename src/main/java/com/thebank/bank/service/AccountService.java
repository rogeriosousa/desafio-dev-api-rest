package com.thebank.bank.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thebank.bank.entity.Account;
import com.thebank.bank.exception.NotFoundException;
import com.thebank.bank.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	/**
	 * Get Account Information
	 * @param accountId
	 * @return
	 * @throws NotFoundException
	 */
	
	public Account get(Integer accountId) throws NotFoundException {
		Optional<Account> account = accountRepository.findById(accountId);
		if (account.isEmpty()) {
			throw new NotFoundException("Account ("+accountId+") not found.");
		}
		return account.get();
	}

	/**
	 * Create an Account
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public Account insert(Account account) throws Exception {
		account.setCreatedAt(new Date());
		return accountRepository.save(account);
	}
	
	/**
	 * Update an Account
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public Account update(Account account) throws Exception {
		return accountRepository.save(account);
	}
	
	/**
	 * Block an Account
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	public Account block(Integer accountId) throws Exception {
		Optional<Account> account = accountRepository.findById(accountId);
		Account accountToBlock = account.orElseThrow();
		accountToBlock.setIsActive(false);
		return accountRepository.save(accountToBlock);
	}

}
