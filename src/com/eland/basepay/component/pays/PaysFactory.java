package com.eland.basepay.component.pays;

import com.eland.basepay.component.model.PayType;
import com.eland.basepay.component.pays.ali.AliPay;
import com.eland.basepay.component.pays.weixin.WeixinPay;

public class PaysFactory {
	
	public static IPayable GetInstance(PayType payType){
		IPayable pay=null;
		switch (payType) {
		case AliPay:
			pay=new AliPay();
			break;
		case WeixinPay:
			pay=new WeixinPay();
		default:
			break;
		}
		return pay;
	}
}
