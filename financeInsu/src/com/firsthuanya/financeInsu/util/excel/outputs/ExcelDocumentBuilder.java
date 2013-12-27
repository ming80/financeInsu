package com.firsthuanya.financeInsu.util.excel.outputs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;



public class ExcelDocumentBuilder<T> {
	private String fileName;
	private List<T> data;
	private SheetHeaderAndRow<T> headerAndRow;	
	private int progress;	//进度
	
	private ExcelDocumentBuilder(){		
	}
	
	public ExcelDocumentBuilder(String fileName,List<T> data,SheetHeaderAndRow<T> headerAndRow){
		if(data == null || data.size() == 0)
			throw new IllegalArgumentException("data is empty!");
		if(data.size() > 65500)
			throw new IllegalArgumentException("too large data size! size:" + data.size());
		if(headerAndRow == null)
			throw new IllegalArgumentException("headerAndRow is null!");
		
		if(fileName == null || fileName.trim().equals(""))
			this.fileName = "book1.xls";
		else
			this.fileName = fileName;
		this.data = data;
		this.headerAndRow = headerAndRow;

	}
	
	public int getProgress(){
		return this.progress;
	}
	
	public File execute() throws ExcelCreationException{
		this.progress = 0;	//进度开始
		WritableWorkbook workbook = null;
		
		File excelFile = new File(fileName);
		try{
			workbook = Workbook.createWorkbook(excelFile);
			WritableSheet sheet0 = workbook.createSheet("结果",0);
			
			//创建表头
			int rowIndex = buildHeader(sheet0);
			//创建数据行
			buildDataRows(sheet0,rowIndex);
			
			workbook.write();
			
			//文件创建完成,进度到100%
			this.progress = 100;	
			return excelFile;
		}
		catch(IOException e){ 
			throw new ExcelCreationException("Caused by IOException!",e);
		}
		finally{
			try {
				if(workbook != null)				
					workbook.close();
			} catch (Exception e) {
				throw new ExcelCreationException("Failed to colse the workbook!",e);
			}
		}
	}	
	
	private int buildHeader(WritableSheet sheet) throws ExcelCreationException{		
		int rowIndex = 0;
		String[] header = headerAndRow.getHeader();
		if(header != null && header.length > 0){	//有表头
			for(int columnIndex = 0;columnIndex < header.length;columnIndex++)
				addLabelToSheet(sheet,columnIndex,rowIndex,header[columnIndex]);
			return ++rowIndex;
		}
		return rowIndex;
	}
	
	private void buildDataRows(WritableSheet sheet,int rowIndex) throws ExcelCreationException{
		Iterator<T> iter = this.data.iterator();
		int size = this.data.size();
		T rawRow = null;		
		int index = 0;
		while(iter.hasNext()){
			rawRow = (T)iter.next();
			String[] row = headerAndRow.getRow(rawRow);
			
			if(row == null)
				throw new ExcelCreationException("row is null!row index:" + rowIndex);
			
			for(int columnIndex = 0;columnIndex < row.length;columnIndex++)
				addLabelToSheet(sheet,columnIndex,rowIndex,row[columnIndex]);				

			rowIndex++;
			this.progress = index * 100 / size;	//百分之几(因为是index,不是(index+1),所以最高到99%,当文件创建完后,到100%)
			index++;
		}
	}
	
	private void addLabelToSheet(WritableSheet sheet,int colum,int row,String content) throws ExcelCreationException{
		try {
			if(content == null){
				sheet.addCell(new Label(colum,row,""));
				return;
			}			
			
			sheet.addCell(new Label(colum,row,content));
		} catch (WriteException e) {
			throw new ExcelCreationException("colum: " + colum + " ,row: " + row + ",failed to add label to sheet!",e);
		}
	}

}
