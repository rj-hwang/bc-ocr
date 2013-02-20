package cn.bc.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OCR 调用
 * <p>
 * 关于Java的验证码识别方面不是太多,有的大多是基于特征码提取的.最近要自动登录网站抓取数据,使用了OCR方式识别的方式处理,记录一下下.
 * 使用的OCR引擎是tesseract,冒得别地选了.
 * 此引擎的优点重复下,字母类语言的识别率几乎可达100%,前提是图像最好黑白(二值化过地),噪点少地图,
 * 测试时发现每个字符的高度必须在10个像素以上才能识别.
 * 增强识别率的关键是要识别的验证码图片越干净越好,为此准备了六种图像过滤,用以滤干净图像,有:图像二值化
 * ,锐化,中值滤波,线性灰度变换,转黑白灰度图,放大(非平滑缩放).
 * 一般干扰不太严重的验证码,如支付宝使用图像二值化和线性灰度变换就可以做到100%识别,有噪点的才需要中值滤波.
 * 使用这六种过滤的组合应该可以搞定大部分比较弱智的验证码 参考了jtOCR的调用封装
 * </p>
 * 
 * @author dragon
 * @ref http://ykf.iteye.com/blog/212431
 */
public class OCR {
	protected transient final Logger logger = LoggerFactory.getLogger(this
			.getClass());
	private final String LANG_OPTION = "-l";
	private final String EOL = System.getProperty("line.separator");
	private String tessPath = "C:\\Program Files (x86)\\Tesseract-OCR";
	//private String tessPath = new File("tesseract").getAbsolutePath();

	public String recognizeText(File imageFile, String imageFormat)
			throws Exception {
		File tempImage = ImageIOHelper.createImage(imageFile, imageFormat);

		File outputFile = new File(imageFile.getParentFile(), "output");
		StringBuffer strB = new StringBuffer();

		List<String> cmd = new ArrayList<String>();
		cmd.add(tessPath + "\\tesseract");
		cmd.add("");
		cmd.add(outputFile.getName());
		cmd.add(LANG_OPTION);
		cmd.add("eng");

		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());

		cmd.set(1, tempImage.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();

		int w = process.waitFor();
		logger.debug("Exit value = {}", w);

		// delete temp working files
		tempImage.delete();

		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
					"UTF-8"));

			String str;

			while ((str = in.readLine()) != null) {
				strB.append(str).append(EOL);
			}
			in.close();
		} else {
			String msg;
			switch (w) {
			case 1:
				msg = "Errors accessing files. There may be spaces in your image's filename.";
				break;
			case 29:
				msg = "Cannot recognize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
			}
			tempImage.delete();
			throw new RuntimeException(msg);
		}

		new File(outputFile.getAbsolutePath() + ".txt").delete();
		logger.info("图像识别结果:{}", strB);
		return strB.toString();
	}
}
