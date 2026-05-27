package com.example.controller;
 
 

import java.security.Principal;
import java.util.List;

import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

 
import com.example.entity.EmpID;
import com.example.entity.EmployeeSkill;
import com.example.entity.Skills;
import com.example.service.EmpSkillService;
import com.example.service.SkillService;
 

import jakarta.validation.Valid;
 
 
@Controller
public class EmpSkillController {
	@Autowired
	private EmpSkillService service;
	@Autowired
	private SkillService serv;

	@GetMapping("/addSkillsForm")
	public String showAddSkillsForm(ModelMap model, Principal principal) {
	    String empId = principal.getName(); 
	   
	    EmployeeSkill employeeSkill = new EmployeeSkill();
	    EmpID empIdObject = new EmpID();
	    empIdObject.setEmpid(empId);
	    employeeSkill.setId(empIdObject);
 
	   
	    List<Skills> skillsList = serv.getSkills();
	    List<String> subdomainsList = skillsList.stream().map(Skills::getSubdomain).distinct().collect(Collectors.toList());
	    List<String> domainsList = skillsList.stream().map(Skills::getDomain).distinct().collect(Collectors.toList());
 
	    model.addAttribute("employeeSkill", employeeSkill);
	    model.addAttribute("skillsList", skillsList);
	    model.addAttribute("subdomainsList", subdomainsList);
	    model.addAttribute("domainsList", domainsList);
 
	    return "addSkillForm";
	}
	@GetMapping("/u_analysis")
    public String SkillDetailss(Model model, Principal principal) {
        String empId = principal.getName(); 

        List<EmployeeSkill> employeeSkills = service.getEmpSkillsByEmpId(empId);
        model.addAttribute("employeeSkills", employeeSkills);

        return "u_analysis";
    }
	 
    @PostMapping("/addSkillss")
    public String addSkills(EmployeeSkill employeeSkill) {
       
        service.saveEmpSkill(employeeSkill);
        return "redirect:/user-page"; 
    }
	    @GetMapping("/skilldet")
	    public String showSkillDetails(Model model, Principal principal) {
	        String empId = principal.getName();
 
	        List<EmployeeSkill> employeeSkills = service.getEmpSkillsByEmpId(empId);
	        model.addAttribute("employeeSkills", employeeSkills);
 
	        return "skillDetails";
	    }
	    
	  
	
	

	    @GetMapping("/EmpSkillDash")
	    public String Empskilldashboard() {
	        return "EmployeeSkillDash";
	    }
 
	 @GetMapping("/addEmpSkill")
	    public String showAddEmpSkillForm(Model model) {
	        
		 model.addAttribute("employeeSkill", new EmployeeSkill());
	        return "addEmpSkill";
	    }
	 @PostMapping("/addEmpSkill")
	    public String addSkill(@ModelAttribute("employeeSkill") @Valid EmployeeSkill employeeSkill, BindingResult bindingResult) {
	        if (bindingResult.hasErrors()) {
	            return "addSkillForm";
	        }
 
	        service.saveEmpSkill(employeeSkill);
 
	        return "success";
	    }
	 @GetMapping("/skillDetails")
	 public String showSkillDetails(Model model) {

	     List<EmployeeSkill> employeeSkills = service.getEmpSkills();
	     model.addAttribute("employeeSkills", employeeSkills);
	 
	     return "AEmpSkill";
	 }
 
	 @GetMapping("/a_analysis")
	 public String sshowSkillDetails(Model model) {

	     List<EmployeeSkill> employeeSkills = service.getEmpSkills();
	     model.addAttribute("employeeSkills", employeeSkills);
	 
	     return "a_analysis";
	 }
 
	
	
 
	@PostMapping("/addEmpSkill1")
	public String addEmpskill(@ModelAttribute EmployeeSkill Eskill) {
	 service.saveEmpSkill(Eskill);
	 return "success";
	}
	@PostMapping("/addEmpSkills")
	public List<EmployeeSkill> addEmpskills(@RequestBody List<EmployeeSkill> Eskills) {
		return service.saveEmpSkills(Eskills);
	}
    @GetMapping("/EmpSkills")
	public List<EmployeeSkill> findAllEmpskills() {
		return service.getEmpSkills();
	}
    @GetMapping("/searchEmployeeSkills")
    public String showEmployeeSkills(Model model) {
        List<EmployeeSkill> employeeSkills = service.getEmpSkills();
        model.addAttribute("employeeSkills", employeeSkills);
        return "Empskillsearch";
    }
   
    @GetMapping("/deleteEmpSkills1")
    public String deleteEmployeeSkills(Model model, Principal principal) {
    	  String empId = principal.getName();
    	   List<EmployeeSkill> employeeSkills = service.getEmpSkillsByEmpId(empId);
     
        model.addAttribute("employeeSkills", employeeSkills);
        return "dltempskills";
    }
 
    
    public void EmployeeSkillController(EmpSkillService employeeSkillService) {
        this.service = employeeSkillService;
    }
    @PostMapping("/deleteEmployeeskills")
    public String deleteEmployeeSkills(@RequestParam(name = "empSkillIds", required = false) List<String> empSkillIds) {
        if (empSkillIds != null) {
            for (String empSkillId : empSkillIds) {
                String[] parts = empSkillId.split("-");
                if (parts.length == 2) {
                    String empId = parts[0];
                    Integer skillId = Integer.parseInt(parts[1]);
                    service.deleteEmpSkill(empId, skillId);
                }
            }
        }
        return "redirect:/deleteEmpSkills1";
    }

    @GetMapping("/employeeskills")
    public String getAllEmpSkills(Model model, Principal principal) {
    	String empId = principal.getName(); 
    	 List<EmployeeSkill> employeeSkills = service.getEmpSkillsByEmpId(empId);
        model.addAttribute("employeeSkills", employeeSkills);
        return "employeeSkillsList";
    }
 
    @PostMapping("/editEmployeeskill")
    public String editEmployeeSkill(@RequestParam String empid, @RequestParam Integer skillid, Model model) {
        EmployeeSkill employeeSkill = service.getEmpSkillById(empid, skillid);
        model.addAttribute("employeeSkill", employeeSkill);
        return "editEmployeeSkill";
    }
 
    @PostMapping("/saveChanges")
    public String saveChanges(@ModelAttribute EmployeeSkill employeeSkill, Model model) {
       
        service.updateEmpSkill(
                employeeSkill.getId().getEmpid(),
                employeeSkill.getId().getSkillid(),
                employeeSkill.getProficiency(),
                employeeSkill.getExp(),
                employeeSkill.getCert_source()
        );
      
        return "redirect:/employeeskills";
    }
    
 
}