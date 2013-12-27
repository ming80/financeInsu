package com.firsthuanya.financeInsu.util.excel.outputs;

public interface SheetHeaderAndRow<T> {
	public String[] getHeader();
	public String[] getRow(T rawRow);
}
