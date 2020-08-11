package com.wata.payslip.service.Interface;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.wata.payslip.model.dto.ProjectDTO;
import com.wata.payslip.model.dto.SearchData;

public interface IProjectService {
	public ResponseEntity<Map<String, Object>> createProject(ProjectDTO projectDTO);

	public ResponseEntity<Map<String, Object>> getAllProject();

	public ResponseEntity<Map<String, Object>> getProjectById(Integer idProject);

	public ResponseEntity<Map<String, Object>> deleteProjectById(Integer idProject);

	public ResponseEntity<Map<String, Object>> updateProjectById(ProjectDTO projectDTO, Integer idProject);

	public ResponseEntity<Map<String, Object>> searchByNameProject(SearchData searchData);
}
