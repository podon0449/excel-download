package com.podong.excel.annotation;


import com.podong.excel.style.ExcelCellStyle;
import com.podong.excel.style.CustomExcelCellStyle;
import com.podong.excel.style.DefaultExcelCellStyle;

public @interface ExcelColumnStyle {

	/**
	 * Enum implements {@link ExcelCellStyle}
	 * Also, can use just class.
	 * If not use Enum, enumName will be ignored
	 * @see DefaultExcelCellStyle
	 * @see CustomExcelCellStyle
	 */
	Class<? extends ExcelCellStyle> excelCellStyleClass();

	/**
	 * name of Enum implements {@link ExcelCellStyle}
	 * if not use Enum, enumName will be ignored
	 */
	String enumName() default "";

}
