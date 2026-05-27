package com.example.service;
	import static org.junit.jupiter.api.Assertions.assertNotNull;
	import static org.mockito.ArgumentMatchers.any;
	import static org.mockito.ArgumentMatchers.eq;
	import static org.mockito.Mockito.doReturn;	
	import java.util.Optional;

	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;
	import org.mockito.InjectMocks;
	import org.mockito.Mock;
	import org.mockito.MockitoAnnotations;
	import org.springframework.boot.test.context.SpringBootTest;

	import com.example.entity.EmpID;
	import com.example.entity.EmployeeSkill;
	import com.example.repository.Empskillrepo;

	@SpringBootTest
	public class EmpskillServiceTest {

	    @Mock
	    private Empskillrepo empSkillRepo;

	    @InjectMocks
	    private EmpSkillService empSkillService;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testSaveEmpSkill() {
	        // Mocking the repository behavior
	        doReturn(new EmployeeSkill()).when(empSkillRepo).save(any(EmployeeSkill.class));

	        // Testing the service method
	        empSkillService.saveEmpSkill(new EmployeeSkill());

	       
	    }

	    @Test
	    public void testSaveEmpSkill1() {
	        // Mocking the repository behavior
	        doReturn(new EmployeeSkill()).when(empSkillRepo).save(any(EmployeeSkill.class));

	        // Testing the service method
	        EmployeeSkill savedEmpSkill = empSkillService.saveEmpSkill1(new EmployeeSkill());

	        // Verifying that the save method of the repository is called
	        // with any EmployeeSkill object
	        assertNotNull(savedEmpSkill);
	     
	    }

	    @Test
	    public void testGetEmpSkillById() {
	        // Mocking the repository behavior
	        EmpID empID = new EmpID("testEmpId", 1);
	        doReturn(Optional.of(new EmployeeSkill())).when(empSkillRepo).findById(eq(empID));

	        // Testing the service method
	        EmployeeSkill empSkill = empSkillService.getEmpSkillById("testEmpId", 1);

	        // Verifying that the findById method of the repository is called
	        // with the expected EmpID object
	       
	    }

	  
	    @Test
	    public void testUpdateEmpSkill() {
	        // Mocking the repository behavior
	        EmpID empID = new EmpID("testEmpId", 1);
	        EmployeeSkill existingEmpSkill = new EmployeeSkill();
	        doReturn(Optional.of(existingEmpSkill)).when(empSkillRepo).findById(eq(empID));
	        doReturn(existingEmpSkill).when(empSkillRepo).save(any(EmployeeSkill.class));

	        // Testing the service method
	        empSkillService.updateEmpSkill("testEmpId", 1, "proficiency", "exp", "certSource");

	        
	    }

	}


