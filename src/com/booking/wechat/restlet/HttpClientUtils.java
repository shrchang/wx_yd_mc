package com.booking.wechat.restlet;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 调用微信接口的处理类
 * @author Luoxx
 *
 */
public class HttpClientUtils {

	public static String httpGet(String strUrl) {
		String result = "";
		try {
			URL url = new URL(strUrl);
			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			HttpGet request = new HttpGet(uri);
			HttpResponse response = HttpClients.createDefault().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String httpPost(String strUrl, String body) throws Exception {
		URL url = new URL(strUrl); 
		URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(body, "UTF-8")); 
		String result = "";
		try {
			HttpResponse response = HttpClients.createDefault().execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
