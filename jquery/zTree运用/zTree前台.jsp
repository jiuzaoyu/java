<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/taglib/c-rt.tld"  prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/fmt-rt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/button.tld"  prefix="b"%>
  
<%String basePath = request.getContextPath();%>

<!doctype html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<title>畅游ERP系统</title>
	<jsp:include page="/common/common.jsp"></jsp:include>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script src="<%=basePath%>/js/common.js"></script>	
	<script src="<%=basePath%>/js/authority/authority_tree_checkbox.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/MonthDatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/artDialog4.1.7/jquery.artDialog.source.js?skin=aero"></script>
	<link rel="stylesheet" href="<%=basePath%>/js/select/select2.min.css" type="text/css" />
 	<script type="text/javascript" src="<%=basePath%>/js/select/select2.min.js"></script>
 	<script type="text/javascript" src="<%=basePath%>/js/select/zh-CN.js"></script> 
	<script src="<%=basePath%>/js/jquery.form.min.js"></script>
	<link rel="stylesheet" href="<%=basePath%>/js/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script src="<%=basePath%>/js/zTree/js/jquery.ztree.all-3.5.min.js"></script>
	<script src="<%=basePath%>/js/zTree/js/jquery.ztree.exhide-3.5.min.js"></script>
</head>
<body>
	<c:import url="/top.do?newpage=true&modelId=3" />
	
	<form class="form-horizontal" name="formOracle" id="formOracle" action="#" method="post">
	<div class="content clearfix">
		<div class="wrap">
	        <ul class="breadcrumb">
	            <li><a href="#">ERP</a> <span class="divider">&gt;</span></li>
	            <li><a href="#">报销系统</a> <span class="divider">&gt;</span></li>
	            <li><a href="#">ERP与Oracle接口</a> <span class="divider">&gt;</span></li>
	            <li class="active">ERP接口</li>
	        </ul>
			<div class="trading">
				<div class="box-title">
					<div class="caption">
						ERP接口
					</div>
				</div>
				<div class="clearfix config_content top_bd" style="height:100px;overflow:auto;">
					<table width="100%">
						<tr height="40px">
							<td>
								<label class="ml_2em"><font id="exportType" style="color:red">*</font>导出类型</label>
								<select id="fiType" name="fiType" style="width:155px" onchange="changeFiType(this.value);">
										<option value=""></option>
                            			<option value="FYBX">费用报销</option>
                            			<option value="BXDK">报销打款</option>
                            			<option value="CZBX">冲账报销</option>
                       			</select>
								<label class="ml_1em"><!-- <font id="exportPeoject" style="color:red">*</font> -->导出项目</label>
								<select id="exFlowType" name="exFlowType" style="width:155px">
										<option value=""></option>
                       			</select>
								<label class="ml_1em"><font id="exportCompany" style="color:red">*</font>导出公司</label>
								<select id="feeCompany" name="feeCompany" style="width:155px">
										<option value=""></option>
                            			<c:forEach items="${companyList}" var="company">
	                						<option value="${company.soleNum}">${company.itemName}</option>
	                					</c:forEach>
								</select>
								<label class="ml_1em"><font style="color:red">*</font>开始日期</label>
								<input type="text" name="startDate" id="startDate" style="cursor:pointer" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly">
								<label class="ml_1em"><font style="color:red">*</font>结束日期</label>
								<input type="text" name="endDate" id="endDate" style="cursor:pointer" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly">	
							</td>
						</tr>
						<tr height="40px">
							<td>
								<label class="ml_2em">&nbsp;表单号</label>
								<input name="formId" id="formId" type="text"/>
								<label class="ml_1em">财务初审姓名</label>
								<input name="cnName" id="cnName" type="text" maxlength="32"/>
								<label class="ml_1em">财务初审工号</label>
								<input name="cnWorkerNo" id="cnWorkerNo" type="text" maxlength="64"/>&nbsp;
								<input type="radio" name="status" id="status" value="CN" onClick="$('#status').val('CN')">财务初审已审核
								<input type="radio" name="status" id="status" value="CWZG" onClick="$('#status').val('CWZG')">财务复审已审核
								<input type="radio" name="status" id="status" value="preTrial" onClick="$('#status').val('preTrial')">财务冲账已修改
								<input type="radio" name="status" id="status" value="End" onClick="$('#status').val('End')"  checked="checked" >已结束
								<br/>
								<label class="">&nbsp;导出文件名</label>
								<input type="text" id="exportName" name="exportName"/>&nbsp;
								<input id="includeErrMsg" name="includeErrMsg" type="checkbox" />包含错误信息
								<label class="ml_1em"></label>
								<a class="basic-btn gray-btn" onclick="exportERPToOracleFTP()"><span>导出到服务器</span></a>
								<label class="ml_3em">
									<a class="basic-btn gray-btn" onclick="exportERPToOracle()"><span>导出到本地</span></a>
									<a class="basic-btn gray-btn" onclick="query()"><span>查询</span></a>
									<a class="basic-btn gray-btn" id="importOracle" name="importOracle" style="display: none;" onclick="importData2Oracle()"><span>导入oracle</span></a>
								</label>
							</td>
						</tr>
					</table>
				</div>
		   	</div>
		    <div scrolling="yes" width="100%">
		   		<iframe id="oracleListIframe" src="" width="100%" onload="changeIframeHight('oracleListIframe',30);" frameborder="0" scrolling="yes"></iframe>
			</div>
			<c:import url="/common/bottom.jsp" /> 
	</div>
	</form>
	
	<!-- 导入oracle -->
	<div id="importDataToOracleOutDiv" class="pop  hide" >		
		<form class="form-horizontal" method="post" action="<%=basePath%>/attendance/vacation/importPubckRecord.do" name="importDataToOracleForm" id="importDataToOracleForm" enctype="multipart/form-data">
			<div id="importDataToOracleInnerDiv" class="pop_body" style="height: 150px;">
	        	<table style="width: auto; border-collapse:separate; border-spacing:10px 10px;font-size:14px;">
	        		<br>
	        		<br>
	            	<tr style="padding-top: 10px;">
	            		<td>
	                	<label id="importOracleInfo" name="importOracleInfo" class="ml_2em" ></label>
	                	</td>
	                </tr>
	                <tr>
	                	<td class="tal" style="width: 500px;" >
							<label class="ml_1em">&nbsp;发票批名：</label>
							<input name="importInvoiceSnum" id="importInvoiceSnum" type="text"/>
							<label class="ml_1em">&nbsp;发票日期：</label>
							<input type="text" name="invoiceYearMonth" id="invoiceYearMonth" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="text input-mini" readonly >
	                 	</td>
	              	</tr>
	        	</table>
			</div>
		</form>
	</div>
	
	 <!--修改入账信息弹窗 -->
	<div class="pop  hide" id="enterInfo_pop" >         
		<div class="pop_body" id="enterInfo_pop" >
			<div class="trading_info">
				<div class="service_intro">
				    <!-- <div class="intro_item even_item">
				        <label for="" class="ml_1em">行金额</label>
				        <input type="text" name="updateLineMoney" id="updateLineMoney" />
				    </div> -->
				    <div class="intro_item even_item">
				        <label for="" class="ml_0em">公司段</label>
				        <input type="text" name="updateCompanyField" id="updateCompanyField" />
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">部门</label>
				        <input type="text" name="updateDeptCode" id="updateDeptCode" />
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">科目</label>
				        <input type="text" name="updateSubjectCode" id="updateSubjectCode" />
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_0em">子目名</label>
				        <select id="updateSubSubjectCodeName" name="updateSubSubjectCodeName" onchange="changeSubSubjectName($(this))">
						</select> 
						<a class="basic-btn gray-btn" onclick="iniSubSubjectTree()"><span>选择</span></a>
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">子目</label>
				        <input type="text" name="updateSubSubjectCode" id="updateSubSubjectCode"/>
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">产品</label>
				        <input type="text" name="updateProductCode" id="updateProductCode" />
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">项目</label>
				        <input type="text" name="updateProjectCode" id="updateProjectCode" />
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">往来</label>
				        <input type="text" name="updateCurrAccount" id="updateCurrAccount" />
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">备用</label>
				        <input type="text" name="updateBak" id="updateBak" />
				    </div>
				    <div class="intro_item even_item">
				        <label for="" class="ml_1em">备注</label>
				        <input type="text" name="updateRemark" id="updateRemark" />
				    </div>
				</div>
			</div>
		</div>          
	</div>	
	
	
	 <!--子目树弹框 -->
	<div class="pop  hide" id="subSubjectTree_pop" >         
		<div class="pop_body" id="subSubjectTree_pop" >
			<div class="trading_info">
				<div class="service_intro" >
				   <input type="text" name="queryTextTree" id="queryTextTree" />
				   <a class="basic-btn gray-btn" onclick="queryTree()"><span>搜索</span></a>
				</div>
				<div class="service_intro" style="height:366px;width:404px;overflow: auto;">
				   <ul id="zTreeSubSubject" class="ztree"></ul>
				</div>
			</div>
		</div>          
	</div>	
	
	
