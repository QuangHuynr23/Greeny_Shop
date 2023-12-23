package com.webnongsan.greenshop.utils;

import com.webnongsan.greenshop.config.MomoPayConfig;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class MoMoPayUtil {
	public String createMoMoPayment(HttpServletRequest req, HttpServletResponse resp,double totalPrice, long orderId) throws InvalidKeyException, NoSuchAlgorithmException, ClientProtocolException, IOException{
		JSONObject json = new JSONObject();
		long amount = (long) (totalPrice);
		Random generator = new Random();
		long random =(long) generator.nextInt(10000);
		String partnerCode = MomoPayConfig.PARTNER_CODE;
		String accessKey = MomoPayConfig.ACCESS_KEY;
		String secretKey = MomoPayConfig.SECRET_KEY;
		String returnUrl = MomoPayConfig.REDIRECT_URL;
		String notifyUrl = MomoPayConfig.NOTIFY_URL;
		json.put("partnerCode", partnerCode);
		json.put("accessKey", accessKey);
		json.put("requestId", String.valueOf(System.currentTimeMillis()));
		json.put("amount", String.valueOf(amount));
		json.put("orderId", String.valueOf(random));
		json.put("orderInfo", "Thanh toan don hang " + orderId);
		json.put("returnUrl", returnUrl);
		json.put("notifyUrl", notifyUrl);
		json.put("requestType", "captureMoMoWallet");

		String data = "partnerCode=" + partnerCode 
				+ "&accessKey=" + accessKey 
				+ "&requestId=" + json.get("requestId")
				+ "&amount=" + amount
				+ "&orderId=" + json.get("orderId") 
				+ "&orderInfo=" + json.get("orderInfo") 
				+ "&returnUrl=" + returnUrl 
				+ "&notifyUrl=" + notifyUrl 
				+ "&extraData=";

		String hashData = MomoEncoderUtils.signHmacSHA256(data, secretKey);
		json.put("signature", hashData);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(MomoPayConfig.CREATE_ORDER_URL);
		StringEntity stringEntity = new StringEntity(json.toString());
		post.setHeader("content-type", "application/json");
		post.setEntity(stringEntity);

		CloseableHttpResponse res = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
		StringBuilder resultJsonStr = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			resultJsonStr.append(line);
		}
		JSONObject result = new JSONObject(resultJsonStr.toString());
		Map<String, Object> kq = new HashMap<>();
		if (result.get("errorCode").toString().equalsIgnoreCase("0")) {
			kq.put("requestType", result.get("requestType"));
			kq.put("orderId", result.get("orderId"));
			kq.put("payUrl", result.get("payUrl"));
			kq.put("signature", result.get("signature"));
			kq.put("requestId", result.get("requestId"));
			kq.put("errorCode", result.get("errorCode"));
			kq.put("message", result.get("message"));
			kq.put("localMessage", result.get("localMessage"));
		} else {
			kq.put("requestType", result.get("requestType"));
			kq.put("orderId", result.get("orderId"));
			kq.put("signature", result.get("signature"));
			kq.put("requestId", result.get("requestId"));
			kq.put("errorCode", result.get("errorCode"));
			kq.put("message", result.get("message"));
			kq.put("localMessage", result.get("localMessage"));
		}
		return "redirect:" + result.get("payUrl");
	}
}
