package com.spring.walletwizard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WalletWizardServiceImpl implements WalletWizardService {
	@Autowired
	UsersRepository usersRepository;

	@Autowired
	IncomeRepository incomeRepository;

	@Autowired
	ExpenseRepository expenseRepository;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	FinancialGoalRepository financialGoalRepository;

	@Override
	public void addUser(User user) {
		usersRepository.save(user);
	}

	@Override
	public void deleteAllUsers() {
		usersRepository.deleteAll();
	}

	@Override
	public User getUserById(long id) {
		Optional<User> opt = usersRepository.findById(id);
		if (opt.isPresent())
			return opt.get();
		else
			return null;
	}

	@Override
	public User getUserByToken(String token) throws Exception {
		List<User> users = usersRepository.findAll();
		for (User u : users) {
			if (u.getToken().equals(token)) {
				return u;
			}
		}
		throw new Exception("User does not exist");
	}

	@Override
	public void addIncome(Income income) {
		incomeRepository.save(income);
	}

	@Override
	public void deleteAllIncomes() {
		incomeRepository.deleteAll();
	}

	@Override
	public List<Income> getIncomesByUser(long id) {
		List<Income> ret = new ArrayList<Income>();
		List<Income> incomes = incomeRepository.findAll();
		for (Income i : incomes) {
			if (i.getUser().getUserId() == id) {
				ret.add(i);
			}
		}
		return ret;
	}

	@Override
	public List<Income> getIncomesByUserToken(String token) {
		List<Income> ret = new ArrayList<Income>();
		List<Income> incomes = incomeRepository.findAll();
		for (Income i : incomes) {
			if (i.getUser().getToken().equals(token)) {
				ret.add(i);
			}
		}
		return ret;
	}

	@Override
	public void addExpense(Expense expense) {
		expenseRepository.save(expense);
	}

	@Override
	public List<Expense> getExpensesByUserId(long id) {
		List<Expense> ret = new ArrayList<Expense>();
		List<Expense> expenses = expenseRepository.findAll();
		for (Expense e : expenses) {
			if (e.getUser().getUserId() == id) {
				ret.add(e);
			}
		}
		return ret;
	}

	@Override
	public List<Expense> getExpensesByToken(String token) {
		List<Expense> ret = new ArrayList<Expense>();
		List<Expense> expenses = expenseRepository.findAll();
		for (Expense e : expenses) {
			if (e.getUser().getToken().equals(token)) {
				ret.add(e);
			}
		}
		return ret;
	}

	@Override
	public void deleteExpenseById(long id) {
		expenseRepository.deleteById(id);
	}

	@Override
	public void deleteIncomeById(long id) {
		incomeRepository.deleteById(id);
	}

	@Override
	public void addBalance(Balance balance) {
		balanceRepository.save(balance);
	}

	@Override
	public Balance getBalanceByUserId(long id) throws Exception {
		List<Balance> balances = balanceRepository.findAll();
		for (Balance b : balances) {
			if (b.getUser().getUserId() == id) {
				return b;
			}
		}
		throw new Exception("Balance does not exist");
	}

	@Override
	public void setBalanceAmount(Integer amount, long id) {
		List<Balance> balances = balanceRepository.findAll();
		for (Balance b : balances) {
			if (b.getUser().getUserId() == id) {
				b.setAmount(amount);
				balanceRepository.save(b);
			}
		}
		return;
	}

	@Override
	public List<FinancialGoal> getFinancialGoalsByUserId(long id) {
		List<FinancialGoal> fgs = financialGoalRepository.findAll();
		List<FinancialGoal> ret = new ArrayList<FinancialGoal>();
		for (FinancialGoal fg : fgs) {
			if (fg.getUser().getUserId() == id) {
				ret.add(fg);
			}
		}
		return ret;
	}

	@Override
	public void addFinancialGoal(FinancialGoal financialGoal) {
		financialGoalRepository.save(financialGoal);
	}

	@Override
	public void deteleFinancialGoalById(long id) {
		financialGoalRepository.deleteById(id);
	}
}