</body>

<script type="text/javascript">
var zTreeObj;
var hiddenNodes=[];	//用于存储被隐藏的结点

function queryTree(){
	//显示上次搜索后背隐藏的结点
	zTreeObj.showNodes(hiddenNodes);

	//查找不符合条件的叶子节点
	function filterFunc(node){
		var _keywords=$("#queryTextTree").val();
		if(node.isParent||node.name.indexOf(_keywords)!=-1) return false;
		return true;		
	};

	//获取不符合条件的叶子结点
	hiddenNodes=zTreeObj.getNodesByFilter(filterFunc);
	
	//隐藏不符合条件的叶子结点
	zTreeObj.hideNodes(hiddenNodes);
}

function iniSubSubjectTree(){
  
	var setting = {
	    	view: {
	            dblClickExpand: false,
	            showLine: true,
	            selectedMulti: false
	        },
	        check: {
	            enable: true,
	            chkStyle:"radio",
	            radioType:"all"
	        },
	      /*   async: {
	           enable: true,
	           url: "../bx/initSubSubjectTree.do",
	           autoParam:["id","chkDisabled"],
	           otherParam:{"zid":"-1"},
	           dataFilter: filter
	        }, */
	        data: {
	        	key: {
					name:"name",
					title: "name"
				},
	        	 simpleData: {
	        	 enable: true,
	        	 idKey: "id",
	        	 pIdKey: "pId",
	        	 rootPId: 0
	        	 }
	       	},
	        callback:{
	        	onCheck: zTreeOnClick
	        }
	};	
	
	$.ajax({
		 type: "POST",
		 url: "../bx/initSubSubjectTree.do",
		 success: function (data) {
			 if (data && data.length != 0) { //如果结果不为空
				 
				 zTreeObj = $.fn.zTree.init($("#zTreeSubSubject"), setting,eval("(" + data + ")"));
			 }
			 else { //搜索不到结果
				
			 }
		 }
	});
	//zTreeObj = $.fn.zTree.init($("#zTreeSubSubject"), setting);		  	
	
	art.dialog({
	    lock: true,
	    title:'子目树',
	    content:document.getElementById("subSubjectTree_pop"),	    
	    cancel: true,
	    fixed: true,
	    ok: function () {
	    	return zTreeOnClick;
	    }
	});
}

