package com.webnongsan.greenshop.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;

public class QRCodeGenerator {

	public void generateQRCode (OrderEntity orderEntity, Collection<CartItemEntity> cartItemEntities) throws WriterException, IOException {
		String qrCodePath = "QRCode/images" ;
		String qrCodeName = qrCodePath+"/"+orderEntity.getUser().getName()+ orderEntity.getId()+"-QRCODE.png";
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		StringBuilder stringBuilder = new StringBuilder();
		int index=1;
		for (CartItemEntity cartItemEntity : cartItemEntities) {
			stringBuilder.append("--Product Numerical :" + index +"\n");
			stringBuilder.append("  Product Name :" + cartItemEntity.getProduct().getProductName()+"\n");
			stringBuilder.append("  Product Price :"+ cartItemEntity.getProduct().getPrice()+"\n");
			stringBuilder.append("  The number of Products :" + cartItemEntity.getQuantity()+"\n");
			stringBuilder.append("  Total Money :" + cartItemEntity.getTotalPrice()+"\n\n");
			index ++;
		}
		BitMatrix bitMatrix = qrCodeWriter.encode(stringBuilder.toString(), BarcodeFormat.QR_CODE, 200, 200);
		Path path = FileSystems.getDefault().getPath(qrCodeName);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}
}
