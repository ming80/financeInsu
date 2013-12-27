<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据导入</title>
<link rel="stylesheet" type="text/css" href="js/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="js/easyui/themes/icon.css">  
<script type="text/javascript" src="js/easyui/jquery-1.8.0.min.js"></script>  
<script type="text/javascript" src="js/easyui/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="js/easyui/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" src="js/ajaxfileupload.js"></script>
</head>

<body>
		<div style="padding:10px">
			<div class="easyui-panel" style="width:700px;height:120px;padding:10px;">
				<!-- form name="fileUpload" enctype="multipart/form-data" method="post" action="importPolicies.action"-->
				<font size="-1">请选择要导入的Excel文件</font><br>
				<input type="file" id="file" name="excelFile" size="60" /><br><br>
				<input type="button" value="导入数据" onclick="doImport()"/>
				<div id="importResult" style="padding:5px;width:580px;height:20px;border:0px solid black;float:right"></div>				
				<!-- /form-->
			</div>
			<div style="height:10px">
			</div>
	
			<div id="datagridContainer"><table id="errorInfoList"></table></div>
		</div>	
	
	<script type="text/javascript">
		function doImport(){
			//隐藏datagrid			
			$('#datagridContainer').hide();
			//清空提示区
			$('#importResult').html('');
			
			if($('#file').val() == ''){
				$('#importResult').html('<font size=-1 color=red>请先选择要导入的excel文件!</font>');
				return;
			}
						
			//清空datagrid
			//$('#errorInfoList').datagrid({data:{total:0,rows:[]}});
			
			$("#loading").ajaxStart(function(){
				$(this).show();
			})
			.ajaxComplete(function(){
				$(this).hide();
			});
			
			$.ajaxFileUpload(
				{
					url:'importPolicies.action',
					secureuri:false,
					fileElementId:'file',
					dataType:'json',
					success:function(data,status){
						if(data.successful){
							$('#importResult').html('<font size=-1 color=green>导入成功!</font>');
							return;
						}							
						
						if(!data.errorInfoList){
							$('#importResult').html('<font size=-1 color=red>导入失败!无法识别的文件!</font>');
							return;
						}
							
						$('#importResult').html('<font size=-1 color=red>导入失败!错误原因如下!</font>');
						$('#errorInfoList').datagrid({
							width: 700,
							height: 300,
							//showFooter:true,
							//pagination: true,	
							columns:[[
								{field:'rowNum',title:'错误定位(Excel文件中)',width:140,
									formatter:function(value){
										return '第 ' + value + ' 行';
					               	}
								},
								{field:'info',title:'错误描述',width:500}		
							]],
							data:data.errorInfoList						
						});
						   
						$('#datagridContainer').show();
					},
					error:function(data,status,e){
						$('#importResult').html('<font size=-1 color=red>导入失败!' + e + '</font>');
					}					
				}
			);
			
			return false;
		}
	</script>
</body>
</html>