// 子目树节点单击或radioButton选中事件；
function zTreeOnClick(event, treeId, treeNode) {
	// 发起ajax请求获取选中的树节点的对应子目数据；
	$.ajax({
        url : "../bx/getSubSubjectByTreeNode.do",
		type : "POST",
		async : false,
		data : {
			itemId : treeNode.id
		},
		dataType : "json",
		cache : false,
		success : function(json) {
			var soleNum = json.soleNum;
			var itemName = json.itemName;
			var fieldStringFirst = json.fieldStringFirst;
			if(fieldStringFirst == ""){
				$("#updateSubSubjectCodeName").empty();
			}else{
				//先清空select
				$("#updateSubSubjectCodeName").empty();
				$("<option>").attr({
					value : soleNum+"_"+fieldStringFirst
				}).text(itemName).appendTo($("#updateSubSubjectCodeName"));
			}
		}
	});
	initSelect2();
};


function updateEnterInfo(id) {
	
	$.ajax({
		url : "<%=basePath%>/bx/getEnterInfo.do",
		type : "POST",
		async : true,
		dataType : "json",
		data : {
			id : id
		},
		cache : false,
		success : function(json) {
			var message = json.message;
			if (message!=null)
			{
				alert(message);
				return;
			}
			art.dialog({
			    lock: true,
			    title:'修改',
			    zIndex:100,
			    content:document.getElementById("enterInfo_pop"),	    
			    cancel: true,
			    fixed: true,
			    ok: function () {
			    	return excuteUpdate(id);
			    }
			});
			//$("#updateLineMoney").val(json.vo.lineMoney);    
			
			$("#updateCompanyField").val(json.vo.companyField);
			$("#updateDeptCode").val(json.vo.deptCode);
			$("#updateSubjectCode").val(json.vo.subjectCode);
			$("#updateSubSubjectCode").val(json.vo.subSubjectCode);
			$("#updateProductCode").val(json.vo.productCode);
			$("#updateProjectCode").val(json.vo.projectCode);
			$("#updateCurrAccount").val(json.vo.currAccount);
			$("#updateBak").val(json.vo.bak);
			$("#updateRemark").val(json.vo.remark);
			//先清空select
			$("#updateSubSubjectCodeName").empty();
			$("<option>").attr({
				value : json.vo.subSubjectNameCode+"_"+json.vo.subSubjectCode
			}).text(json.vo.subSubjectName).appendTo($("#updateSubSubjectCodeName"));
			initSelect2();
		}
	});
	
}

