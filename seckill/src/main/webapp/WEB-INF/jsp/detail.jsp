<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/tag.jsp" %>
<html lang="zh-cn">
  <head>
 	<title>秒杀详情页</title>
 	<%@include file="/WEB-INF/common/head.jsp" %>
  </head>
  <body>
	  <div class="container">
			<div class="panel panel-default text-center">
				<div class="panel-heading">
					<h1>${seckill.name}</h1>
				</div>
				<div class="panel-body">
					<h2 class="text-danger">
						<!-- 显示time图标 -->
						<span class="glyphicon glyphicon-time"></span>
						<!-- 展示倒计时 -->
						<span class="glyphicon" id="seckill-box"></span>
					</h2>
				<div>
			</div>
		</div>
		<div id="killPhoneModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h3 class="modal-title text-center">
							<span class="glyphicon glyphicon-phone"></span>秒杀电话
						</h3>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-8 col-xs-offset-2">
								<input type="text" name="killPhone" id="killPhoneKey"
								placeholder="填写手机号码o(∩_∩)o" class="form-control"/>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<!-- 验证消息 -->
						<span id="killPhoneMessage" class="glyphicon"></span>
						<button type="button" id="killPhoneBtn" class="btn btn-success">
							<span class="glyphicon glyphicon-phone"></span>
							提交
						</button>
					</div>
				</div>
			</div>
		</div>
	</body>
	<!-- jQuery cookie操作插件 -->
	<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
	<!-- jQuery 倒计时插件插件 -->
	<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
	<script src="../resources/seckill.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			seckill.detail.init({
				seckillId:${seckill.seckillId},
				startTime:${seckill.startTime.time},//毫秒表示
				endTime:${seckill.endTime.time},
			});
		});
	</script>
</html>
