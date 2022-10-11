package com.podong.excel.annotation;

import com.podong.excel.style.NoExcelCellStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 엑셀 컬럼을 유동적으로 사용하기 위함.
 * 필드에 headr / body style 지정가능
 *  ExcelColumn annotation 이 붙어있는 dto / vo 함수에 붙여주면 자동으로 header 값 셋팅
 * */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    String headerName() default "";

    ExcelColumnStyle headerStyle() default @ExcelColumnStyle(excelCellStyleClass = NoExcelCellStyle.class);
    ExcelColumnStyle bodyStyle() default @ExcelColumnStyle(excelCellStyleClass = NoExcelCellStyle.class);

}
