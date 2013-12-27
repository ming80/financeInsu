package com.firsthuanya.financeInsu.util.excel.imports;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;


public abstract class AbstractExcelParser<T> {
	private List<ExcelParserErrorInfo> errorInfoList;	
	
	public List<T> parse(InputStream in) throws ExcelParserException{
		Workbook workbook = null;
		try {
			//读取Excel
			workbook = Workbook.getWorkbook(in);
			
			if(workbook.getNumberOfSheets() < 1){
				addErrorInfo(new ExcelParserErrorInfo(1,"读取失败,没有发现数据表!"));
				return null;
			}			
			
			//检查sheet是否为空,检查有无数据行,检查行数,行数最多50000行
			Sheet sheet = workbook.getSheet(0);
			ExcelParserErrorInfo sheetInfo = checkSheet(sheet);
			if(sheetInfo != null){
				addErrorInfo(sheetInfo);
				return null;
			}		
			
			//检查表头,表头错,认为文件错,直接返回
			List<ExcelParserErrorInfo> headerInfo = checkHeader(sheet.getRow(0));
			if(headerInfo != null && headerInfo.size() > 0){
				addErrorInfo(headerInfo);
				return null;
			}
			
			int rowNum = sheet.getRows();
			
			//从表格中获得数据,保存到Map中,key为行号,value为一行数据
			//在检查空表时,已经排除2行以下的情况,所以这里不会抛出异常
			Map<Integer,String[]> rows = new HashMap();		
			for(int rowIndex = 1;rowIndex < rowNum;rowIndex++){	
				Cell[] row = sheet.getRow(rowIndex);
				String[] strRow = new String[row.length];
				for(int columnIndex = 0;columnIndex < strRow.length;columnIndex++)
					strRow[columnIndex] = getCellContents(row[columnIndex]);
				
				rows.put(rowIndex + 1, strRow);
			}	
			workbook.close();
			
			//检查数据行
			for(int key:rows.keySet())
				addErrorInfo(checkRow(key,rows.get(key),rows));
			
			if(getErrorInfoList() != null && getErrorInfoList().size() > 0){
				//先根据excel中的行号排序,然后返回
				Collections.sort(this.getErrorInfoList(), new Comparator<ExcelParserErrorInfo>(){
					@Override
					public int compare(ExcelParserErrorInfo o1, ExcelParserErrorInfo o2) {
						if(o1 == null) return -1;
						if(o2 == null) return 1;
						if(o1.getRowNum() < o2.getRowNum())
							return -1;
						if(o1.getRowNum() == o2.getRowNum())
							return 0;
						return 1;
					}					
				});
				return null;
			}				
					
			//转换
			List<T> targets = convertAll(rows.values());
			
			return targets;
		} catch (Exception e) {
			throw new ExcelParserException("parse failed!",e);			
		}
		finally{
			if(workbook != null)
				workbook.close();
		}
	}
	
	public List<ExcelParserErrorInfo> getErrorInfoList(){
		return this.errorInfoList;
	}
	
	//检查sheet是否为空
	private ExcelParserErrorInfo checkSheet(Sheet sheet){		
		int rowNum = sheet.getRows();
		if(rowNum == 0)
			return new ExcelParserErrorInfo(1,"此表格为空表!");
		
		if(rowNum == 1)
			return new ExcelParserErrorInfo(1,"只读取到表头行,没有数据行!");
		
		//忽略尾部的空行,计算实际的行数
		int realRowNum = rowNum;
		for(int rowIndex = rowNum - 1;rowIndex > 0;rowIndex--)
			if(isEmptyRowInSheet(sheet.getRow(rowIndex)))
				realRowNum--;
			else
				break;
		
		//实际行数不能大于50000行
		if(realRowNum > 50000)
			return new ExcelParserErrorInfo(realRowNum,"要导入的数据过多,每次导入不能超过50000行!");
		
		return null;
	}
	
	//检查表头
	protected abstract List<ExcelParserErrorInfo> checkHeader(Cell[] header);	
	
	//检查数据行
	protected abstract List<ExcelParserErrorInfo> checkRow(int rowNum,String[] row,Map<Integer,String[]> rows);
	
	//转换
	protected abstract T convert(String[] row) throws ExcelParserException;
	
	private String getCellContents(Cell cell){
		//日期单元格
		if(cell.getType() == CellType.DATE){
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(((DateCell)cell).getDate());
		}			
		
		//文本单元格	
		String contents = cell.getContents();
		//解决字符串中含有8206字符的问题,会导致数据导入到库中后,8206字符变成?,
		if(contents != null)
			contents = contents.replaceAll(String.valueOf((char)8206), "").trim();
		return contents;
	}
	
	private void addErrorInfo(ExcelParserErrorInfo errorInfo){
		if(this.errorInfoList == null)
			this.errorInfoList = new ArrayList();
		
		if(errorInfo != null)
			this.errorInfoList.add(errorInfo);		
	}
	
	private void addErrorInfo(List<ExcelParserErrorInfo> errorInfoList){
		if(this.errorInfoList == null)
			this.errorInfoList = new ArrayList();		
		
		if(errorInfoList != null)
			this.errorInfoList.addAll(errorInfoList);	
	}
	
	private List<T> convertAll(Collection<String[]> rows) throws ExcelParserException{
		List<T> targets = new ArrayList();
		for(String[] row:rows)
			if(!isEmptyRow(row))
				targets.add(convert(row));
		
		return targets;
	}
	
	private boolean isEmptyRow(String[] row){
		if(row == null) return true;
		if(row.length == 0) return true;
		for(int i = 0;i < row.length;i++)
			if(row[i] != null && !row[i].trim().equals(""))
				return false;
		
		return true;
	}
	
	private boolean isEmptyRowInSheet(Cell[] row){
		if(row == null)
			return true;
		
		if(row.length == 0)
			return true;
		
		for(int columnIndex = 0;columnIndex < row.length;columnIndex++){
			Cell cell = row[columnIndex];
			if(cell != null){
				String contents = cell.getContents();
				if(contents != null && !contents.trim().equals(""))
					return false;
			}				 
		}
		
		return true;		
	}
	
	public static void main(String[] args){		
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		System.out.print("aaa/bbb".contains("/"));
		try {
			System.out.print(format.parse("2010/1/1 1:1:1"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
