package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.config.PaypalPaymentIntent;
import com.example.demo.config.PaypalPaymentMethod;
import com.example.demo.dao.CampaignDAO;
import com.example.demo.dao.DonationDAO;
import com.example.demo.lib.Libraries;
import com.example.demo.lib.Utils;
import com.example.demo.model.Campaign;
import com.example.demo.model.CampaignCriteria;
import com.example.demo.model.Donation;
import com.example.demo.model.DonationCriteria;
import com.example.demo.model.ResponseBodyCampaign;
import com.example.demo.model.ResponseBodyDonation;
import com.example.demo.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
@RequestMapping("Donation")
public class MainController {
	/*
	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private PaypalService paypalService;
	*/
	
	@Autowired
	CampaignDAO campaignDAO;
	
	@Autowired
	DonationDAO donationDAO;

	/*show index page*/
	@RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
	public String index(Model model) {
		int limitActive = 6;
		int limitNews = 4;
				
		int countActive = campaignDAO.getCountActiveCampaign();
		int countNews = campaignDAO.getCountNewsCampaign();
		//System.out.println(countActive);
		//System.out.println(countNews);
		boolean showMoreActive = (countActive <= limitActive) ? false : true; 
		boolean showMoreNews = (countNews <= limitNews) ? false : true; 
		
		List<Campaign> campaigns = campaignDAO.getActiveCampaigns(0, limitActive);
		
		List<Campaign> newsCampaigns = campaignDAO.getNewsCampaigns(2, 0, limitNews);
		//System.out.println(newsCampaigns.get(0).getDateCreated());
		model.addAttribute("campaigns", campaigns);
		model.addAttribute("newsCampaigns", newsCampaigns);
		model.addAttribute("showMoreActive", showMoreActive);		
		model.addAttribute("showMoreNews", showMoreNews);
		return "index";
	}
	
	
	/*handle request ajax get view more campaign*/
	@RequestMapping(value = "/actionViewMoreCampaign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getViewMoreCampaignAjax(@RequestBody CampaignCriteria campaignCriteria){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		int limitActive = 6;
		int limitNews = 4;
		
		String action = campaignCriteria.getAction();
		int currentItem = campaignCriteria.getCurrentItem();
		
		List<Campaign> campaigns = null;
		boolean showMore = false;
		
		if(action.equalsIgnoreCase("viewMoreActive")) {
			int countActive = campaignDAO.getCountActiveCampaign();
			showMore = (countActive <= currentItem + limitActive) ? false : true; 
			campaigns = campaignDAO.getActiveCampaigns(currentItem, currentItem + limitActive);	
			
		}else {
			int countNews = campaignDAO.getCountNewsCampaign();
			showMore = (countNews <= currentItem + limitNews) ? false : true;
			campaigns = campaignDAO.getNewsCampaigns(2, currentItem, currentItem + limitNews);
		}
		responseBodyCampaign.setCampaigns(campaigns);
		responseBodyCampaign.setShow(showMore);
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	/*handle request ajax get view more campaign*/
	@RequestMapping(value = "/actionViewCampaign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getViewCampaignAjax(@RequestBody CampaignCriteria campaignCriteria){
		ResponseBodyCampaign responseBodyCampaign = new ResponseBodyCampaign();
		int id = Integer.parseInt(campaignCriteria.getId());
		
		Campaign campaign = campaignDAO.getViewCampaign(id);
		if(campaign == null) {
			responseBodyCampaign.setStatus(1);
		}else {
			responseBodyCampaign.setStatus(0);
		}
		responseBodyCampaign.setCampaign(campaign);
		
		return ResponseEntity.ok(responseBodyCampaign);
	}
	
	/*show view info campaign with id*/	
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String viewDonation(@PathVariable(name = "id") int id, Model model) {
		//ModelAndView mAndView = new ModelAndView("view-donation");
		int i1;
		int i2 = 0;
		boolean checkRandom = false;
		try {
			Campaign campaign = campaignDAO.getViewCampaign(id);
			
			int max = campaignDAO.getMaxCampaignId();
			
			do {
				i1 = Libraries.random(1, max - 10);		
				if(i1 == id) {
					checkRandom = true;
				}else {
					checkRandom = false;
				}
			} while (checkRandom);
			
			Campaign top1Campaign = campaignDAO.getTop1RandomCampaigns(i1);//chương trình quyên góp khác đang diễn ra
			i1 = top1Campaign.getCampaignId();
			checkRandom = false;
			i2 = i1;
			do {			
				i2 += 1;
				if(i2 == id) {
					checkRandom = true;
				}else {
					checkRandom = false;
				}
			} while (checkRandom);
						
			
			List<Campaign> top3Campaigns = campaignDAO.getTop3RandomCampaigns(i2);//các chương trình quyên góp khác
			List<Donation> donations = donationDAO.getTopDonations(id);//nhà hảo tâm mới nhất
			List<Donation> totalDonations = donationDAO.getTopTotalDonations(id);//nhà hảo tâm hàng đầu
			model.addAttribute("campaign", campaign);	
			model.addAttribute("top3Campaigns", top3Campaigns);
			model.addAttribute("top1Campaign", top1Campaign);
			model.addAttribute("countDonor", donations.size());
			model.addAttribute("donations",donations);
			model.addAttribute("countTopDonor", totalDonations.size());
			model.addAttribute("totalDonations",totalDonations);
			return "view-donation";
		}catch (Exception e) {
			System.out.println(e.toString());
			return "404Page";
		}
		
	}
	
	/*handle request ajax get load donor*/
	@RequestMapping(value = "/actionLoadDonor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getLoadDonationAjax(@RequestBody DonationCriteria donationCriteria){
		ResponseBodyDonation responseBodyDonation = new ResponseBodyDonation();
		
		String action = donationCriteria.getAction();
		int campaignId = donationCriteria.getCampaignId();
		List<Donation> donations = null;
		int limitItem = 5;
		int currentItem = 10;
		int status = 0;
		if(action.equalsIgnoreCase("load")) {
			currentItem = 0;
			limitItem = 10;
		}else {
			
			currentItem = donationCriteria.getCurrentItem();
			
		}
		
		try {
			donations = donationDAO.getLoadMore(campaignId, currentItem, currentItem + limitItem);
			
		}catch (Exception e) {
			status = 1;
		}
		
		responseBodyDonation.setStatus(status);
		responseBodyDonation.setDonations(donations);
		
		return ResponseEntity.ok(responseBodyDonation);
	}
	
	/*handle request ajax get load help*/
	@RequestMapping(value = "/actionHelp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public @ResponseBody ResponseEntity<?> getHelpDonationAjax(@RequestBody DonationCriteria donationCriteria){
		ResponseBodyDonation responseBodyDonation = new ResponseBodyDonation();
		int campaignId = donationCriteria.getCampaignId();
		Donation donation = null;
		int status = 0;
		try {
			donation = donationDAO.getHelpDonation(campaignId);
		} catch (Exception e) {
			System.out.println(e.toString());
			status = 1;
		}
		
		responseBodyDonation.setStatus(status);
		responseBodyDonation.setDonation(donation);
		return ResponseEntity.ok(responseBodyDonation);
	}
	
	//thanh toán
	/*
	@PostMapping("/pay")
	public String pay(HttpServletRequest request,@RequestParam("price") double price ) {
		String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
		try {
			Payment payment = paypalService.createPayment(
					price,
					"USD",
					PaypalPaymentMethod.paypal,
					PaypalPaymentIntent.sale,
					"payment description",
					cancelUrl,
					successUrl);
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
					return "redirect:" + links.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}
	
	@GetMapping(URL_PAYPAL_CANCEL)
	public String cancelPay(){
		return "cancel";
	}
	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if(payment.getState().equals("approved")){
				return "success";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}
	*/
}
