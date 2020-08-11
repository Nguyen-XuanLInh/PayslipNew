package com.wata.payslip.controller;

import java.util.List;
import java.util.Optional;

import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wata.payslip.model.dto.EmployeeDTO;
import com.wata.payslip.service.Interface.IEmployeeService;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

	@Autowired
	private IEmployeeService iEmployeeService;
	@Autowired
	public JavaMailSender emailSender;

	// Get all employees info
	@GetMapping("/")
	public List<EmployeeDTO> findAll() {
		return iEmployeeService.getAll();
	}

	// Get employee info base on id
	@GetMapping("/{id}")
	public Optional<EmployeeDTO> getGreetingById(@PathVariable("id") int Id) {
		return iEmployeeService.getById(Id);
	}

	@GetMapping(value = "/name/{name}", headers = "Accept=application/json")
	public List<EmployeeDTO> getGreetingByName(@PathVariable("name") String name) {
		return iEmployeeService.getByFullName(name);
	}

	/*
	 * @RequestMapping(value = "/pages", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>> searchEmployeeByFullName(@RequestBody
	 * SearchData searchData) { // default currentPage = 0, pageSize = 3 return
	 * employeeService.searchEmployeeByFullName(searchData); }
	 */

	@RequestMapping(value = "/create", headers = "Accept=application/json", method = RequestMethod.POST)
	public ResponseEntity<?> createNguoiDung(@Validated @RequestBody EmployeeDTO nguoiDung)
			throws RelationNotFoundException {
		try {
			String token = iEmployeeService.sendMail(this.emailSender, nguoiDung);
			return iEmployeeService.createNguoiDung(nguoiDung, token);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete employee base on id info
	@DeleteMapping("/employee/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") int employeeId)
			throws RelationNotFoundException {
		return iEmployeeService.deleteEmployee(employeeId);
	}

	// Update employee info base on id
	@PutMapping("/employee/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable(value = "id") int employeeId,
			@Validated @RequestBody EmployeeDTO employeeDetails) throws RelationNotFoundException {
		return iEmployeeService.updates(employeeDetails, employeeId);
	}

}
