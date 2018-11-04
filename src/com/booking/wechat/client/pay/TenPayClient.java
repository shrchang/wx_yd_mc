package com.booking.wechat.client.pay;

import java.io.Writer;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.booking.wechat.client.BaseClient;
import com.booking.wechat.client.model.pay.UnifiedorderBean;
import com.booking.wechat.restlet.HttpClientUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 微信支付接口
 * @author Luoxx
 *
 */
public class TenPayClient extends BaseClient{

	public TenPayClient(Long busId) {
		super(busId);
	}


	private static Logger logger = Logger.getLogger(TenPayClient.class);
	/**
	 * 统一下单接口，在微信中生成预支付订单
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	public Map<String,String> unifiedorder(UnifiedorderBean order) throws Exception{
		xstream.alias("xml", UnifiedorderBean.class);
		String xml = xstream.toXML(order);
		xml = xml.replaceAll("__", "_");
		String result = HttpClientUtils.httpPost("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
		logger.info("返回结果："+result);
		return TenPayUtils.parseXml(result);
	}
	
	public Map<String, String> queryOrder(String orderNumber) throws Exception{
		SortedMap<String, String> params = new TreeMap<String, String>();
//		params.put("appid", config.getAppId());
		params.put("appid", "wx0f0a6062e33d800c");
		params.put("mch_id", config.getMachId());
		params.put("out_trade_no", orderNumber);
		String nonce_str=TenPayUtils.buildRandom(32);
		params.put("nonce_str", nonce_str);
		String sign = TenPaySign.createSign(config.getPrivateKey(),params);
		String postXml = "<xml>"+
		   "<appid>"+config.getAppId()+"</appid>"+
		   "<mch_id>"+config.getMachId()+"</mch_id>"+
		   "<nonce_str>"+nonce_str+"</nonce_str>"+
		   "<out_trade_no>"+orderNumber+"</out_trade_no>"+
		   "<sign>"+sign+"</sign>"+
		"</xml>";
		
		String xml = HttpClientUtils.httpPost("https://api.mch.weixin.qq.com/pay/orderquery", postXml);
		Map<String, String> map = TenPayUtils.parseXml(xml);
		return map;
	}
	
	
	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
	
	
	
	
	
}
