package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.walletwizard.WalletWizardApplication;
import com.spring.walletwizard.dao.ExpenseRepository;
import com.spring.walletwizard.dao.IncomeRepository;
import com.spring.walletwizard.dao.UsersRepository;
import com.spring.walletwizard.model.Balance;
import com.spring.walletwizard.model.Expense;
import com.spring.walletwizard.model.FinancialGoal;
import com.spring.walletwizard.model.Income;
import com.spring.walletwizard.model.User;
import com.spring.walletwizard.service.WalletWizardServiceImpl;

@SpringBootTest(classes = WalletWizardApplication.class)
class WalletWizardApplicationTests {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private IncomeRepository incomeRepository;

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private WalletWizardServiceImpl walletWizardServiceImpl;

	private User user1;

	private Income income;

	private Expense expense;

	private Date date;

	private Balance balance;

	private FinancialGoal financialGoal;

	@BeforeEach
	void setup() {
		// usersRepository.deleteAll();
		date = Date.valueOf("2023-04-04");
		user1 = new User("Many", "Kim", "seuon@gmail.com", "Ppl99313955");
		income = new Income(user1, 10000, "Salary", "weekly");
		expense = new Expense(user1, 100, "notes", date, "weekly");
		balance = new Balance(user1, 100000);
		financialGoal = new FinancialGoal(user1, 100000, "title", date);
		walletWizardServiceImpl.addUser(user1);
		walletWizardServiceImpl.addIncome(income);
		walletWizardServiceImpl.addExpense(expense);
		walletWizardServiceImpl.addBalance(balance);
		walletWizardServiceImpl.addFinancialGoal(financialGoal);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testAddUser() {
		Long user1Id = user1.getUserId();
		User user2 = usersRepository.findById(user1Id).orElse(null);
		assertEquals(user1.getUserId(), user2.getUserId());
	}

	@Test
	void testGetUserById() {
		User user2 = walletWizardServiceImpl.getUserById(user1.getUserId());
		assertEquals(user1.getUserId(), user2.getUserId());
	}

	@Test
	void testAddIncome() {
		Income i = incomeRepository.findById(income.getIncomeId()).orElse(null);
		assertEquals(income.getIncomeId(), i.getIncomeId());

	}

	@Test
	void testGetIncomes() {
		List<Income> incomes1 = walletWizardServiceImpl.getIncomesByUser(user1.getUserId());
		List<Income> incomes2 = walletWizardServiceImpl.getIncomesByUserToken(user1.getToken());
		assertEquals(incomes1.size(), incomes2.size());
	}

	@Test
	void testDeleteIncomeById() {
		walletWizardServiceImpl.deleteIncomeById(income.getIncomeId());
		Income i = incomeRepository.findById(income.getIncomeId()).orElse(null);
		assertEquals(null, i);
	}

	@Test
	void testGetExpenses() {
		List<Expense> e1 = walletWizardServiceImpl.getExpensesByUserId(user1.getUserId());
		List<Expense> e2 = walletWizardServiceImpl.getExpensesByToken(user1.getToken());
		assertEquals(e1.size(), e2.size());
	}

	@Test
	void testDeleteExpenseById() {
		Long expenseId = expense.getExpenseId();
		walletWizardServiceImpl.deleteExpenseById(expenseId);
		Expense e = expenseRepository.findById((expenseId)).orElse(null);
		assertEquals(null, e);
	}

	@Test
	void testGetBalanceByUserId() throws Exception {
		assertEquals(balance.getAmount(), walletWizardServiceImpl.getBalanceByUserId(user1.getUserId()).getAmount());
	}

	@Test
	void testSetBalanceAmount() throws Exception {
		walletWizardServiceImpl.setBalanceAmount(500, user1.getUserId());
		assertEquals(500, walletWizardServiceImpl.getBalanceByUserId(user1.getUserId()).getAmount());
	}

	@Test
	void testGetFinancialGoalsByUserId() {
		List<FinancialGoal> lf = walletWizardServiceImpl.getFinancialGoalsByUserId(user1.getUserId());
		assertEquals(1, lf.size());
	}

	@Test
	void testDeteleFinancialGoalById() {
		walletWizardServiceImpl.deteleFinancialGoalById(financialGoal.getfinancialGoalId());
		List<FinancialGoal> lf = walletWizardServiceImpl.getFinancialGoalsByUserId(user1.getUserId());
		assertEquals(0, lf.size());
	}
}
