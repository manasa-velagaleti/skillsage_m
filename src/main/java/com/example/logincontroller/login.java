package com.example.logincontroller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.LoginDto;
import com.example.dto.VerifyOtpDTO;
import com.example.entity.EmployeeSkill;
import com.example.logrepo.logRepository;
import com.example.logservice.LogService;
import com.example.logservice.LoginDetService;
import com.example.model.Login_det;
import com.example.service.EmpSkillService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class login {

	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LogService logService;

	@Autowired
	private logRepository logrepo;

	@Autowired
	private LoginDetService loginDetService;

	@Autowired
	private logRepository repository;

	@Autowired
	private EmpSkillService service;
	@GetMapping("/home")
	public String landingPage() {
		return "home";
	}

	@GetMapping("/loginDetails")
	public String showLoginDetails(Model model) {
		List<Login_det> loginDetails = loginDetService.getLoginDets();
		model.addAttribute("loginDetails", loginDetails);
		return "EmpDetails";
	}

//
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") LoginDto loginDto, Model model) {
		model.addAttribute("user", new Login_det());
		return "register";
	}

	@GetMapping("/login")
	public String llogin(@ModelAttribute("user") LoginDto loginDto, Model model, HttpServletRequest request) {
		model.addAttribute("user", new Login_det());

		
		  
		  if (request.getParameter("error") != null) { model.addAttribute("loginError",true); }
		 

		return "login";
	}

	@GetMapping("/employeeess")
	public String employeees() {
		return "EmpDash";
	}
