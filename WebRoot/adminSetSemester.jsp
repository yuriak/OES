<%@page import="edu.dufe.oes.bean.Semester"%>
<%@page import="edu.dufe.oes.service.SemesterService"%>
<%@page import="edu.dufe.oes.service.AdministratorService"%>
<%@page import="edu.dufe.oes.bean.Administrator"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
AdministratorService administratorService=new AdministratorService();
SemesterService semesterService=new SemesterService();
Semester semester=semesterService.getCurrentSemester();
String token=(String)session.getAttribute("token");
if(token==null||token.length()==0){
	response.sendRedirect("adminLogin.jsp");
	return;
}
Administrator administrator=administratorService.getAdministratorByToken(token);
if(administrator==null){
	response.sendRedirect("adminLogin.jsp");
	return;
}
%>

<!doctype html>
<html class="no-js">
<head>
	<base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="管理员主页">
    <meta name="keywords" content="管理员 审批 设置">
    <meta name="viewport"
          content="width=device-width, initial-scale=1">
    <title>管理员页面</title>

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
<body>
<!-- Begin "返回"公共头部 -->
<header data-am-widget="header" class="my-header my-text-center am-header am-header-default">
    <div class="am-header-left am-header-nav" style="left: 0;">
        <a id="back" class="header-back" href="adminIndex.jsp">
            <img class="am-header-icon-custom"
                 src="data:image/svg+xml;charset=utf-8,&lt;svg xmlns=&quot;http://www.w3.org/2000/svg&quot; viewBox=&quot;0 0 12 20&quot;&gt;&lt;path d=&quot;M10,0l2,2l-8,8l8,8l-2,2L0,10L10,0z&quot; fill=&quot;%23fff&quot;/&gt;&lt;/svg&gt;"
                 alt=""/>
        </a>
        <span id="register-text">管理员设置学期</span>
    </div>
    <!-- 按钮触发器， 需要指定 target -->
    <a href="#personalCenter" class="my-personal-icon my-white am-icon-btn am-icon-user"
       data-am-offcanvas="{target: '#personalCenter',effect: 'push'}">
    </a>
    <!-- Begin功能中心 -->
    <div id="personalCenter" class="am-offcanvas">
        <div class="my-offcanvas am-offcanvas-bar am-offcanvas-bar-flip">
            <div class="am-offcanvas-content">
                <div class="my-person-text">功能中心</div>
                <span class="my-photo am-icon-optin-monster am-icon-lg"></span>

                <div id="realName" class="realName">admin</div>
                <div class="am-btn-group-stacked">
                    <button id="approve" type="button" class="my-person-menu am-btn am-btn-default am-icon-cog"><span>审批教师</span> </button>
                   <button id="set-semester" type="button" class="my-person-menu am-btn am-btn-default am-icon-cog"><span>设置当前学期</span> </button>
                    <button id="exit" type="button" class="my-exit my-person-menu am-btn am-btn-default">退出登录</button>
                </div>
            </div>
        </div>
    </div>
    <!-- End个人中心 -->
</header>
<!-- End "返回"公共头部 -->



<section class="am-panel am-panel-warning">
	<header class="am-panel-hd">
        <h3 class="am-panel-title">当前学期为：<%=semester.getSemesterName() %></h3>
    </header>
    <header class="am-panel-hd">
        <h3 class="am-panel-title">设置须知：</h3>
    </header>
    <div class="am-panel-bd">
        第一学期为2月到7月，设置为201601<br> 第二学期为9月到1月，设置为201602
        <br>以此类推!!!
    </div>
</section>

<form id="doc-vld-msg" class="am-form">
    <fieldset>
        <div class="am-form-group am-form-icon">
            <i class="am-icon-dedent"></i>
            <input type="password" id="date" class="am-form-field" minlength="6" maxlength="6" placeholder="请输入当前学期，例：201601"
                   data-validation-message="请务必保证格式正确！！！"/>
        </div>
        <button id="doc-confirm-toggle" type="button" class="my-next my-center am-btn  am-btn-warning" disabled >确认提交</button>
    </fieldset>
</form>

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
<script src="assets/js/adminSetSemester.js"></script>
</body>
</html>
