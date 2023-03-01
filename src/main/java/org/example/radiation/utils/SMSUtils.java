package org.example.radiation.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;

/**
 * 短信发送工具类
 */
public class SMSUtils {

	/**
	 * 发送短信
	 * @param SecretId
	 * @param SecretKey
	 * @param phoneNumberSet1
	 * @param templateParamSet1
	 */
	public static void sendMessage(String signName, String templateId, String[] phoneNumberSet1, String[] templateParamSet1){
		try{
			// 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
			// 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
			// 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
			Credential cred = new Credential("AKIDrXyHXjo37yc1DBvSlnaHl05xa2HI6g1A", "xgBRTGqFSP6bsuhFakhR0LUQ8amlbDaj");
			// 实例化一个http选项，可选的，没有特殊需求可以跳过
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("sms.tencentcloudapi.com");
			// 实例化一个client选项，可选的，没有特殊需求可以跳过
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			// 实例化要请求产品的client对象,clientProfile是可选的
			SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
			// 实例化一个请求对象,每个接口都会对应一个request对象
			SendSmsRequest req = new SendSmsRequest();
			// String[] phoneNumberSet1 = {"13640735811"};
			req.setPhoneNumberSet(phoneNumberSet1);

			req.setSmsSdkAppId("1400798443");
			req.setSignName(signName);
			req.setTemplateId(templateId);

			// String[] templateParamSet1 = {"125138", "2"};
			req.setTemplateParamSet(templateParamSet1);

			// 返回的resp是一个SendSmsResponse的实例，与请求对象对应
			SendSmsResponse resp = client.SendSms(req);
			// 输出json格式的字符串回包
			System.out.println(SendSmsResponse.toJsonString(resp));
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
		}
	}

}
