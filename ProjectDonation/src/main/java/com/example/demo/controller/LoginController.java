package com.example.demo.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping("Donation")
public class LoginController {
	
	/*view login page*/
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
	
	/*view user info page*/
	@RequestMapping(value = "/loginAccount", method = RequestMethod.GET)
	public String loginAccount(Model model, Principal principal) {
		

		 // (1) (en)
		 // After user login successfully.
		 // (vi)
		 // Sau khi user login thanh cong se co principal
		 String userName = principal.getName();
		 

		 //System.out.println("User Name: " + userName);
		 
       
		 User loginedUser = (User) ((Authentication) principal).getPrincipal();
		 //System.out.println(loginedUser);
		 //System.out.println(loginedUser.getAuthorities());
		 //user.getUsername()
		 //user.getAuthorities()
		//model.addAttribute("userInfo", loginedUser);
		
		String role = getRole(loginedUser.getAuthorities());
		if(role.equals("ROLE_USER")) {
			return "redirect:index";
		}else if(role.equals("ROLE_ADMIN") || role.equals("ROLE_ROOT")) {
			return "redirect:admin";
		}
		return "redirect:index";
	}
	
	/*
	@PostMapping(value = "/fail_login")
	public String handleFailedLogin(Model model) {
		model.addAttribute("msg", "Tên đăng nhập hoặc mật khẩu không đúng!");
		//return "login";
		return "redirect:/login?error";
	}
	*/
	
	/*error when user login do not have permission to access this page*/
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();

			//String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", loginedUser);

			String message = "Chào " + principal.getName() //
                   + "<br> Bạn không có quyền truy cập trang này!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}
	
	/*get role*/
	private String getRole(Collection<GrantedAuthority> authorities) {
		String role = "";
		if (authorities != null && !authorities.isEmpty()) {
			
			for (GrantedAuthority a : authorities) {
				role = a.getAuthority();				
			}
			
	    }
		return role;
	}
}
