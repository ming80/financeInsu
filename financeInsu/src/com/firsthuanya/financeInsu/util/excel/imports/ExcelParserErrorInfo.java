package com.firsthuanya.financeInsu.util.excel.imports;

public class ExcelParserErrorInfo {
	private int rowNum;
	private String info;
	
	public ExcelParserErrorInfo(int rowNum,String info){
		if(rowNum <= 0) 
			throw new IllegalArgumentException("rowNum should be greater than 0!");
		if(info == null || info.trim().equals(""))
			throw new IllegalArgumentException("info is null!");
		
		this.rowNum = rowNum;
		this.info = info;
	}
	
	public int getRowNum(){
		return rowNum;
	}
	
	public String getInfo(){
		return info;
	}
}
