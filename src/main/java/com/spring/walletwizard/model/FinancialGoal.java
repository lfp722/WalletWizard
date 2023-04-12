package com.spring.walletwizard.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FinancialGoal")
public class FinancialGoal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment ID
	private Long financialGoalId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private int targetAmount;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Date dueDate;

	public FinancialGoal() {

	}

	public FinancialGoal(User user, int targetAmount, String title, Date dueDate) {
		this.user = user;
		this.targetAmount = targetAmount;
		this.title = title;
		this.dueDate = dueDate;
	}

	public Long getfinancialGoalId() {
		return financialGoalId;
	}

	public void setfinancialGoalId(Long financialGoalId) {
		this.financialGoalId = financialGoalId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(int targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}
