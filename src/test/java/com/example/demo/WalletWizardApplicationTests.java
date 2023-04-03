package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.walletwizard.WalletWizardApplication;
import com.spring.walletwizard.dao.IncomeRepository;
import com.spring.walletwizard.dao.UsersRepository;
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
	private WalletWizardServiceImpl walletWizardServiceImpl;

	private User user1;

	private Income income;

	@BeforeEach
	void setup() {
		// usersRepository.deleteAll();
		user1 = new User("Many", "Kim", "seuon@gmail.com", "Ppl99313955");
		income = new Income(user1, 10000, "Salary", "weekly");
	}

	@Test
	void contextLoads() {
	}
	/*
	 * @Test void addUser() { List<User> users = usersRepository.findAll(); for
	 * (User user : users) { System.out.println("Hello");
	 * System.out.println(user.getFirstName() + " " + user.getLastName()); }
	 * walletWizardServiceImpl.addUser(user1);
	 * 
	 * User result = usersRepository.findAll().get(0);
	 * assertEquals(result.toString(), ""); }
	 */

	@Test
	void addIncome() {
		walletWizardServiceImpl.addUser(user1);
		walletWizardServiceImpl.addIncome(income);
	}

}
