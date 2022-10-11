package com.podong.poi.resource;

import com.podong.annotation.*;
import com.podong.CommonException;
import com.podong.excel.annotation.*;
import com.podong.poi.resource.collection.PreCalculatedCellStyleMap;
import com.podong.style.ExcelCellStyle;
import com.podong.style.NoExcelCellStyle;
import com.podong.utils.SuperClassReflectionUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelRenderResourceFactory
 *
 */
public final class ExcelRenderPoiResourceFactory {

	public static ExcelRenderPoiResource prepareRenderResource(Class<?> type, Workbook wb,
                                                               DataFormatDecider dataFormatDecider) {
		PreCalculatedCellStyleMap styleMap = new PreCalculatedCellStyleMap(dataFormatDecider);
		Map<String, String> headerNamesMap = new LinkedHashMap<>();
		List<String> fieldNames = new ArrayList<>();

		ExcelColumnStyle classDefinedHeaderStyle = getHeaderExcelColumnStyle(type);
		ExcelColumnStyle classDefinedBodyStyle = getBodyExcelColumnStyle(type);
		for (Field field : SuperClassReflectionUtils.getAllFields(type)) {
			if (field.isAnnotationPresent(ExcelColumn.class)) {
				ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
				styleMap.put(
						String.class,
						ExcelCellKey.of(field.getName(), ExcelRenderLocation.HEADER),
						getCellStyle(decideAppliedStyleAnnotation(classDefinedHeaderStyle, annotation.headerStyle())), wb);
				Class<?> fieldType = field.getType();
				styleMap.put(
						fieldType,
						ExcelCellKey.of(field.getName(), ExcelRenderLocation.BODY),
						getCellStyle(decideAppliedStyleAnnotation(classDefinedBodyStyle, annotation.bodyStyle())), wb);
				fieldNames.add(field.getName());
				headerNamesMap.put(field.getName(), annotation.headerName());
			}
		}

		if (headerNamesMap.isEmpty()) {
			throw new CommonException("hedaerNameMap is null !!", new NullPointerException());
		}
		return new ExcelRenderPoiResource(styleMap, headerNamesMap, fieldNames, ExcelCustomHeader.EXCEL_COLUMN);
	}

	/**
	 * 유저가 직접 keyColumn 을 생성하여 사용하기 위한 커스텀 함수
	 * ExcelDto 로 기본 디폴트 스타일을 지정시킴.  수정하려면 ExcelDto.class 에  excelCellStyleClass 로 변경가능.
	 * 되도록이면 ExcelColumn 을 사용하여 사용하기 바람.
	 * */
	public static ExcelRenderPoiResource prepareRenderResource(List<String> headerKeys, List<String> fieldColumns, Workbook wb,
                                                               DataFormatDecider dataFormatDecider, ExcelCustomHeader excelCustom) {
		ExcelColumnStyle classDefinedHeaderStyle = getHeaderExcelColumnStyle(ExcelDto.class);
		ExcelColumnStyle classDefinedBodyStyle = getBodyExcelColumnStyle(ExcelDto.class);

		PreCalculatedCellStyleMap styleMap = new PreCalculatedCellStyleMap(dataFormatDecider);
		Map<String, String> headerNamesMap = new LinkedHashMap<>();

		for (int i=0; i < headerKeys.size(); i++) {

			headerNamesMap.put(fieldColumns.get(i), headerKeys.get(i));

			styleMap.put(
					String.class,
					ExcelCellKey.of(fieldColumns.get(i), ExcelRenderLocation.HEADER),
					getCellStyle(classDefinedHeaderStyle), wb);
			styleMap.put(
					String.class,
					ExcelCellKey.of(fieldColumns.get(i), ExcelRenderLocation.BODY),
					getCellStyle(classDefinedBodyStyle), wb);
		}
		return new ExcelRenderPoiResource(styleMap, headerNamesMap, fieldColumns, excelCustom);
	}


	private static ExcelColumnStyle getHeaderExcelColumnStyle(Class<?> clazz) {
		Annotation annotation = SuperClassReflectionUtils.getAnnotation(clazz, DefaultHeaderStyle.class);
		if (annotation == null) {
			return null;
		}
		return ((DefaultHeaderStyle) annotation).style();
	}

	private static ExcelColumnStyle getBodyExcelColumnStyle(Class<?> clazz) {
		Annotation annotation = SuperClassReflectionUtils.getAnnotation(clazz, DefaultBodyStyle.class);
		if (annotation == null) {
			return null;
		}
		return ((DefaultBodyStyle) annotation).style();
	}

	private static ExcelColumnStyle decideAppliedStyleAnnotation(ExcelColumnStyle classAnnotation,
																 ExcelColumnStyle fieldAnnotation) {
		if (fieldAnnotation.excelCellStyleClass().equals(NoExcelCellStyle.class) && classAnnotation != null) {
			return classAnnotation;
		}
		return fieldAnnotation;
	}

	private static ExcelCellStyle getCellStyle(ExcelColumnStyle excelColumnStyle) {
		Class<? extends ExcelCellStyle> excelCellStyleClass = excelColumnStyle.excelCellStyleClass();
		// 1. Case of Enum
		if (excelCellStyleClass.isEnum()) {
			String enumName = excelColumnStyle.enumName();
			return findExcelCellStyle(excelCellStyleClass, enumName);
		}

		// 2. Case of Class
		try {
			return excelCellStyleClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new CommonException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private static ExcelCellStyle findExcelCellStyle(Class<?> excelCellStyles, String enumName) {
		try {
			return (ExcelCellStyle) Enum.valueOf((Class<Enum>) excelCellStyles, enumName);
		} catch (NullPointerException e) {
			throw new CommonException("enumName must not be null", e);
		} catch (IllegalArgumentException e) {
			throw new CommonException(
					String.format("Enum %s does not name %s", excelCellStyles.getName(), enumName), e);
		}
	}

}
