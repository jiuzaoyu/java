package com.changyou.erp.bx.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.changyou.erp.bx.common.BxConstants;
import com.changyou.erp.bx.dao.AccountEnterApplyDao;
import com.changyou.erp.bx.model.BxAccountEnterMain;
import com.changyou.erp.bx.model.BxInvoiceNumSeq;
import com.changyou.erp.bx.vo.AccountEnterMainVo;
import com.changyou.erp.bx.vo.AccountEnterSourceSearchVo;
import com.changyou.erp.common.BasePage;
import com.changyou.erp.core.dao.BaseDao;
import com.changyou.erp.model.BxFeeCrashExport;
/**
 * 
 * Title:AccountEnterApplyDaoHibernate.java
 * Description:入账流程申请Dao实现类
 * Company:changyou
 * @author:shihongmei
 * date:Apr 7, 2015 3:56:27 PM
 */
@Repository("accountEnterApplyDaoHibernate")
public class AccountEnterApplyDaoHibernate extends BaseDao implements AccountEnterApplyDao{

	/**
	 * 
	 * @param searchVo
	 * @param basePage
	 * @return 查询入账的源数据---入账流程用
	 * @throws Exception
	 */
	@Override
	public BasePage getAccountEnterSourceList(
			AccountEnterSourceSearchVo searchVo, BasePage basePage)
			throws Exception {
		StringBuilder hql = new StringBuilder("from BxFeeCrashExport where 1=1");
		List params = new ArrayList();
		
		buildHqlSourceList(hql,searchVo,params);		
		return this.pagedQuery(hql.toString(), basePage, BxFeeCrashExport.class, params.toArray());
	}
	/**
	 * 财务入账查询的数据源总金额
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public BigDecimal getLineMoneySumBySource(AccountEnterSourceSearchVo searchVo) throws Exception{
		StringBuilder hql = new StringBuilder("select sum(lineMoney) from BxFeeCrashExport where 1=1");
		List params = new ArrayList();
		
		buildHqlSourceList(hql,searchVo,params);	
		List list = this.find(hql.toString(),params.toArray());
		return (BigDecimal)list.get(0);
	}
	private void buildHqlSourceList(StringBuilder hql,AccountEnterSourceSearchVo searchVo,List params){
		if(!StringUtils.isBlank(searchVo.getCompanySection())){
			hql.append(" and companyField =?");
			params.add(searchVo.getCompanySection());
		}
		if(!StringUtils.isBlank(searchVo.getDeptSection())){
			hql.append(" and deptCode like ?");
			params.add("%"+searchVo.getDeptSection()+"%");
		}
		if("yes".equals(searchVo.getIsNullInvoice())){
			hql.append(" and invoiceNum is null");			
		}
		if(!StringUtils.isBlank(searchVo.getInvoiceNum())){
			hql.append(" and invoiceNum like ?");
			params.add("%"+searchVo.getInvoiceNum()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getLineDescribe())){
			hql.append(" and lineDescribe like ?");
			params.add("%"+searchVo.getLineDescribe()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getOfferName())){
			hql.append(" and offerName like ?");
			params.add("%"+searchVo.getOfferName()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getProductCode())){
			hql.append(" and productCode like ?");
			params.add("%"+searchVo.getProductCode()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getSubjectCode())){
			hql.append(" and subjectCode like ?");
			params.add("%"+searchVo.getSubjectCode()+"%");
		}		
		if(!StringUtils.isBlank(searchVo.getSubSubjectCode())){
			hql.append(" and subSubjectCode like ?");
			params.add("%"+searchVo.getSubSubjectCode()+"%");
		}	
		if(!StringUtils.isBlank(searchVo.getFiType())){
			hql.append(" and fiType=?");
			params.add(searchVo.getFiType());
		}	
		if(!StringUtils.isBlank(searchVo.getExFlowType())){
			hql.append(" and exFlowType=?");
			params.add(searchVo.getExFlowType());
		}
		//财务预审时间查询条件
		if(!StringUtils.isBlank(searchVo.getPreTrailName())){
			hql.append(" and preTrailName like ?");
			params.add("%"+searchVo.getPreTrailName()+"%");
		}
		if(searchVo.getStartDate()!=null){
			hql.append(" and preTrialOperateTime >= ?");
			params.add(searchVo.getStartDate());
		}
		if(searchVo.getEndDate()!=null){
			hql.append(" and preTrialOperateTime <= ?");
			params.add(searchVo.getEndDate());
		}
		//财务复审时间查询条件
		if(!StringUtils.isBlank(searchVo.getCwzgName())){
			hql.append(" and cwzgName like ?");
			params.add("%"+searchVo.getCwzgName()+"%");
		}
		if(searchVo.getCwzgOperateTimeStart()!=null){
			hql.append(" and cwzgOperateTime >= ?");
			params.add(searchVo.getCwzgOperateTimeStart());
		}
		if(searchVo.getCwzgOperateTimeEnd()!=null){
			hql.append(" and cwzgOperateTime <= ?");
			params.add(searchVo.getCwzgOperateTimeEnd());
		}
		//0
		if(!StringUtils.isBlank(searchVo.getZero())){
			hql.append(" and zero=?");
			params.add(searchVo.getZero());
		}
		
		if("apply".equals(searchVo.getViewOrApply())){
			//修改冲账和结束的，不包括冰冻
			hql.append(" and companyField!='3117' and companyField!='3118' and (status=? or status=?)");
			params.add(BxConstants.BX_ORACLE_PRETRIAL);
			params.add(BxConstants.BX_ORACLE_END);
			hql.append(" and invoiceNum is null");
		}
		hql.append(" order by cwzgOperateTime asc,formId asc");
	}
	/**
	 * 
	 * @param searchVo
	 * @return 查询入账的源数据---入账流程用
	 * @throws Exception
	 */
	@Override
	public List<Integer> getAccountEnterSourceList(AccountEnterSourceSearchVo searchVo) throws Exception{
		StringBuilder hql = new StringBuilder("select id from BxFeeCrashExport where 1=1");
		List params = new ArrayList();
		buildHqlSourceList(hql,searchVo,params);	
		
		return this.find(hql.toString(), params.toArray());
	}
	/**
	 * 根据表单号得到对应的入账数据(明细表)
	 * @param searchVo
	 * @param basePage
	 * @param billCode
	 * @return
	 * @throws Exception
	 */
	public BasePage getAccountEnterDetailList(AccountEnterSourceSearchVo searchVo,BasePage basePage,String billCode) throws Exception{
		StringBuilder hql = new StringBuilder("from BxAccountEnterDetail where billCode=?");
		List params = new ArrayList();
		params.add(billCode);
		if(!StringUtils.isBlank(searchVo.getCompanySection())){
			hql.append(" and companyField like ?");
			params.add("%"+searchVo.getCompanySection()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getDeptSection())){
			hql.append(" and deptCode like ?");
			params.add("%"+searchVo.getDeptSection()+"%");
		}
		if("yes".equals(searchVo.getIsNotation())){
			hql.append(" and (notation is not null and notation!='')");			
		}
		if("yes".equals(searchVo.getIsDicussion())){
			hql.append(" and (discussionReason is not null and discussionReason!='')");			
		}
		if(!StringUtils.isBlank(searchVo.getLineDescribe())){
			hql.append(" and lineDescribe like ?");
			params.add("%"+searchVo.getLineDescribe()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getOfferName())){
			hql.append(" and offerName like ?");
			params.add("%"+searchVo.getOfferName()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getProductCode())){
			hql.append(" and productCode like ?");
			params.add("%"+searchVo.getProductCode()+"%");
		}
		if(!StringUtils.isBlank(searchVo.getSubjectCode())){
			hql.append(" and subjectCode like ?");
			params.add("%"+searchVo.getSubjectCode()+"%");
		}		
		if(!StringUtils.isBlank(searchVo.getSubSubjectCode())){
			hql.append(" and subSubjectCode like ?");
			params.add("%"+searchVo.getSubSubjectCode()+"%");
		}	
		if(!StringUtils.isBlank(searchVo.getFiType())){
			hql.append(" and fiType=?");
			params.add(searchVo.getFiType());
		}	
		if(!StringUtils.isBlank(searchVo.getExFlowType())){
			hql.append(" and exFlowType=?");
			params.add(searchVo.getExFlowType());
		}
		if(!StringUtils.isBlank(searchVo.getInvoiceNum())){
			hql.append(" and invoiceNum like ?");
			params.add("%"+searchVo.getInvoiceNum()+"%");
		}	
		if("yes".equals(searchVo.getImportFailur())){
			hql.append(" and orgMessage is not null and orgMessage!=''");			
		}
		//财务预审时间查询条件
		if(!StringUtils.isBlank(searchVo.getPreTrailName())){
			hql.append(" and preTrailName like ?");
			params.add("%"+searchVo.getPreTrailName()+"%");
		}
		if(searchVo.getStartDate()!=null){
			hql.append(" and preTrialOperateTime >= ?");
			params.add(searchVo.getStartDate());
		}
		if(searchVo.getEndDate()!=null){
			hql.append(" and preTrialOperateTime <= ?");
			params.add(searchVo.getEndDate());
		}
		//财务复审时间查询条件
		if(!StringUtils.isBlank(searchVo.getCwzgName())){
			hql.append(" and cwzgName like ?");
			params.add("%"+searchVo.getCwzgName()+"%");
		}
		if(searchVo.getCwzgOperateTimeStart()!=null){
			hql.append(" and cwzgOperateTime >= ?");
			params.add(searchVo.getCwzgOperateTimeStart());
		}
		if(searchVo.getCwzgOperateTimeEnd()!=null){
			hql.append(" and cwzgOperateTime <= ?");
			params.add(searchVo.getCwzgOperateTimeEnd());
		}
		//0
		if(!StringUtils.isBlank(searchVo.getZero())){
			hql.append(" and zero=?");
			params.add(searchVo.getZero());
		}
		hql.append(" order by cwzgOperateTime asc,formId asc");
		return this.pagedQuery(hql.toString(), basePage, BxFeeCrashExport.class, params.toArray());
	}
	/**
	 * 根据财务入账明细表编号，得到所属的报销表单号
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<String> getFormIdFromDetailId(List<Integer> ids) throws Exception{
		StringBuilder hql = new StringBuilder("select distinct formId from BxAccountEnterDetail where id in(");
		List params = new ArrayList();
		for(int i=0;i<ids.size();i++){
			if(i!=ids.size()-1){
				hql.append(ids.get(i)).append(",");
			}else{
				hql.append(ids.get(i));
			}			
		}
		hql.append(")");
		return this.find(hql.toString(), params.toArray());
	}
	/**
	 * 根据报销表单号删除明细表数据
	 * @param formId
	 * @throws Exception
	 */
	public void delDetailByFormId(String formId)throws Exception{
		StringBuilder hql = new StringBuilder("delete from BxAccountEnterDetail where formId=?");
		List params = new ArrayList();
		params.add(formId);
		this.bulkUpdate(hql.toString(), params.toArray());
	}
	/**
	 * 将数据源中对应的表单号的发票编号置空
	 * @param formId
	 * @throws Exception
	 */
	public void setNullInvoiceNumForSource(String formId)throws Exception{
		StringBuilder hql = new StringBuilder("update BxFeeCrashExport set invoiceNum=null where formId=?");
		List params = new ArrayList();
		params.add(formId);
		this.bulkUpdate(hql.toString(), params.toArray());
	}
	/**
	 * 得到发票编号序列
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@Override
	public BxInvoiceNumSeq getInvoiceNumSeq(BxInvoiceNumSeq seq) throws Exception{
		StringBuilder hql = new StringBuilder("from BxInvoiceNumSeq where 1=1");
		List params = new ArrayList();		
		if(!StringUtils.isBlank(seq.getInvoiceNum())){
			hql.append(" and invoiceNum=?");
			params.add(seq.getInvoiceNum());
		}else{
			hql.append(" and invoiceNum is null");
		}
		if(!StringUtils.isBlank(seq.getInvoiceNumPre())){
			hql.append(" and invoiceNumPre=?");
			params.add(seq.getInvoiceNumPre());
		}
		List<BxInvoiceNumSeq> list = this.find(hql.toString(), params.toArray());
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据表单号得到数据源
	 * @param fromIds
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<BxFeeCrashExport> getSourceByFormId(List<String> formIds)throws Exception{
		StringBuilder hql = new StringBuilder("from BxFeeCrashExport where formId in(");
		List params = new ArrayList();
		for(int i=0;i<formIds.size();i++){
			if(i!=formIds.size()-1){
				hql.append("'").append(formIds.get(i)).append("',");
			}else{
				hql.append("'").append(formIds.get(i)).append("'");
			}			
		}
		hql.append(")");
		return this.find(hql.toString(), params.toArray());
	}
	/**
	 * 根据数据源ID得到表单号
	 * @param sourceIds
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<String> getFormIdsBySourceIds(List<Integer> sourceIds)throws Exception{
		StringBuilder hql = new StringBuilder("select distinct formId from BxFeeCrashExport where id in (");
		List params = new ArrayList();
		for(int i=0;i<sourceIds.size();i++){
			if(i!=sourceIds.size()-1){
				hql.append(sourceIds.get(i)).append(",");
			}else{
				hql.append(sourceIds.get(i));
			}			
		}
		hql.append(")");
		return this.find(hql.toString(), params.toArray());
	}
	/**
	 * 得到一个表单报销行金额的总和
	 * @param formId
	 * @return
	 * @throws Exception
	 */
	@Override
	public BigDecimal getLineMoneySumByFormId(String formId) throws Exception{
		StringBuilder hql = new StringBuilder("select sum(lineMoney) from BxAccountEnterDetail where formId=?");
		List params = new ArrayList();
		params.add(formId);
		return (BigDecimal) this.find(hql.toString(), params.toArray()).get(0);
	}
	/**
	 * 根据表单号得到主表
	 * @param billcode
	 * @return
	 * @throws Exception
	 */
	@Override
	public BxAccountEnterMain getMainByBillcode(String billcode)throws Exception{
		StringBuilder hql = new StringBuilder("from BxAccountEnterMain where billCode=?");
		List params = new ArrayList();
		params.add(billcode);
		List<BxAccountEnterMain> list = this.find(hql.toString(), params.toArray());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 财务入帐申请单的总金额
	 * @param billCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public BigDecimal getLineMoneySumByBillcode(String billCode) throws Exception{
		StringBuilder hql = new StringBuilder("select sum(lineMoney) from BxAccountEnterDetail where billCode=?");
		List params = new ArrayList();
		params.add(billCode);
		List list = this.find(hql.toString(), params.toArray());
		return (BigDecimal)list.get(0);
	}
	/**
	 * 删除所有表单明细
	 * @param billcode
	 * @throws Exception
	 */
	@Override
	public void delDetailByBillcode(String billcode) throws Exception{
		StringBuilder hql = new StringBuilder("delete BxAccountEnterDetail where billCode=?");
		List params = new ArrayList();
		params.add(billcode);
		this.bulkUpdate(hql.toString(), params.toArray());		
	}
	/**
	 * 删除所属表单主表
	 * @param billcode
	 * @throws Exception
	 */
	@Override
	public void delMainByBillcode(String billcode) throws Exception{		
		StringBuilder hql = new StringBuilder("delete BxAccountEnterMain where billCode=?");
		List params = new ArrayList();
		params.add(billcode);
		this.bulkUpdate(hql.toString(), params.toArray());		
	}/**
	 * 当申请表单删除时，恢复源数据
	 * @param billcode
	 * @throws Exception
	 */
	@Override
	public void reStoreSource(String billcode) throws Exception{
		StringBuilder hql = new StringBuilder("update Bx_Account_Enter_Detail d,Bx_Fee_Crash_Export e set e.invoice_Num=null where e.invoice_Num=d.invoice_Num and d.bill_Code=?");
		List<Object> params = new ArrayList();
		params.add(billcode);
		this.bulkUpdateJdbc(hql.toString(), params.toArray());	
	}	
	/**
	 * 保存主表单时，明细数据的总账日期和发票批名也需要修改
	 * @param mainVo
	 * @throws Exception
	 */
	public void batchUpdateDetail(AccountEnterMainVo mainVo) throws Exception{
		StringBuilder hql = new StringBuilder("update BxAccountEnterDetail set invoiceSnum=?,invoiceDate=?,sumFeeDate=? where billCode=?");
		List<Object> params = new ArrayList();
		params.add(mainVo.getInvoiceBatchNum());
		params.add(mainVo.getInvoiceDate());
		params.add(mainVo.getInvoiceDate());
		params.add(mainVo.getBillcode());
		this.bulkUpdate(hql.toString(), params.toArray());
	}
}
