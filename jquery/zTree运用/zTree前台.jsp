<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/taglib/c-rt.tld"  prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/fmt-rt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/button.tld"  prefix="b"%>
  
<%String basePath = request.getContextPath();%>

<!doctype html>
<html lang="en">

<head>
	1、ztree要引入的js文件		
	<link rel="stylesheet" href="<%=basePath%>/js/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script src="<%=basePath%>/js/zTree/js/jquery.ztree.all-3.5.min.js"></script>
	<script src="<%=basePath%>/js/zTree/js/jquery.ztree.exhide-3.5.min.js"></script>
</head>
<body>
	<!--功能说明：点击选择后出现弹框，弹框中是个子目的ztree树，可以根据所搜来获取不同的结果-->
	
	<a class="basic-btn gray-btn" onclick="iniSubSubjectTree()"><span>选择</span>
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

//点击搜索按钮后不符合条件的被隐藏
function queryTree(){
	//显示上次搜索后背隐藏的结点
	zTreeObj.showNodes(hiddenNodes);

	//查找不符合条件的叶子节点
	function filterFunc(node){
		//3、这个地方一定要改成你查询框的id
		var _keywords=$("#queryTextTree").val();
		if(node.isParent||node.name.indexOf(_keywords)!=-1) return false;
		return true;		
	};

	//获取不符合条件的叶子结点
	hiddenNodes=zTreeObj.getNodesByFilter(filterFunc);
	
	//隐藏不符合条件的叶子结点
	zTreeObj.hideNodes(hiddenNodes);
}

//2、ztree加载有两种方式：
//方式一：异步加载先只加载顶级节点然后点击的时候一级一级展开
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
	        async: {
	           enable: true,
	           url: "../bx/initSubSubjectTree.do",
	           autoParam:["id","chkDisabled"],
	           otherParam:{"zid":"-1"},
	           dataFilter: filter
	        },
	        callback:{
	           onCheck: zTreeOnClick
	        }
	};	
	zTreeObj = $.fn.zTree.init($("#zTreeSubSubject"), setting);		  	
	
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

//这里的查询用的是方式二，方式一还未实现查询
//方式二：一下子把所有节点都从数据库中取出来展示，这里就不能用async属性,只能用data属性
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
				 //注：后台穿过来的json数据，一定要eval()一下，要不解析不出来
				 zTreeObj = $.fn.zTree.init($("#zTreeSubSubject"), setting,eval("(" + data + ")"));
			 }
			 else { //搜索不到结果
				
			 }
		 }
	});	  	
	
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

</script>

</html>
