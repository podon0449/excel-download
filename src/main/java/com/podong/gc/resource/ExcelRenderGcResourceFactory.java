package com.podong.gc.resource;

import com.grapecity.documents.excel.PivotFieldOrientation;
import com.podong.utils.SuperClassReflectionUtils;

import java.util.*;

/**
 * ExcelRenderGcResourceFactory
 */
public final class ExcelRenderGcResourceFactory {
	private static final String[] EXCEL_LINE = {"",
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "W", "X", "Y", "Z"
	};

	public static ExcelRenderGcResource prepareRenderResource(List data, Class<?> type) {
		int columnSize = ((ArrayList) data.get(0)).size();
		int rowSize = data.size();
		//header line ex) size 가 10인경우 J
		String line = EXCEL_LINE[columnSize];
		//data size : 1000  = J1000
		String lineRow = line + rowSize;
		//Class<?> type 으로 해당 헤더 리스트를 data 리스트 0번째에 넣어줘야함.
		List<String> pivotHeaderList = getAllPivotHeaderNames(type);
		data.add(0, pivotHeaderList);

		return new ExcelRenderGcResource(getFieldPivotKeysMapList(type), lineRow);
	}
	/**
	 * 피벗 테이블 보고서 필터 / 열 레이블 / 행 레이블 / 값 에 대한 keyMapList 를 가져오기 위한 함수
	 * */
	private static List<Map<String, Object>> getFieldPivotKeysMapList(Class<?> type) {
		return SuperClassReflectionUtils.getPivotEnumClassFieldOrientations(type);
	}
	/** 넘어온 Class<?> Enum 타입에 대한 헤더를 가져오기 위한 함수 */
	private static List<String> getAllPivotHeaderNames(Class<?> type) {
		return SuperClassReflectionUtils.getPivotEnumClassHeaderNames(type);
	}
	/**
	 * 유동적으로 Gc lib 를 사용하기 위해 처리.
	 * **/
	public static PivotFieldOrientation getPivotFieldOrientation(int index) {
		switch (index) {
			case 1 : return PivotFieldOrientation.PageField;
			case 2 : return PivotFieldOrientation.RowField;
			case 3 : return PivotFieldOrientation.DataField;
			case 4 : return PivotFieldOrientation.Hidden;
			case 5 : return PivotFieldOrientation.ColumnField;
		}
		return null;
	}


}
