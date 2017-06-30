<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/taglib/c-rt.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/fmt-rt.tld"  prefix="fmt"%>
<%String basePath = request.getContextPath();%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>畅游ERP系统</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<script src="<%=basePath%>/js/clbx_tool.js"></script>
		<script src="<%=basePath%>/js/dateUtils.js"></script>
		<script src="<%=basePath%>/js/attendance/detailTime.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/MonthDatePicker/WdatePicker.js"></script>
		<script type="text/javascript">
    
    
		</script>
	</head>
	<body>
	    //
	    <c:choose>
		<c:when test="${bav.isCN == 'Y'}">
				<c:forEach items="${bav.blmList}" var="detail">
					<c:if test="${detail.jtItem!=null}">
					<tr>
					    <td>
                                                <select  name="jtItemD" id="jtItemD">
						<c:forEach var="item" items="${itemSecList1}">
							<option value="${item.soleNum}" <c:if test="${detail.jtItem==item.soleNum}">selected</c:if>>${item.itemName}</option>
						</c:forEach>
						</select>
						
					    <td><fmt:formatDate value='${detail.jtDate}' pattern='yyyy-MM-dd'/><input type="hidden" name="jtDate" value="<fmt:formatDate value='${detail.jtDate}' pattern='yyyy-MM-dd'/>"/></td>
					   
					    <td><input type="text" maxlength="30"  name="commonMoneyD_cl" class="jtMoney" value="<fmt:formatNumber value='${detail.jtMoney}' pattern='#0.00' type='number'/>"><input type="hidden" name="jtMoney" value="${detail.jtMoney}"/></td>
					    <td><fmt:formatNumber value='${detail.jtRealsum}' pattern='#0.00' type='number'/><input type="hidden" name="jtRealsum" value="${detail.jtRealsum}"/></td>
					</tr>
					</c:if>
				    </c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach items="${bav.blmList}" var="detail">
					<c:if test="${detail.jtItem!=null}">
					<tr>
					    <td>${detail.commonItemName}
						<c:if test="${wordbook[detail.jtItem].preNum=='-1'}">
						<input type="hidden" name="jtDetailFirItem" value="${wordbook[detail.jtItem].soleNum}">
						</c:if>
						<c:if test="${wordbook[detail.jtItem].preNum!='-1'}">
						<input type="hidden" name="jtDetailFirItem" value="${wordbook[detail.jtItem].preNum}">
						</c:if>
						
					    <td><fmt:formatDate value='${detail.jtDate}' pattern='yyyy-MM-dd'/><input type="hidden" name="jtDate" value="<fmt:formatDate value='${detail.jtDate}' pattern='yyyy-MM-dd'/>"/></td>
					  
					    <td><fmt:formatNumber value='${detail.jtMoney}' pattern='#0.00' type='number'/><input type="hidden" name="jtMoney" value="${detail.jtMoney}"/></td>
					    <td><fmt:formatNumber value='${detail.jtRealsum}' pattern='#0.00' type='number'/><input type="hidden" name="jtRealsum" value="${detail.jtRealsum}"/></td>
					</tr>
					</c:if>
				    </c:forEach>
			</c:otherwise>
		</c:choose>
	</body>
</html>
	
	
	
			 
