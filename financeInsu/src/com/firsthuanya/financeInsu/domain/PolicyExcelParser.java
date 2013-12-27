package com.firsthuanya.financeInsu.domain;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import com.firsthuanya.financeInsu.service.PolicyService;
import com.firsthuanya.financeInsu.util.SimpleDateParser;
import com.firsthuanya.financeInsu.util.excel.imports.AbstractExcelParser;
import com.firsthuanya.financeInsu.util.excel.imports.ExcelParserErrorInfo;
import com.firsthuanya.financeInsu.util.excel.imports.ExcelParserException;

public class PolicyExcelParser extends AbstractExcelParser<Policy>{
	@Override
	protected List<ExcelParserErrorInfo> checkHeader(Cell[] header) {
		List<ExcelParserErrorInfo> errorInfoList = new ArrayList();
		
		int length = header.length;
		if(header == null || length == 0){
			errorInfoList.add(new ExcelParserErrorInfo(1,"没有找到表头!"));
			return errorInfoList;
		}			
		
		if(length != 11){
			errorInfoList.add(new ExcelParserErrorInfo(1,"表头错误,表头应该为11列!"));
			return errorInfoList;
		}
	
		checkHeaderCell(errorInfoList,header[0],"A","保单号");
		checkHeaderCell(errorInfoList,header[1],"B","业务员");
		checkHeaderCell(errorInfoList,header[2],"C","总保费");
		checkHeaderCell(errorInfoList,header[3],"D","成本");
		checkHeaderCell(errorInfoList,header[4],"E","保险公司");
		checkHeaderCell(errorInfoList,header[5],"F","被保险人");
		checkHeaderCell(errorInfoList,header[6],"G","制单日期");
		checkHeaderCell(errorInfoList,header[7],"H","手续费率");
		checkHeaderCell(errorInfoList,header[8],"I","手续费");
		checkHeaderCell(errorInfoList,header[9],"J","险种类别");
		checkHeaderCell(errorInfoList,header[10],"K","开票号");
		
		return errorInfoList;
	}

	private void checkHeaderCell(List<ExcelParserErrorInfo> errorInfoList,Cell cell,String columnStr,String columnName){
		if(cell == null){
			errorInfoList.add(new ExcelParserErrorInfo(1,"表头错误," + columnStr + "列应为" + columnName + "!"));
			return;
		}
			
		String contents = cell.getContents();
		if(contents == null || !contents.trim().equals(columnName))
			errorInfoList.add(new ExcelParserErrorInfo(1,"表头错误," + columnStr + "列应为" + columnName + "!"));		
	}

	@Override
	protected List<ExcelParserErrorInfo> checkRow(int rowNum, String[] row, Map rows) {
		//空行不检查
		if(isEmptyRow(row))	return null;
		
		List<ExcelParserErrorInfo> errorInfoList = new ArrayList();
		
		//11个不能为空
		int length = row.length;
		for(int i = 0;i < 11;i++){
			if(i == 0 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"保单号不能为空!"));
			if(i == 1 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"业务员不能为空!"));
			if(i == 2 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"总保费不能为空!"));
			if(i == 3 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"成本不能为空!"));
			if(i == 4 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"保险公司不能为空!"));
			if(i == 5 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"被保险人不能为空!"));
			if(i == 6 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"制单日期不能为空!"));
			if(i == 7 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"手续费率不能为空!"));
			if(i == 8 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"手续费不能为空!"));
			if(i == 9 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"险种类别不能为空!"));
			if(i == 10 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"开票号不能为空!"));		
		}
		//有空的数据就直接返回,不做后续检查
		if(errorInfoList.size() > 0)
			return errorInfoList;		
		
		//保单号长度
		if(row[0].trim().length() > 45)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"保单号过长!"));	
		
		if(row[0].trim().contains(String.valueOf((char)8206)))
			System.out.println(row[0].trim() + " 含有8206字符");
		
