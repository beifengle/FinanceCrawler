package com.yysoft.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 功能描述：股票首字母缩写采集
 * @author Huanyan.Lu
 * @date:2016年5月12日
 * @time:下午2:13:46
 * @version:1.0
 */
public class AcronymHttpUtils {
	/**
	 * Description:首字母缩写采集
	 * @param code 股票代码
	 * @return
	 */
	public static String gatherAcronym(String code) {
		String response = null;
		if (code.contains("s")) {
			code = code.replace("sh", "").replace("sz", "");
		}
		String url = "http://smartbox.gtimg.cn/s3/?q=" + code + "&t=all";
		String readString;

		DataInputStream dis;
		try {

			dis = new DataInputStream(new URL(url).openStream());// 得到数据输入流
			while ((readString = dis.readLine()) != null) {
				response=readString;
			}

			dis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO异常：" + e);
		}

		return response;
	}
	
}
