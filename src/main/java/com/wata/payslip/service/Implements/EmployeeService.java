package com.wata.payslip.service.Implements;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.wata.payslip.filter.JwtUtil;
import com.wata.payslip.model.dto.EmployeeDTO;
import com.wata.payslip.model.dto.SearchData;
import com.wata.payslip.model.entity.AccountEntity;
import com.wata.payslip.model.entity.EmployeeEntity;
import com.wata.payslip.repository.AccountRepository;
import com.wata.payslip.repository.AssignmentRepository;
import com.wata.payslip.repository.BlackListRepository;
import com.wata.payslip.repository.EmployeeRepository;
import com.wata.payslip.repository.TypeProjectRepository;
import com.wata.payslip.service.Interface.IEmployeeService;

@Service
public class EmployeeService implements IEmployeeService {

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
	private MyUserDetailsService userDetailsService;

	// Get List of Employee
	@Override
	public List<EmployeeDTO> getAll() {
		return ((List<EmployeeEntity>) employeeRepository.findAll()).stream().map(this::convertToEmployeeDTO)
				.collect(Collectors.toList());
	}

	// Convert Entity to DTO
	@Override
	public EmployeeDTO convertToEmployeeDTO(EmployeeEntity user) {
		EmployeeDTO userLocationDTO = new EmployeeDTO();
//		userLocationDTO.setId(user.getId());
//		userLocationDTO.setFullName(user.getFullName());
//		userLocationDTO.setTelephone(user.getTelephone());
//		userLocationDTO.setEmail(user.getEmail());
//		userLocationDTO.setBirthday(user.getBirthday());
//		userLocationDTO.setAccountEntity(null);
//		userLocationDTO.setAccountId(user.getEmployeeAccount().getId());
//		userLocationDTO.setJoinDay(user.getJoinDay());
		// userLocationDTO.setEmployeeAccount(user.getIdAccount());
		return userLocationDTO;
	}

	// Get a Employee Info
	@Override
	public Optional<EmployeeDTO> getById(int id) {
		return (employeeRepository.findById(id)).map(this::convertToEmployeeDTO);
	}

	@Override
	public List<EmployeeDTO> getByFullName(String id) {
		return ((List<EmployeeEntity>) employeeRepository.findByName(id)).stream().map(this::convertToEmployeeDTO)
				.collect(Collectors.toList());
	}

