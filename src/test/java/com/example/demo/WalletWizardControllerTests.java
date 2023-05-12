package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spring.walletwizard.WalletWizardApplication;
import com.spring.walletwizard.controller.WalletWizardController;
import com.spring.walletwizard.dao.BalanceRepository;
import com.spring.walletwizard.dao.ExpenseRepository;
import com.spring.walletwizard.dao.FinancialGoalRepository;
import com.spring.walletwizard.dao.IncomeRepository;
import com.spring.walletwizard.dao.UsersRepository;
import com.spring.walletwizard.model.Balance;
import com.spring.walletwizard.model.Expense;
import com.spring.walletwizard.model.FinancialGoal;
import com.spring.walletwizard.model.Income;
import com.spring.walletwizard.model.User;
import com.spring.walletwizard.service.WalletWizardServiceImpl;

/*
 * Have not tested the funtionalities that delete the whole table data.
 */

@SpringBootTest(classes = WalletWizardApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletWizardControllerTests {
	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	WalletWizardController walletWizardController;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	IncomeRepository incomeRepository;

	@Autowired
	ExpenseRepository expenseRepository;

	@Autowired
	FinancialGoalRepository financialGoalRepository;

	@Mock
	private WalletWizardServiceImpl walletWizardServiceImpl;

	private User user;

	private FinancialGoal fg;

	@BeforeEach
	void setup() {
		user = new User("Many", "Kim", "seuon@gmail.com", "Ppl99313955");
		Balance balance = new Balance(user, 10000);
		usersRepository.save(user);
		balanceRepository.save(balance);
		String stringDate = "2000-07-18";
		LocalDate date = LocalDate.parse(stringDate);
		Date sqlDate = Date.valueOf(date);
		fg = new FinancialGoal(user, 1000000, "title", sqlDate);
		financialGoalRepository.save(fg);
	}

	@Test
	void testAddUser() {
		Map<String, Object> payload = new HashMap<>();
		payload.put("firstname", user.getFirstName());
		payload.put("lastname", user.getLastName());
		payload.put("email", user.getEmail());
		payload.put("password", user.getPassword());

		doAnswer(invocation -> {
			User argUser = invocation.getArgument(0);
			argUser.setToken("test_token");
			return null;
		}).when(walletWizardServiceImpl).addUser(user);

		ResponseEntity<Map<String, Object>> response = walletWizardController.addUser(null, null, payload);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody().get("token"));
	}

	@Test
	void testGetUserInfo() throws Exception {
		String token = user.getToken();
		ResponseEntity<Map<String, Object>> response = walletWizardController.getUserInfo(null, null, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Many", response.getBody().get("firstname"));
		assertEquals("Kim", response.getBody().get("lastname"));
		assertEquals("seuon@gmail.com", response.getBody().get("email"));
	}

	@Test
	void testLogin() {
		Map<String, Object> payload = new HashMap<>();
		payload.put("email", user.getEmail());
		payload.put("password", user.getPassword());
		ResponseEntity<Map<String, Object>> response = walletWizardController.userLogin(null, null, payload);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().containsKey("token"));

		payload.put("password", "wrongpassword");
		response = walletWizardController.userLogin(null, null, payload);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertTrue(response.getBody().containsKey("error"));
		assertEquals("Invalid email or password", response.getBody().get("error"));
	}

	@Test
	void testGetIncomesInfoByToken() throws Exception {
		String token = user.getToken();
		ResponseEntity<List<Income>> response = walletWizardController.getUserIncomes(null, null, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testAddIncome() throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("amount", "1000");
		payload.put("source", "source");
		payload.put("frequency", "weekly");
		String token = user.getToken();

		ResponseEntity<Map<String, Object>> response = walletWizardController.addIncome(null, null, payload, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testDeleteIncome() {
		Income income = new Income(user, 1000, "source", "weekly");
		incomeRepository.save(income);

		String incomeID = "" + income.getIncomeId();
		ResponseEntity<Map<String, Object>> response = walletWizardController.deleteIncome(null, null, incomeID);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testAddExpense() throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("amount", "1000");
		payload.put("notes", "notes");
		payload.put("frequency", "weekly");
		payload.put("date", "2000-07-18");
		String token = user.getToken();

		ResponseEntity<Map<String, Object>> response = walletWizardController.addExpense(null, null, payload, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testGetExpensesByToken() throws Exception {
		String token = user.getToken();
		String stringDate = "2000-07-18";
		LocalDate date = LocalDate.parse(stringDate);
		Date sqlDate = Date.valueOf(date);
		Expense expense = new Expense(user, 1000, "notes", sqlDate, "weekly");

		expenseRepository.save(expense);

		ResponseEntity<List<Expense>> response = walletWizardController.getUserExpenses(null, null, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
	}

	@Test
	void testDeleteExpense() {
		String stringDate = "2000-07-18";
		LocalDate date = LocalDate.parse(stringDate);
		Date sqlDate = Date.valueOf(date);
		Expense expense = new Expense(user, 1000, "notes", sqlDate, "weekly");

		expenseRepository.save(expense);

		String id = "" + expense.getExpenseId();
		ResponseEntity<Map<String, Object>> response = walletWizardController.deleteExpense(null, null, id);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testAddBalance() throws Exception {
		User user2 = new User("Many", "Kim", "seuon@gmail.com", "Ppl99313955");
		usersRepository.save(user2);

		String token = user2.getToken();

		Map<String, Object> payload = new HashMap<>();
		payload.put("amount", "20000");

		ResponseEntity<Map<String, Object>> response = walletWizardController.addBalance(null, null, payload, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testGetBalance() throws Exception {
		String token = user.getToken();
		ResponseEntity<Balance> response = walletWizardController.getBalance(null, null, token);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(10000, response.getBody().getAmount());
	}

	@Test
	void testSetBalance() throws Exception {
		String token = user.getToken();
		Map<String, Object> payload = new HashMap<>();
		payload.put("amount", "20000");

		ResponseEntity<Map<String, Object>> response = walletWizardController.setBalance(null, null, payload, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testGetFinancialGoal() throws Exception {
		String token = user.getToken();
		ResponseEntity<List<FinancialGoal>> response = walletWizardController.getFinancialGoals(null, null, token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
	}

	@Test
	void testAddFinancialGoal() throws Exception {
		String token = user.getToken();
		Map<String, Object> payload = new HashMap<>();
		payload.put("targetAmount", "10000");
		payload.put("title", "title");
		payload.put("dueDate", "2000-07-18");

		ResponseEntity<Map<String, Object>> response = walletWizardController.addFinancialGoal(null, null, payload,
				token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}