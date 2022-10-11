package com.podong.excel.gc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.podong.excel.PivotExcelFile;
import com.podong.excel.ExcelException;
import com.podong.excel.utils.JsonUtils;

import java.util.List;
import java.util.Map;



/**
 * GcSheetExcelFile - 피벗 테이블 차트 생성을 위한 ExcelFile class
 */
public final class GcSheetExcelFile<T> extends PivotExcelFile<T> {



	public GcSheetExcelFile(List data, Class<?> type) {
		super(data, type);
	}

	@Override
	public void renderExcel(Object data) {
		//로우데이터를 넣을 시트 생성
		sheet = wb.getWorksheets().add();
		sheet.setName("로우데이터");
		// 로우 width 조정
		sheet.setStandardWidth(25);
		// 데이터를 Gc lib 에서 사용하는 Object[][] 형태 구조로 convert
		Object sourceData = JsonUtils.toObject(JsonUtils.toJson(data), new TypeReference<Object[][]>() {});
		//로우 데이터 생성
		renderBody(sourceData);
		// 피벗테이블 생성.
		renderTable();
	}
	@Override
	protected void validateData(Object data) {
		if (null == data) {
			throw new ExcelException("data is null !!", new NullPointerException());
		}
	}
	@Override
	public void addRows(List<T> data) {}

	@Override
	public void addSheet(Map<String, Object> dataMap) {}




}
