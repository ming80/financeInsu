<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  
	<style>
	.formFieldError {
	font-family: verdana, arial, helvetica, sans-serif;
	font-size: 12px;
	color: red;
	vertical-align: middle;
	}
	
	.formFieldError ul{
	margin: 0px;
	padding: 3px;
	vertical-align: middle;
	}
	
	.formActionError {
	font-family: verdana, arial, helvetica, sans-serif;
	font-size: 12px;
	color: red;
	vertical-align: middle;
	}
	
	.formActionError ul{
	margin: 0px;
	padding: 3px;
	vertical-align: middle;
	}
	</style>

  	<title>环亚财务</title>
  </head>

  <body style=text-align:center>
    <form name=form2 action=login.action method=post>
    <table width=450px border=0>
      <tr height=22px>
        <td width=80px align=right><font size=-1>用户名：</font></td>  
        <td align=left width=170px><input name=userID type=text size=20 value="<s:property value="userID" />"></td>
        <td class="formFieldError" align=left><s:fielderror fieldName="userID" /></td>
      </tr>
      <tr height=22px>
        <td align=right><font size=-1>密码：</font></td>
        <td align=left><input name=password type=password size=20></td>
        <td class="formFieldError" align=left><s:fielderror fieldName="password" /></td>
      </tr>
      <tr height=22px>
        <td class="formActionError" colspan=2><s:actionerror/></td>
      </tr>
      <tr height=22px>
        <td colspan=2>
          <input name=submit type=submit value="登 录">
          <input name=reset type=reset value="取 消">
        </td>
      </tr>
    </table>
    </form>
  </body>
</html>