function initSelect2(){
	$("select[name='updateSubSubjectCodeName']").select2({
		language : 'zh-CN',
		minimumInputLength : 1, //至少输入n个字符，才去加载数据
		maximumInputLength : 100,//限制最大字符，以防坑货
		placeholder : "选择报销条目",
		width : '143px',
		allowClear : false,
		multiple : false,
		ajax : {
			url : "<%=basePath%>/bx/queryBxItem.do",
			dataType : "json",
			delay : 250,
			type:"POST",
			data : function(params) {
				return {
					term : params.term
				};
			},
			processResults : function(data, params) {
				var parsed = data;
				var arr = [];
				for ( var x in parsed) {
					arr.push(parsed[x]); //这个应该是个json对象
				}
				console.log(arr);
				return {
					results : arr
				};
			},
			cache : true
		}
	}).on("select2:select", function (e) {
		//
	});
}

function changeSubSubjectName(currobj){
	var subSubjectNameKey = currobj.val();
	var subSubjectNameValue = currobj.text();
	
	var strs= new Array(); //定义一数组
	strs=subSubjectNameKey.split("_");
	$("#updateSubSubjectCode").val(strs[1]);
}

//执行更新操作
function excuteUpdate(id) {
	//var updateLineMoney = $("#updateLineMoney").val();
	var updateCompanyField = $("#updateCompanyField").val();
	var updateDeptCode = $("#updateDeptCode").val();
	var updateSubjectCode = $("#updateSubjectCode").val();
	var updateSubSubjectCode = $("#updateSubSubjectCode").val();
	var updateProductCode = $("#updateProductCode").val();
	var updateProjectCode = $("#updateProjectCode").val();
	var updateCurrAccount = $("#updateCurrAccount").val();
	var updateBak = $("#updateBak").val();
	var updateRemark = $("#updateRemark").val();
	var subSubjectNameKey = $("#updateSubSubjectCodeName option:selected").val();
	var subSubjectNameValue = $("#updateSubSubjectCodeName option:selected").text();
	
	var strs= new Array(); //定义一数组
	strs=subSubjectNameKey.split("_");
	
	/* if(updateLineMoney==null || updateLineMoney=="") {
		alert_art("行金额不能为空!","warning");
		return false;
	} */
	if(updateCompanyField==null || updateCompanyField=="") {
		alert_art("公司段不能为空!","warning");
		return false;
	}
	if(updateDeptCode==null || updateDeptCode=="") {
		alert_art("部门不能为空!","warning");
		return false;
	}
	if(updateSubjectCode==null || updateSubjectCode=="") {
		alert_art("科目不能为空!","warning");
		return false;
	}
	if(subSubjectNameValue == null || subSubjectNameValue.trim() == ""){
		alert_art("子目名不能为空!","warning");
		return false;
	}
	if(updateSubSubjectCode==null || updateSubSubjectCode=="") {
		alert_art("子目不能为空!","warning");
		return false;
	}
	if(updateProductCode==null || updateProductCode=="") {
		alert_art("产品不能为空!","warning");
		return false;
	}
	if(updateProjectCode==null || updateProjectCode=="") {
		alert_art("项目不能为空!","warning");
		return false;
	}
	if(updateCurrAccount==null || updateCurrAccount=="") {
		alert_art("往来不能为空!","warning");
		return false;
	}
	if(updateBak==null || updateBak=="") {
		alert_art("备用不能为空!","warning");
		return false;
	}
	if(updateRemark==null || updateRemark=="") {
		alert_art("备注不能为空!","warning");
		return false;
	}
	
	$.ajax({
		url : "<%=basePath%>/bx/excuteUpdateEnterInfo.do",
		type : "POST",
		async : true,
		dataType : "json",
		data : {
			id : id,
			//lineMoney : updateLineMoney,
			companyField : updateCompanyField,
			deptCode:updateDeptCode,
			subjectCode: updateSubjectCode,
			subSubjectNameCode:strs[0],
			subSubjectName:subSubjectNameValue,
			subSubjectCode:updateSubSubjectCode,
			productCode:updateProductCode,
			projectCode:updateProjectCode,
			currAccount:updateCurrAccount,
			bak:updateBak,
			remark:updateRemark
		},
		cache : false,
		success : function(json) {
		    var message = json.message;
		    if (message != "ok") {
		       alert_art(message,"warning");
		    } else {
		       query();
            }					
	    }
	});
}




