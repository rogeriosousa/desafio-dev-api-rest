package com.thebank.bank.resource;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebank.bank.entity.Account;
import com.thebank.bank.entity.Transaction;
import com.thebank.bank.service.AccountService;
import com.thebank.bank.service.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class BankResourceTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionService transactionService;
    
	@Test
	void getAccountNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/account/999")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isNotFound())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void createAccount() throws Exception {
		Account newAccount = new Account();
		newAccount.setId(1);
		newAccount.setPersonId(10);
		newAccount.setBalance(new BigDecimal(1000));
		newAccount.setIsActive(true);
		newAccount.setLimitPerDay(new BigDecimal(100));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/account")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(newAccount)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void getAccount() throws Exception {
		Account newAccount = new Account();
		newAccount.setId(1);
		newAccount.setPersonId(10);
		newAccount.setBalance(new BigDecimal(1000));
		newAccount.setIsActive(true);
		newAccount.setLimitPerDay(new BigDecimal(100));
		Account account = accountService.insert(newAccount);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/account/" + account.getId())
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString("")))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(MockMvcRestDocumentation.document("account"));
	}
	
	@Test
	void doDeposit() throws Exception {
		Account newAccount = new Account();
		newAccount.setId(1);
		newAccount.setPersonId(10);
		newAccount.setBalance(new BigDecimal(1000));
		newAccount.setIsActive(true);
		newAccount.setLimitPerDay(new BigDecimal(100));
		Account account = accountService.insert(newAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setValue(new BigDecimal(100));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/account/deposit")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}

	@Test
	void doDepositFailAccountBlocked() throws Exception {
		Account newAccount = new Account();
		newAccount.setId(1);
		newAccount.setPersonId(10);
		newAccount.setBalance(new BigDecimal(1000));
		newAccount.setIsActive(false);
		newAccount.setLimitPerDay(new BigDecimal(100));
		Account account = accountService.insert(newAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setValue(new BigDecimal(100));
	
		
		mockMvc.perform(MockMvcRequestBuilders.post("/account/deposit")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}

	@Test
	void doWithdraw() throws Exception {
		Account newAccount = new Account();
		newAccount.setId(1);
		newAccount.setPersonId(10);
		newAccount.setBalance(new BigDecimal(1000));
		newAccount.setIsActive(true);
		newAccount.setLimitPerDay(new BigDecimal(100));
		Account account = accountService.insert(newAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setValue(new BigDecimal(100));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/account/withdraw")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isCreated())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
	@Test
	void doWithdrawFailDayLimit() throws Exception {
		Account newAccount = new Account();
		newAccount.setId(1);
		newAccount.setPersonId(10);
		newAccount.setBalance(new BigDecimal(1000));
		newAccount.setIsActive(true);
		newAccount.setLimitPerDay(new BigDecimal(100));
		Account account = accountService.insert(newAccount);

		Transaction transaction2 = new Transaction();
		transaction2.setAccountId(account.getId());
		transaction2.setValue(new BigDecimal(100));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/account/withdraw")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction2)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("transaction2"));
	}
	
	@Test
	void doWithdrawNotEnoughMoney() throws Exception {
		Account newAccount = new Account();
		newAccount.setId(1);		
		newAccount.setPersonId(10);
		newAccount.setBalance(new BigDecimal(100));
		newAccount.setIsActive(true);
		newAccount.setLimitPerDay(new BigDecimal(100));
		Account account = accountService.insert(newAccount);
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getId());
		transaction.setValue(new BigDecimal(200));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/account/withdraw")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(transaction)))
		        .andExpect(MockMvcResultMatchers.status().isBadRequest())
		        .andDo(MockMvcRestDocumentation.document("transaction"));
	}
	
}
