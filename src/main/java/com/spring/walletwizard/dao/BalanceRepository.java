package com.spring.walletwizard.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.walletwizard.model.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

}