//	 @GetMapping("/skilldetails")
//	    public String SkillDetails(Model model, Principal principal) {
//	        String empId = principal.getName(); 
//
//	        List<EmployeeSkill> employeeSkills = service.getEmpSkillsByEmpId(empId);
//	        model.addAttribute("employeeSkills", employeeSkills);
//
//	        return "AEmpSkill";
//	    }
//	 
	 
 
	 
	@GetMapping("user-page")
	public String userPage(Model model, Principal principal) {
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "index";
	}

	@GetMapping("admin-page")
	public String adminPage(Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "N_admin";
	}

	@GetMapping("/login-details")
	public String getAllLoginDetails(Model model) {
		model.addAttribute("loginDetails", loginDetService.getLoginDets());
		return "login_details";
	}

	@PostMapping("/delete-login-detail")
	public String deleteLoginDetail(@RequestParam("empid") String empId) {
		loginDetService.deleteLoginDet(empId);
		return "redirect:/login-details";
	}

	@GetMapping("/forgot")
	public String forgot() {
		return "forgot";
	}

	@Autowired
	private LoginDetService userService;

	@PostMapping("/sendOtp/{email}")
	public String sendOtpToMail(@PathVariable("email") String email, @ModelAttribute Login_det loginDto) {

		System.out.println("Otp sent" + email);
		Login_det a = new Login_det();
		a.setEmail(email);
		a.setEmpid(loginDto.getEmpid()); // Use empid from the form
		a.setFullname(loginDto.getFullname());
		a.setPassword(loginDto.getPassword());
		a.setDesignation(loginDto.getDesignation());

		loginDetService.savePartialDetails(a);

		// Send OTP to the provided email
		loginDetService.sendOtpService(email);

		return "success";
	}

	@PostMapping("/otp/{email}")
	public String sendOtpToMail(@PathVariable("email") String email) {
		userService.fsendOtpService(email);
		return "otp send successfully";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(@ModelAttribute("detail") Login_det user, Model model) {

		int status = userService.resetPassword(user);

		if (status == 1) {
			model.addAttribute("message", "User is not registered");
			return "forgetPassword";
		} else if (status == 3) {
			model.addAttribute("message", "Otp is not matched");
			return "forgetPassword";
		}
		return "login";
	}

	@PostMapping("/saveEmailAndOTP")
	public ResponseEntity<String> saveEmailAndOTP(@RequestBody VerifyOtpDTO verifyOtpDTO) {
		try {
			userService.saveEmailAndOTP(verifyOtpDTO);
			return ResponseEntity.ok("Email and OTP saved successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error saving email and OTP: " + e.getMessage());
		}
	}

	@GetMapping("/edit-employee-details")
	public String editEmployeeDetails(Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "employee_details";
	}

	@GetMapping("/edit-admin-details")
	public String AeditEmployeeDetails(Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "admin_details";
	}
	@PostMapping("/update-admin-details")
	public String updateadminDetails(@ModelAttribute("user") LoginDto loginDto, Model model) {
		logService.updateEmployeeDetails(loginDto);
		model.addAttribute("message", "Employee details updated successfully!");
		return "admin_details";
	}

	@PostMapping("/update-employee-details")
	public String updateEmployeeDetails(@ModelAttribute("user") LoginDto loginDto, Model model) {
		logService.updateEmployeeDetails(loginDto);
		model.addAttribute("message", "Employee details updated successfully!");
		return "employee_details";
	}

	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") Login_det loginDto, Model model) {
		try {
			System.out.println("Employee ID: " + loginDto.getEmpid());

			List<Login_det> existingDetails = logService.getDetails(loginDto.getEmail());

			model.addAttribute("message", "Registration successful!");
			for (Login_det existingDetail : existingDetails) {

				existingDetail.setEmail(loginDto.getEmail());
				existingDetail.setEmpid(loginDto.getEmpid());
				existingDetail.setFullname(loginDto.getFullname());
				existingDetail.setPassword(passwordEncoder.encode(loginDto.getPassword()));
				existingDetail.setDesignation(loginDto.getDesignation());
				repository.save(existingDetail);
			}
			return "register";
		} catch (DataAccessException e) {
			model.addAttribute("message", "Failed to complete registration due to a database issue. Please try again.");
			return "register";
		} catch (Exception e) {
			model.addAttribute("message", "An unexpected error occurred during registration. Please try again.");
			return "register";
		}
	}

	@PostMapping("/verifyOtp")
	@ResponseBody
	public ResponseEntity<Map<String, String>> verifyOtp(@ModelAttribute("user") LoginDto loginDto, Model model) {
		Map<String, String> response = new HashMap<>();
		System.out.println("Checking.." + loginDto.getOtp() + loginDto.getEmail());
		boolean isValidOtp = loginDetService.verifyOtp(loginDto.getEmail(), loginDto.getOtp());

		String trimmedEmail = loginDto.getEmail().trim();
		String trimmedOtp = loginDto.getOtp().trim();

		if (isValidOtp) {

			response.put("status", "success");
			response.put("message", "User registered successfully!");
			System.out.println("OTP verification successful for user with email: " + trimmedOtp + trimmedEmail);
		} else {
			response.put("status", "error");
			response.put("message", "Invalid OTP. Please try again.");
			System.out.println("OTP verification failed for user with email: " + trimmedOtp);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/checkEmailExists/{email}")
	public ResponseEntity<Map<String, Boolean>> checkEmailExists(@PathVariable String email) {
		boolean emailExists = loginDetService.emailExists(email);
		Map<String, Boolean> response = new HashMap<>();
		response.put("exists", emailExists);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/admin/Rdashboard") /* send */
	public String adminDashboard(Model model) {
		// Fetch users from the database or any other source
		List<Login_det> users = logrepo.findAll(); // Assuming userService is a service to fetch users
		model.addAttribute("users", users);
		return "RAdmin";
	}
	

	@GetMapping("/admin/sendmsg") /* send */
	public String aaadminDashboard(Model model) {
		// Fetch users from the database or any other source
		List<Login_det> users = logrepo.findAll(); // Assuming userService is a service to fetch users
		model.addAttribute("users", users);
		return "A_sendmsg";
	}

	@PostMapping("/RsendReminder")
	public String sendReminder(@RequestParam("userIds") List<String> userIds, @RequestParam("message") String message,
			HttpServletResponse response) {
		// Get the currently authenticated user ID (sender)
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String senderName = auth.getName();

		for (String userId : userIds) {
			// Fetch the fullname associated with the user ID
			String recipientFullname = logrepo.findFullnameByEmpid(senderName); // Fetch fullname by empid

			// Encode the reminder message using Base64
			String reminderWithNames = "From: " + recipientFullname + ", Message: " + message;
			String encodedMessage = Base64.getEncoder().encodeToString(reminderWithNames.getBytes());

			// Generate a unique cookie name based on recipient ID and timestamp
			String cookieName = "reminder_" + userId + "_" + System.currentTimeMillis(); // Unique identifier using
																							// timestamp

			// Store the reminder message in a cookie
			Cookie reminderCookie = new Cookie(cookieName, encodedMessage);
			reminderCookie.setMaxAge(3600); // Cookie expires in 1 hour
			response.addCookie(reminderCookie);
		}
		return "redirect:/admin/Rdashboard";
	}
	
	@PostMapping("/sendReminder")
	public String sendddReminder(@RequestParam("userIds") List<String> userIds, @RequestParam("message") String message,
			HttpServletResponse response) {
		// Get the currently authenticated user ID (sender)
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String senderName = auth.getName();

		for (String userId : userIds) {
			// Fetch the fullname associated with the user ID
			String recipientFullname = logrepo.findFullnameByEmpid(senderName); // Fetch fullname by empid

			// Encode the reminder message using Base64
			String reminderWithNames = "From: " + recipientFullname + ", Message: " + message;
			String encodedMessage = Base64.getEncoder().encodeToString(reminderWithNames.getBytes());

			// Generate a unique cookie name based on recipient ID and timestamp
			String cookieName = "reminder_" + userId + "_" + System.currentTimeMillis(); // Unique identifier using
																							// timestamp

			// Store the reminder message in a cookie
			Cookie reminderCookie = new Cookie(cookieName, encodedMessage);
			reminderCookie.setMaxAge(3600); // Cookie expires in 1 hour
			response.addCookie(reminderCookie);
		}
		return "redirect:/admin/sendmsg";
	}

	@GetMapping("/user/Rdashboard") /* receive */
	public String userDashboard(Model model, HttpServletRequest request) {
		// Get the currently authenticated user ID
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUserId = auth.getName();

		// Retrieve reminder messages from cookies and decode them
		Cookie[] cookies = request.getCookies();
		List<String> userReminderMessages = new ArrayList<>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {

				if (cookie.getName().startsWith("reminder_" + loggedInUserId)) {
					// Decode the reminder message using Base64
					String decodedMessage = new String(Base64.getDecoder().decode(cookie.getValue()));
					userReminderMessages.add(decodedMessage);
				}
			}
		}

		model.addAttribute("reminderMessages", userReminderMessages);
		return "RUser";
	}
	
	@GetMapping("/user/receivemsg") /* receive */
	public String uuuserDashboard(Model model, HttpServletRequest request) {
		// Get the currently authenticated user ID
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUserId = auth.getName();

		// Retrieve reminder messages from cookies and decode them
		Cookie[] cookies = request.getCookies();
		List<String> userReminderMessages = new ArrayList<>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {

				if (cookie.getName().startsWith("reminder_" + loggedInUserId)) {
					// Decode the reminder message using Base64
					String decodedMessage = new String(Base64.getDecoder().decode(cookie.getValue()));
					userReminderMessages.add(decodedMessage);
				}
			}
		}

		model.addAttribute("reminderMessages", userReminderMessages);
		return "A_receivemsg";
	}

	@PostMapping("/deleteAllReminders")
	public ResponseEntity<String> deleteAllReminders(HttpServletRequest request, HttpServletResponse response) {
		// Retrieve the currently logged-in user ID
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUserId = auth.getName();

		// Retrieve all cookies
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {

			for (Cookie cookie : cookies) {

				if (cookie.getName().startsWith("reminder_" + loggedInUserId)) {

					cookie.setMaxAge(0);

					cookie.setPath("/");

					response.addCookie(cookie);
				}
			}
		}

		return ResponseEntity.ok().body("All reminder messages deleted successfully");
	}

	@GetMapping("/user/fetchReminderMessages")
	public ResponseEntity<List<String>> fetchReminderMessages() {

		List<String> reminderMessages = Collections.emptyList();
		return ResponseEntity.ok().body(reminderMessages);
	}

}
