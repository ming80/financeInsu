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
			//��ȡExcel
			workbook = Workbook.getWorkbook(in);
			
			if(workbook.getNumberOfSheets() < 1){
				addErrorInfo(new ExcelParserErrorInfo(1,"��ȡʧ��,û�з������ݱ�!"));
				return null;
			}			
			
			//���sheet�Ƿ�Ϊ��,�������������,�������,�������50000��
			Sheet sheet = workbook.getSheet(0);
			ExcelParserErrorInfo sheetInfo = checkSheet(sheet);
			if(sheetInfo != null){
				addErrorInfo(sheetInfo);
				return null;
			}		
			
			//����ͷ,��ͷ��,��Ϊ�ļ���,ֱ�ӷ���
			List<ExcelParserErrorInfo> headerInfo = checkHeader(sheet.getRow(0));
			if(headerInfo != null && headerInfo.size() > 0){
				addErrorInfo(headerInfo);
				return null;
			}
			
			int rowNum = sheet.getRows();
			
			//�ӱ���л������,���浽Map��,keyΪ�к�,valueΪһ������
			//�ڼ��ձ�ʱ,�Ѿ��ų�2�����µ����,�������ﲻ���׳��쳣
			Map<Integer,String[]> rows = new HashMap();		
			for(int rowIndex = 1;rowIndex < rowNum;rowIndex++){	
				Cell[] row = sheet.getRow(rowIndex);
				String[] strRow = new String[row.length];
				for(int columnIndex = 0;columnIndex < strRow.length;columnIndex++)
					strRow[columnIndex] = getCellContents(row[columnIndex]);
				
				rows.put(rowIndex + 1, strRow);
			}	
			workbook.close();
			
			//���������
			for(int key:rows.keySet())
				addErrorInfo(checkRow(key,rows.get(key),rows));
			
			if(getErrorInfoList() != null && getErrorInfoList().size() > 0){
				//�ȸ���excel�е��к�����,Ȼ�󷵻�
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
					
			//ת��
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
	
	//���sheet�Ƿ�Ϊ��
	private ExcelParserErrorInfo checkSheet(Sheet sheet){		
		int rowNum = sheet.getRows();
		if(rowNum == 0)
			return new ExcelParserErrorInfo(1,"�˱��Ϊ�ձ�!");
		
		if(rowNum == 1)
			return new ExcelParserErrorInfo(1,"ֻ��ȡ����ͷ��,û��������!");
		
		//����β���Ŀ���,����ʵ�ʵ�����
		int realRowNum = rowNum;
		for(int rowIndex = rowNum - 1;rowIndex > 0;rowIndex--)
			if(isEmptyRowInSheet(sheet.getRow(rowIndex)))
				realRowNum--;
			else
				break;
		
		//ʵ���������ܴ���50000��
		if(realRowNum > 50000)
			return new ExcelParserErrorInfo(realRowNum,"Ҫ��������ݹ���,ÿ�ε��벻�ܳ���50000��!");
		
		return null;
	}
	
	//����ͷ
	protected abstract List<ExcelParserErrorInfo> checkHeader(Cell[] header);	
	
	//���������
	protected abstract List<ExcelParserErrorInfo> checkRow(int rowNum,String[] row,Map<Integer,String[]> rows);
	
	//ת��
	protected abstract T convert(String[] row) throws ExcelParserException;
	
	private String getCellContents(Cell cell){
		//���ڵ�Ԫ��
		if(cell.getType() == CellType.DATE){
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(((DateCell)cell).getDate());
		}			
		
		//�ı���Ԫ��	
		String contents = cell.getContents();
		//����ַ����к���8206�ַ�������,�ᵼ�����ݵ��뵽���к�,8206�ַ����?,
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
