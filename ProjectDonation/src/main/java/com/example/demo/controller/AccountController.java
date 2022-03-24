package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.DonationDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.form.AccountInfoForm;
import com.example.demo.form.UserForm;
import com.example.demo.lib.EncrytedPassword;
import com.example.demo.lib.HistoryDonationExcelExporter;
import com.example.demo.lib.Libraries;
import com.example.demo.model.AccountCriteria;
import com.example.demo.model.AppUser;
import com.example.demo.model.Campaign;
import com.example.demo.model.CampaignCriteria;
import com.example.demo.model.Donation;
import com.example.demo.model.ResponseBodyAccount;
import com.example.demo.model.ResponseBodyCampaign;
import com.example.demo.service.MailServiceImpl;
import com.example.demo.validator.AccountInfoValidator;

@Controller
@RequestMapping("Donation")
public class AccountController {
	
	@Autowired
	private AccountInfoValidator accountInfoValidator;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private DonationDAO donationDAO;
	
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
		
		if (target.getClass() == AccountInfoForm.class) {
			dataBinder.setValidator(accountInfoValidator);
		}
				// ...
	}
	
	/*show forgot password page*/
	@RequestMapping(value = {"/forgotPassword"}, method = RequestMethod.GET)
	public String forgotPassword(Model model) {
		UserForm userForm = new UserForm();	
		model.addAttribute("userForm", userForm);
		return "forgotPassword";
	}
	
	/*action send email*/
	@RequestMapping(value = {"/forgotPassword"}, method = RequestMethod.POST)
	public String sendPassword(Model model, @ModelAttribute("userForm")UserForm userForm) {
		AppUser appUser = userDAO.findAccountEmail(userForm.getEmail());
		String msg = "";
		if(appUser!=null) {
			String password = Libraries.randomPassword(8);
			String encodedPassword = EncrytedPassword.encrytePassword(password);
			if(!userDAO.updatePass(appUser.getUsername(), encodedPassword)) {
				msg = "Không thể lấy lại mật khẩu! Hãy thử lại.";
			}else {
				Thread thread = new Thread(new Runnable() {
					public void run() {
						mailService.sendMail(appUser.getFullName(), appUser.getUsername(), password, userForm.getEmail());					
					}
				});
				thread.start();
				
				msg="Bạn hãy kiểm tra email!";
			}
		}else {
			msg = "Email không đúng";
		}
		
		model.addAttribute("msg", msg);
		return "forgotPassword";
	}
	
	/*show info account page*/
	@RequestMapping(value = {"/user/accountInfo"}, method = RequestMethod.GET)
	public String accountInfo(Model model, Principal principal) {
		AccountInfoForm accountInfoForm = userDAO.findAccount(principal.getName());				
		model.addAttribute("account", accountInfoForm);
		return "info-account";
	}
	
	/*save info account*/
	
	@RequestMapping(value = "/user/updateAccountInfo", method = RequestMethod.POST)
	public String saveInfoAccount(@ModelAttribute("account") @Validated AccountInfoForm infoForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes,
			Principal principal) {
		// Validate result
		
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("account", infoForm);
			
			return "info-account";
		}
		
		if(!userDAO.updateInfoUser(infoForm, principal.getName())) {
			redirectAttributes.addFlashAttribute("account", infoForm);
			
			redirectAttributes.addFlashAttribute("status", "1");
			redirectAttributes.addFlashAttribute("message", "Lỗi: không thể cập nhật!");
			return "info-account";
		}
		redirectAttributes.addFlashAttribute("status", "0").addFlashAttribute("message", "Cập nhật thành công!");
		return "redirect:/Donation/user/accountInfo";
	}
	
	/*handle request ajax get view more campaign*/
	@RequestMapping(value = "/user/check", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getViewMoreCampaignAjax(@RequestBody AccountCriteria accountCriteria){
		ResponseBodyAccount responseBodyAccount = new ResponseBodyAccount();
		String action = accountCriteria.getAction();
		
		boolean check = false;
		
		if(action.equalsIgnoreCase("checkpass")) {
			
			AppUser account = userDAO.findUserAccount(accountCriteria.getUsername());
			if(account != null) {
				String encodedPassword = account.getPass();
				
				check = EncrytedPassword.checkPass(accountCriteria.getValue(), encodedPassword);
			}
			
		}else if(action.equalsIgnoreCase("update")) {
			String encodedPassword = EncrytedPassword.encrytePassword(accountCriteria.getValue());
			
			check = userDAO.updatePass(accountCriteria.getUsername(), encodedPassword);
		}
		responseBodyAccount.setCheck(check);
		return ResponseEntity.ok(responseBodyAccount);
	}
	
	/*show history donation of donor*/
	@RequestMapping(value = {"/user/historyDonation"}, method = RequestMethod.GET)
	public String historyDonation(Model model, Principal principal) {
		int limit = 5;
		//String dateNow = Libraries.getDateNow();
		String date = donationDAO.getDonationDate(principal.getName());
		Donation generalDonation = donationDAO.getGeneralDonation(principal.getName());
		
		List<Donation> hisDonations = donationDAO.getAccountDonations(principal.getName(),date, 0, limit);
		int totalRecords = donationDAO.getCountAccountDonation(principal.getName(), date);
		double avgPage = (double)totalRecords / limit;		
		float ceil = (float)(avgPage + (avgPage % 1 == 0 ? 0 : 0.5));		
		int totalPage = Math.round(ceil);		
		
		model.addAttribute("hisDonations", hisDonations);	
		model.addAttribute("hisDate", date);
		model.addAttribute("generalDonation", generalDonation);
		model.addAttribute("limit", limit);
		model.addAttribute("totalRecords", totalRecords);
		model.addAttribute("totalPage", totalPage);		
		model.addAttribute("page", "get");
		
		return "history-donation";
	}
	
	/*handle request ajax get donation with choose id*/
	
	@RequestMapping(value = "/user/actionHisDonation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getHisDonationAjax(@RequestBody CampaignCriteria campaignCriteria, Principal principal){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		List<Donation> hDonations = null;
		String message = "";
		String error = "";
		int status = 0;
		
		String action = campaignCriteria.getAction();
		String fromDate = campaignCriteria.getFromDate();		
		int limit = campaignCriteria.getLimitItem();		
		int current = campaignCriteria.getCurrent();
		int begin = (current - 1) * limit;
		int end = current * limit;
		int totalRecords = 0;
		if(action.equalsIgnoreCase("get")) {
			hDonations = donationDAO.getAccountDonations(principal.getName(), fromDate, begin, end);			
			totalRecords = donationDAO.getCountAccountDonation(principal.getName(), fromDate);
		}else if(action.equalsIgnoreCase("search")) {
			
			hDonations = donationDAO.getSearchAccountDonations(principal.getName(), fromDate, campaignCriteria.getKeySearch(), begin, end);
			totalRecords = donationDAO.getCountSearchAccountDonation(principal.getName(), fromDate, campaignCriteria.getKeySearch());
		}
		
		responseBodyCampaign.setStatus(status);
		responseBodyCampaign.setMessage(message);
		responseBodyCampaign.setTotalRecords(totalRecords);
		responseBodyCampaign.setDonations(hDonations);
		responseBodyCampaign.setError(error);
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	/*handle request ajax delete donation with choose id*/
	
	@RequestMapping(value = "/user/deleteGeneralDonation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> deleteGeneralDonationAjax(@RequestBody CampaignCriteria campaignCriteria, Principal principal){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		String[] iDs = campaignCriteria.getId().split(" ");
		
		String message = "";
		String error = "";
		int status = 0;
		int limit = campaignCriteria.getLimitItem();
		try {
			boolean checkDel = true;
			for (String string : iDs) {
				int id = Integer.parseInt(string);
				if(!donationDAO.deleteGeneralDonation(principal.getName(), id)) {
					checkDel = false;
					break;
				}
			}
			if(!checkDel) {
				//không thể tiếp tục xóa!
				message = "Không thể tiếp tục xóa!";
				status = 1;
			}else {
				message = "Xóa thành công!";
			}
		} catch (Exception e) {
			message = "Không thể tiếp tục xóa!";
			status = 1;
			error = e.toString();
		}
		
		
		String fromDate = campaignCriteria.getFromDate();		
		List<Donation> hisDonations = donationDAO.getAccountDonations(principal.getName(),fromDate, 0, limit);
		int totalRecords = donationDAO.getCountAccountDonation(principal.getName(), fromDate);		
		
		
		responseBodyCampaign.setStatus(status);
		responseBodyCampaign.setMessage(message);
		responseBodyCampaign.setTotalRecords(totalRecords);
		responseBodyCampaign.setDonations(hisDonations);
		responseBodyCampaign.setError(error);
		responseBodyCampaign.setLimit(limit);
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	//export excel file
	@GetMapping("/user/exportExcel")
    public String exportToExcel(HttpServletResponse response, Principal principal) throws IOException {
        response.setContentType("application/octet-stream");
        
        String currentDateTime = Libraries.getDateTimeNow();
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+principal.getName()+"_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Donation> hisDonations = donationDAO.getAccountDonations(principal.getName());
         
        HistoryDonationExcelExporter excelExporter = new HistoryDonationExcelExporter(hisDonations);
         
        excelExporter.export(response);  
        return "history-donation";
    }
}
