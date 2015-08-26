package com.eland.basepay.component.model;

public class KeyLibs {

	public static String mark = "\"";

	// 签约的支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。
	public static String ali_partner = null;
	// 卖家支付宝账号（邮箱或手机号码格式）或其对应的支付宝唯一用户号（以2088开头的纯16位数字）。
	public static String ali_sellerId = null;
	//商户rsa私钥，pkcs8格式
	public static String ali_privateKey = null;
	// appId（在请同时修改  androidmanifest.xml里面，.PayActivityd里的属性
	//       <data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid）
	public static String weixin_appId = null;
	// 商户号
	public static String weixin_mchId = null;
	//API密钥
	public static String weixin_privateKey = null;

}
