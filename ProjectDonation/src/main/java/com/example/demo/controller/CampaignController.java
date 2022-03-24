package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.ast.Literal;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import com.example.demo.dao.BankAccountDAO;
import com.example.demo.dao.CampaignDAO;
import com.example.demo.form.CreateCampaignForm;
import com.example.demo.lib.Libraries;
import com.example.demo.model.BankAccountInfo;
import com.example.demo.model.Campaign;
import com.example.demo.model.CampaignCriteria;
import com.example.demo.model.OptionCampaign;
import com.example.demo.model.ResponseBodyCampaign;


@Controller
@RequestMapping("Donation")
public class CampaignController {
	
	@Autowired
	public CampaignDAO campaignDAO;
	
	@Autowired
	public BankAccountDAO bankAccountDAO;
	
	//private static String UPLOAD_DIR = System.getProperty("uer.home") + "/test";
	@Value("${upload.path}")
	private String uploadPath;
	
	/*load admin page*/
	@RequestMapping(value = {"/admin", "/admin/dashboard"}, method = RequestMethod.GET)
	public String dashboard(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		
		int inactive = campaignDAO.getCountCampaign(0);
		int active = campaignDAO.getCountCampaign(1);
		int completed = campaignDAO.getCountCampaign(3);
		int expired = campaignDAO.getCountCampaign(2);
		
		model.addAttribute("userInfo", loginedUser);
		model.addAttribute("inactive", inactive);
		model.addAttribute("active", active);
		model.addAttribute("completed", completed);
		model.addAttribute("expired", expired);
		return "admin/index";
	}
	
	/*load default page manager campaign*/
	@RequestMapping(value = {"/admin/managerCampaign", "/admin/managerCampaign/"}, method = RequestMethod.GET)
	public String managerCampaign(Model model) {
		int statusDefault = 0;
		int limit = 5;
		List<Campaign> campaigns = campaignDAO.getCampaigns(statusDefault, 0, limit);	
		
		int totalRecords = campaignDAO.getCountCampaign(statusDefault);
		double avgPage = (double)totalRecords / limit;		
		float ceil = (float)(avgPage + (avgPage % 1 == 0 ? 0 : 0.5));		
		int totalPage = Math.round(ceil);		
		
		model.addAttribute("campaigns", campaigns);	
		model.addAttribute("limit", limit);
		model.addAttribute("totalRecords", totalRecords);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("campaignStatus", statusDefault);
		model.addAttribute("page", "get");
		return "admin/manager-campaign";
	}
	
	/*show campaign with id*/
	@RequestMapping(value = "/admin/managerCampaign/{id}", method = RequestMethod.GET)
	public String managerCampaign(@PathVariable(name = "id") int status, Model model) {
		int campaignStatus = status;
		int limit = 5;
		List<Campaign> campaigns = campaignDAO.getCampaigns(campaignStatus, 0, limit);	
		
		int totalRecords = campaignDAO.getCountCampaign(campaignStatus);
		double avgPage = (double)totalRecords / limit;		
		float ceil = (float)(avgPage + (avgPage % 1 == 0 ? 0 : 0.5));		
		int totalPage = Math.round(ceil);	
		
		OptionCampaign optionCampaign = new OptionCampaign(campaignStatus, limit);
		
		model.addAttribute("campaigns", campaigns);	
		model.addAttribute("limit", limit);
		model.addAttribute("totalRecords", totalRecords);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("campaignStatus", campaignStatus);
		model.addAttribute("optionCampaign", optionCampaign);
		model.addAttribute("page", "getId");
		return "admin/manager-campaign";
	}
	
