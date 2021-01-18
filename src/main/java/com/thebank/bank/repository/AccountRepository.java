package com.thebank.bank.repository;

import org.springframework.data.repository.CrudRepository;

import com.thebank.bank.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {

}

