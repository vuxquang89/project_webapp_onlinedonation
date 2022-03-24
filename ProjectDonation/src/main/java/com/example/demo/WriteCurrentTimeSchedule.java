package com.example.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.dao.CampaignDAO;

@Component
public class WriteCurrentTimeSchedule {

	 private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");
	 
	 @Autowired
	 private CampaignDAO campaignDAO;
	    
	 // initialDelay = 3 second.
	 // fixedDelay = 2 second.
	 //cron (* * * * * *) => (giây phút giờ ngày-trong-tháng tháng ngày-trong-tuần)
	 @Scheduled(cron = "0 0 0 * * *") //once a day
	 public void writeCurrentTime() {
	        
		 Date now = new Date();
	        
		 String nowString = df.format(now);
		 campaignDAO.update();
		 
	        
	 }
}