	// Delete a Employee
	@Override
	public ResponseEntity<Map<String, Object>> deleteEmployee(int id) throws RelationNotFoundException {
		try {
			Optional<EmployeeEntity> employee = employeeRepository.findById(id);

			EmployeeEntity employee1 = employeeRepository.findById(id)
					.orElseThrow(() -> new RelationNotFoundException("Employee not found for this id :: " + id));
			Map<String, Boolean> response = new HashMap<>();
			int account = employee1.getEmployeeAccount().getId();
			if (employee.isPresent()) {
				try {
					authRepository.deleteById(account);
				} catch (BadCredentialsException e) {
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
				}
				employeeRepository.deleteById(id);

				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// Create a Account of Employee
	@Override
	public ResponseEntity<Map<String, Object>> createNguoiDung(EmployeeDTO nguoiDung, String token)
			throws RelationNotFoundException {
		try {
			if (token == "false")
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			AccountEntity user = authRepository.myCustomQuery2(nguoiDung.getEmail());
			if (user != null) {
				EmployeeEntity userInfo = new EmployeeEntity();
				if (employeeRepository.findByEmployeeAccount(user) != null) {
					userInfo = employeeRepository.findByEmployeeAccount(user);
				}
				BeanUtils.copyProperties(nguoiDung, user);
				user.setUsername(nguoiDung.getEmail());
				user.setToken(token);
				authRepository.save(user);
				BeanUtils.copyProperties(nguoiDung, userInfo);
				userInfo.setEmployeeAccount(user);
				employeeRepository.save(userInfo);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {

				EmployeeEntity userEntity = new EmployeeEntity();
				AccountEntity account = new AccountEntity();
				BeanUtils.copyProperties(nguoiDung, account);
				account.setUsername(nguoiDung.getEmail());
				account.setToken(token);
				authRepository.save(account);
				BeanUtils.copyProperties(nguoiDung, userEntity);
				userEntity.setEmployeeAccount(account);
				employeeRepository.save(userEntity);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Updates Account Info
	@Override
	public ResponseEntity<Map<String, Object>> updates(EmployeeDTO user, int id) throws RelationNotFoundException {
		try {
			EmployeeEntity employee = employeeRepository.findById(id)

					.orElseThrow(() -> new RelationNotFoundException("Employee not found for this id :: " + id));

			if (user.getFullName() != null) {
				employee.setFullName(user.getFullName());
			}

			if (user.getTelephone() != null) {
				employee.setTelephone(user.getTelephone());
			}

			if (user.getBirthday() != null) {
				employee.setBirthday(user.getBirthday());
			}

			if (user.getJoinDay() != null) {
				employee.setJoinDay(user.getJoinDay());
			}

			final EmployeeEntity updatedEmployee = employeeRepository.save(employee);
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// SendEmail
	@Override
	public String sendMail(JavaMailSender emailSender, EmployeeDTO email) {
		SimpleMailMessage message = new SimpleMailMessage();

		String random = getRandomNumberInts(1, 100);
		AccountEntity user = authRepository.myCustomQuery2(email.getEmail());
		if (user != null) {
			if (user.isActive())
				return "false";
			else {
				message.setTo(email.getEmail());
				message.setSubject("Test Simple Email");
				message.setText("Welcome to our Website. Thankyou for Register \n Click here for verify: "
						+ "\n https://www.google.com/search?token=" + random); // Send
				// Message!
				emailSender.send(message);
				return random;
			}
		} else {
			message.setTo(email.getEmail());
			message.setSubject("Test Simple Email");
			message.setText("Welcome to our Website. Thankyou for Register \n Your Token: " + "\n " + random); // Send
			// Message!
			emailSender.send(message);
			return random;
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> MailReset(JavaMailSender emailSender, EmployeeDTO email) {
		SimpleMailMessage message = new SimpleMailMessage();
		try {
			String random = getRandomNumberInts(1, 100);
			AccountEntity user = authRepository.myCustomQuery2(email.getEmail());
			if (user != null) {
				if (user.isActive() == false)
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
				else {
					message.setTo(email.getEmail());
					message.setSubject("Test Simple Email");
					message.setText(
							"Please don't forget password again \n Click here for reset: " + "\n token=" + random); // Send
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

	// Random account verify code
	@Override
	public String getRandomNumberInts(int min, int max) {
		Random random = new Random();
		return Long.toString(random.ints(min, (max + 1)).findFirst().getAsInt());
	}

	@Override
	public List<EmployeeEntity> createSearchEmployee(EmployeeDTO employee) {
		ArrayList<EmployeeEntity> result = new ArrayList<EmployeeEntity>();
		ArrayList<EmployeeEntity> data = (ArrayList<EmployeeEntity>) employeeRepository.findAll();
		ArrayList<EmployeeEntity> tmp = new ArrayList<EmployeeEntity>();

		if (employee.getEmail() != null) {
			result.clear();
			for (EmployeeEntity employeeEntity : data) {
				/*
				 * if (employeeEntity.getEmail() == employee.getEmail()) {
				 * result.add(employeeEntity); return result; }
				 */
			}
		}

		if (employee.getFullName() == null) {
			employee.setFullName("");
		}

		if (employee.getTelephone() == null) {
			employee.setTelephone("");
		}

		if (employee.getBirthday() == null) {
			employee.setBirthday(new Date(0));
		}

		if (employee.getJoinDay() == null) {
			employee.setJoinDay(new Date(0));
		}

		return result;
	}

	@Override
	public ResponseEntity<Map<String, Object>> searchEmployeeByFullName(SearchData searchData) {
		String fullName = searchData.getSearchValue();
		Integer currentPage, pageSize;
		String sort = searchData.getSort();

		if (searchData.getCurrentPage() != null) {
			currentPage = searchData.getCurrentPage();
		} else {
			currentPage = 0;
		}

		if (searchData.getPageSize() != null) {
			pageSize = searchData.getPageSize();
		} else {
			pageSize = 3;
		}

		try {
			List<EmployeeEntity> employeeEntities = new ArrayList<EmployeeEntity>();
			Pageable paging;

			if (sort != null) {
				switch (sort) {
				case "ASC":
					paging = PageRequest.of(currentPage, pageSize, Sort.by("fullName"));
					break;
				case "DESC":
					paging = PageRequest.of(currentPage, pageSize, Sort.by("fullName").descending());
					break;
				default:
					paging = PageRequest.of(currentPage, pageSize);
					break;
				}
			} else {
				paging = PageRequest.of(currentPage, pageSize);
			}

			Page<EmployeeEntity> pageTuts;
			List<EmployeeDTO> page;
			if (fullName == null) {
				pageTuts = employeeRepository.findAll(paging);
				page = ((List<EmployeeEntity>) pageTuts).stream().map(this::convertToEmployeeDTO)
						.collect(Collectors.toList());

			} else {
				pageTuts = employeeRepository.findByFullNameContaining(fullName.trim(), paging);
			}

			employeeEntities = pageTuts.getContent();

			if (employeeEntities.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			Map<String, Object> response = new HashMap<>();
			List<EmployeeDTO> account = employeeEntities.stream().map(this::convertToEmployeeDTO)
					.collect(Collectors.toList());
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());
			response.put("employee", account);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
