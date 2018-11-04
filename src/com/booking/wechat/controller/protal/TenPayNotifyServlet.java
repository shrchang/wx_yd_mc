package com.booking.wechat.controller.protal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.booking.wechat.client.pay.TenPayClient;
import com.booking.wechat.client.pay.TenPayUtils;
import com.booking.wechat.persistence.bean.order.OrderPayItems;
import com.booking.wechat.persistence.bean.order.Orders;
import com.booking.wechat.persistence.service.order.OrderPayItemsDao;
import com.booking.wechat.persistence.service.order.OrdersDao;

@Controller
@RequestMapping("/protal/tenpay/notify")
public class TenPayNotifyServlet{
	
	@Autowired
	private OrdersDao orderDao;
	
	@Autowired
	private OrderPayItemsDao payItemDao;
	
	@RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        //sb为微信返回的xml
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        //修改订单信息
        try {
			Map<String, String> notifyMap = TenPayUtils.parseXml(sb.toString());
			if("SUCCESS".equals(notifyMap.get("result_code")) && "SUCCESS".equals(notifyMap.get("return_code"))){
				String orderNumber = notifyMap.get("out_trade_no");
				Orders order = orderDao.selectByOrderNumber(orderNumber.replace("FULL", ""));
				
				TenPayClient payClient = new TenPayClient(order.getBusId());
				Map<String, String> tenOrder = payClient.queryOrder(orderNumber);
				//查询订单验证订单支付状态
				if(null != tenOrder.get("trade_state") && "SUCCESS".equals(tenOrder.get("trade_state"))){
					if(null != order){
						OrderPayItems payItem = null;
						if(Orders.UNPAID.equals(notifyMap.get("attach"))){
							payItem = payItemDao.findByOrder(order.getId(), OrderPayItems.TYPE_BOOKING);
							order.setStatus(Orders.PAID);
						}else if(Orders.PAID.equals(notifyMap.get("attach"))){
							order.setStatus(Orders.FINISH);
							payItem = payItemDao.findByOrder(order.getId(), OrderPayItems.TYPE_FINAL);
						}
						payItem.setPayMode(OrderPayItems.MODE_TENAPY);
						payItem.setStatus(OrderPayItems.STATUS_PAID);
						payItemDao.update(payItem);
						
						orderDao.update(order);
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.println("success");
	}

	@RequestMapping(method=RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		doGet(request, response);
	}

}
