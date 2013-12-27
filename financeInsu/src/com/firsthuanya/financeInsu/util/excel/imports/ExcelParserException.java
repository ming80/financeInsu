package com.firsthuanya.financeInsu.util.excel.imports;

public class ExcelParserException extends Exception{
	public ExcelParserException(String str){
		super(str);
	}
	
	public ExcelParserException(String str,Exception e){
		super(str,e);
	}
}
