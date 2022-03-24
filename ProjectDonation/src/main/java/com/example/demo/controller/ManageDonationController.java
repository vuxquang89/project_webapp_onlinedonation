package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.CampaignDAO;
import com.example.demo.dao.DonationDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.form.AccountInfoForm;
import com.example.demo.form.UpdateDonationForm;
import com.example.demo.lib.HistoryDonationExcelExporter;
import com.example.demo.lib.Libraries;
import com.example.demo.model.AppUser;
import com.example.demo.model.Campaign;
import com.example.demo.model.CampaignCriteria;
import com.example.demo.model.Donation;
import com.example.demo.model.DonationCriteria;
import com.example.demo.model.Donor;
import com.example.demo.model.ResponseBodyCampaign;
import com.example.demo.validator.UpdateDonationValidator;

@Controller
@RequestMapping("Donation")
public class ManageDonationController {

	@Autowired
	private UpdateDonationValidator updateDonationValidator;
	
	@Autowired
	private DonationDAO donationDAO;
	
	@Autowired
	private CampaignDAO campaignDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	// Set a form validator
	
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		// Form mục tiêu
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		
		if (target.getClass() == UpdateDonationForm.class) {
			dataBinder.setValidator(updateDonationValidator);
		}
					// ...
	}
	
	/*show index manage donation page*/
	@RequestMapping(value = "/admin/manageDonation", method = RequestMethod.GET)
	public String getDonation(Model model) {
		int statusDefault = 1;
		int limit = 5;
		List<Donation> donations = donationDAO.getDonations(statusDefault, 0, limit);	
		
		int totalRecords = campaignDAO.getCountCampaign(statusDefault);
		double avgPage = (double)totalRecords / limit;		
		float ceil = (float)(avgPage + (avgPage % 1 == 0 ? 0 : 0.5));		
		int totalPage = Math.round(ceil);		
		
		model.addAttribute("donations", donations);	
		model.addAttribute("limit", limit);
		model.addAttribute("totalRecords", totalRecords);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("campaignStatus", statusDefault);
		model.addAttribute("page", "get");
		return "admin/manage-donation";
	}
	
	/*handle request ajax get donation with choose status*/
	@RequestMapping(value = "/admin/actionDonation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getDonationAjax(@RequestBody CampaignCriteria campaignCriteria){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		List<Donation> donations = null;
		String message = "";
		String error = "";
		int status = 0;
		
		String action = campaignCriteria.getAction();
		int limit = campaignCriteria.getLimitItem();		
		int current = campaignCriteria.getCurrent();
		int begin = (current - 1) * limit;
		int end = current * limit;
		int totalRecords = 0;
		if(action.equalsIgnoreCase("get")) {
			donations = donationDAO.getDonations(campaignCriteria.getCampaignStatus(), begin, end);
			totalRecords = campaignDAO.getCountCampaign(campaignCriteria.getCampaignStatus());
		}else if(action.equalsIgnoreCase("search")) {	
			
			donations = donationDAO.getSearchDonations(campaignCriteria.getCampaignStatus(), campaignCriteria.getKeySearch(), begin, end);
			totalRecords = campaignDAO.getCountSearchCampaigns(campaignCriteria.getCampaignStatus(), campaignCriteria.getKeySearch());
			
		}
		
		responseBodyCampaign.setStatus(status);
		responseBodyCampaign.setMessage(message);
		responseBodyCampaign.setTotalRecords(totalRecords);
		responseBodyCampaign.setDonations(donations);
		responseBodyCampaign.setError(error);
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	/*show info donation page with campaign id*/
	@RequestMapping(value = "/admin/infoDonation/{id}", method = RequestMethod.GET)
	public ModelAndView  getInfoDonation(@PathVariable(name = "id") int id) {
		ModelAndView mAndView = new ModelAndView("admin/info-donation");
		int limit = 5;
		try {
			Donation donation = donationDAO.getDonation(id);
			List<Donation> infoDonations = donationDAO.getInfoDonations(id, 0, limit);
			int totalRecords = donationDAO.getCountDonation(id);
			double avgPage = (double)totalRecords / limit;		
			float ceil = (float)(avgPage + (avgPage % 1 == 0 ? 0 : 0.5));		
			int totalPage = Math.round(ceil);
			
			if(donation == null) {
				mAndView = new ModelAndView("admin/404Page");
			}else {
				mAndView.addObject("donation",donation);
				mAndView.addObject("infoDonations", infoDonations);
				mAndView.addObject("limit", limit);
				mAndView.addObject("totalRecords", totalRecords);
				mAndView.addObject("totalPage", totalPage);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			mAndView = new ModelAndView("admin/404Page");
		}
		
		
		return mAndView;
	}
	
	/*handle request ajax get donation with choose id*/
	@RequestMapping(value = "/admin/actionInfoDonation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getInfoDonationAjax(@RequestBody CampaignCriteria campaignCriteria){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		List<Donation> iDonations = null;
		String message = "";
		String error = "";
		int status = 0;
		
		String action = campaignCriteria.getAction();
		int limit = campaignCriteria.getLimitItem();		
		int current = campaignCriteria.getCurrent();
		int begin = (current - 1) * limit;
		int end = current * limit;
		int totalRecords = 0;
		if(action.equalsIgnoreCase("get")) {
			iDonations = donationDAO.getInfoDonations(Integer.parseInt(campaignCriteria.getId()), begin, end);			
			totalRecords = donationDAO.getCountDonation(Integer.parseInt(campaignCriteria.getId()));
		}else if(action.equalsIgnoreCase("search")) {
			
			iDonations = donationDAO.getSearchInfoDonations(Integer.parseInt(campaignCriteria.getId()), campaignCriteria.getKeySearch(), begin, end);
			totalRecords = donationDAO.getCountSearchInfoDonation(Integer.parseInt(campaignCriteria.getId()), campaignCriteria.getKeySearch());
		}
		
		responseBodyCampaign.setStatus(status);
		responseBodyCampaign.setMessage(message);
		responseBodyCampaign.setTotalRecords(totalRecords);
		responseBodyCampaign.setDonations(iDonations);
		responseBodyCampaign.setError(error);
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	/*handle request ajax update donation*/
	@RequestMapping(value = "/admin/updateInfoDonation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> updateInfoDonationAjax(@RequestBody CampaignCriteria campaignCriteria){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		Float amount = campaignCriteria.getAmount();
		String donationId = campaignCriteria.getId();
		String campaignId = campaignCriteria.getCampaignId();
		Donation donation = null;
		String message = "";
		String error = "";
		int status = 0;
		try {
			if(!donationDAO.updateInfoDonation(donationId, amount)) {
				message = "Lỗi! Kiểm tra nhập liệu.";
				status= 1;
			}else {
				donation = donationDAO.getDonation(Integer.parseInt(campaignId));
				message = "Cập nhật thành công!";
			}
		} catch (Exception e) {
			status = 1;
			message = "Lỗi! Kiểm tra nhập liệu.";
			error = e.toString();
		}
		
		responseBodyCampaign.setStatus(status);
		responseBodyCampaign.setMessage(message);		
		responseBodyCampaign.setError(error);
		responseBodyCampaign.setDonation(donation);
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	//show update donation page
	@RequestMapping(value = "/admin/updateDonation", method = RequestMethod.GET)
	public String getUpdateDonation(Model model) {
		UpdateDonationForm updateDonationForm = new UpdateDonationForm();
		model.addAttribute("updateDonationForm", updateDonationForm);
		return "admin/update-donation";
	}
	
	//show udate add new donation
	@RequestMapping(value = "/admin/updateAddDonation/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateAddDonation(@PathVariable(name = "id") int id) {
		ModelAndView mAndView = new ModelAndView("admin/update-add-donation");
		UpdateDonationForm updateDonationForm = new UpdateDonationForm();
		updateDonationForm.setCampaignId(id);
		mAndView.addObject("updateDonationForm", updateDonationForm);
		return mAndView;
	}
	
	/*handle request ajax search userID */
	@RequestMapping(value = "/admin/searchDonor", method = RequestMethod.GET)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> searchDonorAjax(@RequestParam String search){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		
		List<Donor> donors = donationDAO.getGeneralDonor(search);
		
		responseBodyCampaign.setResults(donors);
		
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	//save update add new donation
	@RequestMapping(value = "/admin/saveUpdateAddDonation", method = RequestMethod.POST)
	public String saveUpdateAddDonation(@ModelAttribute("updateDonationForm") @Validated UpdateDonationForm updateDonationForm, //
			BindingResult result, //
			Model model,
			Principal principal) {
		
		if (result.hasErrors()) {
			
			return "admin/update-add-donation";
		}
		
		if(!donationDAO.saveUpdateDonation(updateDonationForm)) {
			model.addAttribute("msg", "Lỗi! Không thể cập nhật");
		}else {
			//model.addAttribute("msg", "Cập nhật thành công");
			return "redirect:/Donation/admin/infoDonation/"+updateDonationForm.getCampaignId();
		}
		return "admin/update-add-donation";
	}
	
	//save update add new donation
	@RequestMapping(value = "/admin/saveUpdateDonation", method = RequestMethod.POST)
	public String saveUpdateDonation(@ModelAttribute("updateDonationForm") @Validated UpdateDonationForm updateDonationForm, //
			BindingResult result, //
			Model model,
			Principal principal) {
			
		if (result.hasErrors()) {
			
			return "admin/update-donation";
		}
			
		if(!donationDAO.saveUpdateDonation(updateDonationForm)) {
			model.addAttribute("msg", "Lỗi! Không thể cập nhật");
		}else {
			//model.addAttribute("msg", "Cập nhật thành công");
			return "redirect:/Donation/admin/manageDonation";
		}
		return "admin/update-donation";
	}
	
	//save update add new donation from file excel
	@RequestMapping(value = "/admin/importFileExcel", method = RequestMethod.POST)
	public String importDonationFromFileExcel(@RequestParam("file") MultipartFile reapExcelDataFile, Model model,
			HttpServletResponse response, Principal principal) throws IOException  {
				
		String currentDateTime = Libraries.getDateTimeNow();
        
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ResultReadFile_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);		
		
		XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		
		List<UpdateDonationForm> dataResults = new ArrayList<UpdateDonationForm>();    
		
		for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
			            
			XSSFRow row = worksheet.getRow(i);
			   
			String content =  row.getCell(0).getStringCellValue();
			String date =  row.getCell(1).getStringCellValue();
			if(content == "" && date == "")
				break;
			
			UpdateDonationForm updateDonationForm = new UpdateDonationForm();			
			updateDonationForm.setContent(content);
			try {
				
				
				int amount =  (int) row.getCell(2).getNumericCellValue();
				
				String[] contents = content.split("_");
				
				updateDonationForm.setCampaignId(Integer.parseInt(contents[2]));
				
				updateDonationForm.setAmount((float)amount);
				updateDonationForm.setDateTime(date);
				AppUser appUser = userDAO.findCheckAccount(contents[0]);
				if(appUser != null && campaignDAO.isCampaignEmpty(Integer.parseInt(contents[2])) && updateDonationForm.getAmount() > 0) {
					updateDonationForm.setUserId(appUser.getId());
					updateDonationForm.setResult("OK");
					donationDAO.saveUpdateDonation(updateDonationForm);
				}else {
					updateDonationForm.setResult("NOT OK");
				}
				
			} catch (Exception e) {
				updateDonationForm.setResult("NOT OK");
				System.out.println(e.toString());
			}
			dataResults.add(updateDonationForm);
		}
		
		HistoryDonationExcelExporter excelExporter = new HistoryDonationExcelExporter();
	    excelExporter.setDataResult(dataResults); 
		excelExporter.exportDataResult(response);
		
		return "redirect:/Donation/admin/manageDonation";
	}
	
	//export excel file
	@GetMapping("/admin/exportExcel/{id}")
	public String exportToExcel(@PathVariable(name = "id") int id,HttpServletResponse response, Principal principal) throws IOException {
		response.setContentType("application/octet-stream");
	        
		String currentDateTime = Libraries.getDateTimeNow();
	         
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename="+principal.getName()+"_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		try {
			Donation donation = donationDAO.getDonation(id);
			int totalRecords = donationDAO.getCountDonation(id);
			List<Donation> infoDonations = donationDAO.getInfoDonations(id, 0, totalRecords);
			HistoryDonationExcelExporter excelExporter = new HistoryDonationExcelExporter(infoDonations, donation);
		     
			excelExporter.exportInfoDonation(response);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			
		}
	         
		  
		return "redirect:/Donation/admin/infoDonation/"+id;
		
	}
}
