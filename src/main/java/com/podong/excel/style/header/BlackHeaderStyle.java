package com.podong.excel.style.header;

import com.podong.excel.style.CustomExcelCellStyle;
import com.podong.excel.style.align.DefaultExcelAlign;
import com.podong.excel.style.border.DefaultExcelBorders;
import com.podong.excel.style.border.ExcelBorderStyle;
import com.podong.excel.style.configurer.ExcelCellStyleConfigurer;

public class BlackHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {

        configurer.foregroundColor(0, 0, 0)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}