package com.example.demo.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.demo.dao.UserDAO;
import com.example.demo.form.AccountInfoForm;

import com.example.demo.lib.Libraries;

@Component
public class AccountInfoValidator implements Validator{

	// common-validator library.
	
	private EmailValidator emailValidator = EmailValidator.getInstance();
	
	@Autowired
	private UserDAO userDAO;
	
	// Các lớp được hỗ trợ bởi Validator này.
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == AccountInfoForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		AccountInfoForm accountInfoForm = (AccountInfoForm) target;

		// Kiểm tra các field của UserForm.				
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.appUserForm.email");			
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.appUserForm.phone");
			
		
		/*check email*/
		if (!errors.hasFieldErrors("email")) {
			
			if (!this.emailValidator.isValid(accountInfoForm.getEmail())) {
				// Email không hợp lệ.
				errors.rejectValue("email", "Pattern.appUserForm.email");
			} else if(!accountInfoForm.getEmail().equalsIgnoreCase(accountInfoForm.getH_email())){
				boolean emailCheck = userDAO.checkEmail(accountInfoForm.getEmail());
				if (emailCheck) {
					// Email đã được sử dụng bởi tài khoản khác.
					errors.rejectValue("email", "Duplicate.appUserForm.email");
				}
			}
		
		}
			
		/*check phone*/
		if (!errors.hasFieldErrors("phone")) {
			if(Libraries.isValidPhone(accountInfoForm.getPhone())) {
				if(!accountInfoForm.getPhone().equalsIgnoreCase(accountInfoForm.getH_phone())){
					boolean phoneCheck = userDAO.checkPhone(accountInfoForm.getPhone());
					if (phoneCheck) {
						// số điện thoại đã tồn tại
						errors.rejectValue("phone", "Duplicate.appUserForm.phone");
					}
				}
			}else {
				//sdt không hợp lệ
				errors.rejectValue("phone", "Pattern.appUserForm.phone");
			}
		}
	}
}
