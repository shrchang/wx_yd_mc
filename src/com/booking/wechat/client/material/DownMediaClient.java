package com.booking.wechat.client.material;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.booking.wechat.client.BaseClient;
import com.booking.wechat.token.WeixinToken;

/**
 * 下载微信服务器上的图片，用于发送图文消息
 * @author Luoxx
 *
 */
public class DownMediaClient extends BaseClient{
	
	public DownMediaClient(Long busId) {
		super(busId);
	}

	/**
	 * 根据文件id下载文件
	 * @param mediaId 媒体id
	 * @throws Exception
	 */
	public InputStream getInputStream(String mediaId) {
		String accessToken = WeixinToken.getAccessToken(config).getAccess_token();
		InputStream is = null;
		String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=" + accessToken;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("POST"); 
			http.setDoOutput(true);
			http.setDoInput(true);
			OutputStream out = http.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out,"utf-8");
			BufferedWriter bwriter = new BufferedWriter(writer);
			bwriter.write("{\"media_id\":\""+mediaId+"\"}");
			bwriter.flush();
			
			http.connect();
			// 获取文件转化为byte流
			is = http.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * 
	 * 获取下载图片信息（jpg）
	 * @param mediaId 文件的id
	 * @throws Exception
	 */
	public void saveImageToDisk(String mediaId) throws Exception {
		InputStream inputStream = getInputStream(mediaId);
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream("D://test1.jpg");
			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
