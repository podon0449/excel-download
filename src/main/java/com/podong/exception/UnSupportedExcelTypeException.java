package com.podong.exception;

import com.podong.ExcelException;

public class UnSupportedExcelTypeException extends ExcelException {

	public UnSupportedExcelTypeException(String message) {
		super(message, null);
	}

}
