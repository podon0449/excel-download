package com.podong.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ExcelFile<T>  {

	void write(OutputStream stream) throws IOException;

	void addRows(List<T> data);

	void addSheet(Map<String, Object> dataMap);

}
