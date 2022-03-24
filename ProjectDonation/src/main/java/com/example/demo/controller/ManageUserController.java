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
			model.addAttribute("msg", "Tên đăng nhập đã tồn tại!");
			return "admin/create-user";
		}
		
		if(userDAO.checkEmail(userForm.getEmail())) {
			model.addAttribute("msg", "Email đã tồn tại!");
			return "admin/create-user";
		}
		if(userDAO.checkPhone(userForm.getPhone())) {
			model.addAttribute("msg", "Số điện thoại đã tồn tại!");
			return "admin/create-user";
		}
		
		if(userDAO.insertUser(user)) {
			//model.addAttribute("msg", "Thêm mới thành công!");
			
			Thread thread = new Thread(new Runnable() {
				public void run() {
					mailService.sendMail(userForm.getFullName(), userForm.getUsername(), userForm.getPass(), userForm.getEmail());					
				}
			});
			thread.start();
			
		}else {
			model.addAttribute("msg", "Lỗi! Không thể thêm mới!");
			return "admin/create-user";
		}
		model.addFlashAttribute("message", "Tạo tài khoản thành công!");
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
				model.addAttribute("msg", "Lỗi! Không thể cập nhật!");
				return "admin/edit-user";
			}
		}catch (Exception e) {
			model.addAttribute("msg", "Lỗi! Không thể cập nhật!");
			return "admin/edit-user";
		}
		model.addFlashAttribute("message", "Cập nhật thành công!");
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
		else if(action.equalsIgnoreCase("search")) {		//tìm kiếm 	
			users = userDAO.getSearchUsers(userCriteria.getKeySearch(), begin, end);
			totalRecords = userDAO.getCountSearchUser(userCriteria.getKeySearch());
		}
		else if(action.equalsIgnoreCase("activeUser")) {//tạm khóa hoặc mở khóa user
			//String[] idStrings = userCriteria.getId().split(" ");
			int st = userCriteria.getUserStatus();
			boolean active = userDAO.activeUser(Integer.parseInt(userCriteria.getId()));
			
			String mString = st == 1 ? "Khóa" : "Mở khóa";
			if(active) {
				message = mString + " tài khoản thành công.";
			}else {
				message = "Lỗi! Không thể "+mString+" tài khoản này.";
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
							message = "Reset mật khẩu thành công!";
							status = 0;
							Thread thread = new Thread(new Runnable() {
								public void run() {
									mailService.sendMail(appUser.getFullName(), appUser.getUsername(), password, appUser.getEmail());					
								}
							});
							thread.start();
						}else {
							message = "Lỗi! Không thể reset mật khẩu!";
							status = 1;
						}
						
					}else {
						message = "Tài khoản đang tạm khóa, không thể reset mật khẩu!";
						status = 1;
					}
					
				}else {
					message = "Không tìm thấy";
					status = 1;
				}
			} catch (Exception e) {
				message = "Lỗi! Không thể reset mật khẩu!";
				status = 1;
			}
			
			
			users = userDAO.getUsers(user.getRole(), begin, end);
			totalRecords = userDAO.getCountUser(user.getRole());
		}
		else if(action.equalsIgnoreCase("deleteUser")) {//xóa 1 user
			int id = Integer.parseInt(userCriteria.getId());
			try {
				boolean checkDel = userDAO.deleteUser(id);
				
				if(!checkDel) {				
					message = "Không thể xóa! Tài khoản chuyển trạng thái tạm khóa!";
					
					userDAO.activeUser(id, 0);
				}else {
					message = "Xóa thành công!";
				}
			}catch (Exception e) {
				message = "Không thể xóa! Tài khoản chuyển trạng thái tạm khóa!";
				
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
