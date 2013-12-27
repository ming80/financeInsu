package com.firsthuanya.financeInsu.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.firsthuanya.financeInsu.domain.Policy;
import com.firsthuanya.financeInsu.domain.PolicyExcelParser;
import com.firsthuanya.financeInsu.service.PolicyService;
import com.firsthuanya.financeInsu.util.excel.imports.ExcelParserErrorInfo;
import com.firsthuanya.financeInsu.util.excel.imports.ExcelParserException;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class ImportPoliciesAction extends ActionSupport{
	private boolean successful;
	private List<ExcelParserErrorInfo> errorInfoList;
	
	private File excelFile;
	private String excelFileContentType;
	private String excelFileFileName;
	
	public String execute(){
		PolicyExcelParser parser = new PolicyExcelParser();
		List<Policy> policyList = null;
		try {
			policyList = parser.parse(new BufferedInputStream(new FileInputStream(this.excelFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcelParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(policyList == null){
			this.successful = false;
			this.errorInfoList = parser.getErrorInfoList();
		}
		else{
			this.successful = true;
			this.errorInfoList = null;
			
			//插入这些保单记录
			PolicyService service = new PolicyService();
			service.addPolicy(policyList);
		}	
		
		return Action.SUCCESS;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public List<ExcelParserErrorInfo> getErrorInfoList() {
		return errorInfoList;
	}
	

//	public File getExcelFile() {
//		return excelFile;
//	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

//	public String getExcelFileContentType() {
//		return excelFileContentType;
//	}

	public void setExcelFileContentType(String excelFileContentType) {
		this.excelFileContentType = excelFileContentType;
	}

//	public String getExcelFileFileName() {
//		return excelFileFileName;
//	}

	public void setExcelFileFileName(String excelFileFileName) {
		this.excelFileFileName = excelFileFileName;
	}	
}
