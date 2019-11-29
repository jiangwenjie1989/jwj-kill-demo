<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>聊天室</title>
    <script src="./js/jquery-1.12.3.min.js"></script>
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<style>
body{
	margin-top:5px;
}
</style>
</head>
  <body>
    <div class="container">
    	<div class="row">
    		<div class="col-md-3">
    		<div class="panel panel-primary">
				  <div class="panel-heading">
				    <h3 class="panel-title">当前登录用户</h3>
				  </div>
				  <div class="panel-body">
				    <div class="list-group">
					 <a href="#" class="list-group-item">你好，${sessionScope.username}</a>
					 <a href="/logout.do?userId=${sessionScope.userId}" class="list-group-item">退出</a>
					</div>
				  </div>
				</div>
    			<div class="panel panel-primary" id="online">
				  <div class="panel-heading">
				    <h3 class="panel-title">当前在线的用户</h3>
				  </div>
				  <div class="panel-body">
				    <div class="list-group" id="users">
				    	<%-- <c:if test="${sessionScope.userList}!=null"> --%>
						    <c:forEach items="${sessionScope.userList}" var="user">  
		  						<a href="#" onclick="talk(this)" class="list-group-item">${user.username}-${user.id}</a>
							</c:forEach>
						<%-- </c:if> --%>
					</div>
				  </div>
				</div>
				<div class="panel panel-primary">
				  <div class="panel-heading">
				    <h3 class="panel-title">群发系统广播</h3>
				  </div>
				  <div class="panel-body">
				    <input type="text" class="form-control"  id="msg" /><br>
				    <button id="broadcast" type="button" class="btn btn-primary">发送</button>
				  </div>
				</div>
    		</div>
  			<div class="col-md-9">
  				<div class="panel panel-primary">
				  <div class="panel-heading">
				    <h3 class="panel-title" id="talktitle"></h3>
				  </div>
				  <div class="panel-body">
				  <button id="cleanMessage" type="button" class="btn btn-primary">清理屏幕消息</button>
				    <div class="well" id="log-container" style="height:400px;overflow-y:scroll">
				    
				    </div>
				    	<input type="text" id="myinfo" class="form-control col-md-12" /> <br>
				    	<button id="send" type="button" class="btn btn-primary">发送</button>
				    </div>
				</div>
  			</div>
    	</div>
    </div> 
<script>
	var toUsername='';
	var toUserId='';

	$(document).ready(
			function() {
				var wsServer = "ws://127.0.0.1:9999/ws?userId=" + ${sessionScope.userId};
				var websocket = new WebSocket(wsServer);
				websocket.onmessage = function(event) {
					var data = JSON.parse(event.data);
					if (data.messageType == 1) {//上线消息
						if (data.fromUsername != "${sessionScope.username}") {
							$("#users").append(
									'<a href="#" onclick="talk(this)" class="list-group-item">'
											+ data.fromUsername+'-'+data.fromUserId+ '</a>');
							alert(data.fromUsername + "上线了");
						}
					} else if (data.messageType == 2) {//下线消息
						if (data.fromUsername != "${sessionScope.username}") {
							$("#users > a").remove(
									":contains('" + data.fromUsername + "')");
							alert(data.fromUsername + "下线了");
						}
					}else{
						// 接收服务端的实时消息并添加到HTML页面中
			            $("#log-container").append("<div class='bg-info'><label class='text-danger'>"+data.fromUsername+"&nbsp;"+dateFormat("YYYY-mm-dd HH:MM:SS", new Date())+"</label><div class='text-success'>"+data.text+"</div></div><br>");
			            // 滚动条滚动到最低部
			            scrollToBottom();
					}
					
				};
				
				//广播发送消息
				$("#broadcast").click(function(){
		        	var text=$("#msg").val();
		        	if(text!=''){
		        		var data={};
						data["fromUserId"]="${sessionScope.userId}";
						data["fromUsername"]="${sessionScope.username}";
						data["toUserId"]="";
						data["toUsername"]="";
						data["groupId"]="";
						data["sendType"]="2";
						data["text"]=text;
						websocket.send(JSON.stringify(data));
						$("#msg").val("");
		        	}else{
		        		alert("请输入消息");
		        	}
		        	
		        });
				
				
				
				
	            $("#send").click(function(){
					var text=$("#myinfo").val();
					if(text!=''){
						if(toUserId!=''){
							var data={};
							data["fromUserId"]="${sessionScope.userId}";
							data["fromUsername"]="${sessionScope.username}";
							data["toUserId"]=toUserId;
							data["toUsername"]=toUsername;
							data["groupId"]="";
							data["sendType"]="1";
							data["text"]=text;
							websocket.send(JSON.stringify(data));
							$("#myinfo").val("");
							// 接收服务端的实时消息并添加到HTML页面中
				            $("#log-container").append("<div class='bg-info'><label class='text-danger'>"+data.fromUsername+"&nbsp;"+dateFormat("YYYY-mm-dd HH:MM:SS", new Date())+"</label><div class='text-success'>"+text+"</div></div><br>");
				            // 滚动条滚动到最低部
				            scrollToBottom();
						}else{
							alert("请当前在线用户选择要发送的人");
						}
						
					}else{
		        		alert("请输入消息");
		        	}
		        	
		        });
				
	            $("#cleanMessage").click(function(){
	            	var div = document.getElementById('log-container');
	            	div.innerHTML="";
		        });
	            
	           
	            
	            document.onkeydown = function(e) {
					var ev = window.event || e;
					var code = ev.keyCode || ev.which;
					if (code == 116||code==13) {
						ev.keyCode ? ev.keyCode = 0 : ev.which = 0;
						cancelBubble = true;
						return false;
					}
				} //禁止f5刷新
				document.οncοntextmenu = function() {
					return false
				};//禁止右键刷新

			});
				
			function talk(a){
				var userInfo=a.innerHTML;
				strs = userInfo.split("-"); //字符分割
				if(strs[1]==${sessionScope.userId}){
					alert("自己不能给自己发消息");
				}else{
					toUsername=strs[0];
				   	toUserId=strs[1];
					$("#talktitle").text("与"+userInfo+"的聊天");
				   	$("body").data("to",a.innerHTML);
				}
			   	
			}
				
			function scrollToBottom(){
				var div = document.getElementById('log-container');
				div.scrollTop = div.scrollHeight;
			}
			
			function dateFormat(fmt, date) {
			    let ret;
			    let opt = {
			        "Y+": date.getFullYear().toString(),        // 年
			        "m+": (date.getMonth() + 1).toString(),     // 月
			        "d+": date.getDate().toString(),            // 日
			        "H+": date.getHours().toString(),           // 时
			        "M+": date.getMinutes().toString(),         // 分
			        "S+": date.getSeconds().toString()          // 秒
			        // 有其他格式化字符需求可以继续添加，必须转化成字符串
			    };
			    for (let k in opt) {
			        ret = new RegExp("(" + k + ")").exec(fmt);
			        if (ret) {
			            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
			        };
			    };
			    return fmt;
			}
			
</script>    
    
  </body>
</html>
