package com.changyou.erp.bx.webapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class BxCommonPageAction extends BaseAction{
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private BillCodeManager billCodeManager;
	
	1、ajax返回html后端
		
	@RequestMapping(value = "/bx/showBxFinanceSubject.do",method={RequestMethod.GET, RequestMethod.POST})
	public String showBxFinanceSubject(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception
	{		
    	String billCode = req.getParameter("billCode");
    	if(billCode == null){
    		logger.error("表单号不能为空！");
    		return "errors/erpError";
    	}
    	bxBillCommonManager.initBxDetailFinanceInfo(billCode);
    	List<BxFinanceSubjectDetail> list = bxBillCommonManager.getFinanceSubjectDetailByBillCode(billCode);
		model.put("list", list);
		model.put("isInit", "true");
		return "bx/bx_finance_subject";
	}
	
	

 	2、ajax返回json后端
	@RequestMapping(value = "/attendance/leavebill/checkSupplementHardForExplain.do", method = {RequestMethod.GET, RequestMethod.POST })
	public void checkSupplementHardForExplain(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		Map<String, Object> returnMap=new HashMap<String, Object>();

		String billCode= req.getParameter("billCode");
		if(StringUtil.isEmpty(billCode)){
			returnErrMsgJson(res,returnMap,"false","表单号不能为空！");
			return;
		}
		//先根据billCode获取产假信息
		List<Map<String, Object>> lists=new ArrayList<Map<String,Object>>();
		lists=leavebillManagerImpl.getChildBirthDetailInfoByBillCode(billCode);
		if(lists == null || lists.size() < 1){
			returnErrMsgJson(res,returnMap,"false","先根据表单获取产假信息失败！");
			return;
		}
		String startNum=StringUtil.getString(lists.size()>0 ? lists.get(0).get("starter_num"):null);
		Date standardTime=lists.size()>0 ? (Date)lists.get(0).get("start_time"):null;
		String supplementHard=StringUtil.getString(lists.size()>0 ? lists.get(0).get("supplement_hard"):null);
		if(supplementHard == null || "no".equals(supplementHard)){
			returnErrMsgJson(res,returnMap,"true","");
			return;
		}
		Date endDate=standardTime;
		Date startDate=DateUtil.addYear(endDate, -1);
		//校验补提难产
		leavebillManagerImpl.checkSupplementHard(startNum,billCode,startDate,endDate,returnMap);
		res.getWriter().print(JsonUtil.mapToJson(returnMap));
		return;
	}
	
	//封装返回json信息
	public void returnErrMsgJson(HttpServletResponse res,Map<String, Object> returnMap,String status,String message) throws Exception{
		returnMap.put("status", status);
		returnMap.put("message", message);
		res.getWriter().print(JsonUtil.mapToJson(returnMap));
	}
    
}
