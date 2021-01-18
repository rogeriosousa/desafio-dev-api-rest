package com.thebank.bank.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.thebank.bank.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	List<Transaction> findByAccountIdAndDateBetween(Integer accountId, Date startDate, Date endDate);
	
	@Query("select COALESCE(sum(value),0) from Transaction t where t.accountId = :#{#accountId} and t.date = :#{#date} and value <0")
	BigDecimal getWithdrawSumByaccountIdAndDate(@Param("accountId") Integer accountId, @Param("date") Date date);
	
}