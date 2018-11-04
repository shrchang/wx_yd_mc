package com.booking.wechat.controller.system;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.booking.wechat.controller.BaseController;
import com.booking.wechat.controller.vo.BaseParamVO;
import com.booking.wechat.persistence.bean.bus.Business;
import com.booking.wechat.persistence.bean.shop.BranchShop;
import com.booking.wechat.persistence.bean.user.SysUser;
import com.booking.wechat.persistence.bean.usercard.CardChangeRecords;
import com.booking.wechat.persistence.bean.usercard.PayItems;
import com.booking.wechat.persistence.bean.usercard.UserCard;
import com.booking.wechat.persistence.service.base.QueryResult;
import com.booking.wechat.persistence.service.bus.BusinessDao;
import com.booking.wechat.persistence.service.shop.BranchShopDao;
import com.booking.wechat.persistence.service.usercard.CardChangeRecordsDao;
import com.booking.wechat.persistence.service.usercard.PayItemsDao;
import com.booking.wechat.persistence.service.usercard.UserCardDao;
import com.booking.wechat.util.StringUtil;

@Controller
@RequestMapping("/system/cards")
public class UserCardController extends BaseController{

	@Autowired
	private BusinessDao busDao;
	
	@Autowired
	private UserCardDao cardDao;
	
	@Autowired
	private BranchShopDao shopDao;
	
	@Autowired
	private PayItemsDao payItemsDao;
	
	@Autowired
	private CardChangeRecordsDao recordDao;
	
	@RequestMapping("/list")
	public String getAllList(BaseParamVO param, Map<String, Object> model) throws UnsupportedEncodingException{
		UserCard params = new UserCard();
		
		Session session = SecurityUtils.getSubject().getSession();
		SysUser user = (SysUser) session.getAttribute("sysUser");
		Subject subject = SecurityUtils.getSubject();
		List<Business> bus = busDao.getBusinessByUser();
		//没有系统管理员权限，根据商户查询
		if(!subject.hasRole("admin")){
			params.setBusId(user.getBusId());
		}
		
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setUserName(param.getSearchKey());
		}
		if(null != param.getBusId() && param.getBusId()>0){
			params.setBusId(param.getBusId());
		}
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		if(StringUtil.isNotEmpty(param.getOrderField())){
			orderby.put(param.getOrderField(), param.getOrderDirection());
		}
		QueryResult<UserCard> list = cardDao.findByExample(params, false, param.getPageNum(), param.getNumPerPage(),orderby);
		model.put("bus", bus);
		
		List<BranchShop> shops = shopDao.getBranchShopByUser();
		model.put("shops", shops);
		
		model.put("list",list);
		
		model.put("pageInfo",param);
		
		return "system/userCard/list";
	}
	
	@RequestMapping("/affire/{cardId}")
	public ModelAndView affirem(@PathVariable Long cardId){
		UserCard card = cardDao.find(cardId);
		ModelAndView model = new ModelAndView("system/userCard/recharge");
		model.addObject("card", card);
		List<PayItems> items = payItemsDao.getAll();
		model.addObject("items", items);
		return model;
	}
	
	@RequestMapping(value="/recharge/{cardId}",method=RequestMethod.POST)
	public ModelAndView recharge(@PathVariable Long cardId,BigDecimal recharge,BigDecimal otherMoney){
		UserCard card = cardDao.find(cardId);
		if(recharge.equals(BigDecimal.ZERO)){
			recharge = otherMoney;
		}
		card.setRecharge(card.getRecharge().add(recharge));
		card.setTotalAmount(card.getTotalAmount().add(recharge));
		card.setRemainingSum(card.getRemainingSum().add(recharge));
		card.setStatus(UserCard.STATUS_OK);
		card.setLastRechargeTime(new Date());
		cardDao.update(card);
		
		//会员卡变更记录
		CardChangeRecords record = new CardChangeRecords();
		record.setChangeDate(new Date());
		record.setChangeMoney(recharge);
		record.setChangeType(CardChangeRecords.TYPE_RECHARGE);
		record.setRemainingSum(card.getRemainingSum());
		record.setTotalAmount(card.getTotalAmount());
		record.setUserCardId(card.getId());
		recordDao.save(record);
		return ajaxDoneSuccess("会员卡充值成功！");
	}
	
	@RequestMapping("/records/{cardId}")
	public String records(@PathVariable Long cardId,BaseParamVO param,Map<String, Object> model){
		CardChangeRecords params = new CardChangeRecords();
		params.setUserCardId(cardId);
		if(StringUtil.isNotEmpty(param.getSearchKey())){
			params.setChangeType(param.getSearchKey());
		}
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("changeDate", "desc");
		QueryResult<CardChangeRecords> list = recordDao.findByExample(params,true, param.getPageNum(), param.getNumPerPage(),orderby);
		model.put("list",list);
		model.put("cardId", cardId);
		model.put("pageInfo",param);
		return "system/userCard/records";
	}
	
	@RequestMapping(value="/delete/{cardId}",method=RequestMethod.POST)
	public ModelAndView recharge(@PathVariable Long cardId){
		UserCard card = cardDao.find(cardId);
		if(null==card){
			return ajaxDoneSuccess("没有找到会员卡信息");
		}
		cardDao.delete(cardId);
		recordDao.deleteByUserCardId(cardId);
		return ajaxDoneSuccess("删除会员卡信息成功");
	}
}
