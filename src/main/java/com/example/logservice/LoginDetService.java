package com.example.logservice;



import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.eclipse.angus.mail.smtp.SMTPSendFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.LoginDto;
import com.example.dto.VerifyOtpDTO;
import com.example.logrepo.logRepository;
import com.example.model.Login_det;
import com.example.service.EmpSkillService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;


@Service
@Retryable(value = { SMTPSendFailedException.class }, maxAttempts = 5, backoff = @Backoff(delay = 5000))

public class LoginDetService {
    @Autowired
    private logRepository repository;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    

    public Login_det saveLoginDet(Login_det loginDet) {
        return repository.save(loginDet);
    }

    public List<Login_det> saveLoginDets(List<Login_det> loginDets) {
        return repository.saveAll(loginDets);
    }

    public List<Login_det> getLoginDets() {
        return repository.findAll();
    }

    public Login_det getLoginDetByEmpId(String empid) {
        return repository.findByEmpid(empid).orElse(null);
    }

    public Login_det getLoginDetByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }
    
    @Autowired
   	private EmpSkillService service;

       @Transactional
       public void deleteLoginDet(String empId) {
       	service.deleteEmpSkillByEmpId(empId);
           repository.deleteByEmpid(empId);
           
       }
    
       public Login_det updateLoginDet(Login_det updatedUser) {
           
           return repository.save(updatedUser);
       }


    public List<LoginDto> getAllEmployeeDetails() {
        
        return null;
    }

    public List<LoginDto> searchEmployees(String empId, String skillname, String domain, String subdomain,
            String proficiency) {
      
        return null;
    }
    
    public void sendOtpService(String email) {
		String otp=generateOtp();
		

		if (otp == null) 
		{
            throw new RuntimeException("Failed to generate OTP");
        }
		
		Login_det user = repository.findByEmail(email);
		if (user==null)
		{
			 user=new Login_det();
		}
		
		user.setEmail(email);
		
		user.setOtp(otp);
		repository.save(user);
		try {
			sendOtpToMail(email,otp);
			
		}catch(MessagingException e) {
			throw new RuntimeException("unable to send otp");
		}
		
	}
    
    public void fsendOtpService(String email) {
 		String otp=generateOtp();
 		

 		if (otp == null) 
 		{
             throw new RuntimeException("Failed to generate OTP");
         }
 		
 		Login_det user = repository.findByEmail(email);
 		if (user==null)
 		{
 			 user=new Login_det();
 		}
 		
 		user.setEmail(email);
 		
 		user.setOtp(otp);
 		repository.save(user);
 		try {
 			fsendOtpToMail(email,otp);
 			
 		}catch(MessagingException e) {
 			throw new RuntimeException("unable to send otp");
 		}
 		
 	}
    
    
	public String generateOtp() {
		SecureRandom random=new SecureRandom();
		int otp= 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}
	
	public void sendOtpToMail(String email,String otp) throws MessagingException {
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("OTP Verification");
		
		String messageBody = "Welcome to SkillSage! Thank you for joining us!\n\n";
	    messageBody += "Your OTP for account verification is: " + otp + "\n\n";
	    messageBody += "Please use this OTP to complete your process.\n\n";
	    
	    
	    
		mimeMessageHelper.setText(messageBody);
		javaMailSender.send(mimeMessage);
				
	}
	public void fsendOtpToMail(String email,String otp) throws MessagingException {
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("OTP Verification");
		
		String messageBody = "Your OTP for account verification is: " + otp + "\n\n";
	    messageBody += "Please use this OTP to reset Password.\n\n";
	    
	    
	    
		mimeMessageHelper.setText(messageBody);
		javaMailSender.send(mimeMessage);
				
	}


	public boolean verifyOtp(String email, String otp) {
		Login_det user = repository.findByEmail(email);

		if (user != null && user.getOtp() != null && user.getOtp().equals(otp))
		 {
			 return true;
		 }
		return false;
	}

	
	public int resetPassword(Login_det user) {
		Login_det user1 = repository.findByEmail(user.getEmail());
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		if (user1==null) {
			return 1;
		}
		 if (user1.getOtp() .equals(user.getOtp())){
			 String password=encoder.encode(user.getPassword());
			 user1.setPassword(password);
			 repository.save(user1);
			 return 2;
		 }
		 return 3;
		 
	}
	
	    @Transactional
	    public void saveEmailAndOTP(VerifyOtpDTO verifyOtpDTO) {
	        String email = verifyOtpDTO.getEmail();
	        String otp = verifyOtpDTO.getOtp();

	        Login_det user = repository.findByEmail(email);

	       
	        if (user == null) {
	            user = new Login_det();
	            user.setEmail(email);
	            user.setOtp(otp);
	            repository.save(user);
	        }
	    }
	    
	    public void savePartialDetails(Login_det loginDto) {
	    	System.out.println("savepartial detail");
	        
	    	Login_det us = new Login_det();
	        us.setEmpid(loginDto.getEmpid());
	        us.setFullname(loginDto.getFullname());
	        us.setEmail(loginDto.getEmail());
           
	       
	        repository.save(us);
	    }


	    public void saveUser(LoginDto loginDto) {
	        
	    	 repository.save(new Login_det(loginDto.getEmpid(),loginDto.getEmail(),  loginDto.getPassword(),loginDto.getRole(),loginDto.getFullname(),loginDto.getProject(),loginDto.getPro_desc(),loginDto.getDateFrom(),loginDto.getDateTo(),loginDto.getOtp(),loginDto.getDesignation()));

	    }
	    
	    
	    public boolean existsByEmpid(String empid) {
	        return repository.existsByEmpid(empid);
	    }
	 
	 
	    public boolean emailExists(String email) {
	        Login_det user = repository.findByEmail(email);
	        return user != null;
	    }
	    
}

