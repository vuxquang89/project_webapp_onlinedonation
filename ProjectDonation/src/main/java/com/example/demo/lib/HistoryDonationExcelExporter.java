package com.example.demo.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.form.CreateCampaignForm;
import com.example.demo.form.UpdateDonationForm;
import com.example.demo.model.Donation;

public class HistoryDonationExcelExporter {

	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Donation> hisDonations;
    private Donation donation;
    private List<UpdateDonationForm> dataResult;
    
    public HistoryDonationExcelExporter() { 
    	workbook = new XSSFWorkbook();
    	dataResult = new ArrayList<UpdateDonationForm>();
    }
    
	public HistoryDonationExcelExporter(List<Donation> hisDonations) {
		
		workbook = new XSSFWorkbook();
		this.hisDonations = hisDonations;
	}
	
	public HistoryDonationExcelExporter(List<Donation> hisDonations, Donation donation) {
		
		workbook = new XSSFWorkbook();
		this.hisDonations = hisDonations;
		this.donation = donation;
	}
	
	public List<UpdateDonationForm> getDataResult() {
		return dataResult;
	}

	public void setDataResult(List<UpdateDonationForm> dataResult) {
		
		this.dataResult = dataResult;
	}
	
	//write header line
	private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Tên chiến dịch", style);      
        createCell(row, 1, "Ngày quyên góp", style);       
        createCell(row, 2, "Số tiền quyên góp(vnđ)", style);    
        
    }
	//write header line
	private void writeHeaderLine_Result() {
        sheet = workbook.createSheet("Result");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Nội dung chuyển khoản", style);      
        createCell(row, 1, "Ngày quyên góp", style);       
        createCell(row, 2, "Số tiền quyên góp(vnđ)", style);    
        createCell(row, 3, "Kết quả", style); 
        
    }
	
	//write header line
	private void writeHeaderLine_Info() {
		sheet = workbook.createSheet("Donor");
        
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Tên chiến dịch", style);      
        createCell(row, 1, "Tổng số tiền quyên góp", style);       
        createCell(row, 2, "Mức tiền quyên góp", style);
        createCell(row, 3, "Đại diện thụ hưởng", style);
        
        row = sheet.createRow(2);
        
        createCell(row, 0, "Tên nhà hảo tâm", style);      
        createCell(row, 1, "Tên đăng nhập", style);       
        createCell(row, 2, "Số tiền quyên góp", style);
        createCell(row, 3, "Ngày quyên góp", style);
	}
     
	//create cell
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if(value instanceof Float){
        	cell.setCellValue((Float) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    
    //write lines from data
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (Donation donation : hisDonations) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, donation.getCampaignName(), style);
            createCell(row, columnCount++, donation.getDateTime(), style);
            createCell(row, columnCount++, donation.getAmount(), style);            
        }
    }
    
  //write lines from data
    private void writeDataLines_Result() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (UpdateDonationForm res : dataResult) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, res.getContent(), style);
            createCell(row, columnCount++, res.getDateTime(), style);
            createCell(row, columnCount++, res.getAmount(), style); 
            createCell(row, columnCount++, res.getResult(), style);
        }
    }
    
    //write lines from data
    private void writeDataLines_Info() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
         
        createCell(row, columnCount++, donation.getCampaignName(), style);
        createCell(row, columnCount++, donation.getAmount(), style);
        createCell(row, columnCount++, donation.getGoal(), style);
        createCell(row, columnCount++, donation.getBank_account_name(), style);
        rowCount = 3;
        for (Donation donation : hisDonations) {
            Row r = sheet.createRow(rowCount++);
            int c = 0;
             
            createCell(r, c++, donation.getFullName(), style);
            createCell(r, c++, donation.getUsername(), style);
            createCell(r, c++, donation.getAmount(), style);         
            createCell(r, c++, donation.getDateTime(), style); 
        }
    }
    
    //export file history donation
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
    
    //export file info donation
    public void exportInfoDonation(HttpServletResponse response) throws IOException{
    	writeHeaderLine_Info();
        writeDataLines_Info();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
    }
    
    //export file result read data
    public void exportDataResult(HttpServletResponse response) throws IOException{
    	writeHeaderLine_Result();
        writeDataLines_Result();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
    }
}
