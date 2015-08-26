package com.eland.basepay.component.pays;

import android.app.Activity;
import android.content.Context;

import com.eland.basepay.component.model.OrderInfo;

public interface IPayable {

	/**
	 * 支付
	 * @param activity 支付页面activity（支付宝）
	 * @param orderInfo 规范的订单信息（支付宝）
	 * @param prepayId 预付单（微信）
	 * @return
	 */
	String Pay(Activity activity,OrderInfo orderInfo,String prepayId);
	
	/**
	 * 生成订单参数
	 * @param body 商品详情。对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
	 * @param invalidTime 未付款交易的超时时间。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。参数不接受小数点，如1.5h，可转换为90m。
	 * @param notifyUrl 服务器异步通知页面路径
	 * @param tradeNo 商户唯一订单号
	 * @param subject 商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字。
	 * @param totalFee 该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。
	 * @param spbillCreateIp 终端ip。APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	 * @return
	 */
	OrderInfo BuildOrderInfo(
			String body,
			String invalidTime,
			String notifyUrl,
			String tradeNo,
			String subject,
			String totalFee,
			String spbillCreateIp
			);
	
	/**
	 * 注册appId（微信在调用支付前需要此步）
	 * @param context 上下文
	 * @param appId APPID
	 */
	void RegisterApp(Context context,String appId);
	/**
	 * 生成预付单
	 * @param orderInfo 规范的订单参数信息
	 * @return
	 */
	String GetPrepayId(OrderInfo orderInfo);
	
}
