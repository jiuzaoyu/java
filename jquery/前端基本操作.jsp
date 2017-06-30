<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/taglib/c-rt.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/fmt-rt.tld"  prefix="fmt"%>
<%String basePath = request.getContextPath();%>

<style type="text/css">
textarea
{
	resize:none;
}
</style>

<script type="text/javascript">

	function billSave(_path) {
		if (!checkForm()) {
			return false;
		}
		document.bill.action = _path;
		document.bill.submit();
	}
	
	function checkForm() {
		if($("#ifStrike").val()=='yes'){
			if ($("#strikeSum").val()=='' || Number($("#strikeSum").val())<=0) {
				$("#strikeSum").focus();
				alert_art("冲账金额必须大于0","warning");
				return false;
			}
		}
		if (Number($(":input[name='voucherMoney']").val()) < Number($(":input[name='strikeSum']").val())) {
			alert_art("报销金额不能小于冲账金额","warning");
			return false;
		}
		return true;
	}
	
    // 冲账选择
    function selIfStrike() {
    	var option = $(":input[name='ifStrike'] option:selected").val();
    	var voucherMoney = Number($.trim($("#voucherMoney").val()));
    	if(option == 'yes') {    		    		
    		$("#strikeSum").attr("readonly", false);
    		
    		var strikeSumOriginal = $("#strikeSumOriginal").val();
    		if(!$.isNumeric(strikeSumOriginal)){
    			$("#strikeSum").val("0");
    		} else {
    			  $("#strikeSum").val(strikeSumOriginal);
    		}
    		$("#realSum").val((voucherMoney - Number($("#strikeSum").val())).toFixed(2));
    		isEditByRealSum();
    	}
    	else {
    		$("#strikeSum").attr("readonly", true);
    		// 冲账重置
    		$("input[name='strikeSum']").val(0);
    		$("#strikeSum").blur();
    		$("#realSum").val(voucherMoney.toFixed(2));
    		isEditByRealSum();
    	}
    }
    
    function cascadeSetAmt(){
    	//debug();
    	var voucherMoneyStr = $.trim($("#voucherMoney").val());
    	if($.isNumeric(voucherMoneyStr)){
    		var voucherMoney = Number(voucherMoneyStr);
    		var voucherMoneyOriginal = Number($("#voucherMoneyOriginal").val());
    		var strikeSumStr = $.trim($("#strikeSum").val());
    		/* if((voucherMoney > voucherMoneyOriginal) || (voucherMoneyOriginal - voucherMoney > 1)){
    			$("#voucherMoney").val($("#voucherMoneyOriginal").val());
    			$("#realSum").val((voucherMoneyOriginal - Number(strikeSumStr)).toFixed(2));
    			$("#opinion").val("");
    			//alert_art("报销金额的核减范围必须在1以内!","warning");
    			return false;
    		} */
    		$("#voucherMoney").val(voucherMoney.toFixed(2));
    		if(strikeSumStr){
    			var strikeSum = Number(strikeSumStr);
        		$("#realSum").val((voucherMoney - strikeSum).toFixed(2));
    		}else{
    			$("#realSum").val(voucherMoney.toFixed(2));
    		}
    		if(voucherMoney != voucherMoneyOriginal){
    			$("#opinion").val($("#opinion").val() + "&nbsp;报销金额从" + voucherMoneyOriginal + "减少到" + voucherMoney);
    		}
    		return true;
    	}else{
    		$("#voucherMoney").val($("#voucherMoneyOriginal").val());
    		$("#opinion").val("");
    		alert_art("报销金额必须是数字!","warning");
    		return false;
    	}
    }
    
    function validateStrike() {
    	//debug();
    	var strikeSumStr = $.trim($("#strikeSum").val());
    	var ifStrikeOriginal = $("#ifStrikeOriginal").val();        
        var strikeSumOriginal = Number($("#strikeSumOriginal").val());                          
        var voucherMoney = Number($("#voucherMoney").val());
        var realSum =  Number($("#realSum").val());
        if(realSum<0){
        	alert_art("打款币种总额不能小于0!","warning");
        	return false;
        }
    	if ($.isNumeric(strikeSumStr)) {
    		var strikeSum = Number(strikeSumStr);
            var ifStrike = $("#ifStrike").val();            
            if (ifStrike == "yes") {
                if (strikeSum <= 0 || strikeSum > voucherMoney) {
                	$("#ifStrike").val(ifStrikeOriginal);
                	$("#strikeSum").val(strikeSumOriginal);
                    $("#opinion").val("");                    
                    $("#strikeSum").attr("readonly", ifStrikeOriginal=="yes" ? false : true);
                    alert_art("冲账金额必须大于0且不能大于报销金额!","warning");
                    return false;
                }else{
                	 $("#strikeSum").val(strikeSum.toFixed(2));
	                $("#realSum").val((voucherMoney - strikeSum).toFixed(2));
	                /* if (strikeSum != strikeSumOriginal) {
	                    $("#opinion").val($("#opinion").val() + "&nbsp;冲账金额从" + strikeSumOriginal + "变为" + strikeSum);
	                } */
	                return true;
                }                
               
	         } else {
	             if (strikeSum > 0) {
	            	 $("#ifStrike").val(ifStrikeOriginal);
	            	 $("#strikeSum").val(strikeSumOriginal);
	                 $("#opinion").val("");                    
	                 $("#strikeSum").attr("readonly", ifStrikeOriginal=="yes" ? false : true);
	                 alert_art("冲账金额必须等于0!","warning");
	                 return false;
	             }else{
	             	$("#realSum").val((voucherMoney - strikeSum).toFixed(2));
		             /* if (strikeSum != strikeSumOriginal) {
		                    $("#opinion").val($("#opinion").val() + "&nbsp;冲账金额从" + strikeSumOriginal + "变为" + strikeSum);
		             } */
		             return true;
	             }	             
	         }            
    	} else {
    		$("#ifStrike").val(ifStrikeOriginal);
    		$("#strikeSum").val($("#strikeSumOriginal").val());
            $("#opinion").val("");
            $("#strikeSum").attr("readonly", ifStrikeOriginal=="yes" ? false : true);
            alert_art("冲账金额必须是数字!","warning");
            return false;
    	}
    }
    
    function strikeSumChange() {
    	var strikeSumStr = $.trim($("#strikeSum").val());
    	var strikeSumOriginal = Number($("#strikeSumOriginal").val());
    	var ifStrikeOriginal = $("#ifStrikeOriginal").val();                                          
        var voucherMoney = Number($("#voucherMoney").val());
        if ($.isNumeric(strikeSumStr)) {
            var strikeSum = Number(strikeSumStr);
            var ifStrike = $("#ifStrike").val();            
            if (ifStrike == "yes") {
                if (strikeSum <= 0 || strikeSum > voucherMoney) {
                    $("#ifStrike").val(ifStrikeOriginal);
                    $("#strikeSum").val(strikeSumOriginal);            
                    $("#strikeSum").attr("readonly", ifStrikeOriginal=="yes" ? false : true);
                    //$("#voucherMoney").val(voucherMoneyOriginal);
                    $("#realSum").val($("#realSumOriginal").val());
                    alert_art("冲账金额必须大于0且不能大于报销金额!","warning");
                    return;
                }                
                $("#strikeSum").val(strikeSum.toFixed(2));
                $("#realSum").val((voucherMoney - strikeSum).toFixed(2));     
                if(Number($("input[name='realSum']").val())<=0 || $("input[name='voucherMoney']").val()<=3000){
					$("input[name='prUseAmount']").each(function() {
						$(this).val("0");
						$(this).attr("readonly","readonly");
					});
				}else{
					$("input[name='prUseAmount']").each(function() {
						$(this).removeAttr("readonly");
					});
				}           
                return;
             } else {
                 if (strikeSum > 0) {
                     $("#ifStrike").val(ifStrikeOriginal);
                     $("#strikeSum").val(strikeSumOriginal);                  
                     $("#strikeSum").attr("readonly", ifStrikeOriginal=="yes" ? false : true);
                     $("#realSum").val($("#realSumOriginal").val());
                     alert_art("冲账金额必须等于0!","warning");
                     return;
                 }               
                 $("#realSum").val((voucherMoney - strikeSum).toFixed(2)); 
                 if(Number($("input[name='realSum']").val())<=0 || $("input[name='voucherMoney']").val()<=3000){
					$("input[name='prUseAmount']").each(function() {
						$(this).val("0");
						$(this).attr("readonly","readonly");
					});
				}else{
					$("input[name='prUseAmount']").each(function() {
						$(this).removeAttr("readonly");
					});
				}               
                 return;
             }            
        } else {
            $("#ifStrike").val(ifStrikeOriginal);
            $("#strikeSum").val($("#strikeSumOriginal").val());
            $("#strikeSum").attr("readonly", ifStrikeOriginal=="yes" ? false : true);
            //$("#voucherMoney").val(voucherMoneyOriginal);
            $("#realSum").val($("#realSumOriginal").val());
            alert_art("冲账金额必须是数字!","warning");
            return;
        }
    }
    
    function voucherMoneyChange() {    
    	var voucherMoneyStr = $.trim($("#voucherMoney").val());
        if($.isNumeric(voucherMoneyStr)){
            var voucherMoney = Number(voucherMoneyStr);
            var voucherMoneyOriginal = Number($("#voucherMoneyOriginal").val());
            var strikeSumStr = $.trim($("#strikeSum").val());
            /* if((voucherMoney > voucherMoneyOriginal) || (voucherMoneyOriginal - voucherMoney > 1)){
                $("#voucherMoney").val($("#voucherMoneyOriginal").val());
                //$("#realSum").val((voucherMoneyOriginal - Number(strikeSumStr)).toFixed(2));
                $("#realSum").val($("#realSumOriginal").val());
                //$("#opinion").val("");
               // alert_art("报销金额的核减范围必须在1以内!","warning");
                return;
            } */
            $("#voucherMoney").val(voucherMoney.toFixed(2));
            if(strikeSumStr){
                var strikeSum = Number(strikeSumStr);
                $("#realSum").val((voucherMoney - strikeSum).toFixed(2));
            }else{
                $("#realSum").val(voucherMoney.toFixed(2));
            }
            //if(voucherMoney != voucherMoneyOriginal){
               // $("#opinion").val($("#opinion").val() + "&nbsp;报销金额从" + voucherMoneyOriginal + "减少到" + voucherMoney);
            //}
            return;
        }else{
            $("#voucherMoney").val($("#voucherMoneyOriginal").val());
            $("#realSum").val($("#realSumOriginal").val());
            //$("#opinion").val("");
            alert_art("报销金额必须是数字!","warning");
            return;
        }
    }
    function checkPR(){
    	var totalSum = 0;
    	var returnVal = true;
    	if($("input[name='prUseAmount']").length>0){
    		$("input[name='prUseAmount']").each(function() {
    			if(Number($(this).val())>Number($(this).parent().next().children("input[id='prSurplusAmount']").val())){
    				alert_art("本次使用金额大于可用金额,请重新填写","warning");
					$(this).val("0");
					returnVal = false;
    				return false;
    			}else{
    				totalSum = totalSum*1 + $(this).val()*1;
    			}
    		})
    	}
    	if(returnVal == false){
    		return false;
    	}
    	
    	if(Number($(":input[name='voucherMoney']").val())>3000){
	    	if(Number($(":input[name='realSum']").val())!=Number(totalSum)){
				alert_art("PR单的本次使用总金额不等于打款币种总额,请重新填写","warning");
				returnVal = false;
				return false;
			}
    	}
    	if(returnVal == false){
    		return false;
    	}
    	return true;
    }
    $("input[name='commonMoneyD']").blur(function(){
   		var totalMoney = $("#totalMoney").val();
   		var sum = 0;
		$("input[name='commonMoneyD']").each(function() {
			var num = Number($(this).val());
			var gup = new RegExp(/^\d*[\.]?\d+$/);
			if (gup.test(num)) {
				sum += num;
			}
		});
		if(Number(totalMoney) < Number(sum)){
			alert_art("报销金额大于原有金额!","warning");
			$("input[name='voucherMoney']").val(sum.toFixed(2));
			var strikeSum =  $("input[name='strikeSum']").val();
			$("input[name='realSum']").val((sum.toFixed(2) - strikeSum).toFixed(2));
			return;
		}else{
			$("input[name='voucherMoney']").val(sum.toFixed(2));
			var strikeSum =  $("input[name='strikeSum']").val();
			$("input[name='realSum']").val((sum.toFixed(2) - strikeSum).toFixed(2));
		}
		if(Number($("input[name='realSum']").val())<=0 || $("input[name='voucherMoney']").val()<=3000){
			$("input[name='prUseAmount']").each(function() {
				$(this).val("0");
				$(this).attr("readonly","readonly");
			});
		}else{
			$("input[name='prUseAmount']").each(function() {
				$(this).removeAttr("readonly");
			});
		}		
		
   	});
    
    
    //根据打款币种总额是否大于0判断本次使用金额是否可编辑--by zwj
    function isEditByRealSum(){
    	//判断打款币种总额是否大于0，然后修改本次使用金额的只读状态--by zwj start
        if(Number($("input[name='realSum']").val())<=0){
			$("input[name='prUseAmount']").each(function() {
				$(this).val("0");
				$(this).attr("readonly","readonly");
			});
		}else{
			$("input[name='prUseAmount']").each(function() {
				$(this).removeAttr("readonly");
			});
		}
        //end
    }
    
    
    //驳回的时候把报销金额和冲账金额的变化写进审批意见中--by zwj
    function rejectAddChangeOpinion(){
  		//驳回的时候只要报销金额和冲账金额发生了变化，就把变化写到申辩意见中--by zwj
   		if(Number($("#totalMoney").val()) != Number($("input[name='voucherMoney']").val()) && $("#opinion").val().indexOf("报销金额从")<0){
  			$("#opinion").val($("#opinion").val() + "&nbsp;报销金额从" + Number($("#totalMoney").val()) + "变为" + Number($("input[name='voucherMoney']").val()));
  		}
  		if(Number($("#strikeSumOriginal").val()) != Number($("input[name='strikeSum']").val()) && $("#opinion").val().indexOf("冲账金额从")<0){
  			$("#opinion").val($("#opinion").val() + "&nbsp;冲账金额从" + Number($("#strikeSumOriginal").val()) + "变为" + Number($("input[name='strikeSum']").val()));
  		}
    }
    
    //确认财务科目
    function confirmFinanceSubject(billCode){
	  art.dialog({
			lock: true,
			title:'修改财务科目',
			content:document.getElementById("financeSubject"),	    
			fixed: true,
			init: function (){
				initFinanceSubject("<%=basePath%>",$("#financeSubject"),billCode);
 			},
	  		ok: function (){
	  			<%-- $.ajax({
					url : "<%=basePath%>/bx/saveBxFinanceSubjectInfo.do",
					type : "POST",
					async : true,
					dataType : "json",
					data : {
						
					},
					cache : false,
					success : function(json) {
						var message = json.message;
						alert_art(message,"warning");
					}
					}); --%>
				<%-- ajaxSubmit("bxFinanceSubjectBill","<%=basePath%>/bx/saveBxFinanceSubjectInfo.do?billCode="+billCode,"save"); --%>
				var fsSubSubjectName = $(":input[name='fsSubSubjectName']");
				var fsSubSubjectCode = $(":input[name='fsSubSubjectCode']");
				var fsDeptCode = $(":input[name='fsDeptCode']");
				var fsLineMoney = $(":input[name='fsLineMoney']");
				var fsSubjectCode = $(":input[name='fsSubjectCode']");
				var fsProductCode = $(":input[name='fsProductCode']");
				var fsProjectCode = $(":input[name='fsProjectCode']");
				var fsCurrAccount = $(":input[name='fsCurrAccount']");
				var fsRemark = $(":input[name='fsRemark']");
				var fsLineDescribe = $(":input[name='fsLineDescribe']");
				for(var i=0;i<fsSubSubjectCode.length;i++)
				{
					if(fsSubSubjectName[i].value.trim()==""||fsSubSubjectName[i].value.trim()==null)
					{
						alert("存在报销条目为空！");
						return false;
					}
					if(fsSubSubjectCode[i].value.trim()==""||fsSubSubjectCode[i].value.trim()==null)
					{
						alert("存在子目为空！");
						return false;
					}
					if(fsDeptCode[i].value.trim()==""||fsDeptCode[i].value.trim()==null)
					{
						alert("存在部门为空！");
						return false;
					}
					if(fsLineMoney[i].value.trim()==""||fsLineMoney[i].value.trim()==null)
					{
						alert("存在金额为空！");
						return false;
					}
					if(fsSubjectCode[i].value.trim()==""||fsSubjectCode[i].value.trim()==null)
					{
						alert("存在科目为空！");
						return false;
					}
					if(fsProductCode[i].value.trim()==""||fsProductCode[i].value.trim()==null)
					{
						alert("存在产品为空！");
						return false;
					}
					if(fsProjectCode[i].value.trim()==""||fsProjectCode[i].value.trim()==null)
					{
						alert("存在项目为空！");
						return false;
					}
					if(fsCurrAccount[i].value.trim()==""||fsCurrAccount[i].value.trim()==null)
					{
						alert("存在往来为空！");
						return false;
					}
					if(fsRemark[i].value.trim()==""||fsRemark[i].value.trim()==null)
					{
						alert("存在备用为空！");
						return false;
					}
					if(fsLineDescribe[i].value.trim()==""||fsLineDescribe[i].value.trim()==null)
					{
						alert("存在报销说明为空！");
						return false;
					}
				}
	  		},
	  		cancel: true
		});
    }
    
    //初始化财务科目弹出框
   	function initFinanceSubject(basePath,setObject,billCode)
	{
    	//获取影响报销科目的数据
    	var corporationId = $("#corporationId").val();//费用发生公司
    	var jtItem = []; //交通条目
    	var jtMoney = [];//交通金额
    	if($("select[name='jtItemD']").length>0){
			$("select[name='jtItemD']").each(function() {
				jtItem.push($(this).val());
				jtMoney.push(this.parent().parent().find("input[name='jtMoney']").val());
			});
		}
    	//var jtMoney = $(":input[name='jtMoney']").val();
    	alert(corporationId);
    	alert(jtItem);
    	alert(jtMoney);
    	return;
    	
   		var isInit = $("#isInit").val();
   		if(isInit == "true"){
   			return;
   		}
		$.ajax({
			url : basePath+"/bx/showBxFinanceSubject.do",
			type : "POST",
			async : true,
			dataType : "html",
			data : {
				billCode : billCode
			},
			cache : false,
			success : function(html) {
				setObject.html(html);
			}
		});
		
	}
