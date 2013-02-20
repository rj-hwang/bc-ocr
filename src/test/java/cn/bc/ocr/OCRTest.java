package cn.bc.ocr;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class OCRTest {

	@Test
	public void test() throws Exception {
		String path = "C:\\Users\\dragon\\Pictures\\bc\\ocr\\广州警民通网\\";
		OCR ocr = new OCR();
		File file;
		String code;
		String text;

		code = "4579";
		file = new File(path + code + ".jpg");
		text = ocr.recognizeText(file, "jpg");
		Assert.assertEquals(code, text);

		code = "6102";
		file = new File(path + code + ".jpg");
		text = ocr.recognizeText(file, "jpg");
		Assert.assertEquals(code, text);
	}
}
