package com.wata.payslip.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wata.payslip.model.entity.AccountEntity;
import com.wata.payslip.model.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
	Page<EmployeeEntity> findByFullNameContaining(String fullName, Pageable pageable);

	@Query("FROM EmployeeEntity")
	ArrayList<EmployeeEntity> findAll();

	@Query(value = "select * from employee u where u.fullname = ?1", nativeQuery = true)
	// @Query(value = "select * from Employee u where u.fullname like ?1",
	// nativeQuery = true)
	ArrayList<EmployeeEntity> findByName(String fullName);

	EmployeeEntity findByFullName(String string);

	EmployeeEntity findByEmployeeAccount(AccountEntity id);

	// EmployeeEntity findByFullName(String id);

}