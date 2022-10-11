package com.podong.annotation;


import com.podong.style.ExcelCellStyle;
import com.podong.style.CustomExcelCellStyle;
import com.podong.style.DefaultExcelCellStyle;

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
