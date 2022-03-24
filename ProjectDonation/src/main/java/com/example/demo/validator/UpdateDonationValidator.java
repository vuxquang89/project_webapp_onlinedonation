package com.example.demo.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.demo.dao.CampaignDAO;
import com.example.demo.dao.DonationDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.form.UpdateDonationForm;
import com.example.demo.lib.Libraries;

@Component
public class UpdateDonationValidator implements Validator {

	@Autowired
	private CampaignDAO campaignDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == UpdateDonationForm.class;		
	}

	@Override
	public void validate(Object target, Errors errors) {
		UpdateDonationForm uDonationForm = (UpdateDonationForm) target;
		
		// Kiểm tra các field của UserForm.				
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "campaignId", "NotEmpty.updateDonationForm.campaignId");			
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "NotEmpty.updateDonationForm.userId");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "NotEmpty.updateDonationForm.amount");
		
		
		if (!errors.hasFieldErrors("campaignId")) {
			if(!campaignDAO.isCampaignEmpty(uDonationForm.getCampaignId())) {
				//mã không tồn tại
				
				errors.rejectValue("campaignId", "Duplicate.updateDonationForm.campaignId");
			}
		}
		
		if (!errors.hasFieldErrors("userId")) {
			if(!userDAO.isUserEmpty(uDonationForm.getUserId())) {
				//mã không tồn tại
				
				errors.rejectValue("userId", "Duplicate.updateDonationForm.userId");
			}
		}
		
		if (!errors.hasFieldErrors("amount")) {
			if(uDonationForm.getAmount() <= 0) {
				//số tiền không hợp lệ
				errors.rejectValue("amount", "Pattern.updateDonationForm.amount");
			}
		}
	}

}
