package com.example.logservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.LoginDto;
import com.example.logrepo.logRepository;
import com.example.model.Login_det;

@Service
public class LogServiceImpl implements LogService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private logRepository logRepository;
	
	 @Override
	    public Login_det save(LoginDto loginDto) {
	
	        Login_det login_det = new Login_det(
	        		loginDto.getEmpid(),
	        		loginDto.getEmail(),  
	        		loginDto.getPassword(),
	        		loginDto.getRole(),
	        		loginDto.getFullname(),
	        		loginDto.getProject(),
	        		loginDto.getPro_desc(),
	        		loginDto.getDateFrom(),
	        		loginDto.getDateTo(),
	        		loginDto.getOtp(),
	        		loginDto.getDesignation()
	        	
	            
	        );
	        return logRepository.save(login_det);
	    }
	
  

	@Override
	public Login_det getEmployeeById(long long1) {
		
		return null;
	}
	
	 @Override
	    public void updateEmployeeDetails(LoginDto loginDto) {
	      
	        Login_det existingLoginDet = logRepository.findByEmpid(loginDto.getEmpid());
	        if (existingLoginDet != null) {
	      
	            existingLoginDet.setEmail(loginDto.getEmail());
	            existingLoginDet.setFullname(loginDto.getFullname());
	            existingLoginDet.setProject(loginDto.getProject());
	            existingLoginDet.setPro_desc(loginDto.getPro_desc());
	            existingLoginDet.setDateFrom(loginDto.getDateFrom());
	            existingLoginDet.setDateTo(loginDto.getDateTo());
	            existingLoginDet.setDesignation(loginDto.getDesignation()); 
	            logRepository.save(existingLoginDet);
	        }
	    }



	@Override
	public List<Login_det> getDetails(String email) {
		// TODO Auto-generated method stub
		List<Login_det> a = logRepository.findAllByEmail(email);
		return a;
		
		 
	}

}
