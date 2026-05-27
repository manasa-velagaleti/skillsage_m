package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Skills;
import com.example.service.SkillService;

@Controller 
public class SkillController {
	@Autowired
	private SkillService service;
	
	   @GetMapping("/addSkill")
	    public String showAddSkillForm(Model model) {
	        List<String> allDomains = service.getAllDomains();
	        List<String> allSubdomains = service.getAllSubdomains();
	        Map<String, List<String>> allSubdomainsByDomain = service.getAllSubdomainsByDomain(); // Modify this method in your service

	        model.addAttribute("allDomains", allDomains);
	        model.addAttribute("allSubdomains", allSubdomains);
	        model.addAttribute("allSubdomainsByDomain", allSubdomainsByDomain);

	        return "Adminskill";
	    }
   
	@PostMapping("/addSkill")
	public String addSkill(@ModelAttribute Skills skill) {
	     service.saveSkill(skill);
	    return "SkillDashboard";
	}

	
	@PostMapping("/addSkills")
	public List<Skills> addskills(@RequestBody List<Skills> skills) {
		return service.saveSkills(skills);
		
	}

	 @GetMapping("/Skilldashboard")
	    public String skilldashboard() {
	        
	        return "SkillDashboard";
	    }
    @GetMapping("/Skills")
    public String findAllSkills(Model model) {
        List<Skills> skills = service.getSkills();
        model.addAttribute("skills", skills);
        return "skillreport";
    }

    
    @GetMapping("/searchSkills")
    public String showSkills(Model model) {
        List<Skills> allSkills = service.getSkills(); 
        model.addAttribute("skills", allSkills);
        return "Skillsearch";
    }
    
    @GetMapping("/searchSkillss")
    public String showSkillsss(Model model) {
        List<Skills> allSkills = service.getSkills(); 
        model.addAttribute("skills", allSkills);
        return "viewuserskills";
    }
    
    
    @GetMapping("/skillForm")
    public String showSkillForm(Model model) {
        List<String> allDomains = service.getAllDomains();
        List<String> allSubdomains = service.getAllSubdomains();
        Map<String, List<String>> allSubdomainsByDomain = service.getAllSubdomainsByDomain();

        model.addAttribute("allDomains", allDomains);
        model.addAttribute("allSubdomains", allSubdomains);
        model.addAttribute("allSubdomainsByDomain", allSubdomainsByDomain);

        return "skillForm";
    }

    @PutMapping("/updateDomain")
    public ResponseEntity<String> updateDomain(@RequestParam("domain") String domain, 
                                               @RequestParam("editedDomain") String editedDomain) {
        service.updateDomain(domain, editedDomain);
        return ResponseEntity.ok("Domain updated successfully");
    }

   

    @PutMapping("/updateSubdomain")
    public ResponseEntity<String> updateSubdomain(@RequestParam String domain, @RequestParam String subdomain, @RequestParam String editedSubdomain) {
        service.updateSubdomain(domain, subdomain, editedSubdomain);
        return ResponseEntity.ok("Subdomain updated successfully");
    }

    @PutMapping("/updateSkillname")
    public ResponseEntity<String> updateSkillname(@RequestParam String domain, @RequestParam String subdomain, @RequestParam String skillname, @RequestParam String editedSkillname) {
        service.updateSkillname(domain, subdomain, skillname, editedSkillname);
        return ResponseEntity.ok("Skill name updated successfully");
    }
 
    @GetMapping("/domains")
    public List<String> getAllDomains() {
        return service.getAllDomains();
    }
    
    
    @ResponseBody
    @GetMapping("/subdomains")
    public List<String> getSubdomains(@RequestParam String domain) {
        return service.getSubdomainsByDomain(domain);
    }

    @ResponseBody
    @GetMapping("/skillnames")
    public List<String> getSkillnames(@RequestParam String domain, @RequestParam String subdomain) {
        return service.getSkillnamesByDomainAndSubdomain(domain, subdomain);
    }
    
    
    @GetMapping("/delskills")
    public String getAllSkills(Model model) {
        List<Skills> skills = service.getSkills();
        model.addAttribute("skills", skills);
        return "DeleteSkill"; 
    }

    @PostMapping("/delete-skills")
    @ResponseBody
    public void deleteSelectedSkills(@RequestBody List<Integer> skillIds) {
        // Delete selected skills and related employee skills
        service.deleteSelectedSkills(skillIds);
    }

}
