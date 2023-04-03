package com.spring.walletwizard.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Balances")
public class Balance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment ID
	private Long balanceId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private Integer amount;

	public Balance() {

	}

	public Balance(User user, Integer amount) {
		this.user = user;
		this.amount = amount;
	}

	public Long getBalanceId() {
		return this.balanceId;
	}

	public void setBalanceId(Long id) {
		this.balanceId = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
