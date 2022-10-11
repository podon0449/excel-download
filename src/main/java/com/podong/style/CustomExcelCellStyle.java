package com.podong.style;

import com.podong.style.configurer.ExcelCellStyleConfigurer;
import org.apache.poi.ss.usermodel.CellStyle;

public abstract class CustomExcelCellStyle implements ExcelCellStyle {

	private ExcelCellStyleConfigurer configurer = new ExcelCellStyleConfigurer();

	public CustomExcelCellStyle() {
		configure(configurer);
	}

	public abstract void configure(ExcelCellStyleConfigurer configurer);

	@Override
	public void apply(CellStyle cellStyle) {
		configurer.configure(cellStyle);
	}

}
