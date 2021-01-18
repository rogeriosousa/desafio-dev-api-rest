package com.thebank.bank.resource;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thebank.bank.entity.Account;
import com.thebank.bank.entity.Transaction;
import com.thebank.bank.exception.BusinessException;
import com.thebank.bank.exception.NotFoundException;
import com.thebank.bank.service.AccountService;
import com.thebank.bank.service.TransactionService;
import com.thebank.bank.util.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="Bank Rest API")
@CrossOrigin(origins="*")
public class BankResource {
	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;
	
	@GetMapping("/account/{accountId}")
	@ApiOperation(value="Get account information")
	public ResponseEntity<?> getAccount(@PathVariable Integer accountId) throws NotFoundException {
		Account account = accountService.get(accountId);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(account);		
	}

	@GetMapping("/account/{accountId}/balance")
	@ApiOperation(value="Get account balance")
	public ResponseEntity<?> getAccountBalance(@PathVariable Integer accountId) throws Exception {
		Account account = accountService.get(accountId);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(account.getBalance());
	}

	@PostMapping("/account")
	@ApiOperation(value="Create a new account")
	public ResponseEntity<?> postAccount(@Valid @RequestBody Account account) throws Exception {
		Account newAccount = accountService.insert(account);
		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(newAccount);		
	} 

	@PostMapping("/account/{accountId}/block")
	@ApiOperation(value="Block an account")
	public ResponseEntity<?> blockAccount(@PathVariable Integer accountId) throws Exception {
		accountService.block(accountId);
		return ResponseEntity.ok("Account " + accountId + " has been blocked.");
	}
		
	@PostMapping("/account/deposit")
	@ApiOperation(value="Deposit values into an account")
	public ResponseEntity<?> deposit(@RequestBody Transaction transaction) throws Exception {
		Transaction newTransaction = transactionService.deposit(transaction);
		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(newTransaction);
	}

	@PostMapping("/account/withdraw")
	@ApiOperation(value="Withdraw values into an account")
	public ResponseEntity<?> withdraw(@RequestBody Transaction transaction) throws Exception {
		Transaction newTransaction = transactionService.withdraw(transaction);
		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(newTransaction);
	}

	@GetMapping("/account/{accountId}/history")
	@ApiOperation(value="Retrieve account transaction history")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "startDate", value = "startDate ", example = "01-01-2021"),
		@ApiImplicitParam(name = "endDate", value = "endDate ", example = "30-01-2021"),
	})
	public ResponseEntity<?> getTransactionByIDDate(@PathVariable Integer accountId, 
			@RequestParam(required = false) 
	  		@DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate, 
			@RequestParam(required = false) 
			@DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate
			) throws Exception { 
		List<Transaction> transactionList = transactionService.findByAccountIdAndDatePeriod(accountId, startDate, endDate);
		if (transactionList==null){
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(transactionList);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public GenericResponse handleNotFoundException(NotFoundException e) {
		return new GenericResponse(e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public GenericResponse handleBusinessException(BusinessException e) {
		return new GenericResponse(e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public GenericResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		return new GenericResponse(ex.getMessage());
	}
	
}