function checkItems(){
	// 导出类型
	var fiType = $("#fiType").val();
//	// 导出项目
//	var exFlowType = $("#exFlowType").val();
	// 导出公司
	var feeCompany = $("#feeCompany").val();
	// 开始时间
	var startDate = $("#startDate").val();
	// 结束时间
	var endDate = $("#endDate").val();
	// 表单号
	var formId = $("#formId").val();
	// 状态
	// var status = $("#status").val();
	var status = $("input[name='status']:checked").val();
	
	// 导出文件名
	var exportName = $("#exportName").val();
	if(""==fiType){
	 alert_art("请选择导出类型!","warning");
	 return false;
	}
//	if(""==exFlowType){
//	 alert_art("请选择导出项目!","warning");
//	 return false;
//	}
	if(""==feeCompany){
	 alert_art("请选择导出公司!","warning");
	 return false;
	}
	if(""==startDate){
	 alert_art("请选择开始时间!","warning");
	 return false;
	}
	if(""==endDate){
	 alert_art("请选择结束时间!","warning");
	 return false;
	}
	if(startDate>endDate){
	 alert_art("开始时间不能大于结束时间","warning");
	 return false;
	}
	return true;
}

// 根据导出类型关联导出项目
function changeFiType(fiType){
	if("" == fiType){
		$("#exFlowType").attr('disabled',false);
		$("#importOracle").hide();
		$("#exFlowType").html("");
	}
	// 费用报销
	else if("FYBX" == fiType){
	$("#exFlowType").attr('disabled',false);
	$("#importOracle").show();
	$("#exFlowType").html("<option value=\"\"></option>" + 
			                  "<option value="+'RCApply'+">"+'日常费用报销'+"</option>"+
							  "<option value="+'EBApply'+">"+'员工福利报销'+"</option>"+
							  "<option value="+'TBApply'+">"+'TB费用报销'+"</option>"+
							  "<option value="+'CRApply'+">"+'员工关怀报销'+"</option>"+
							  "<option value="+'HRApply'+">"+'HR招聘相关报销'+"</option>"+
							  "<option value="+'CLApply'+">"+'差旅费用报销'+"</option>"+
							  "<option value="+'BuyDeviceApply'+">"+'设备采买费用报销'+"</option>"+
							  "<option value="+'QTApply'+">"+'其他费用报销'+"</option>");
	
	}
	// 报销打款
	else if("BXDK" == fiType){
	$("#exFlowType").attr('disabled',false);
	$("#importOracle").hide();
	$("#exFlowType").html("<option value=\"\"></option>" +
			                  "<option value="+'RCApply'+">"+'日常费用报销打款'+"</option>"+
							  "<option value="+'EBApply'+">"+'员工福利报销打款'+"</option>"+
							  "<option value="+'TBApply'+">"+'TB费用报销打款'+"</option>"+
							  "<option value="+'CRApply'+">"+'员工关怀报销打款'+"</option>"+
							  "<option value="+'HRApply'+">"+'HR招聘相关报销打款'+"</option>"+
							  "<option value="+'CLApply'+">"+'差旅费用报销打款'+"</option>"+
							  "<option value="+'BuyDeviceApply'+">"+'设备采买费用报销打款'+"</option>"+
							  "<option value="+'QTApply'+">"+'其他费用报销打款'+"</option>");
	}
	// 冲账报销
	else if("CZBX" == fiType){
	$("#exFlowType").html("");
	$("#exFlowType").attr('disabled',true); 
	$("#importOracle").show();
	}
}

// 查询
function query(){
	if(checkItems()){
		var srcUrl = "<%=basePath%>/bx/queryOracle.do?fiType=" + $("#fiType").val();
		srcUrl += "&exFlowType="+$("#exFlowType").val();
		srcUrl += "&feeCompany="+$("#feeCompany").val();
		srcUrl += "&startDate="+$("#startDate").val();
		srcUrl += "&endDate="+$("#endDate").val();
		srcUrl += "&formId="+$("#formId").val();
		srcUrl += "&status="+$("input[name='status']:checked").val();
		srcUrl += "&exportName="+$("#exportName").val();
		srcUrl += "&cnName="+$("#cnName").val();
		srcUrl += "&cnWorkerNo="+$("#cnWorkerNo").val();
		srcUrl += "&includeErrMsg="+$("#includeErrMsg").is(':checked');
		
		
		
	    $('#oracleListIframe').attr('src', srcUrl);
	}
}

