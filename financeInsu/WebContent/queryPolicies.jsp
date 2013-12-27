<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>保单查询</title>
<link rel="stylesheet" type="text/css" href="js/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="js/easyui/themes/icon.css">  
<script type="text/javascript" src="js/json2.js"></script> 
<script type="text/javascript" src="js/easyui/jquery-1.8.0.min.js"></script>  
<script type="text/javascript" src="js/easyui/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="js/easyui/locale/easyui-lang-zh_CN.js"></script>

</head>

<body>
	<div style="padding:10px">		
		<table id="tt"></table>
		<div id="tb" style="padding:5px;height:auto">
			<form metod="post" action="">
			<table border="0" width="650px">
				<tr>
					<td align="right">保单号&nbsp</td>
					<td colspan="3">
					  <input type="text" id="policyNo" style="border:1px solid #99BBE8"><font size="-1" color="grey">#</font>
					</td>
				</tr>				
				<tr>
					<td align="right">客户全称&nbsp</td>
					<td>
					  <input type="text" id="insured" style="border:1px solid #99BBE8"><font size="-1" color="grey">#</font>
					</td>
					<td align="right">业务员编号&nbsp</td>
					<td><input type="text" id="brokerNo" style="border:1px solid #99BBE8"></td>
				</tr>
				<tr>
					<td align="right">核销状态&nbsp</td>
					<td>
						<select class="easyui-combobox" id="remarkScope" data-options="width:150,editable:false,panelHeight:70">
							<option value="all">全部</option>
							<option value="unremarked">未核销</option>
							<option value="remarked">已核销</option>
						</select>
					</td>
					<td align="right">结算状态&nbsp</td>
					<td>
						<select class="easyui-combobox" id="settlementScope" data-options="width:150,editable:false,panelHeight:70">
							<option value="all">全部</option>
							<option value="unsettled">未结算</option>
							<option value="settled">已结算</option>
						</select>
					</td>					
				</tr>
				<tr>
					<td align="right">输单日期范围&nbsp</td>
					<td colspan="2">
					    <span><input id="inputDateFrom" type="text" class="easyui-datebox"></input></span> 至 
					    <span><input id="inputDateTo" type="text" class="easyui-datebox"></input></span>
					</td>
					<td align="center">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查 询</a> 
						<font size="-1" color="grey">&nbsp#表示此条件支持模糊查询</font>
					</td>
				</tr>
			</table>
			</form>			
		</div>
	</div>	
	<div id="win2"></div>
	
	<script type="text/javascript">
		$(function(){	
			//{'total':100,'rows':[{id:'1',name:'一'},{id:'2',name:'二'}]};
			//var nullData = {"total":1,"rows":[],"footer":[{"policyNo":"Total","premium":18.00}]};
			//var json = JSON.parse(nullData);
			//data:{'total':1,'rows':[],'footer':[{'policyNo':'Total','premium':18.00}]}			
			$('#tt').datagrid({
				//width: 830,
				height: 480,
				showFooter:true,
				pagination: true,	
				frozenColumns:[[
					{field:'policyNo',title:'保单号',width:160,sortable:true}
				]],
				columns:[[
					{field:'brokerNo',title:'业务员编号',width:70,sortable:true},
					{field:'insured',title:'客户全称',width:210,sortable:true},
					{field:'premium',title:'总保费',width:90,sortable:true},
					{field:'cost',title:'成本',width:90,sortable:true},
					{field:'insuranceCompany',title:'保险公司',width:90,sortable:true},
					{field:'factorageRate',title:'手续费率',width:90,sortable:true},
					{field:'factorage',title:'手续费',width:90,sortable:true},
					{field:'insuranceType',title:'险种类别',width:90,sortable:true},
					{field:'invoiceNo',title:'开票号',width:90,sortable:true},
					{field:'inputDate',title:'输单日期',width:80,sortable:true,
						formatter:function(value){
							if(value == null)	return '';
							
			            	var date = new Date(value.time);
			            	if(isNaN(date))
			            		return '';
			            	
			                var year = date.getFullYear();
			                var month = date.getMonth() + 1;
			                month = month < 10 ? '0' + month : month;
			                var date = date.getDate();
			                date = date < 10 ? '0' + date : date ;
			                //alert(date.pattern('yyyy-MM-dd hh:mm:ss.S'));
			                return year + '-' + month + '-' + date;			            	
			        	}
					},
					{field:'remarkInfo',title:'核销',width:90,sortable:true,
						styler:function(value,row,index){
							if(!$.isEmptyObject(value))
								return 'color:green';
						},
						formatter:function(value){
							//alert(value);
							if(!value)
								return '';
							
							if($.isEmptyObject(value))	
								return '未核销';
							else
								return '已核销';
		                }	
					},
					{field:'remarkInfo.operationDate',title:'核销日期',width:90,sortable:true,
						formatter:function(value,rowData,rowIndex){	
							//var operationDate = rowData.remarkInfo['operationDate'];
							//alert(rowData.remarkInfo.operationDate);
							if($.isEmptyObject(rowData.remarkInfo))
								return '';
							
							var operationDate = rowData.remarkInfo.operationDate;
							
							if(!operationDate)	return '';
							
			            	var date = new Date(operationDate.time);
			            	if(isNaN(date))
			            		return '';
			            	
			                var year = date.getFullYear();
			                var month = date.getMonth() + 1;
			                month = month < 10 ? '0' + month : month;
			                var date = date.getDate();
			                date = date < 10 ? '0' + date : date ;
			                //alert(date.pattern('yyyy-MM-dd hh:mm:ss.S'));
			                return year + '-' + month + '-' + date;	
			        	}
					},
					{field:'settlementInfo',title:'结算',width:90,sortable:true,
						styler:function(value,row,index){
							if(!$.isEmptyObject(value))
								return 'color:green';
						},
						formatter:function(value){
							if(!value)
								return '';
							
							if($.isEmptyObject(value))	
								return '未结算';
							else
								return '已结算';
		                }	
					},
					{field:'settlementInfo.operationDate',title:'结算日期',width:90,sortable:true,
						formatter:function(value,rowData,rowIndex){	
							//var operationDate = rowData.remarkInfo['operationDate'];
							//alert(rowData.remarkInfo.operationDate);
							if($.isEmptyObject(rowData.settlementInfo))
								return '';
							
							var operationDate = rowData.settlementInfo.operationDate;
							
							if(!operationDate)	return '';
							
			            	var date = new Date(operationDate.time);
			            	if(isNaN(date))
			            		return '';
			            	
			                var year = date.getFullYear();
			                var month = date.getMonth() + 1;
			                month = month < 10 ? '0' + month : month;
			                var date = date.getDate();
			                date = date < 10 ? '0' + date : date ;
			                //alert(date.pattern('yyyy-MM-dd hh:mm:ss.S'));
			                return year + '-' + month + '-' + date;	
			        	}
					}
				]],	                   
				onLoadSuccess: function(data){   
					var panel = $(this).datagrid('getPanel');   
					var tr = panel.find('div.datagrid-footer tr');   
					tr.each(function(){   
						var td = $(this).children('td[field="policyNo"]');   
						td.children("div").css({ 
							"text-align": "center",
							"font-weight":"bold",
							"color":"red" 
						});
						td = $(this).children('td[field="premium"]');   
						td.children("div").css({
							"font-weight":"bold",
							"color":"red" 
						});
						td = $(this).children('td[field="cost"]');
						td.children("div").css({
							"font-weight":"bold",
							"color":"red" 
						});
						td = $(this).children('td[field="factorage"]');   
						td.children("div").css({
							"font-weight":"bold",
							"color":"red" 
						});
					});   
				},
				toolbar:'#tb'				
			});	
			
			
			$('#inputDateFrom').datebox({  
				width:100,
				editable:false,
				formatter:dateFormatter,
				parser:dateParser
			}); 
			
			$('#inputDateTo').datebox({  
				width:100,
				editable:false,
				formatter:dateFormatter,
				parser:dateParser
			}); 			

		});
		
		function query(){
			//alert($('#dateFrom').datebox.currentText);			
			//alert($('#remarkScope').combobox('getValue'));
			//var policyNo = $('#policyNo').val();
			//$('#inputDateFrom').datebox('setValue','2013-01-01');
			if($('#inputDateFrom').datebox('getValue') == '' || $('#inputDateTo').datebox('getValue') == ''){
				alert('请选择日期范围!');
				return;
			}
			
			$('#tt').datagrid('options').url = "queryPolicy.action";
			$('#tt').datagrid('load',{
				queryTimestamp: new Date().getTime(),
				policyNo: $('#policyNo').val(),
				brokerNo: $('#brokerNo').val(),
				insured:$('#insured').val(),
				remarkScope: $('#remarkScope').combobox('getValue'),
				settlementScope: $('#settlementScope').combobox('getValue'),
				inputDateFrom:$('#inputDateFrom').datebox('getValue'),
				inputDateTo:$('#inputDateTo').datebox('getValue')
			});
			
		}
		
		function dateFormatter(date){ 			
            var y = date.getFullYear();  
            var m = date.getMonth()+1;  
            var d = date.getDate();  
            return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);  
        }  
		
        function dateParser(s){  
            if (!s) return new Date();  
            var sss = s.split('-');  
            var y = parseInt(ss[0],10);  
            var m = parseInt(ss[1],10);  
            var d = parseInt(ss[2],10);  
            if (!isNaN(y) && !isNaN(m) && !isNaN(d)){  
                return new Date(y,m-1,d);  
            } else {  
                return new Date();
            }  
        }
	</script>
</body>
</html>