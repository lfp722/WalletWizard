package com.spring.walletwizard.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.walletwizard.dao.BalanceRepository;
import com.spring.walletwizard.dao.UsersRepository;
import com.spring.walletwizard.model.Balance;
import com.spring.walletwizard.model.Expense;
import com.spring.walletwizard.model.FinancialGoal;
import com.spring.walletwizard.model.Income;
import com.spring.walletwizard.model.User;
import com.spring.walletwizard.service.WalletWizardServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class WalletWizardController {
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private BalanceRepository balanceRepository;

	@Autowired
	private WalletWizardServiceImpl walletWizardServiceImpl;

	@PostMapping("/addUser")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> addUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> payload) {
		String firstname = (String) payload.get("firstname");
		String lastname = (String) payload.get("lastname");
		String email = (String) payload.get("email");
		String password = (String) payload.get("password");
		User user = new User(firstname, lastname, email, password);
		walletWizardServiceImpl.addUser(user);
		String token = user.getToken();
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("token", token);
		return ResponseEntity.ok(responseMap);
	}

	@GetMapping("/getUserInfobyToken/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String token) throws Exception {
		User user = walletWizardServiceImpl.getUserByToken(token);
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("firstname", user.getFirstName());
		responseMap.put("lastname", user.getLastName());
		responseMap.put("email", user.getEmail());
		return ResponseEntity.ok(responseMap);
	}

	@PostMapping("/userLogin")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> userLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> payload) {
		String email = (String) payload.get("email");
		String password = (String) payload.get("password");

		List<User> Users = usersRepository.findAll();
		for (User user : Users) {
			if (user.getEmail().equals(email)) {
				if (user.getPassword().equals(password)) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put("token", user.getToken());
					return ResponseEntity.ok(responseMap);
				}
			}
		}
		Map<String, Object> errorMap = new HashMap<>();
		errorMap.put("error", "Invalid email or password");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
	}

	@GetMapping("/getIncomesInfobyToken/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<List<Income>> getUserIncomes(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String token) throws Exception {
		User u = walletWizardServiceImpl.getUserByToken(token);
		List<Income> ret = walletWizardServiceImpl.getIncomesByUser(u.getUserId());
		return ResponseEntity.ok(ret);
	}

	@PostMapping("/addIncome/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> addIncome(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> payload, @PathVariable String token) throws Exception {
		User user = walletWizardServiceImpl.getUserByToken(token);
		String amountStr = (String) payload.get("amount");
		Integer amount = Integer.parseInt(amountStr);
		String source = (String) payload.get("source");
		String frequency = (String) payload.get("frequency");
		Income income = new Income(user, amount, source, frequency);
		walletWizardServiceImpl.addIncome(income);

		Balance b = walletWizardServiceImpl.getBalanceByUserId(user.getUserId());
		b.setAmount(b.getAmount() + amount);
		balanceRepository.save(b);

		Map<String, Object> responseMap = new HashMap<>();
		return ResponseEntity.ok(responseMap);
	}

	@DeleteMapping("/deleteIncome/{incomeId}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> deleteIncome(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String incomeId) {
		walletWizardServiceImpl.deleteIncomeById(Long.parseLong(incomeId));
		Map<String, Object> responseMap = new HashMap<>();
		return ResponseEntity.ok(responseMap);
	}

	@GetMapping("/getExpensesByToken/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<List<Expense>> getUserExpenses(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String token) throws Exception {
		User u = walletWizardServiceImpl.getUserByToken(token);
		List<Expense> ret = walletWizardServiceImpl.getExpensesByUserId(u.getUserId());
		return ResponseEntity.ok(ret);
	}

	@PostMapping("/addExpense/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> addExpense(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> payload, @PathVariable String token) throws Exception {
		User user = walletWizardServiceImpl.getUserByToken(token);
		String amountStr = (String) payload.get("amount");
		Integer amount = Integer.parseInt(amountStr);
		String notes = (String) payload.get("notes");
		String frequency = (String) payload.get("frequency");
		String dateString = (String) payload.get("date");
		LocalDate date = LocalDate.parse(dateString);
		Date sqlDate = Date.valueOf(date);
		Expense expense = new Expense(user, amount, notes, sqlDate, frequency);
		walletWizardServiceImpl.addExpense(expense);

		Balance b = walletWizardServiceImpl.getBalanceByUserId(user.getUserId());
		b.setAmount(b.getAmount() - amount);
		balanceRepository.save(b);

		Map<String, Object> responseMap = new HashMap<>();
		return ResponseEntity.ok(responseMap);
	}

	@DeleteMapping("/deleteExpense/{expenseId}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> deleteExpense(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String expenseId) {
		walletWizardServiceImpl.deleteExpenseById(Long.parseLong(expenseId));
		Map<String, Object> responseMap = new HashMap<>();
		return ResponseEntity.ok(responseMap);
	}

	@PostMapping("/addBalance/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> addBalance(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> payload, @PathVariable String token) throws Exception {
		User user = walletWizardServiceImpl.getUserByToken(token);
		String amountStr = (String) payload.get("amount");
		Integer amount = Integer.parseInt(amountStr);

		Balance b = new Balance(user, amount);
		walletWizardServiceImpl.addBalance(b);
		Map<String, Object> responseMap = new HashMap<>();
		return ResponseEntity.ok(responseMap);
	}

	@GetMapping("/getBalance/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Balance> getBalance(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String token) throws Exception {
		User u = walletWizardServiceImpl.getUserByToken(token);
		Balance b = walletWizardServiceImpl.getBalanceByUserId(u.getUserId());
		return ResponseEntity.ok(b);
	}

	@PostMapping("/setBalance/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> setBalance(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> payload, @PathVariable String token) throws Exception {
		User user = walletWizardServiceImpl.getUserByToken(token);
		String amountStr = (String) payload.get("amount");
		Integer amount = Integer.parseInt(amountStr);
		walletWizardServiceImpl.setBalanceAmount(amount, user.getUserId());
		Map<String, Object> responseMap = new HashMap<>();
		return ResponseEntity.ok(responseMap);
	}

	@GetMapping("/getFinancialGoalsbyToken/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<List<FinancialGoal>> getFinancialGoals(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String token) throws Exception {
		User u = walletWizardServiceImpl.getUserByToken(token);
		List<FinancialGoal> ret = walletWizardServiceImpl.getFinancialGoalsByUserId(u.getUserId());
		return ResponseEntity.ok(ret);
	}

	@PostMapping("/addFinancialGoal/{token}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> addFinancialGoal(HttpServletRequest request,
			HttpServletResponse response, @RequestBody Map<String, Object> payload, @PathVariable String token)
			throws Exception {
		User user = walletWizardServiceImpl.getUserByToken(token);
		String amountStr = (String) payload.get("targetAmount");
		Integer targetAmount = Integer.parseInt(amountStr);
		String title = (String) payload.get("title");
		String dueDateString = (String) payload.get("dueDate");
		LocalDate dueDate = LocalDate.parse(dueDateString);
		Date sqlDate = Date.valueOf(dueDate);
		FinancialGoal fg = new FinancialGoal(user, targetAmount, title, sqlDate);
		walletWizardServiceImpl.addFinancialGoal(fg);
		Map<String, Object> responseMap = new HashMap<>();
		return ResponseEntity.ok(responseMap);
	}
}
