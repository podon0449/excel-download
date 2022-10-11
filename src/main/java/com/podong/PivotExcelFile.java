package com.podong;

import com.grapecity.documents.excel.*;
import com.grapecity.documents.excel.drawing.ChartType;
import com.grapecity.documents.excel.drawing.IChart;
import com.podong.gc.resource.ExcelRenderGcResource;
import com.podong.gc.resource.ExcelRenderGcResourceFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


public abstract class PivotExcelFile<T> implements ExcelFile<T> {
	protected Workbook wb;
	protected IWorksheet sheet;
	protected ExcelRenderGcResource resource;



	public PivotExcelFile(List data, Class<?> type) {
		validateData(data);
		this.wb = new Workbook();
		this.resource = ExcelRenderGcResourceFactory.prepareRenderResource(data, type);
		renderExcel(data);
	}

	protected void validateData(Object data) { }

	protected abstract void renderExcel(Object data);

	protected void renderBody(Object data) {
		//데이터 생성 후 엑셀에 해당 라인수 만큼 삽입.
		sheet.getRange("A1:" + resource.getLineRow()).setValue(data);

	}
	/** 피벗 테이블을 만드는 함수
	 * 인자로 넘어온  sheet 에 담겨진 로우 데이터를 기준으로 피벗 테이블을 만듬
	 * */
	protected void renderTable() {
		//sheet 첫번쨰에 피벗 테이블을 가져오기 위함.
		IWorksheet tableSheet = wb.getWorksheets().get(0);
		tableSheet.setStandardWidth(25);
		//피벗 캐싱 변수 설정
		IPivotCache pivotCache = wb.getPivotCaches().create(sheet.getRange("A1:" + resource.getLineRow()));
		//피벗 테이블 생성
		IPivotTable pivotTable = tableSheet.getPivotTables().add(pivotCache, tableSheet.getRange("A1"), "pivottable1");
		//피벗 테이블에 해당 필터 / 행 레이블 / 열 레이블 / 컬럼 / 값 을 셋팅하는 필드
		for (Map<String, Object> fieldProductKeyMap : resource.getFieldPivotKeysMapList()) {
			IPivotField iPivotField = pivotTable.getPivotFields().get((String)fieldProductKeyMap.get("key"));
			iPivotField.setOrientation((PivotFieldOrientation) fieldProductKeyMap.get("field"));
		}
		//보여줄 차트를 구현 및 스타일 설정
		IChart chart = tableSheet.getShapes().addChartInPixel(ChartType.ColumnClustered, 450, 40, 1100, 620).getChart();

		chart.setSourceData(pivotTable.getTableRange1());
	}
	/** 엑셀 파일 write 후 자원 반환 */
	public void write(OutputStream stream) throws IOException {
		wb.save(stream);
		stream.close();
	}


}
