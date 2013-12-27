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
			errorInfoList.add(new ExcelParserErrorInfo(1,"û���ҵ���ͷ!"));
			return errorInfoList;
		}			
		
		if(length != 11){
			errorInfoList.add(new ExcelParserErrorInfo(1,"��ͷ����,��ͷӦ��Ϊ11��!"));
			return errorInfoList;
		}
	
		checkHeaderCell(errorInfoList,header[0],"A","������");
		checkHeaderCell(errorInfoList,header[1],"B","ҵ��Ա");
		checkHeaderCell(errorInfoList,header[2],"C","�ܱ���");
		checkHeaderCell(errorInfoList,header[3],"D","�ɱ�");
		checkHeaderCell(errorInfoList,header[4],"E","���չ�˾");
		checkHeaderCell(errorInfoList,header[5],"F","��������");
		checkHeaderCell(errorInfoList,header[6],"G","�Ƶ�����");
		checkHeaderCell(errorInfoList,header[7],"H","��������");
		checkHeaderCell(errorInfoList,header[8],"I","������");
		checkHeaderCell(errorInfoList,header[9],"J","�������");
		checkHeaderCell(errorInfoList,header[10],"K","��Ʊ��");
		
		return errorInfoList;
	}

	private void checkHeaderCell(List<ExcelParserErrorInfo> errorInfoList,Cell cell,String columnStr,String columnName){
		if(cell == null){
			errorInfoList.add(new ExcelParserErrorInfo(1,"��ͷ����," + columnStr + "��ӦΪ" + columnName + "!"));
			return;
		}
			
		String contents = cell.getContents();
		if(contents == null || !contents.trim().equals(columnName))
			errorInfoList.add(new ExcelParserErrorInfo(1,"��ͷ����," + columnStr + "��ӦΪ" + columnName + "!"));		
	}

	@Override
	protected List<ExcelParserErrorInfo> checkRow(int rowNum, String[] row, Map rows) {
		//���в����
		if(isEmptyRow(row))	return null;
		
		List<ExcelParserErrorInfo> errorInfoList = new ArrayList();
		
		//11������Ϊ��
		int length = row.length;
		for(int i = 0;i < 11;i++){
			if(i == 0 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�����Ų���Ϊ��!"));
			if(i == 1 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"ҵ��Ա����Ϊ��!"));
			if(i == 2 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�ܱ��Ѳ���Ϊ��!"));
			if(i == 3 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�ɱ�����Ϊ��!"));
			if(i == 4 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"���չ�˾����Ϊ��!"));
			if(i == 5 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�������˲���Ϊ��!"));
			if(i == 6 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�Ƶ����ڲ���Ϊ��!"));
			if(i == 7 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�������ʲ���Ϊ��!"));
			if(i == 8 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�����Ѳ���Ϊ��!"));
			if(i == 9 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"���������Ϊ��!"));
			if(i == 10 && isEmpty(i,length,row))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"��Ʊ�Ų���Ϊ��!"));		
		}
		//�пյ����ݾ�ֱ�ӷ���,�����������
		if(errorInfoList.size() > 0)
			return errorInfoList;		
		
		//�����ų���
		if(row[0].trim().length() > 45)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�����Ź���!"));	
		
		if(row[0].trim().contains(String.valueOf((char)8206)))
			System.out.println(row[0].trim() + " ����8206�ַ�");
		
//		if(rowNum == 243 || rowNum == 244){
//			char[] row0 = row[0].toCharArray();
//			for(int i = 0;i < row0.length;i++){
//				System.out.println(row0[i] + " --->> " + (int)row0[i]);
//			}
//		}
		
		//ҵ��Ա����
		if(row[1].trim().length() > 25)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"ҵ��Ա���ݹ���!"));	
		
		//�ܱ��ѱ���������  ^(-?\d+)(\.\d+)?$
		if(!row[2].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�ܱ��ѱ���������!"));	
		
		//�ɱ�����������
		if(!row[3].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�ɱ�����������!"));	
		
		//���չ�˾����
		if(row[4].trim().length() > 80)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"���չ�˾���ֹ���!"));	
		
		//�������˳���
		if(row[5].trim().length() > 80)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�����������ֹ���!"));	
		
		//�Ƶ����ڱ���������
		SimpleDateParser dateParser = new SimpleDateParser(row[6].trim());
		if(!dateParser.isValid())
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�Ƶ����ڴ���,���ڸ�ʽ������  yyyy-MM-dd"));	
		
//		if(!row[6].trim().matches("^(?:(?:1[6-9]|[2-9][0-9])[0-9]{2}([-])(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:(?:1[6-9]|[2-9][0-9])(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)([-])0?2\\2(?:29))(\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])?$"))
//			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�Ƶ����ڴ���,���ڸ�ʽ������  yyyy-MM-dd HH:mm:ss "));	
		
		//�������ʱ���������
		if(!row[7].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�������� ����������!"));	
		
		//�����ѱ���������
		if(!row[8].trim().matches("^(-?\\d+)(\\.\\d+)?$"))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"������ ����������!"));	
		
		//������𳤶�
		if(row[9].trim().length() > 80)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"����������!"));	
		
		//��Ʊ�ų���
		if(row[10].trim().length() > 25)
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"��Ʊ�Ź���!"));	
		
		//�ļ����ظ����
		for(int key:(Set<Integer>)rows.keySet()){
			String[] currentRow = (String[])rows.get(key);
			if(key > rowNum && row[0].trim().equals(currentRow[0].trim()))
				errorInfoList.add(new ExcelParserErrorInfo(rowNum,"��������� " + key + " �еı������ظ�!"));		
		}	
		
		//�����ݿ��е��ظ����
		PolicyService service = new PolicyService();
		if(service.hasSuchPolicy(row[0].trim()))
			errorInfoList.add(new ExcelParserErrorInfo(rowNum,"�˱�������ϵͳ���Ѵ���!������: " + row[0].trim()));
		
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
			List<Policy> list = parser.parse(new BufferedInputStream(new FileInputStream("e:/����insuϵͳ���ݵ����ʽԼ��1.xls")));
			
			if(list == null){
				for(ExcelParserErrorInfo errorInfo:parser.getErrorInfoList())
					System.out.println(errorInfo.getRowNum() + "," + errorInfo.getInfo());					
					
				System.out.println("\n���� " + parser.getErrorInfoList().size() + " ������~");
			}
			else{
				for(Policy policy:list)
					System.out.println(policy);
				
				PolicyService service = new PolicyService();
				service.addPolicy(list);
				System.out.println("\n�������� " + list.size() + " ��������¼~");
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
