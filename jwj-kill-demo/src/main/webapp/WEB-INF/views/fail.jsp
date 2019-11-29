<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<script>

    var error='${sessionScope.error}';
	alert(error);
	window.history.go(-1);
</script>