<%@page import="edu.dufe.oes.service.AdministratorService"%>
<%@page import="edu.dufe.oes.bean.Administrator"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html>
<head>
	<base href="<%=basePath%>">
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="description" content="教学评估系统的登录">
  <meta name="keywords" content="教学 评估 学生 教师 登录">
  <meta name="viewport"
        content="width=device-width, initial-scale=1">
  <title>登录</title>

  <!-- Set render engine for 360 browser -->
  <meta name="renderer" content="webkit">

  <!-- No Baidu Siteapp-->
  <meta http-equiv="Cache-Control" content="no-siteapp"/>

  <link rel="icon" type="image/png" href="assets/i/favicon.png">

  <!-- Add to homescreen for Chrome on Android -->
  <meta name="mobile-web-app-capable" content="yes">
  <link rel="icon" sizes="192x192" href="assets/i/app-icon72x72@2x.png">

  <!-- Add to homescreen for Safari on iOS -->
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-title" content="Amaze UI"/>
  <link rel="apple-touch-icon-precomposed" href="assets/i/app-icon72x72@2x.png">

  <!-- Tile icon for Win8 (144x144 + tile color) -->
  <meta name="msapplication-TileImage" content="assets/i/app-icon72x72@2x.png">
  <meta name="msapplication-TileColor" content="#0e90d2">

  <link rel="stylesheet" href="assets/css/amazeui.min.css">
  <link rel="stylesheet" href="assets/css/app.css">
</head>
<body id="indexBody">
<!-- 背景图片 -->
<img id="indexImg" src="assets/i/index1.jpg" width="100%"/>
<!-- 遮罩层 -->
<div id="mask"></div>
<!-- Logo -->
<img id="indexLogo" src="assets/i/logo.png" alt="" width="100px">
<!-- 登录面板 -->
<div id="loginPanel" class="am-panel am-panel-default">
  <div id="hd" class="am-panel-hd am-text-center am-text-lg">
      管理员登录
  </div>
  <div id="bd" class="am-panel-bd">
    <form class="am-form am-form-inline">
      <div  class="my-form-group am-form-group am-form-icon">
        <i class="am-icon-user"></i>
        <input type="text" id="user" name="userName" class="am-form-field" placeholder=" 用户名" required>
      </div>
      <div class="my-form-group am-form-group am-form-icon">
        <i class="am-icon-lock"></i>
        <input type="password" id="password" name="password" class="my-input am-form-field" placeholder=" 密码" required>
      </div>
      <button type="button" id="loginIn" class="am-btn am-btn-success am-radius"> 登&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp录</button>
    </form>
  </div>
</div>
<!-- end 登录面板 -->

<!--public 提示框-->
<!-- Begin success提示框 -->
<div id="my-alert-success" class="am-modal am-modal-alert" tabindex="-1">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">成功</div>
    <div class="am-modal-bd">
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn">确定</span>
    </div>
  </div>
</div>
<!-- End success提示框-->
<!-- Begin err提示框 -->
<div id="my-alert-false" class="am-modal am-modal-alert" tabindex="-1">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">抱歉</div>
    <div class="am-modal-bd">
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn">确定</span>
    </div>
  </div>
</div>
<!-- End err提示框-->
<!-- Begin 加载-->
<div class="am-modal am-modal-loading am-modal-no-btn" tabindex="-1" id="my-loading">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">努力加载中...</div>
    <div class="am-modal-bd">
      <span class="am-icon-spinner am-icon-spin"></span>
    </div>
  </div>
</div>
<!-- End 加载-->

<!--[if (gte IE 9)|!(IE)]><!-->
<script src="assets/js/jquery.min.js"></script>
<!--<![endif]-->
<!--[if lte IE 8 ]>
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<script src="assets/js/amazeui.ie8polyfill.min.js"></script>
<![endif]-->
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/js.cookie.js"></script>
<script src="assets/js/adminLogin.js"></script>
</body>
</html>