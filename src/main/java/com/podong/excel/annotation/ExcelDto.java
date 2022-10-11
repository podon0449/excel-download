package com.podong.excel.annotation;

import com.podong.excel.style.DefaultExcelCellStyle;

/**
 * 기본 유저 커스텀으로 설정하는 경우 디폴트로 header / body 스타일을 적용시켜주기 위함
 * */
@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "GREY_HEADER"))
@DefaultBodyStyle(style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY"))
public class ExcelDto {


}