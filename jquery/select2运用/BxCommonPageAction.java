/*
 * 未经畅游允许禁止使用该文件
 * 版权归畅游所有，2014
 * The contents and use of this file are not to be used without express permission of CYOU Inc.
 * Copyright 2014 CYOU Inc. All rights reserved
 */
package com.changyou.erp.bx.webapp.action;

//Standard java imports
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

/**  
 * <p> 
 * @Title: BxCommonPageAction.java 
 * </p>
 * <p>
 * @Package com.changyou.erp.bx.webapp.action 
 * </p>
 * <p>
 * @Description: 报销功能页面所用到的公共页面封装类 
 * </p>
 * <p>
 * @author lijiankang  
 * </p>
 * @date 2013-7-29 下午3:34:07 新建
 * 
 * @author <A href="mailto:[youyan@cyou-inc.com]">youyan</A>
 * @modify-date 2014-8-12 上午10:39:00
 * 		 		--更改了附件展现页面
 * 		 		--增加了附件操作权限
 * 		 		--增加了版权信息及包注释
 * @version V1.0  
 **/
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

	
	/**
	 * 报销功能页面添加头部
	 * @param bx_type
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bx/cobx_apply.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String comAdd(String bx_type,HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		String billCode = billCodeManager
				.generateBillCode(bx_type);
		SessionVo sessionVo = (SessionVo) req.getSession()
				.getAttribute((String)req.getSession().getAttribute(Constants.SESSION_NAME));
		
		ApolloErpWorkerinfo apolloErpWorkerinfo = sessionVo.getWorkerinfo();
		
		ApolloErpWorker worker = bxBillCommonManager
				.getWorkerNoByWorkerinfoNo(apolloErpWorkerinfo.getWorkerinfoNo());
		
		List<ApolloWorkerErpPost> postList = bxBillCommonManager
				.getWorkerPostsByWorkerinfoNo(apolloErpWorkerinfo.getWorkerinfoNo());

		
		model.put("billCode", billCode);
		
		List<ApolloWordbookDetail> companyList = systemStartManager.getWordbookListByEntryNum(SysConstants.SYS_WORDBOOK.COMPANY.getId());
		Iterator<ApolloWordbookDetail> iterator = companyList.iterator();
		while (iterator.hasNext()) {
		    ApolloWordbookDetail awd = iterator.next();
    		if (awd.getStatus().equals("invalid")) {
    		    iterator.remove();
    		}
		}
		model.put("companyList", companyList);
		model.put("sessionVo", sessionVo);
		model.put("worker", worker);
		model.put("bx_type", bx_type);
		//币种列表
		List<ApolloWordbookDetail> bzList = dataDictionaryGetManager.getDictionaryFir(BxConstants.BX_WORDBOOK.EX_BZ_TYPE.getId());
		model.put("bzList", bzList);
		if(postList != null && postList.size()>0) {
			model.put("postList", postList);
			String postNo = postList.get(0).getApolloErpPost().getPostNo();
			List departList_person = bxBillCommonManager.getDepartByPostNo(postNo);
			if (departList_person != null && !departList_person.isEmpty()) {
				ApolloErpDepart dept = (ApolloErpDepart) departList_person.get(0);
				model.put("dept", dept);
			}
		}

		return "bx/bx_top_common";
	}
	
	/**
	 * 报销公共页面更新_头部
	 * @param billcode
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bx/cobx_apply_update.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String comUpdate(String billcode,HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		
		
		return "bx/bx_top_update_common";
	}
	
	/**
	 * 查询岗位对应部门项目
	 * <p>
	 * Title: rcbxGetDeptByPostId
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param postId
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bx/cobx_getdeptbypostid.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String rcbxGetDeptByPostId(String postNo, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		List departList = bxBillCommonManager.getDepartByPostNo(postNo);

		if (departList != null && !departList.isEmpty()) {
			ApolloErpDepart dept = (ApolloErpDepart) departList.get(0);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			
			//是否需要显示部门全路径
			String allDepartPath = req.getParameter("allDepartPath");
			if("yes".equals(allDepartPath)){//显示部门全路径
				dept.setDepartName(organizeManager.getDepartPathByDepartNum(dept.getDepartNo()));
			}
			jsonMap.put("dept", dept);
			res.getWriter().write(JsonUtil.mapToJson(jsonMap));
		}

		return null;
	}
	
	/**
	 * 查询所有代报人信息
	 * <p>
	 * Title: rcbxGetWorkerByPostId
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param postId
	 * @param workerinfoNo
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bx/cobx_getworkerbypostid.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String rcbxGetWorkerByPostId(String workerinfoNo,
			HttpServletRequest req, HttpServletResponse res, ModelMap model)
			throws Exception {
		List<ApolloErpWorkerinfo> workerList = bxBillCommonManager.getSameDepartWorkerByWorkerinfoNo(workerinfoNo);
		model.put("workerList", workerList);
		return "bx/bx_common_replacerList";
	}
	
	/**
	 * 查询申请人资料
	 * <p>
	 * Title: rcbxGetWorkerByNo
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param workerinfoNo
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bx/cobx_getworkerbyno.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String rcbxGetWorkerByNo(String workerinfoNo,
			HttpServletRequest req, HttpServletResponse res, ModelMap model)
			throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		ApolloErpDepart company = null;
		ApolloErpWorkerinfo workerinfo = bxBillCommonManager
				.getWorkerinfoByWorkerinfoNo(workerinfoNo);
		ApolloErpWorker worker = bxBillCommonManager
				.getWorkerNoByWorkerinfoNo(workerinfoNo);
		if (workerinfo != null) {
			company = organizeManager.getDepartByNo(workerinfo.getCorporation());
		}

		List<ApolloErpPost> postList = bxBillCommonManager
				.getPostsByWorkerinfoNo(workerinfoNo);

		if (workerinfo != null) {
			jsonMap.put("workerinfo", workerinfo);
		}
		if (worker != null) {
			jsonMap.put("worker", worker);
		}
		if (postList != null) {
			jsonMap.put("postList", postList);
		}
		if (company != null) {
			jsonMap.put("company", company);
		}
		res.getWriter().write(JsonUtil.mapToJson(jsonMap));
		return null;
	}
	
	
	
	/**
	 * 查看ES
	 * @param req
	 * @param res
	 * @param billType
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bx/cobx_getes.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String rcbxGetEs(HttpServletRequest req, HttpServletResponse res,String billType,
			ModelMap model) throws Exception {
		List<FlowRoleListVo> ESList = flowRoleManager.getFlowConfigbyNodeKey(
				billType, BxConstants.ES_NODEKEY);
		if (ESList != null) {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("ESList", ESList);
			res.getWriter().write(JsonUtil.mapToJson(jsonMap));
		}
		return null;
	}
	
	/**
	 * <p>Title: 附件明细</p>
	 * <p>Description: 查看附件明细，并过滤操作权限</p>
	 * 
	 * @param billCode	-- 表单号
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bx/bx_fileupload_readonly.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String fileDetailView(String billCode, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		//附件管理系统URL路径
		String url = StringUtils.generateUrl(billCode, req, res, model,
											 BxConstants.FILE_OPTION_UNSUBMIT,
											 BxConstants.FILE_OPTION_UNDELETE,
											 BxConstants.FILE_OPTION_VIEW,
											 BxConstants.FILE_OPTION_LIST);
		model.put("uploadUrl", url);
		return "/bx/attachment/fileupload";
	}
	
	/**
	 * 
	* @Title: getEs 
	* @Description: (得到Es的弹出层的页面) 
	* @param @param billCode
	* @param @param req
	* @param @param res
	* @param @param model
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/bx/getEs.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getEs(HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		return "bx/addEs";
	}

	
	/**
	 * 
	* @Title: goBxDepartAndProjectAndProductCodeIndex 
	* @Description: (跳转到项目管理的页面)  
	* @return String    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_PPCONFIG,description="")
	@RequestMapping(value = "/bx/goBxConfigIndex.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String goBxDepartAndProjectAndProductCodeIndex(BxConfigVo vo,PageBean page,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) {
		try
		{
			vo.setType("project");
			page = bxBillCommonManager.getBxProjectProductDataConfigList(vo, page);
			model.put("pageBean", page);
			model.put("bxConfigResultList", (List<BxDepartProjectProductdataConfig>)page.getResult());
		}
		catch(ErpException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		return "bx/config/configIndex";
	}
	
	
	/**
	 * 保存配置项目
	* @Title: saveBxProjectConfig 
	* @Description: (这里用一句话描述这个方法的作用)  
	* @return String    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_PPCONFIG,description="")
	@RequestMapping(value = "/bx/saveBxProjectConfigIndex.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void saveBxProjectConfig(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		int existFlag = -1;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try
		{
			logger.info(new StringBuilder("先查询该项目编号是否已经存在,projectNo: ").append(vo.getProjectNo()).toString());
			vo.setType("project");
			vo.setProductCode(vo.getProjectNo());
			existFlag =  bxBillCommonManager.saveBxProjectProductDataConfig(vo);
			
		}
		catch(ErpException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			jsonMap.put("error", e.getMessage());
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			jsonMap.put("error", "系统错误");
		}
		
		if(existFlag == 1)
		{
			jsonMap.put("exist", "exist");
		}
		else
		{
			jsonMap.put("success", "success");
		}
		
		res.getWriter().write(JsonUtil.mapToJson(jsonMap));
	
	}
	
	/**
	 * 
	* @Title: getUpdateHtml 
	* @Description:(得到修改的页面)  
	* @return String    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_PPCONFIG,description="")
	@RequestMapping(value = "/bx/getBxProjectConfigUpdateHtml.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getUpdateHtml(HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception 
	{
		return "bx/config/updateProjectConfig";
	}
	
	
	/**
	 * @throws IOException 
	 * 
	* @Title: updateBxProjectConfig 
	* @Description:(修改报销系统  项目--产品段代码的配置)  
	* @return String    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_PPCONFIG,description="")
	@RequestMapping(value = "/bx/updateBxProjectConfig.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void updateBxProjectConfig(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws IOException
	{
		
		vo.setProductCode(vo.getProjectNo());
		vo.setType("project");
		int existFlag = -1;
		String idTemp = vo.getId();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		try
		{
			vo.setId(null);
			BxDepartProjectProductdataConfig config = bxBillCommonManager.getBxProjectProductDataConfig(vo);
			//根据产品段代码等条件查询到 库中已存在  并且不是此条数据,此时提示用户已存在
			if(null != config && !idTemp.trim().equals(config.getId().toString().trim()))
			{
				existFlag = 1;
				jsonMap.put("exist", "exist");
			}
			else
			{
				vo.setId(idTemp);
				bxBillCommonManager.updateBxProjectProductDataConfig(vo);
			}
		}
		catch(ErpException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			jsonMap.put("error", e.getMessage());
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			jsonMap.put("error", "系统错误");
		}
		
		jsonMap.put("success", "success");
		res.getWriter().write(JsonUtil.mapToJson(jsonMap));
	}
	
	
	/**
	 * 
	* @Title: delBxProjectConfig 
	* @Description: (删除项目配置)  
	* @return void    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_PPCONFIG,description="")
	@RequestMapping(value = "/bx/delBxProjectConfig.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void delBxProjectConfig(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception 
	{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		if(null == bxBillCommonManager.getBxProjectProductDataConfig(vo))
		{
			jsonMap.put("error", "该条信息不存在!");
		}
		bxBillCommonManager.delBxProjectProductDataConfig(Integer.parseInt(vo.getId()));
		jsonMap.put("success", "success");
		res.getWriter().write(JsonUtil.mapToJson(jsonMap));
	
	}
	
	
	/**
	 * 
	* @Title: goBxDepartConfigIndex 
	* @Description: (跳转到部门产品段代码配置)  
	* @return void    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_DPCONFIG,description="")
	@RequestMapping(value = "/bx/goBxDepartConfigIndex.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String goBxDepartConfigIndex(BxConfigVo vo,PageBean page,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws IOException 
	{

//		try
//		{
//			vo.setType("depart");
//			page = bxBillCommonManager.getBxProjectProductDataConfigList(vo, page);
//			
//			model.put("pageBean", page);
//			model.put("departNo", vo.getDepartNo());
//			model.put("departName", vo.getDepartName());
//			model.put("productCode", vo.getProductCode());
//			model.put("bxConfigResultList", (List<BxConfigVo>)page.getResult());
//		}
//		catch(ErpException e)
//		{
//			logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//		catch(Exception e)
//		{
//			logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//		return "bx/config/departproduct";
	    throw new IOException("该功能已废弃！");
	}
	
	
	/**
	 * 
	* @Title: goBxDepartConfigIndex 
	* @Description: (保存部门产品段代码配置)  
	* @return void    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_DPCONFIG,description="")
	@RequestMapping(value = "/bx/saveBxDepartConfig.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void saveBxDepartConfig(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws IOException 
	{
		int existFlag = -1;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try
		{
            logger.info(new StringBuilder("先查询该部门编码是否已经存在,projectNo: ").append(vo.getDepartNo()).toString());
			
			vo.setType("depart");
			existFlag = bxBillCommonManager.saveBxProjectProductDataConfig(vo);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			jsonMap.put("error", "系统错误");
		}
		
		if(existFlag == 1)
		{
			jsonMap.put("exist", "exist");
		}
		else
		{
			jsonMap.put("success", "success");
		}
		
		res.getWriter().write(JsonUtil.mapToJson(jsonMap));
		
	}
	
	
	/**
	 * 
	* @Title: getUpdateHtml 
	* @Description: (得到部门修改的页面)  
	* @return String    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_DPCONFIG,description="")
	@RequestMapping(value = "/bx/getBxDepartConfigHtml.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getBxDepartConfigHtml(HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception 
	{
		return "bx/config/updateDepartConfig";
	}
	
	
	/**
	 * @throws IOException 
	 * 
	* @Title: updateBxProjectConfig 
	* @Description: (修改报销系统  项目--产品段代码的配置)  
	* @return String    返回类型 
	* @throws
	 */
	@AuthorityDescription(menu=MenuDefine.BX_DPCONFIG,description="")
	@RequestMapping(value = "/bx/updateBxDepartConfig.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void updateBxDepartConfig(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws IOException
	{
		vo.setType("depart");
	
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		try
		{
			
			bxBillCommonManager.updateBxProjectProductDataConfig(vo);
		
		}
		catch(ErpException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			jsonMap.put("error", e.getMessage());
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			jsonMap.put("error", "系统错误");
		}
		
		jsonMap.put("success", "success");
		res.getWriter().write(JsonUtil.mapToJson(jsonMap));
	}

	
	/**
	 * 
	* @Title: testBxDepartConfig 
	* @Description: (返回费用发生项目的选择页面)  
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/bx/goBxProjectProductCodeConfigIndex.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String goBxProjectProductCodeConfigIndex(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws IOException
	{
		
		return "bx/projectConfigList";
	}
	
	/**
	 * 
	* @Title: testBxDepartConfig 
	* @Description: (返回费用发生项目的选择页面)  
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/bx/getBxProjectProductCodeConfigList.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void getBxProjectProductCodeConfigList(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception
	{
		
		List<ZTreeVo> list = bxBillCommonManager.getBxAllProductCodeConfigList("project");
		res.getWriter().print(JSONArray.fromObject(list));
	}
	/**
	 * 
	* <p>Title: invoiceRule</p> 
	* <p>Description:发票规则说明</p> 
	* @param vo
	* @param req
	* @param res
	* @param model
	* @return
	* @throws Exception
	 */
	@RequestMapping(value = "/bx/invoice_rule.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String invoiceRule(BxConfigVo vo,HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception
	{
		
		return "bx/invoice_rule";
	}
	@ResponseBody
	@RequestMapping(value = "/bx/busToErp.action", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String busToErp(HttpServletRequest request,HttpServletResponse response) throws Exception{
	    String billcode = request.getParameter("flowid");
	    String status = request.getParameter("status");
	    String returnReason = request.getParameter("returnReason");
	    logger.info("+++++++++++++++++++++得到的参数为：+++++++++++start+++++++++++billcode:"+billcode+"++++打款状态："+status);
	    this.bxBillCommonManager.busToErpForAutoApproveBill(billcode, status, returnReason);
	    logger.info("+++++++++++++++++++++返回值为：+++++++++++end+++++++++++");
	    return "1";
	}
	
	
	/**
	 * 检查是否已经上传了对应的附件公共方法；
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
   @RequestMapping(value = "/bx/IsUploadAttachmentCommon.do", method = {RequestMethod.GET, RequestMethod.POST})
	public void IsUploadAttachmentCommon(HttpServletRequest req, HttpServletResponse resp) throws Exception {
	   	String billCode = req.getParameter("billCode");
	   	//类型说明
	   	String type = req.getParameter("type");
	    Map<String, String> jsonMap = bxBillCommonManager.IsUploadAttachmentCommon(billCode,type);
	    resp.getWriter().print(JsonUtil.mapToJson(jsonMap));
	}
   
   /**
	 * 判断员工申请福利的时间是否超过入职时间
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
    @RequestMapping(value = "/bx/isBeyondJoinDate.do", method = {RequestMethod.GET, RequestMethod.POST})
	public void isBeyondJoinDate(HttpServletRequest req, HttpServletResponse resp) throws Exception {
	   	String startNum = req.getParameter("startNum");
	   	//福利发生时间
	   	String happenDateStr = req.getParameter("happenDate");
	   	//福利类型
	   	String type = req.getParameter("type");
	   	//保存还是提交
	   	String saveOrSubmit = req.getParameter("saveOrSubmit");
	   	Date happenDate = DateUtil.StringToDate(happenDateStr);
	    Map<String, String> jsonMap = bxBillCommonManager.isBeyondJoinDate(startNum,happenDate,type,saveOrSubmit);
	    resp.getWriter().print(JsonUtil.mapToJson(jsonMap));
	}
    
    
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
    
   
	@RequestMapping(value = "/bx/saveBxFinanceSubjectInfo.do", method = {RequestMethod.GET, RequestMethod.POST })
	public void saveBxFinanceSubjectInfo(BxFinanceSubjectVo bfSub,HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		Map<String,String> retMap=new HashMap<String,String>();
		try {
			String billCode = req.getParameter("billCode");
	    	if(billCode == null){
	    		logger.error("表单号不能为空！");
	    		retMap.put("flag", "false");
	    		retMap.put("message", "保存失败!");
				res.getWriter().write(JsonUtil.mapToJson(retMap));
				return;
	    	}
			
	    	bxBillCommonManager.saveBxFinanceSubjectInfo(bfSub, billCode);
	    	retMap.put("flag", "true");
	    	retMap.put("message", "保存成功!");
			res.getWriter().write(JsonUtil.mapToJson(retMap));
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			retMap.put("flag", "false");
			retMap.put("message", "保存失败!");
			res.getWriter().write(JsonUtil.mapToJson(retMap));
		}
		
	}
    
}
