package com.podong.excel.poi.resource;

import com.podong.excel.poi.resource.collection.PreCalculatedCellStyleMap;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;
import java.util.Map;

public class ExcelRenderPoiResource {

	private PreCalculatedCellStyleMap styleMap;

	private Map<String, String> excelHeaderNames;
	private List<String> dataFieldNames;
	private ExcelCustomHeader excelCustom;

	public ExcelRenderPoiResource(PreCalculatedCellStyleMap styleMap, Map<String, String> excelHeaderNames, List<String> dataFieldNames, ExcelCustomHeader excelCustom) {
		this.styleMap = styleMap;
		this.excelHeaderNames = excelHeaderNames;
		this.dataFieldNames = dataFieldNames;
		this.excelCustom = excelCustom;
	}

	public ExcelRenderPoiResource(Map<String, String> excelHeaderNames, List<String> dataFieldNames) {
		this.excelHeaderNames = excelHeaderNames;
		this.dataFieldNames = dataFieldNames;
	}
	public CellStyle getCellStyle(String dataFieldName, ExcelRenderLocation excelRenderLocation) {
		return styleMap.get(ExcelCellKey.of(dataFieldName, excelRenderLocation));
	}

	public String getExcelHeaderName(String dataFieldName) {
		return excelHeaderNames.get(dataFieldName);
	}

	public List<String> getDataFieldNames() {
		return dataFieldNames;
	}

	public ExcelCustomHeader getExcelCustom() {return excelCustom;}
}
