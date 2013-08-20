<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>维尼小弟的博客 公众平台</title>
		<meta charset="UTF-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="维尼小弟的博客，维尼小弟，公众平台">
		<meta http-equiv="description" content="winionline">
		<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="css/weixin.css">
	</head>

	<body>
		<div class="navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
						<span class="icon-bar"></span> 
						<span class="icon-bar"></span> 
						<span class="icon-bar"></span>
					</button>
					<a class="brand" href="/index">维尼小弟公众平台</a>
					<div class="nav-collapse collapse">
						<ul class="nav">
							<li class="active"><a href="/index" title="公众平台" data-toggle="tooltip">维尼小弟公众平台</a></li>
	                      	<li><a href="http://www.winilog.com" title="本人的技术博客，欢迎大家来访！" data-toggle="tooltip" target="_blank">维尼小弟的博客</a> </li>
	                      	<li><a href="http://www.hadoopchina.net" title="Hadoop技术交流" data-toggle="tooltip" target="_blank">Hadoop中文资源网</a> </li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="container">
			<div class="modal">
				<div class="modal-header">
					<h3>维尼小弟公众平台</h3>
				</div>
				<div class="modal-body center" >
					<p>
						<img src="img/weixin.jpg" class="img-rounded">
					</p>
				</div>
				<div class="modal-footer">
					${message}
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="js/bootstrap.js"></script>
</html>
