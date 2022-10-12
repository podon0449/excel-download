package com.podong;

import com.podong.exception.NoExcelColumnAnnotationsException;
import com.podong.poi.resource.*;
import com.podong.utils.SuperClassReflectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public abstract class XSSFExcelFile<T> implements ExcelFile<T> {

	protected XSSFWorkbook wb;
	protected Sheet sheet;
	protected ExcelRenderPoiResource resource;

	/**
	 *XSSFExcelFile
	 * @param type Class type to be rendered
	 */
	public XSSFExcelFile(Class<T> type) {
		this(Collections.emptyList(), type, new DefaultDataFormatDecider());
	}

	/**
	 * XSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 */
	public XSSFExcelFile(List<T> data, Class<T> type) {
		this(data, type, new DefaultDataFormatDecider());
	}
	public XSSFExcelFile(List<T> data, List<String> headerKeys, ExcelCustomHeader excelCustom) {
		this(data, headerKeys, excelCustom, new DefaultDataFormatDecider());
	}

	/**
	 * XSSFExcelFile
	 * ExcelColumn annotation 이 지정되어 있는 class 사용
	 */
	public XSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		validateData(data);
		this.wb = new XSSFWorkbook();
		this.resource = ExcelRenderPoiResourceFactory.prepareRenderResource(type, wb, dataFormatDecider);
		renderExcel(data);
	}
	/**
	 * XSSFExcelFile
	 * USER 가 직접 keyColumn 커스텀 하는 경우 사용.
	 * */
	public XSSFExcelFile(List<T> data, List<String> headerKeys, ExcelCustomHeader excelCustom, DataFormatDecider dataFormatDecider) {
		validateData(data);
		this.wb = new XSSFWorkbook();
		this.resource = ExcelRenderPoiResourceFactory.prepareRenderResource(headerKeys, data, wb, dataFormatDecider, excelCustom);
		renderExcel(data);
	}


	protected void validateData(List<T> data) { }

	protected abstract void renderExcel(List<T> data);
	/**
	 * 헤더 설정
	 * */
	protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex, int columnStartIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);

			cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
			cell.setCellValue(resource.getExcelHeaderName(dataFieldName));
		}
	}
	/**
	 * 로우 설정
	 * */
	protected void renderBody(Object data, int rowIndex, int columnStartIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			Object cellValue = null;
			try {
				//ExcelColumn annotation 기반 필드
				if (ExcelCustomHeader.EXCEL_COLUMN == resource.getExcelCustom()) {
					Field field = SuperClassReflectionUtils.getField(data.getClass(), (dataFieldName));
					field.setAccessible(true);
					cellValue = field.get(data);
				}
				// 유저 커스텀 기반 필드
				else {
					Map<String, Object> mapData = (Map<String, Object>) data;
					cellValue = mapData.get(dataFieldName);
				}

				cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.BODY));
				renderCellValue(cell, cellValue);
			} catch (Exception e) {
				throw new NoExcelColumnAnnotationsException("cell body is failed!!");
			}
		}
	}

	/** Cell value 설정 */
	private void renderCellValue(Cell cell, Object cellValue) {
		if (cellValue instanceof Number) {
			Number numberValue = (Number) cellValue;
			cell.setCellValue(numberValue.doubleValue());
			return;
		}
		cell.setCellValue(cellValue == null ? "" : cellValue.toString());
	}
	/** 엑셀 파일 write 후 자원 반환 */
	public void write(OutputStream stream) throws IOException {

		wb.write(stream);
		wb.close();
		stream.close();
	}


}
