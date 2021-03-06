package com.wata.payslip.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wata.payslip.model.entity.WorkEntity;

public interface WorkRepository extends JpaRepository<WorkEntity, Integer> {
	Page<WorkEntity> findByIdWorkContaining(Integer Id, Pageable pageable);
}
