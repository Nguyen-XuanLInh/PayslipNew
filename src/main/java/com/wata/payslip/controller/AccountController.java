package com.wata.payslip.controller;

import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wata.payslip.filter.AuthenticationResponse;
import com.wata.payslip.filter.JwtUtil;
import com.wata.payslip.model.dto.EmployeeDTO;
import com.wata.payslip.model.dto.MyUserDetails;
import com.wata.payslip.service.Implements.AccountService;
import com.wata.payslip.service.Implements.EmployeeService;
import com.wata.payslip.service.Implements.MyUserDetailsService;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private AccountService accountService;
	@Autowired
	public JavaMailSender emailSender;

	@RequestMapping(value = "/verify", headers = "Accept=application/json", method = RequestMethod.POST)
	public ResponseEntity<?> verifyToken(@RequestBody EmployeeDTO token) throws Exception {
		return accountService.verifyToken(token);
	}

	// Active account after Register
	@RequestMapping(value = "/active", headers = "Accept=application/json", method = RequestMethod.POST)
	public ResponseEntity<?> activatedEmployee(@RequestBody EmployeeDTO token) throws Exception {
		return accountService.activation(token);
	}

	@RequestMapping(value = "/forgot", headers = "Accept=application/json", method = RequestMethod.POST)
	public ResponseEntity<String> resetPassword(@RequestBody EmployeeDTO employeeDTO) throws RelationNotFoundException {

		return accountService.MailReset(this.emailSender, employeeDTO);
	}

	@RequestMapping(value = "/login", headers = "Accept=application/json", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody MyUserDetails authenticationRequest)
			throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@RequestMapping(value = "/logout", headers = "Accept=application/json", method = RequestMethod.POST)
	public ResponseEntity<String> logoutNguoiDung(@RequestHeader(name = "Authorization") String jwt)
			throws RelationNotFoundException {
		String token = jwt.substring(7);
		return accountService.logout(token);
	}

}
