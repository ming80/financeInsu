<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>环亚财务</title>
<link rel="stylesheet" type="text/css" href="js/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="js/easyui/themes/icon.css">  
<script type="text/javascript" src="js/easyui/jquery-1.8.0.min.js"></script>  
<script type="text/javascript" src="js/easyui/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	function openTab(page,pageName){
		if ($('#content').tabs('exists',pageName)){
			$('#content').tabs('select', pageName);
		} else {
			var content = 
				'<iframe scrolling="no" frameborder="0" src="' + page + '" style="width:100%;height:100%;"></iframe>';
				
			$('#content').tabs('add',{
				title:pageName,
				content:content,
				//href:page,
				closable:true
			});
		}
	}
	
	function setLayoutHeight(){
		//alert($(window).height());	//窗口高度
		//alert(screen.availHeight);	//可用高度
		
		$('#cc').height(screen.height <= 768 ? 690 : (screen.availHeight * 0.92));	//可用高度的92%
		$('#cc').layout('resize');
	}

</script>
</head>

<body onload="setLayoutHeight() ">
  <div id="cc" class="easyui-layout" style="margin:0px 25px">
    <div data-options="region:'north',border:false" style="height:25px;position:relative">
    	<!-- div style="border:0px solid black;position:absolute;left:0px;bottom:5px;">
    		<img src="images/huanya_logo.png">
    	</div-->
    	<div style="border:0px solid black;position:absolute;right:0px;bottom:5px;"><font size=-1>
    		欢迎您，${sessionScope.user.name }！ &nbsp&nbsp&nbsp<img src="images/logout.png" style="vertical-align:bottom;">&nbsp<a href="logout.action">安全退出</a>
    	</font></div>   	
    </div>   
  
    <div data-options="region:'west'" title="操作" style="width:150px;padding:5px;">
      <ul class="easyui-tree">
        <li iconCls="icon-base">
          <span>保 单</span>
          <ul>
            <li><a href="#" onclick="openTab('importer.jsp','导入保单')">导入保单</a></li> 
            <li><a href="#" onclick="openTab('queryPolicies.jsp','保单查询')">保单查询</a></li>  
            <li><a href="#" onclick="openTab('remark.jsp','核销')">核销</a></li>  
            <li><a href="#" onclick="openTab('settle.jsp','结算')">结算</a></li>  
	      </ul>
        </li>
      </ul> 
    </div>
    
    <div data-options="region:'center',border:false"  style="padding:0px 0px 0px 5px;">
      <div id="content" class="easyui-tabs" fit="true" border="true" plain="false"></div>
    </div>
    
    <div data-options="region:'south',border:false" style="height:90px;padding:5px 0px 0px 0px;" >
      <div align=center style="border:1px solid #7290B8;height:70px;padding:8px 0px 0px 0px;">
        <font size=-1 color=#555555><b>
                     上海环亚保险经纪有限公司 版权所有 <br>
                     地址:上海六合路98号港陆黄埔中心13楼<br>
　                邮编：200001 总机：63616888　传真：63507288<br>
　                E-mail：email@firsthuanya.com　
      </b></font></div>
    </div>
  </div>
    
</body>
</html>