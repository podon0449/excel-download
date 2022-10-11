package com.podong.gc.resource;


import java.util.List;
import java.util.Map;

public class ExcelRenderGcResource {

	private List<Map<String, Object>> fieldPivotKeysMapList;
	private String lineRow;

	public ExcelRenderGcResource(List<Map<String, Object>> fieldPivotKeysMapList, String lineRow) {
		this.fieldPivotKeysMapList = fieldPivotKeysMapList;
		this.lineRow = lineRow;
	}

	public List<Map<String, Object>> getFieldPivotKeysMapList() {
		return fieldPivotKeysMapList;
	}


	public String getLineRow() {
		return lineRow;
	}

}
