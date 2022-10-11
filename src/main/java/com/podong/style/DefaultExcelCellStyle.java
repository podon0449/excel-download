package com.podong.style;

import com.podong.style.align.DefaultExcelAlign;
import com.podong.style.align.ExcelAlign;
import com.podong.style.border.DefaultExcelBorders;
import com.podong.style.border.ExcelBorderStyle;
import com.podong.style.color.DefaultExcelColor;
import com.podong.style.color.ExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Example of using ExcelCellStyle as Enum
 */
public enum DefaultExcelCellStyle implements ExcelCellStyle {

	GREY_HEADER(DefaultExcelColor.rgb(217, 217, 217),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
	BLUE_HEADER(DefaultExcelColor.rgb(223, 235, 246),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
	BODY(DefaultExcelColor.rgb(255, 255, 255),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.RIGHT_CENTER);

	private final ExcelColor backgroundColor;
	/**
	 * like CSS margin or padding rule,
	 * List<DefaultExcelBorder> represents rgb TOP RIGHT BOTTOM LEFT
	 */
	private final DefaultExcelBorders borders;
	private final ExcelAlign align;

	DefaultExcelCellStyle(ExcelColor backgroundColor, DefaultExcelBorders borders, ExcelAlign align) {
		this.backgroundColor = backgroundColor;
		this.borders = borders;
		this.align = align;
	}

	@Override
	public void apply(CellStyle cellStyle) {
		backgroundColor.applyForeground(cellStyle);
		borders.apply(cellStyle);
		align.apply(cellStyle);
	}

}