</script>
 
	<div class="intro_item even_item">
	<table>
		<tr>
			<td> 
				<label for="country" class="ml_2em">报销金额</label>
		        <input onchange="voucherMoneyChange();" id="voucherMoney" class="text input-180" type="text" name="voucherMoney" value="<fmt:formatNumber value='${bav.voucherMoney}' pattern='#0.00' type='number'/>" readonly="readonly" />
		        <input id="isCN" name="isCN" type="hidden" value="${bav.isCN }"/>
		        <input id="voucherMoneyOriginal" name="voucherMoneyOriginal" type="hidden" value="<fmt:formatNumber value='${bav.voucherMoneyOriginal }' pattern='#0.00' type='number'/>" />
		        <input type="hidden" id="totalMoney" value="<fmt:formatNumber value='${bav.voucherMoney}' pattern='#0.00' type='number'/>">
			</td>
			<td>
				<label for="country" class="ml_2em">是否冲账</label>
		        <select id="ifStrike" name="ifStrike" class="select" onchange="selIfStrike();" <c:if test="${bav.isCN != 'Y'}">disabled</c:if>>
			        <c:set var="IFSTRIKE_YES" value="yes"/>
			        <c:choose>
				    	<c:when test="${bav!=null && bav.ifStrike==IFSTRIKE_YES}">
	               			<option value="yes" selected>是</option>
	              				<option value="no">否</option>
	               		</c:when>
	               		<c:otherwise>
	               			<option value="yes">是</option>
	              				<option value="no" selected>否</option>
	               		</c:otherwise>
	           		</c:choose>
		        </select>
		        <c:if test="${bav.isCN != 'Y'}">
		        	<input id="ifStrike" name="ifStrike" type="hidden" value="${bav.ifStrike }"/>
		        </c:if>
		        <input id="isPreTrial" name="isPreTrial" type="hidden" value="${bav.isPreTrial }"/>
		        <input id="ifStrikeOriginal" name='ifStrikeOriginal' type="hidden" value="${bav.ifStrikeOriginal}" />
		         <!-- 
		        <script>
					selIfStrike();
			    </script> -->
			</td>
			<td> 
				<label for="country" class="ml_2em">冲账金额</label>
				<c:choose>
					<c:when test="${bav.isCN == 'Y'&& bav.ifStrike==IFSTRIKE_YES}">
						<input onchange="strikeSumChange();" id="strikeSum" class="text input-180"  type="text"  name="strikeSum"  value="<fmt:formatNumber value='${bav.strikeSum}' pattern='#0.00' type='number'/>"/>
					</c:when>
					<c:otherwise>
						<input onchange="strikeSumChange();" id="strikeSum" class="text input-180"  type="text"  name="strikeSum"  value="<fmt:formatNumber value='${bav.strikeSum}' pattern='#0.00' type='number'/>" <c:if test="${bav.isCN != 'Y'}">readonly="readonly"</c:if> />
					</c:otherwise>
				</c:choose>
				<input id="strikeSumOriginal" class="text input-180"  type="hidden"  name="strikeSumOriginal"  value="<fmt:formatNumber value='${bav.strikeSumOriginal}' pattern='#0.00' type='number'/>" />
			</td>		
		</tr>
	</table>
	</div>
	<div class="intro_item even_item">
    <table>
		<tr>
			<td> 
				<label for="country" class="ml_0em">打款币种总额</label>
				<input id="realSum" class="text input-180"  type="text" name="realSum" value="<fmt:formatNumber value='${bav.realSum}' pattern='#0.00' type='number'/>" readonly="readonly" />
				<input id="realSumOriginal" class="text input-180"  type="hidden" name="realSumOriginal" value="<fmt:formatNumber value='${bav.realSum}' pattern='#0.00' type='number'/>"  />
			</td>
		   <td colspan="2"> 
        		<c:choose>
			    	<c:when test="${bav!=null && bav.prNum!=null}">
               			<label for="country" class="ml_0em"><span style='color:red'>历史PR单:${bav.prNum}</span></label>
               		</c:when>
               		<c:otherwise>
               			
               		</c:otherwise>
           		</c:choose>
       		</td>
       		<c:if test="${bav.isCN == 'Y'}">
       		<td align="right">
       			<c:if test="${showBfs == 'yes'}">
	       			<a class="basic-btn" href="#" onclick="confirmFinanceSubject('${bav.billCode}')">
					                   <span>请确认财务科目</span>
					</a>
				</c:if>
       		</td>
       		</c:if>
		</tr>
	</table>
	</div>
	<!-- 确认财务科目弹窗 -->
	<div class="hide" id="financeSubject"  style="height:650px;width:1000px;overflow:auto;">
	</div>
	<c:import url="/bx/common/pr_view_common.do" /> 
	<c:import url="/bx/common/loan_view_common.do" /> 
	<div class="intro_item even_item">
	<table>
		<tr>
			<td> 
				<label for="country" class="ml_2em">备注说明</label>
				<textarea id="remark" class="input-block-level" rows="5" cols="70" name="remark" readonly>${bav.remark}</textarea>
				<c:if test="${childBx!=null}">
				<font color='red'>审批提示：子女教育津贴上次报销情况【${childBx['billCode']} 报销金额:${childBx['bxMoney']}】</font>
				</c:if>
			</td>
		</tr>
	</table>
	</div>

	<c:import url="/bx/bx_fileupload_readonly.do?billCode=${bav.billCode}" />
	<c:import url="/workflow/get_completeOpinion.do?billCode=${bav.billCode}" />
	
	
	
			 