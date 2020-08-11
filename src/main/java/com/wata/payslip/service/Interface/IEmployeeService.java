package com.wata.payslip.service.Interface;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.relation.RelationNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import com.wata.payslip.model.dto.EmployeeDTO;
import com.wata.payslip.model.dto.SearchData;
import com.wata.payslip.model.entity.EmployeeEntity;

public interface IEmployeeService {

	// Get List of Employee
	public List<EmployeeDTO> getAll();

	// Convert Entity to DTO
	public EmployeeDTO convertToEmployeeDTO(EmployeeEntity user);

	// Get a Employee Info
	public Optional<EmployeeDTO> getById(int id);

	public List<EmployeeDTO> getByFullName(String id);

	// Delete a Employee
	public ResponseEntity<Map<String, Object>> deleteEmployee(int id) throws RelationNotFoundException;

	// Create a Account of Employee

	public ResponseEntity<Map<String, Object>> createNguoiDung(EmployeeDTO nguoiDung, String token)
			throws RelationNotFoundException;

	// Updates Account Info
	public ResponseEntity<Map<String, Object>> updates(EmployeeDTO user, int id) throws RelationNotFoundException;

	// SendEmail
	public String sendMail(JavaMailSender emailSender, EmployeeDTO email);

	public ResponseEntity<Map<String, Object>> MailReset(JavaMailSender emailSender, EmployeeDTO email);

	// Random account verify code
	public String getRandomNumberInts(int min, int max);

	public List<EmployeeEntity> createSearchEmployee(EmployeeDTO employee);

	public ResponseEntity<Map<String, Object>> searchEmployeeByFullName(SearchData searchData);

}
