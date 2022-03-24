package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.UserDAO;
import com.example.demo.form.UserForm;
import com.example.demo.lib.Libraries;
import com.example.demo.model.AppUser;
import com.example.demo.service.MailServiceImpl;
import com.example.demo.validator.UserValidator;

@Controller
@RequestMapping("Donation")
public class RegisterController {

	@Autowired
	private UserValidator appUserValidator;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private MailServiceImpl mailService;
	
	// Set a form validator
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		// Form mục tiêu
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		
		if (target.getClass() == UserForm.class) {
			dataBinder.setValidator(appUserValidator);
		}
			// ...
	}
	
	//hiển thị trang đăng ký
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		UserForm form = new UserForm();
		
		model.addAttribute("appUserForm", form);
		
		return "/register";
	}
	
	// Phương thức này được gọi để lưu thông tin đăng ký.
	// @Validated: Để đảm bảo rằng Form này
	// đã được Validate trước khi phương thức này được gọi.
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveRegister(Model model, //
			@ModelAttribute("appUserForm") @Validated UserForm appUserForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		// Validate result
		if (result.hasErrors()) {
			
			return "register";
		}
		
		String password = Libraries.randomPassword(8);
		appUserForm.setPass(password);
		if(appUserForm.getFullName() == "" || appUserForm.getFullName() == null)
			appUserForm.setFullName("Nhà hảo tâm");
		if(!userDAO.insertUser(appUserForm)) {
			model.addAttribute("errorMessage", "Error: do not create new user");
			return "register";
		}else {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					mailService.sendMail(appUserForm.getFullName(), appUserForm.getUsername(), password, appUserForm.getEmail());					
				}
			});
			thread.start();
			
		}
		
		return "redirect:/login";
	}
}
