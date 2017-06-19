package com.changyou.erp.bx.service.impl;

/**
 * Title BxERPAndOracleManagerImpl.java
 * 
 * @author Hanshengkun
 * @see  ERP与Oracle导出
 * @date 2014-02-17
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.cxf.binding.corba.wsdl.Array;
import org.apache.cxf.ws.rm.v200702.SequenceAcknowledgement.Final;
import org.apache.log4j.Logger;
import org.codehaus.groovy.syntax.Types;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.changyou.erp.attendance.SyncIceCardRecord.pesistence.IceJdbcTemplate;
import com.changyou.erp.attendance.common.DateUtil;
import com.changyou.erp.bx.common.BxConstants;
import com.changyou.erp.bx.dao.BxERPAndOracleDao;
import com.changyou.erp.bx.service.BxERPAndOracleManager;
import com.changyou.erp.bx.service.BxFinancecodeMailpoolManager;
import com.changyou.erp.common.ExcelUtils;
import com.changyou.erp.common.PageBean;
import com.changyou.erp.common.SessionUtil;
import com.changyou.erp.common.StringUtil;
import com.changyou.erp.core.dao.BaseDao;
import com.changyou.erp.core.service.BaseService;
import com.changyou.erp.esb.pesistence.EsbJdbcTemplate;
import com.changyou.erp.model.ApolloErpBatchIdGenerate;
import com.changyou.erp.model.ApolloErpBillcode;
import com.changyou.erp.model.BxFeeCrashExport;
import com.changyou.erp.model.BxGrantFeeExport;
import com.changyou.erp.model.ImportOracleData;
import com.changyou.erp.sys.dao.HibernateTemplateDao;
import com.changyou.erp.sys.vo.SessionVo;
import com.changyou.erp.workflow.common.WFConstants;

@Service("BxERPAndOracleManager")
public class CopyOfBxERPAndOracleManagerImpl extends BaseService implements
		BxERPAndOracleManager {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private BxERPAndOracleDao bxERPAndOracleDao;
	@Resource
	protected HibernateTemplate importOracleHibernateTemplate;
	@Resource
	private JdbcTemplate importOracleJdbcTemplate;
	@Resource
	protected HibernateTemplate hibernateTemplate;
	                            
	@Override
	public String excuteImportOracle(String fiType, String exFlowType, String feeCompany, String startDate, String endDate, String formId, String status, String exportName,String cnName,String cnWorkerNo,String importInvoiceSnum,String invoiceYearMonth, HttpServletRequest req) throws Exception {
		SessionVo vo = SessionUtil.getInfoFromSession(req);
		if (vo==null)
		{
			throw new Exception("session无效!");
		}
		if (vo.getWorkerinfo()==null)
		{
			throw new Exception("用户信息为空!");
		}
		//先清空错误信息
		String clearSql = "UPDATE bx_fee_crash_export SET err_msg = NULL";
		
		@Override
		public String excuteImportOracle(String fiType, String exFlowType, String feeCompany, String startDate, String endDate, String formId, String status, String exportName,String cnName,String cnWorkerNo,String importInvoiceSnum,String invoiceYearMonth, HttpServletRequest req) throws Exception {
			SessionVo vo = SessionUtil.getInfoFromSession(req);
			if (vo==null)
			{
				throw new Exception("session无效!");
			}
			if (vo.getWorkerinfo()==null)
			{
				throw new Exception("用户信息为空!");
			}
			//先清空错误信息
			String clearSql = "UPDATE bx_fee_crash_export SET err_msg = NULL";
			jdbcTemplate.execute(clearSql);
			
			//获取当前操作人的编号
			String workerNum = vo.getWorkerinfo().getWorkerinfoNo();
			List<Map<String,Object>> lists = bxERPAndOracleDao.excuteImportOracle(fiType, exFlowType, feeCompany, startDate, endDate, formId, status, exportName,cnName,cnWorkerNo);
			List<ImportOracleData> saveList = new LinkedList<ImportOracleData>();
			ImportOracleData iod = null;
			Integer batchId = generateBatchId();
			for(Map<String,Object> map : lists){
				iod = new ImportOracleData();
				iod.setBatchId(batchId);
				iod.setBatchEstate("input");
				iod.setDateCreated(new Date());
				iod.setErpUser(workerNum);
				iod.setItem1((Integer)map.get("id"));
				iod.setItem2(importInvoiceSnum);
				iod.setItem3(StringUtil.getString(map.get("invoice_type")));
				iod.setItem4(StringUtil.getString(map.get("offer_name")));
				iod.setItem5(StringUtil.getString(map.get("offer_address")));
				iod.setItem6(invoiceYearMonth);
				iod.setItem7(StringUtil.getString(map.get("form_id")));
				iod.setItem8(StringUtil.getString(map.get("invoice_mtype")));
				iod.setItem9(StringUtil.getString(map.get("invoice_money")));
				iod.setItem10(invoiceYearMonth);
				iod.setItem11(StringUtil.getString(map.get("invoice_describe_1")));
				iod.setItem12(StringUtil.getString(map.get("invoice_describe_2")));
				iod.setItem13(StringUtil.getString(map.get("invoice_describe_3")));
				iod.setItem14(StringUtil.getString(map.get("invoice_describe_4")));
				iod.setItem15(StringUtil.getString(map.get("invoice_describe_5")));
				iod.setItem16(StringUtil.getString(map.get("invoice_describe_6")));
				iod.setItem17(StringUtil.getString(map.get("invoice_describe_7")));
				iod.setItem18(StringUtil.getString(map.get("line_num")));
				iod.setItem19(StringUtil.getString(map.get("line_type")));
				iod.setItem20(StringUtil.getString(map.get("line_money")));
				iod.setItem21(StringUtil.getString(map.get("company_field")));
				iod.setItem22(StringUtil.getString(map.get("dept_code")));
				iod.setItem23(StringUtil.getString(map.get("subject_code")));
				iod.setItem24(StringUtil.getString(map.get("sub_subject_code")));
				iod.setItem25(StringUtil.getString(map.get("product_code")));
				iod.setItem26(StringUtil.getString(map.get("ZERO")));
				iod.setItem27(StringUtil.getString(map.get("curr_account")));
				iod.setItem28(StringUtil.getString(map.get("bak")));
				iod.setItem29(StringUtil.getString(map.get("line_describe")));
				saveList.add(iod);
			}
			
			importOracleHibernateTemplate.saveOrUpdateAll(saveList);
			String restatus = getOracleFeedback(2);
			if("F".equals(restatus)){
				String errSql = "select ITEM1,ERROR_MSG from cerp.cux_erp_apinv_input_data_all where ITEM2 = '" + importInvoiceSnum + "' and ERROR_MSG is not null";
				List<String[]> errList = new ArrayList<String[]>();
				errList = importOracleJdbcTemplate.queryForList(errSql, String[].class);
				List<BxFeeCrashExport> feeList = new ArrayList<BxFeeCrashExport>();
				for(String[] tmp : errList){
					BxFeeCrashExport bxFee = new BxFeeCrashExport();
					bxFee.setId(Integer.parseInt(tmp[0]));
					List<BxFeeCrashExport> tmpList = hibernateTemplate.findByExample(bxFee);
					if(tmpList == null || tmpList.size() < 1){
						continue;
					}
					BxFeeCrashExport upBxFee = tmpList.get(0);
					upBxFee.setErrMsg(tmp[1]);
					feeList.add(upBxFee);
				}
				hibernateTemplate.saveOrUpdateAll(feeList);
			}
			
			

			return null;
			
		}
	
	
	


}
