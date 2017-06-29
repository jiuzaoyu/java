package com.changyou.erp.bx.webapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.changyou.erp.attendance.common.DateUtil;
import com.changyou.erp.attendance.totalresult.vo.PersonalAttendanceDetailVo;
import com.changyou.erp.attendance.totalresult.vo.TotalresultSearchVo;
import com.changyou.erp.bx.common.BxConstants;
import com.changyou.erp.bx.common.StringUtils;
import com.changyou.erp.bx.model.ApolloBillBx;
import com.changyou.erp.bx.model.ApolloBillClbx;
import com.changyou.erp.bx.model.BxDepartProjectProductdataConfig;
import com.changyou.erp.bx.model.BxFinanceSubjectDetail;
import com.changyou.erp.bx.model.BxFinanceSubjectWord;
import com.changyou.erp.bx.service.AttachmentManager;
import com.changyou.erp.bx.service.BxBillCommonManager;
import com.changyou.erp.bx.vo.ApolloWorkerErpPost;
import com.changyou.erp.bx.vo.BxConfigVo;
import com.changyou.erp.bx.vo.BxFinanceSubjectVo;
import com.changyou.erp.bx.vo.ClbxAddVo;
import com.changyou.erp.common.Constants;
import com.changyou.erp.common.JsonUtil;
import com.changyou.erp.common.PageBean;
import com.changyou.erp.common.SessionUtil;
import com.changyou.erp.core.action.BaseAction;
import com.changyou.erp.core.aspect.AuthorityDescription;
import com.changyou.erp.core.aspect.MenuDefine;
import com.changyou.erp.core.exception.ErpException;
import com.changyou.erp.finance.model.SysFinanceOricalProducts;
import com.changyou.erp.model.ApolloErpDepart;
import com.changyou.erp.model.ApolloErpPost;
import com.changyou.erp.model.ApolloErpWorker;
import com.changyou.erp.model.ApolloErpWorkerinfo;
import com.changyou.erp.model.ApolloWordbookDetail;
import com.changyou.erp.model.FlowListVariable;
import com.changyou.erp.sys.common.SysConstants;
import com.changyou.erp.sys.service.BillCodeManager;
import com.changyou.erp.sys.service.DataDictionaryGetManager;
import com.changyou.erp.sys.service.FlowRoleManager;
import com.changyou.erp.sys.service.OrganizeManager;
import com.changyou.erp.sys.service.SystemStartManager;
import com.changyou.erp.sys.service.WorkerinfoManager;
import com.changyou.erp.sys.vo.FlowRoleListVo;
import com.changyou.erp.sys.vo.SessionVo;
import com.changyou.erp.sys.vo.ZTreeVo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


@Controller
public class BxCommonPageAction extends BaseAction{
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private BillCodeManager billCodeManager;
	@Resource
	private FlowRoleManager flowRoleManager;
	@Resource
	private BxBillCommonManager bxBillCommonManager;
	@Resource
	private DataDictionaryGetManager dataDictionaryGetManager;
	@Resource
	private OrganizeManager organizeManager;
	@Resource
	private SystemStartManager systemStartManager;
	@Resource
	private AttachmentManager attachmentManager;
	
   	@RequestMapping(value = "/bx/showBxFinanceSubject.do",method={RequestMethod.GET, RequestMethod.POST})
	public String showBxFinanceSubject(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception
	{		
    	String billCode = req.getParameter("billCode");
    	if(billCode == null){
    		logger.error("表单号不能为空！");
    		return "errors/erpError";
    	}
    	//第一次打开时，按照规则初始化明细的财务信息
    	bxBillCommonManager.initBxDetailFinanceInfo(billCode);
    	List<BxFinanceSubjectDetail> list = bxBillCommonManager.getFinanceSubjectDetailByBillCode(billCode);
		model.put("list", list);
		model.put("isInit", "true");
		return "bx/bx_finance_subject";
	}
   
    
    	@ResponseBody
    	@RequestMapping(value = "/bx/queryBxItem.do",method={RequestMethod.GET, RequestMethod.POST})
	public List<JSONObject> queryBxItem(HttpServletRequest req, HttpServletResponse res) throws Exception
	{		
    	String term=req.getParameter("term");
    	List<BxFinanceSubjectWord> list = bxBillCommonManager.getBxItemByTerm(term);
        List<JSONObject> rList=new ArrayList<JSONObject>();
        for(BxFinanceSubjectWord bw:list){
        	JSONObject json=new JSONObject();
        	json.put("id", bw.getItemCode());
        	json.put("text", bw.getItemName());
        	rList.add(json);
        }
        return rList;
	}
    
}
