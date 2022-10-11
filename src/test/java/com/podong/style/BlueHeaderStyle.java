package com.podong.style;


import com.podong.style.align.DefaultExcelAlign;
import com.podong.style.border.DefaultExcelBorders;
import com.podong.style.border.ExcelBorderStyle;
import com.podong.style.configurer.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}