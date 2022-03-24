package com.example.demo.validator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.demo.dao.UserDAO;
import com.example.demo.form.UserForm;
import com.example.demo.lib.Libraries;

@Component
public class UserValidator implements Validator {
	
	// common-validator library.
	
	private EmailValidator emailValidator = EmailValidator.getInstance();
	@Autowired
	private UserDAO userDAO;

	// Các lớp được hỗ trợ bởi Validator này.
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == UserForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserForm appUserForm = (UserForm) target;

		// Kiểm tra các field của UserForm.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.appUserForm.username");		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.appUserForm.email");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pass", "NotEmpty.appUserForm.pass");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPass", "NotEmpty.appUserForm.confirmPass");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.appUserForm.phone");
		
		

		if (!errors.hasFieldErrors("email")) {
			if (!this.emailValidator.isValid(appUserForm.getEmail())) {
				// Email không hợp lệ.
				errors.rejectValue("email", "Pattern.appUserForm.email");
			} else {
				boolean emailCheck = userDAO.checkEmail(appUserForm.getEmail());
				if (emailCheck) {
					// Email đã được sử dụng bởi tài khoản khác.
					errors.rejectValue("email", "Duplicate.appUserForm.email");
				}
			}
		}

		if (!errors.hasFieldErrors("username")) {
			if (appUserForm.getUsername().length() < 6 || appUserForm.getUsername().length() > 20) {
	            errors.rejectValue("username", "Size.appUserForm.username");
	        }else if(!Libraries.isSpecial(appUserForm.getUsername())) {
	        	errors.rejectValue("username", "Special.appUserForm.username");
	        }else {
	        	boolean userCheck = userDAO.checkUserName(appUserForm.getUsername());
				if (userCheck) {
					// Tên tài khoản đã bị sử dụng bởi người khác.
					errors.rejectValue("username", "Duplicate.appUserForm.username");
				}
	        }
			
		}

		/*
		if (!errors.hasErrors()) {
			if (appUserForm.getPass().length() < 8 || appUserForm.getPass().length() > 32) {
	            errors.rejectValue("pass", "Size.appUserForm.pass");
	        }else if (!appUserForm.getConfirmPass().equals(appUserForm.getPass())) {
				errors.rejectValue("confirmPass", "Match.appUserForm.confirmPass");
			}
		}
		*/
		if (!errors.hasFieldErrors("phone")) {
			if(Libraries.isValidPhone(appUserForm.getPhone())) {
				boolean phoneCheck = userDAO.checkPhone(appUserForm.getPhone());
				if (phoneCheck) {
					// số điện thoại đã tồn tại
					errors.rejectValue("phone", "Duplicate.appUserForm.phone");
				}
			}else {
				//sdt không hợp lệ
				errors.rejectValue("phone", "Pattern.appUserForm.phone");
			}
		}
	}
	
	
}
