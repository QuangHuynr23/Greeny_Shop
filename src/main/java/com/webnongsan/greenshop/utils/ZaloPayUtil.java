package com.webnongsan.greenshop.utils;

import com.webnongsan.greenshop.config.ZaloPayConfig;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ZaloPayUtil {
		public static String getCurrentTimeString(String format) {
			Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			fmt.setCalendar(cal);
			return fmt.format(cal.getTimeInMillis());
		}
		
		public String createVNPayPayment(HttpServletRequest request, HttpServletResponse response, double totalPrice) throws ClientProtocolException, IOException{
			Map<String, Object> zalopay_Params = new HashMap<>();
			zalopay_Params.put("appid", ZaloPayConfig.APP_ID);
			zalopay_Params.put("apptransid", getCurrentTimeString("yyMMdd") + "_" + new Date().getTime());
			zalopay_Params.put("apptime", System.currentTimeMillis());
			String appuser = "Nguyễn Quang Huy";
			zalopay_Params.put("appuser", "demo");
			long amount = (long) totalPrice;
			zalopay_Params.put("amount", amount);
			long order_id = 10;
			zalopay_Params.put("description", "Thanh toan don hang #" + order_id);
			zalopay_Params.put("bankcode", "zalopayapp");
			zalopay_Params.put("item", "[{\"itemid\":\"knb\",\"itemname\":\"kim nguyen bao\",\"itemprice\":198400,\"itemquantity\":1}]");
			Map<String, String> embeddata = new HashMap<>();
			embeddata.put("merchantinfo", "eshop123");
			embeddata.put("promotioninfo", "");
			embeddata.put("redirecturl", ZaloPayConfig.REDIRECT_URL);

			Map<String, String> columninfo = new HashMap<String, String>();
			columninfo.put("store_name", "E-Shop");
			embeddata.put("columninfo", new JSONObject(columninfo).toString());
			zalopay_Params.put("embeddata", new JSONObject(embeddata).toString());

			String data = zalopay_Params.get("appid") + "|" + zalopay_Params.get("apptransid") + "|"
					+ zalopay_Params.get("appuser") + "|" + zalopay_Params.get("amount") + "|"
					+ zalopay_Params.get("apptime") + "|" + zalopay_Params.get("embeddata") + "|"
					+ zalopay_Params.get("item");
			zalopay_Params.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZaloPayConfig.KEY1, data));
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(ZaloPayConfig.CREATE_ORDER_URL);

			List<NameValuePair> params = new ArrayList<>();
			for (Map.Entry<String, Object> e : zalopay_Params.entrySet()) {
				params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
			}
			post.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse res = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
			StringBuilder resultJsonStr = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				resultJsonStr.append(line);
			}
			JSONObject result = new JSONObject(resultJsonStr.toString());
			Map<String, Object> kq = new HashMap<String, Object>();
			kq.put("returnmessage", result.get("returnmessage"));
			kq.put("orderurl", result.get("orderurl"));
			kq.put("returncode", result.get("returncode"));
			kq.put("zptranstoken", result.get("zptranstoken"));
			return "redirect:" + result.get("orderurl");
		}
}