	/*handle request ajax get campaign with choose status*/
	@RequestMapping(value = "/admin/actionCampaign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getCampaignAjax(@RequestBody CampaignCriteria campaignCriteria){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		List<Campaign> campaigns = null;
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
			campaigns = campaignDAO.getCampaigns(campaignCriteria.getCampaignStatus(), begin, end);
			totalRecords = campaignDAO.getCountCampaign(campaignCriteria.getCampaignStatus());
		}else if(action.equalsIgnoreCase("search")) {	
			
			campaigns = campaignDAO.getSearchCampaigns(campaignCriteria.getCampaignStatus(), campaignCriteria.getKeySearch(), begin, end);
			totalRecords = campaignDAO.getCountSearchCampaigns(campaignCriteria.getCampaignStatus(), campaignCriteria.getKeySearch());
		}else if(action.equalsIgnoreCase("delete")) {
			String[] idStrings = campaignCriteria.getId().split(" ");
			boolean checkDel = true;
			for (String string : idStrings) {
				int id = Integer.parseInt(string);
				if(!campaignDAO.deleteCampaign(id)) {
					checkDel = false;
					break;
				}
			}
			if(!checkDel) {
				//không thể tiếp tục xóa!
				message = "Không thể tiếp tục xóa!";
				status = 1;
			}else {
				status = 0;
				message = "Xóa thành công!";
			}
			
			campaigns = campaignDAO.getCampaigns(campaignCriteria.getCampaignStatus(), 0, limit);
			totalRecords = campaignDAO.getCountCampaign(campaignCriteria.getCampaignStatus());
			
		}
		
		responseBodyCampaign.setStatus(status);
		responseBodyCampaign.setMessage(message);
		responseBodyCampaign.setTotalRecords(totalRecords);
		responseBodyCampaign.setCampaigns(campaigns);
		responseBodyCampaign.setError(error);
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	/*load create campaign page*/
	@RequestMapping(value = "/admin/createCampaign", method = RequestMethod.GET)
	public String createCampaign(Model model) {
		CreateCampaignForm createCampaignForm = new CreateCampaignForm();
		List<BankAccountInfo> bankAccounts = bankAccountDAO.getBankAccount();
		model.addAttribute("bankAccounts", bankAccounts);
		model.addAttribute("createCampaignForm", createCampaignForm);
		return "admin/create-campaign";
	}
	
	/*create new campaign*/ 
	@RequestMapping(value = "/admin/createCampaign", method = RequestMethod.POST)
	public String createCampaign(Model model, RedirectAttributes redirAttrs, 
			@ModelAttribute("createCampaignForm")CreateCampaignForm createCampaignForm) {
		
		try {
			
			Campaign campaign = new Campaign();
			campaign.setName(createCampaignForm.getCampaignName());
			campaign.setSummary(createCampaignForm.getSummary());
			campaign.setContent(createCampaignForm.getContent());
			campaign.setGoal(createCampaignForm.getGoal());
			campaign.setDateCreated(Libraries.getDateNow());
			campaign.setDateStart(createCampaignForm.getDateStart());
			campaign.setDateEnd(createCampaignForm.getDateEnd());
			//campaign.setUrlAvatar(createCampaignForm.getUrlPicture());
			String nameFolder = Libraries.getDateNow(); //tạo tên thư mục theo ngày tạo
			campaign.setUrlAvatar(this.saveUploadedImageFile(createCampaignForm.getFile(), nameFolder));
			campaign.setBankId(createCampaignForm.getBankId());
			if(Libraries.convertStringToDate(campaign.getDateStart()).compareTo(Libraries.convertStringToDate(campaign.getDateEnd())) == 0) {
				//CreateCampaignForm createCampaignForm = new CreateCampaignForm();
				List<BankAccountInfo> bankAccounts = bankAccountDAO.getBankAccount();
				createCampaignForm.setUrlPicture("");
				model.addAttribute("bankAccounts", bankAccounts);
				model.addAttribute("createCampaignForm", createCampaignForm);
				model.addAttribute("msg", "Không thể thêm mới! Ngày kết thúc phải lớn hơn ngày bắt đầu!");
				return "admin/create-campaign";
			}
			if(campaignDAO.insertCampaign(campaign)) {	
				redirAttrs.addFlashAttribute("status", "0");
				redirAttrs.addFlashAttribute("message", "Thêm thành công!");
			}else { 
				redirAttrs.addFlashAttribute("status", "1");
				redirAttrs.addFlashAttribute("message", "Thêm thất bại!");
			}
			
		}catch (Exception e) {
			List<BankAccountInfo> bankAccounts = bankAccountDAO.getBankAccount();
			createCampaignForm.setUrlPicture("");
			model.addAttribute("bankAccounts", bankAccounts);
			model.addAttribute("createCampaignForm", createCampaignForm);			
			model.addAttribute("msg", "Lỗi! Không thể thêm mới");
			return "admin/create-campaign";
		}
		
		
		return "redirect:/Donation/admin/managerCampaign";
	}
	
