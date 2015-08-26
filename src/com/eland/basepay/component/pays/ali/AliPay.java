package com.eland.basepay.component.pays.ali;

import android.app.Activity;
import android.content.Context;

import com.alipay.sdk.app.PayTask;
import com.eland.basepay.component.model.KeyLibs;
import com.eland.basepay.component.model.OrderInfo;
import com.eland.basepay.component.pays.IPayable;
import com.eland.basepay.component.security.ali.SignUtils;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AliPay implements IPayable {
	
	/**
	 * 支付
	 * @param activity 支付页面activity
	 * @param orderInfo 规范的订单信息
	 * @param prepayId 预付单号（微信）
	 * @return
	 */
	public String Pay(Activity activity,OrderInfo orderInfo,String prepayId){
		PayTask payTask = new PayTask(activity);
		String result=payTask.pay(orderInfo.GetContent());
		return result;
	}
	
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
	public OrderInfo BuildOrderInfo(
			String body,
			String invalidTime,
			String notifyUrl,
			String tradeNo,
			String subject,
			String totalFee,
			String spbillCreateIp
			){
		
		//商户网站使用的编码格式，固定为utf-8
		String orderInfo="_input_charset="+KeyLibs.mark+"utf-8"+KeyLibs.mark;
		
		//客户端号(可空)
		//orderInfo+="&app_id="+KeyLibs.mark+appId+KeyLibs.mark;
		
		//客户端来源(可空)
		//orderInfo+="&appenv="+KeyLibs.mark+"system=android^version=3.0.1.2"+KeyLibs.mark;
		
		//商品详情。对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
		orderInfo+="&body="+KeyLibs.mark+body+KeyLibs.mark;
		
		//授权令牌(可空)
		//orderInfo+="&extern_token="+KeyLibs.mark+"1b258b84ed2faf3e88b4d979ed9fd4db"+KeyLibs.mark;
		
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo+="&it_b_pay="+KeyLibs.mark+invalidTime+KeyLibs.mark;
		
		//服务器异步通知页面路径
		orderInfo+="&notify_url="+KeyLibs.mark+notifyUrl+KeyLibs.mark;
		
		//商户网站唯一订单号
		orderInfo+="&out_trade_no="+KeyLibs.mark+tradeNo+KeyLibs.mark;
		
		//签约的支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。
		orderInfo+="&partner="+KeyLibs.mark+KeyLibs.ali_partner+KeyLibs.mark;
		
		//支付类型。默认值为：1（商品购买）。
		orderInfo+="&payment_type="+KeyLibs.mark+"1"+KeyLibs.mark;
		
		//卖家支付宝账号（邮箱或手机号码格式）或其对应的支付宝唯一用户号（以2088开头的纯16位数字）。
		orderInfo+="&seller_id="+KeyLibs.mark+KeyLibs.ali_sellerId+KeyLibs.mark;
		
		//接口名称，固定值。
		orderInfo+="&service="+KeyLibs.mark+"mobile.securitypay.pay"+KeyLibs.mark;
		
		//签名类型，目前仅支持RSA。
		orderInfo+="&sign_type="+KeyLibs.mark+"RSA"+KeyLibs.mark;
		
		//商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字。
		orderInfo+="&subject="+KeyLibs.mark+subject+KeyLibs.mark;
		
		//该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。
		orderInfo+="&total_fee="+KeyLibs.mark+totalFee+KeyLibs.mark;
		
		String sign=SignUtils.sign(orderInfo, KeyLibs.ali_privateKey);
		orderInfo+="&sign="+KeyLibs.mark+sign+KeyLibs.mark;
		
		return new OrderInfo(orderInfo);
	}
	
	public void RegisterApp(Context context,String appId){
		return;
	}
	public String GetPrepayId(OrderInfo orderInfo){
		return null;
	}
	
	
}
