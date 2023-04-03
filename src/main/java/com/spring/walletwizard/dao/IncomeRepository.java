package com.spring.walletwizard.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.walletwizard.model.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

}
