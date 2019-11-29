<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>regist</title>
    <script src="./js/jquery-1.12.3.min.js"></script>
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<style type="text/css">
.vertical-center{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
  </head>
  <body>
    <div class="container vertical-center">
	<div class="col-md-6 col-md-offset-3">
      <form  action="/regist.do" method="post">
        <h2 >用户注册</h2>
        <label for="inputEmail" class="sr-only">userid</label>
        <input type="text" name="username" id="inputEmail" class="form-control" placeholder="用户名称" required autofocus>
        <br>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="密码" required>
        <br>
        <button class="btn btn-lg btn-primary btn-block" type="submit">注册</button>
      </form>
        	
	</div>
    </div> <!-- /container -->
  </body>
  <script>
	  $(document).ready(
			function() {
				 
				//$("#regist").click(function(){
					//window.location.href = '/toRegist';
				//});
				  
	  });
	  
  
  </script>
</html>
