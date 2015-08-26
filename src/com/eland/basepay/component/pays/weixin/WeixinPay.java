package com.eland.basepay.component.pays.weixin;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.eland.basepay.component.model.KeyLibs;
import com.eland.basepay.component.model.OrderInfo;
import com.eland.basepay.component.pays.IPayable;
import com.eland.basepay.component.security.weixin.MD5;
import com.eland.basepay.component.security.weixin.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeixinPay implements IPayable {

	//微信sdk对象
	private IWXAPI msgApi;
	//生成预付单需要的参数
	private List<NameValuePair> paramsForPrepay=null;
	//预付单
	private Map<String,String> resultOfPrepay;
	
	@Override
	public String Pay(Activity activity, OrderInfo orderInfo,String prepayId) {
		// TODO Auto-generated method stub
		
		boolean isSuccess=msgApi.sendReq(BuildCallAppParams(prepayId));
		return String.valueOf(isSuccess);
	}

	@Override
	public OrderInfo BuildOrderInfo(String body, String invalidTime,
			String notifyUrl, String tradeNo,
			String subject, String totalFee,String spbillCreateIp) {
		// TODO Auto-generated method stub
		StringBuffer xml = new StringBuffer();

		try {
			String	nonceStr = GetNonceStr();

			xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", KeyLibs.weixin_appId));
			packageParams.add(new BasicNameValuePair("body", subject));//和支付宝的subject类似
			packageParams.add(new BasicNameValuePair("mch_id", KeyLibs.weixin_mchId));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
			packageParams.add(new BasicNameValuePair("out_trade_no",tradeNo));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",spbillCreateIp));
			packageParams.add(new BasicNameValuePair("total_fee", totalFee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			
			paramsForPrepay=packageParams;//将参数保存一份，待调用支付时使用


			String sign = Sign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));


		   String xmlstring =ToXml(packageParams);

		   return new OrderInfo(xmlstring);

		} catch (Exception e) {
			return null;
		}
	}
	
	public void RegisterApp(Context context,String appId){
		msgApi=WXAPIFactory.createWXAPI(context, null);
		msgApi.registerApp(appId);
	}
	
	public String GetPrepayId(OrderInfo orderInfo){
		String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");

		byte[] buf = Util.httpPost(url, orderInfo.GetContent());

		String content = new String(buf);
		Map<String,String> xml=DecodeXml(content);
		
		resultOfPrepay=xml;//保存预支付订单
		return xml.get("prepay_id");

	}
	
	private PayReq BuildCallAppParams(String prepayId) {

		PayReq req=new PayReq();
		req.appId = KeyLibs.weixin_appId;
		req.partnerId = KeyLibs.weixin_mchId;
		req.prepayId = prepayId;
		req.packageValue = "Sign=WXPay";
		req.nonceStr = GetNonceStr();
		req.timeStamp = String.valueOf(GetTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = Sign(signParams);
		return req;

	}
	
	
	private String GetNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	private String Sign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(KeyLibs.weixin_privateKey);
		

		String sign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return sign;
	}
	private String ToXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		return sb.toString();
	}
	private Map<String,String> DecodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;
	}
	private long GetTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

}