//		if(rowNum == 243 || rowNum == 244){
//			char[] row0 = row[0].toCharArray();
//			for(int i = 0;i < row0.length;i++){
//				System.out.println(row0[i] + " --->> " + (int)row0[i]);
//			}
//		}
		
		//业务员长度
		if(row[1].trim().length() > 25)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"业务员数据过长!"));	
		
		//总保费必须是数字  ^(-?\d+)(\.\d+)?$
		if(!row[2].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"总保费必须是数字!"));	
		
		//成本必须是数字
		if(!row[3].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"成本必须是数字!"));	
		
		//保险公司长度
		if(row[4].trim().length() > 80)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"保险公司名字过长!"));	
		
		//被保险人长度
		if(row[5].trim().length() > 80)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"被保险人名字过长!"));	
		
		//制单日期必须是日期
		SimpleDateParser dateParser = new SimpleDateParser(row[6].trim());
		if(!dateParser.isValid())
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"制单日期错误,日期格式必须是  yyyy-MM-dd"));	
		
//		if(!row[6].trim().matches("^(?:(?:1[6-9]|[2-9][0-9])[0-9]{2}([-])(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:(?:1[6-9]|[2-9][0-9])(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)([-])0?2\\2(?:29))(\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])?$"))
//			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"制单日期错误,日期格式必须是  yyyy-MM-dd HH:mm:ss "));	
		
		//手续费率必须是数字
		if(!row[7].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"手续费率 必须是数字!"));	
		
		//手续费必须是数字
		if(!row[8].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"手续费 必须是数字!"));	
		
		//险种类别长度
		if(row[9].trim().length() > 80)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"险种类别过长!"));	
		
		//开票号长度
		if(row[10].trim().length() > 25)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"开票号过长!"));	
		
		//文件中重复情况
		for(int key:(Set<Integer>)rows.keySet()){
			String[] currentRow = (String[])rows.get(key);
			if(key > rowNum && row[0].trim().equals(currentRow[0].trim()))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"保单号与第 " + key + " 行的保单号重复!"));		
		}	
		
		//与数据库中的重复情况
		PolicyService service = new PolicyService();
		if(service.hasSuchPolicy(row[0].trim()))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"此保单号在系统中已存在!保单号: " + row[0].trim()));
		
		return errorInfoList;
	}

	@Override
	protected Policy convert(String[] row) throws ExcelParserException {
		SimpleDateParser dateParser = new SimpleDateParser(row[6].trim());
		
		return new Policy(row[0].trim(),
				row[1].trim(),
				new BigDecimal(row[2].trim()),
				new BigDecimal(row[3].trim()),
				row[4].trim(),
				row[5].trim(),
				dateParser.parse(),
				new BigDecimal(row[8].trim()),
				Double.valueOf(row[7].trim()),
				row[9].trim(),
				row[10].trim(),
				new Date());
	}
	
	private boolean isEmpty(int columnIndex,int length,String[] row){
		if(columnIndex >= length)
			return true;
		if(row[columnIndex] == null)
			return true;
		if(row[columnIndex].trim().equals(""))
			return true;
		return false;
	}
	
	private boolean isEmptyRow(String[] row){
		if(row == null) return true;
		for(int i = 0;i < row.length;i++)
			if(row[i] != null && !row[i].trim().equals(""))
				return false;
		
		return true;
	}
	
	public static void main(String[] args){
		PolicyExcelParser parser = new PolicyExcelParser();
		try {
			List<Policy> list = parser.parse(new BufferedInputStream(new FileInputStream("e:/财务insu系统数据导入格式约定1.xls")));
			
			if(list == null){
				for(ExcelParserErrorInfo errorInfo:parser.getErrorInfoList())
					System.out.println(errorInfo.getRowNum() + "," + errorInfo.getInfo());					
					
				System.out.println("\n共有 " + parser.getErrorInfoList().size() + " 个错误~");
			}
			else{
				for(Policy policy:list)
					System.out.println(policy);
				
				PolicyService service = new PolicyService();
				service.addPolicy(list);
				System.out.println("\n共生成了 " + list.size() + " 条保单记录~");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcelParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
