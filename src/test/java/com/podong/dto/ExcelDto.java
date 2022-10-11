package com.podong.dto;

import com.podong.annotation.DefaultHeaderStyle;
import com.podong.annotation.ExcelColumn;
import com.podong.annotation.ExcelColumnStyle;
import com.podong.style.BlackHeaderStyle;
import com.podong.style.BlueHeaderStyle;

@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
public class ExcelDto {

    @ExcelColumn(headerName = "name")
    private String name;

    private String hideColumn;

    @ExcelColumn(headerName = "age",
            headerStyle = @ExcelColumnStyle(excelCellStyleClass = BlackHeaderStyle.class))
    private int age;

}