	/*edit campaign with id*/
	@RequestMapping(value = "/admin/editCampaign/{id}", method = RequestMethod.GET)
	public ModelAndView showEditCampaignForm(@PathVariable(name = "id") int id) {
		ModelAndView mAndView = new ModelAndView("admin/edit-campaign");
		Campaign campaign = campaignDAO.getCampaign(id).get(0);
		List<BankAccountInfo> bankAccounts = bankAccountDAO.getBankAccount();
		mAndView.addObject("bankAccounts", bankAccounts);
		
		mAndView.addObject("campaign",campaign);
		return mAndView;
	}
	
	/*update campaign*/ 
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveCampaign(@ModelAttribute("campaign") Campaign campaign, RedirectAttributes redirAttrs) {
		
		if(campaign.getFile() != null) {
			String url = campaign.getHisUrl();
			String nameFolder = getFolderImage(url);
			try {
				campaign.setUrlAvatar(this.saveUploadedImageFile(campaign.getFile(), nameFolder));
				//this.saveUploadedImageFile(campaign.getFile(), nameFolder);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		if(Libraries.convertStringToDate(campaign.getDateStart()).compareTo(Libraries.convertStringToDate(campaign.getDateEnd())) == 0 ||
				Libraries.convertStringToDate(campaign.getDateStart()).compareTo(Libraries.convertStringToDate(campaign.getDateEnd())) > 0) {
			List<BankAccountInfo> bankAccounts = bankAccountDAO.getBankAccount();
			redirAttrs.addAttribute("id", campaign.getCampaignId())
						.addFlashAttribute("msg", "Không thể cập nhật! Ngày kết thúc phải lớn hơn ngày bắt đầu!")
						.addFlashAttribute("campaign",campaign)
						.addFlashAttribute("bankAccounts", bankAccounts);
			return "redirect:/Donation/admin/editCampaign/{id}";
		}
		if(campaignDAO.saveCampaign(campaign)) {	
			redirAttrs.addFlashAttribute("status", "0");
			redirAttrs.addFlashAttribute("message", "Cập nhật thành công!");
		}else { 
			redirAttrs.addFlashAttribute("status", "1");
			redirAttrs.addFlashAttribute("message", "Cập nhật thất bại!");
		}
		return "redirect:/Donation/admin/managerCampaign";
	}
	
	/*delete campaign with id*/
	@RequestMapping(value = "/admin/deleteCampaign/{id}", method = RequestMethod.GET)
	public String deleteCampaignForm(Model model, @PathVariable(name = "id") int id) {
		boolean result = campaignDAO.deleteCampaign(id);
		String msg = "Xóa thành công!";
		if(!result)
			msg = "Xóa không thành công!";
		model.addAttribute("msg", msg);
		
		return "redirect:/Donation/admin/managerCampaign";
	}
	
	//save file image
	private String saveUploadedImageFile(MultipartFile file, String nameFolder) throws IOException{
				
		String uploadDir = uploadPath + "/" + nameFolder;
		Path root = Paths.get(uploadDir);
		try {
			if(!Files.exists(root)) //kiểm tra nếu không tồn tại thư mục thì tạo mới 
				Files.createDirectories(root);
			File fileUpload = new File(uploadDir + "/" + file.getOriginalFilename());
						
			boolean checkFile = Files.deleteIfExists(fileUpload.toPath());
						
			String saveUploadFilePath = "/uploads/" + nameFolder + "/" + file.getOriginalFilename(); //đường dẫn được lưu lên database
			Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
			
			return saveUploadFilePath;
		}catch(IOException e) {
			//return "error";
			throw new IOException("Could not save image file: " + file.getOriginalFilename(), e);			
		}
		
		
		
	}
	
	/*get name folder from path url upload image*/
	private String getFolderImage(String toPath) {
		String[] strPaths = toPath.split("/");		
		return strPaths[strPaths.length - 2];
	}
	
}
