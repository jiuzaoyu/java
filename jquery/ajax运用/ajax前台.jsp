<%@ page language="java"  contentType="text/html;"  pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/taglib/c-rt.tld" prefix="c"%>          
<%@ taglib uri="/WEB-INF/taglib/fmt-rt.tld"  prefix="fmt"%>  
 <%
 String basePath = request.getContextPath();
 %>
<html>
<head>
	<script type="text/javascript">

        ajax第一种运用：ajax返回html页面	

        //点击“请确认财务科目”在financeSubject的div中加载一个html页面
        function initFinanceSubject(billCode)
        {
            var basePath = "<%=basePath%>";
            var setObject = $("#financeSubject");
            $.ajax({
                url : basePath+"/bx/showBxFinanceSubject.do",
                type : "POST",
                async : true,     //true表示同步加载，false为异步加载
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
        
        
        ajax第二种运用：ajax返回json信息
        function showavermentDialog(billcode){
            //申辩的时候判断补提产假是否能申辩成功
			if(flowSubject.indexOf("产假")>0){
			$.ajax({
				url : "<%=basePath%>/attendance/leavebill/checkSupplementHardForExplain.do",
				type : "POST",
				async : false,
				dataType : "json",
				data : {
					billCode:billcode
				},
				cache : false,
				success : function(json) { 
					var status = json.status;
					var msg = json.message;
					if(status == "false"){   		   					
						alert_art(msg,"warning");
					}
				}
			});
			}
        }
        
	</script>
</head>
<body>	
	<a class="basic-btn" href="#" onclick="confirmFinanceSubject('${bav.billCode}')">
		<span>请确认财务科目</span>
	</a>
	<!-- 确认财务科目弹窗 -->
	<div class="hide" id="financeSubject"  style="height:650px;width:1000px;overflow:auto;">
	</div>
    
    
    
    
    <a class="basic-btn" href="#" onclick="showavermentDialog('${bav.billCode}')">
		<span>申辩</span>
	</a>
    
    
    
    
</body>
</html>

