<%@ page language="java"  contentType="text/html;"  pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/taglib/c-rt.tld" prefix="c"%>          
<%@ taglib uri="/WEB-INF/taglib/fmt-rt.tld"  prefix="fmt"%>  
 <%
 String basePath = request.getContextPath();
 %>
 <head>
	select2的使用心得：
	1、要使用select2必须引入select2的插件，即下面四个文件，如果项目中已经引入jquery就无需引入；
	2、select2的控件如果放在dialog的弹框中，此时select2会失效，需要修改最外层弹框的zIndex属性，这里的最外层指：如erp中，先点审批弹出审批页面,
	   再点审批页面中的确认财务科目，弹出修改财务科目的页面，select2运用在修改财务科目的页面，这里要修改zIndex,只要修改审批按钮的弹框属性即可，
	   erp项目中是改成了zIndex:100。
 	
        <link rel="stylesheet" href="<%=basePath%>/js/select/select2.min.css" type="text/css" />
	<script type="text/javascript" src="<%=basePath%>/js/select/jquery-1.11.1.min.js"></script>
 	<script type="text/javascript" src="<%=basePath%>/js/select/select2.min.js"></script>
 	<script type="text/javascript" src="<%=basePath%>/js/select/zh-CN.js"></script>

 </head>
 <div class="stock">
	<div class="stock-body">
		<div class="search_region">
	          公司段<input type="text" tabindex="3" id="workerNo" value="${vo.workNo}" readonly>   
	         <a class="basic-btn" href="#" onclick="addFsRow()">
				                   <span>新增</span>
				</a> 
			<!-- 用于判断是否要从后台去取数据  -->
			<input type="hidden" id="isInit" name="isInit" value="${isInit}"/>      		
		</div> 
		<!-- <form class="form-horizontal" id="bxFinanceSubjectBill" name="bxFinanceSubjectBill" method="post">  -->
		<div class="stock_info">     
			<table width="100%" class="trading_detail" id= "fsTable">
				 <tr>
				    <th>报销条目</th>
					<th>子目</th>
					<th>部门</th>
					<th>金额</th>
					<th>科目</th>
					<th>产品</th>
					<th>项目</th>
					<th>往来</th>	
					<th>备用</th>
					<th>报销说明</th>
					<th>操作</th>
				 </tr>
				 <c:forEach var="bxFsDetail" items="${list}">
					<tr  style="text-align:center;">
						<input type="hidden" name="fsId" value="${bxFsDetail.fsId}"/>
						<td>
							<!--select2被使用的地方-->	
							<select id="subSubjectName" name="subSubjectName" onchange="updateSubSubjectCode($(this));"> 
							</select> 
							<input type="hidden" style="width:50px;" name="fsSubSubjectName" value="${bxFsDetail.fsSubSubjectName}"/>
						</td>
						<td><input type="text" style="width:50px;" name="fsSubSubjectCode" value="${bxFsDetail.fsSubSubjectCode}"/></td>
						<td><input type="text" style="width:80px;" name="fsDeptCode" value="${bxFsDetail.fsDeptCode}"/></td>
						<td><input type="text" style="width:50px;" maxlength="30"  name="fsLineMoney" value="<fmt:formatNumber value='${bxFsDetail.fsLineMoney}' pattern='#0.00' type='number'/>"></td>
						<td><input type="text" style="width:50px;" name="fsSubjectCode" value="${bxFsDetail.fsSubjectCode}"/></td>
						<td><input type="text" style="width:50px;" name="fsProductCode" value="${bxFsDetail.fsProductCode}"/></td>
						<td><input type="text" style="width:50px;" name="fsProjectCode" value="${bxFsDetail.fsProjectCode}"/></td>
						<td><input type="text" style="width:50px;" name="fsCurrAccount" value="${bxFsDetail.fsCurrAccount}"/></td>
						<td><input type="text" style="width:50px;" name="fsRemark" value="${bxFsDetail.fsRemark}"/></td>
						<td><input type="text" style="width:50px;" name="fsLineDescribe" value="${bxFsDetail.fsLineDescribe}"/></td>
						<td><a style="opacity: 0.2">删除</a></td>										
					</tr>
					
				</c:forEach>          
			</table>
		</div>
		<!-- </form> -->
	</div>
</div> 
<script>
	$(document).ready(function() {
 		var $element_subSubjectName=$("select[name='subSubjectName']").select2({
		language : 'zh-CN',
		minimumInputLength : 1, //至少输入n个字符，才去加载数据
		maximumInputLength : 100,//限制最大字符，以防坑货
		placeholder : "选择报销条目",
		width : '200px',
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
	)
	
	function updateSubSubjectCode(currobj){
		var subSubjectNameKey = currobj.val();
		var subSubjectNameValue = currobj.text();
		currobj.parent().parent().find("input[name='fsSubSubjectCode']").val(subSubjectNameKey);
		currobj.parent().parent().find("input[name='fsSubSubjectName']").val(subSubjectNameValue);
	}
</script>