// 导出
function exportERPToOracle(){
	var includeErrMsg = $("#includeErrMsg").is(':checked');
	if(checkItems()){
		document.formOracle.action="<%=basePath%>/bx/exportErpToOracle.do?includeErrMsg="+includeErrMsg+"&startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val();
		document.formOracle.submit();
	}
}

// 导出到服务器
function exportERPToOracleFTP(){
var exportName = $("#exportName").val();
	if(checkItems()){
	if(""==exportName){
	 alert_art("请输入导出文件名!","warning");
	 return false;
	}
	
	$("#formOracle").ajaxSubmit({
		 type:'POST',
		 dataType:'html',
    	 url:"<%=basePath%>/bx/exportErpToOracleFTP.do?startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val(),
  	     success:function (data){
  	     if(data=="error"){
  	     	alert_art("无法连接FTP服务器，请联系管理员!","error");
  	     }else{
  	     	alert_art("文件:"+data+"已导出!","succeed");
  	     }
  	     }
  	});
	}
}

//导入oracle到ebs
function importData2Oracle(){
	if(checkItems()){
		var status = $("input[name='status']:checked").val();
		if(status != "End"){
			alert_art("请选择已结束，其他类型不允许导入oracle！","warning");
			return;
		}
		var fiType = $("#fiType").val();
		var feeCompany = $("#feeCompany").val();
		var exFlowType = $("#exFlowType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var formId = $("#formId").val();
		var exportName = $("#exportName").val();
		var cnName = $("#cnName").val();
		var cnWorkerNo = $("#cnWorkerNo").val();
	
		$.ajax({
			url : "<%=basePath%>/bx/queryImportOracleInfo.do",
			type : "POST",
			async : false,
			dataType : "html",
			data : {
				fiType:fiType,
				feeCompany:feeCompany,
				exFlowType:exFlowType,
				startDate:startDate,
				endDate:endDate,
				formId:formId,
				exportName:exportName,
				cnName:cnName,
				cnWorkerNo:cnWorkerNo,
				status:status
			},
			cache : false,
			success : function(html) {
				$("#importOracleInfo").html(html);
			}
		});	
		
		art.dialog({
		    lock: true,
		    title:'导入数据到Oracle',
		    content:document.getElementById("importDataToOracleOutDiv"),	    
		    cancel: true,
		    fixed: true,
		    ok: function(){
		    	return excuteImportData();
		    },
			close : function(){
				$("#importInvoiceSnum").val("");
				$("#invoiceYearMonth").val("");
			}
		});	
		
	}
}
	
	function excuteImportData(){
		var fiType = $("#fiType").val();
		var feeCompany = $("#feeCompany").val();
		var exFlowType = $("#exFlowType").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var formId = $("#formId").val();
		var exportName = $("#exportName").val();
		var cnName = $("#cnName").val();
		var cnWorkerNo = $("#cnWorkerNo").val();
		var status = $("input[name='status']:checked").val();
		var importInvoiceSnum = $("#importInvoiceSnum").val();
		var invoiceYearMonth = $("#invoiceYearMonth").val();
		
		if(importInvoiceSnum == "" || importInvoiceSnum == null){
			alert_art("发票批名不能为空!","warning");
			return false;
		}
		
		if(invoiceYearMonth == "" || invoiceYearMonth == null){
			alert_art("发票日期不能为空!","warning");
			return false;
		}
		
		$.ajax({
			url : "<%=basePath%>/bx/excuteImportOracle.do",
			type : "POST",
			async : true,
			dataType : "json",
			data : {
				fiType:fiType,
				feeCompany:feeCompany,
				exFlowType:exFlowType,
				startDate:startDate,
				endDate:endDate,
				formId:formId,
				exportName:exportName,
				cnName:cnName,
				cnWorkerNo:cnWorkerNo,
				status:status,
				importInvoiceSnum:importInvoiceSnum,
				invoiceYearMonth:invoiceYearMonth
			},
			cache : false,
			success : function(json) {
				var status = json.flag;
				if(status == "true"){
					alert_art(json.info,"warning");
				}
		  	}
		});
		
	}


</script>

</html>