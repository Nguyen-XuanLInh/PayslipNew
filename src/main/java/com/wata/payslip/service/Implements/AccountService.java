package com.wata.payslip.service.Implements;

import java.util.HashMap;
import java.util.Map;

import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wata.payslip.filter.AuthenticationResponse;
import com.wata.payslip.filter.JwtUtil;
import com.wata.payslip.model.dto.AccountDTO;
import com.wata.payslip.model.entity.AccountEntity;
import com.wata.payslip.model.entity.BlackListEntity;
import com.wata.payslip.repository.AccountRepository;
import com.wata.payslip.repository.AssignmentRepository;
import com.wata.payslip.repository.BlackListRepository;
import com.wata.payslip.repository.EmployeeRepository;
import com.wata.payslip.repository.TypeProjectRepository;
import com.wata.payslip.service.Interface.IAccountService;

@Service
public class AccountService implements IAccountService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;
	@Autowired
	private AccountRepository authRepository;
	@Autowired
	private BlackListRepository blackListRepository;
	@Autowired
	private TypeProjectRepository typeProjectRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	public JavaMailSender emailSender;
	EmployeeService employeeService;
	@Autowired
	private MyUserDetailsService userDetailsService;

	// Create a Account of Employee

	@Override
	public ResponseEntity<?> activation(AccountDTO token) throws Exception {

		Map<String, Boolean> response = new HashMap<>();
		try {
			AccountEntity employeeAccount = authRepository.findByToken(token.getToken());
			// AccountEntity employeeAccount = authRepository.findByToken("2");
			if (employeeAccount != null) {
				employeeAccount.setActive(true);
				employeeAccount.setToken(null);
				employeeAccount.setPassword(token.getPassword());

				response.put("success", Boolean.TRUE);
				final AccountEntity updatedEmployee = authRepository.save(employeeAccount);

				final UserDetails userDetails = userDetailsService.loadUserByUsername(employeeAccount.getUsername());

				final String jwt = jwtTokenUtil.generateToken(userDetails);

				return ResponseEntity.ok(new AuthenticationResponse(jwt));

			} else {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> verifyToken(String token) throws Exception {
		try {
			if (authRepository.findByToken(token) != null) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> MailReset(JavaMailSender emailSender, AccountDTO email) {
		SimpleMailMessage message = new SimpleMailMessage();

		String random = employeeService.getRandomNumberInts(1, 100);
		// AccountEntity user = authRepository.myCustomQuery2(email.getEmail());
		try {
			AccountEntity user = authRepository.myCustomQuery2(email.getUsername());
			if (user != null) {
				if (user.isActive() == false)
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
				else {
					message.setTo(email.getUsername());
					message.setSubject("Test Simple Email");
					message.setText(" Your verify Token: " + "\n token=" + random); // Send
					// Message!
					emailSender.send(message);
					user.setToken(random);
					authRepository.save(user);
					return new ResponseEntity<>(HttpStatus.OK);

				}
			} else {

				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> logout(String token) throws RelationNotFoundException {
		try {
			BlackListEntity blackList = new BlackListEntity();
			blackList.setToken(token);
			blackListRepository.save(blackList);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (

		Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
