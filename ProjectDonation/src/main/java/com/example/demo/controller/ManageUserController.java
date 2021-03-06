package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.UserDAO;
import com.example.demo.form.CreateCampaignForm;
import com.example.demo.form.UserForm;
import com.example.demo.lib.EncrytedPassword;
import com.example.demo.lib.Libraries;
import com.example.demo.model.BankAccountInfo;
import com.example.demo.model.Campaign;
import com.example.demo.model.CampaignCriteria;
import com.example.demo.model.ResponseBodyCampaign;
import com.example.demo.model.ResponseBodyUser;
import com.example.demo.model.AppUser;
import com.example.demo.model.UserCriteria;
import com.example.demo.service.MailServiceImpl;

@Controller
@RequestMapping("Donation")
public class ManageUserController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private MailServiceImpl mailService;

	/*load manage user default page*/
	@RequestMapping(value = "/admin/manageUser", method = RequestMethod.GET)
	public String showAccount(Model model, Principal principal) {
		//System.out.println("user name: " + principal.getName());
		AppUser user = userDAO.findUserAccount(principal.getName());
		int begin = 0;
		int limit = 5;
		int totalRecords = userDAO.getCountUser(user.getRole());
		double avgPage = (double)totalRecords / limit;		
		float ceil = (float)(avgPage + (avgPage % 1 == 0 ? 0 : 0.5));		
		int totalPage = Math.round(ceil);		
		
		List<AppUser> users = userDAO.getUsers(user.getRole(), begin, limit);
		
		model.addAttribute("users", users);
		model.addAttribute("limit", limit);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalRecords", totalRecords);
		
		return "admin/manage-user";
	}
	
	/*load page create user */
	@RequestMapping(value = "/admin/createUser", method = RequestMethod.GET)
	public String createUser(Model model) {
		String ranadomPass = Libraries.generateRandomPassword(8);
		UserForm userForm = new UserForm();
		userForm.setPass(ranadomPass);
		
		model.addAttribute("userForm", userForm);
		
		return "admin/create-user";
	}
	
	/*create user*/
	@RequestMapping(value = "/admin/createUser", method = RequestMethod.POST)
	public String createUser(RedirectAttributes model, @ModelAttribute("userForm")UserForm userForm) {
		AppUser user = new AppUser();
		user.setUsername(userForm.getUsername());
		//user.setPass(Libraries.hashMD5(userForm.getPass()));
		user.setPass(userForm.getPass());
		user.setEmail(userForm.getEmail());
		user.setPhone(userForm.getPhone());
		user.setFullName(userForm.getFullName());
		if(userDAO.checkUserName(userForm.getUsername())) {
			model.addAttribute("msg", "T??n ????ng nh???p ???? t???n t???i!");
			return "admin/create-user";
		}
		
		if(userDAO.checkEmail(userForm.getEmail())) {
			model.addAttribute("msg", "Email ???? t???n t???i!");
			return "admin/create-user";
		}
		if(userDAO.checkPhone(userForm.getPhone())) {
			model.addAttribute("msg", "S??? ??i???n tho???i ???? t???n t???i!");
			return "admin/create-user";
		}
		
		if(userDAO.insertUser(user)) {
			//model.addAttribute("msg", "Th??m m???i th??nh c??ng!");
			
			Thread thread = new Thread(new Runnable() {
				public void run() {
					mailService.sendMail(userForm.getFullName(), userForm.getUsername(), userForm.getPass(), userForm.getEmail());					
				}
			});
			thread.start();
			
		}else {
			model.addAttribute("msg", "L???i! Kh??ng th??? th??m m???i!");
			return "admin/create-user";
		}
		model.addFlashAttribute("message", "T???o t??i kho???n th??nh c??ng!");
		return "redirect:/Donation/admin/manageUser";
	}
	
	/*load page edit user*/
	@RequestMapping(value = "/admin/editUser/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable(name = "id") int id) {
		ModelAndView mAndView = new ModelAndView("admin/edit-user");
		AppUser user = userDAO.getUser(id).get(0);			
		mAndView.addObject("user", user);
		return mAndView;
	}
	
	/*update user*/
	@RequestMapping(value = "/admin/saveUser", method = RequestMethod.POST)
	public String updateUser(RedirectAttributes model, @ModelAttribute("user") AppUser user) {
		try {
			if(!userDAO.saveUser(user)) {
				model.addAttribute("msg", "L???i! Kh??ng th??? c???p nh???t!");
				return "admin/edit-user";
			}
		}catch (Exception e) {
			model.addAttribute("msg", "L???i! Kh??ng th??? c???p nh???t!");
			return "admin/edit-user";
		}
		model.addFlashAttribute("message", "C???p nh???t th??nh c??ng!");
		return "redirect:/Donation/admin/manageUser";
	}
	
	/*handle request ajax get user*/
	@RequestMapping(value = "/admin/actionUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> getUserAjax(@RequestBody UserCriteria userCriteria, Principal principal){
		ResponseBodyUser responseBodyUser = new ResponseBodyUser();
		List<AppUser> users = null;
		String message = "";
		String error = "";
		int status = 0;
		
		String action = userCriteria.getAction();
		int limit = userCriteria.getLimitItem();		
		int current = userCriteria.getCurrent();
		int begin = (current - 1) * limit;
		int end = current * limit;
		int totalRecords = 0;
		AppUser user = userDAO.findUserAccount(principal.getName());		
		
		if(action.equalsIgnoreCase("get")) {//load list user
			users = userDAO.getUsers(user.getRole(), begin, end);
			totalRecords = userDAO.getCountUser(user.getRole());
		}
		else if(action.equalsIgnoreCase("search")) {		//t??m ki???m 	
			users = userDAO.getSearchUsers(userCriteria.getKeySearch(), begin, end);
			totalRecords = userDAO.getCountSearchUser(userCriteria.getKeySearch());
		}
		else if(action.equalsIgnoreCase("activeUser")) {//t???m kh??a ho???c m??? kh??a user
			//String[] idStrings = userCriteria.getId().split(" ");
			int st = userCriteria.getUserStatus();
			boolean active = userDAO.activeUser(Integer.parseInt(userCriteria.getId()));
			
			String mString = st == 1 ? "Kh??a" : "M??? kh??a";
			if(active) {
				message = mString + " t??i kho???n th??nh c??ng.";
			}else {
				message = "L???i! Kh??ng th??? "+mString+" t??i kho???n n??y.";
			}
			users = userDAO.getUsers(user.getRole(), begin, end);
			totalRecords = userDAO.getCountUser(user.getRole());
		}		
		else if(action.equalsIgnoreCase("resetPassword")) {//reset pasword
			try {
				AppUser appUser = userDAO.getUser(Integer.parseInt(userCriteria.getId())).get(0);
				if(appUser != null) {
					int st = appUser.getStatus();
					if(st == 1) {
						String password = Libraries.randomPassword(8);
						String encodedPassword = EncrytedPassword.encrytePassword(password);					
						if(userDAO.updatePass(appUser.getUsername(), encodedPassword)) {
							message = "Reset m???t kh???u th??nh c??ng!";
							status = 0;
							Thread thread = new Thread(new Runnable() {
								public void run() {
									mailService.sendMail(appUser.getFullName(), appUser.getUsername(), password, appUser.getEmail());					
								}
							});
							thread.start();
						}else {
							message = "L???i! Kh??ng th??? reset m???t kh???u!";
							status = 1;
						}
						
					}else {
						message = "T??i kho???n ??ang t???m kh??a, kh??ng th??? reset m???t kh???u!";
						status = 1;
					}
					
				}else {
					message = "Kh??ng t??m th???y";
					status = 1;
				}
			} catch (Exception e) {
				message = "L???i! Kh??ng th??? reset m???t kh???u!";
				status = 1;
			}
			
			
			users = userDAO.getUsers(user.getRole(), begin, end);
			totalRecords = userDAO.getCountUser(user.getRole());
		}
		else if(action.equalsIgnoreCase("deleteUser")) {//x??a 1 user
			int id = Integer.parseInt(userCriteria.getId());
			try {
				boolean checkDel = userDAO.deleteUser(id);
				
				if(!checkDel) {				
					message = "Kh??ng th??? x??a! T??i kho???n chuy???n tr???ng th??i t???m kh??a!";
					
					userDAO.activeUser(id, 0);
				}else {
					message = "X??a th??nh c??ng!";
				}
			}catch (Exception e) {
				message = "Kh??ng th??? x??a! T??i kho???n chuy???n tr???ng th??i t???m kh??a!";
				
				userDAO.activeUser(id, 0);
			}			
			
			totalRecords = userDAO.getCountUser(user.getRole());
			if(begin == totalRecords) {
				current -= 1;
				begin = (current - 1) * limit;
				end = current * limit;
			}
			users = userDAO.getUsers(user.getRole(), begin, end);
						
		}
		
		responseBodyUser.setStatus(status);
		responseBodyUser.setMessage(message);
		responseBodyUser.setTotalRecords(totalRecords);
		responseBodyUser.setUsers(users);
		responseBodyUser.setError(error);
		return ResponseEntity.ok(responseBodyUser);
	}
}
