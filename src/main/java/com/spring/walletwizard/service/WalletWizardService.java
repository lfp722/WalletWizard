package com.spring.walletwizard.service;

import java.util.List;

import com.spring.walletwizard.model.Balance;
import com.spring.walletwizard.model.Expense;
import com.spring.walletwizard.model.Income;
import com.spring.walletwizard.model.User;

public interface WalletWizardService {
	void addUser(User user);

	void deleteAllUsers();

	User getUserById(long id);

	User getUserByToken(String token) throws Exception;

	void addIncome(Income income);

	void deleteIncomeById(long id);

	void deleteAllIncomes();

	List<Income> getIncomesByUser(long id);

	List<Income> getIncomesByUserToken(String token);

	void addExpense(Expense expense);

	List<Expense> getExpensesByUserId(long id);

	List<Expense> getExpensesByToken(String token);

	void deleteExpenseById(long id);

	void addBalance(Balance balance);

	Balance getBalanceByUserId(long id) throws Exception;

	void setBalanceAmount(Integer amount, long id);
}
