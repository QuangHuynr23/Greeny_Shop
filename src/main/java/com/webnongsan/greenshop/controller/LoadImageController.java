package com.webnongsan.greenshop.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
public class LoadImageController {

	@Value("${upload.path}")
	private String pathUploadImage;

	@GetMapping(value = "loadImage")
	@ResponseBody
	public byte[] index(@RequestParam(value = "imageName") String imageName, HttpServletResponse response)
			throws IOException {
		response.setContentType("image/jpeg");
		File file = new File(pathUploadImage + File.separatorChar + imageName);
		InputStream inputStream = null;
		if (file.exists()) {
			try {
				inputStream = new FileInputStream(file);
				if (inputStream != null) {
					return IOUtils.toByteArray(inputStream);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		} else {
			// Trả về hình ảnh mặc định khi không tìm thấy hình ảnh được yêu cầu
			File defaultImage = new File(pathUploadImage + File.separatorChar + "notfound.jpg");
			inputStream = new FileInputStream(defaultImage);
			return IOUtils.toByteArray(inputStream);
		}
		return null;
	}
}
