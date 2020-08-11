package com.wata.payslip.service.Interface;

import java.util.Map;

import javax.management.relation.RelationNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import com.wata.payslip.model.dto.AccountDTO;

public interface IAccountService {

	public ResponseEntity<?> activation(AccountDTO token) throws Exception;

	public ResponseEntity<Map<String, Object>> verifyToken(String token) throws Exception;

	public ResponseEntity<Map<String, Object>> MailReset(JavaMailSender emailSender, AccountDTO email);

	public ResponseEntity<Map<String, Object>> logout(String token) throws RelationNotFoundException;

}
