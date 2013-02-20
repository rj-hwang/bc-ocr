package cn.bc.ocr.exception;

import cn.bc.core.exception.CoreException;

/**
 * OCR异常的封装
 * 
 * @author dragon
 * 
 */
public class OcrException extends CoreException {
	private static final long serialVersionUID = 1L;

	public OcrException() {
		super();
	}

	public OcrException(String message, Throwable cause) {
		super(message, cause);
	}

	public OcrException(String message) {
		super(message);
	}

	public OcrException(Throwable cause) {
		super(cause);
	}
}
