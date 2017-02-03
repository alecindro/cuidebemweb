<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cuide Bem</title>
<meta name="viewport" content="width=device-width, initial-scale=1"></meta>
<link type="text/css" rel="stylesheet"
	href="/resources/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet"
	href="/resources/css/bootstrap-theme.min.css">
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<style type="text/css">
.fontecel {
	font-size: 14px;
	text-align: center;
}
</style>
</head>
<body>
	<div class="fundo-login">
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-2 col-md-4"></div>
				<div class="col-xs-12 col-sm-8 col-md-4">
					<img class="img-responsive" src="/resources/img/logo.png" />
				</div>
				<div class="col-sm-2 col-md-4"></div>
			</div>
			<div class="row">
				<div class="col-sm-2 col-md-4"></div>
				<div class="col-xs-12 col-sm-8 col-md-4">

					<form class="form-horizontal" method="post">
						<div class="form-group">
							<input type="email" class="form-control fontecel"
								name="user" placeholder="Usuário" required="true" />
						</div>
						<div class="form-group">
							<input type="password" class="form-control fontecel" name="pass"
								id="exampleInputPassword1" placeholder="Senha" required="true" />
						</div>
						<div class="form-group">
							<button class="btn btn-success form-control fontecel"
								type="submit">Entrar</button>
						</div>

					</form>
				</div>
				<div class="col-sm-2 col-md-4"></div>
			</div>
			<div class="row">
				<div class="col-sm-2 col-md-4"></div>
				<div class="col-xs-12 col-sm-8 col-md-4">
				<form class="form-horizontal">
				<div class="form-group">
					<a class="btn btn-danger form-control" href="#" role="button">Recuperar
						Senha</a>
						</div>
							
						</form>
				</div>
				<div class="col-sm-2 col-md-4"></div>
			</div>
			<div class="row">
				<div class="col-sm-2 col-md-4"></div>
				<div class="col-xs-12 col-sm-8 col-md-4">
				<form class="form-horizontal">
				<div class="form-group">
					<a class="btn btn-info form-control" href="/resources/cadastro.xhtml"
						role="button">Não Possui uma conta? Clique aqui para se
						cadastrar!</a>
						</div>
						</form>
				</div>
				<div class="col-sm-2 col-md-4"></div>
			</div>
			<div class="row">
				<div class="col-sm-2 col-md-4"></div>
				<div class="col-xs-12 col-sm-8 col-md-4">
				
					<%
						String errorDescription = (String) request.getAttribute("qualiLoginFailure");
						if (errorDescription != null) {
					%>

					<div class="alert alert-danger fadein" style="text-align: center;margin-left: -15px;margin-right: -15px;">
						<%=errorDescription%>
					</div>
					<%
						}
					%>
					
				</div>
				<div class="col-sm-2 col-md-4"></div>
			</div>

			<!-- div class="row">
				<div class="col-sm-2 col-md-4"></div>
				<div class="col-xs-12 col-sm-8 col-md-4">
					<span> <%=getServletContext().getInitParameter("proprietary")%>
					</span><span> - </span> <span> <%=getServletContext().getInitParameter("messageLogin")%></span>
					<span> <%=getServletContext().getInitParameter("year")%></span> <br />
					<label>Software </label><span> <%=getServletContext().getInitParameter("softwareName")%></span><br />
					<label>Versão</label><span> <%=getServletContext().getInitParameter("version")%></span><br />
					<label>Suporte (tel):</label><span> <%=getServletContext().getInitParameter("support")%></span><br />
				</div>
				<div class="col-sm-2 col-md-4"></div>
			</div -->
		</div>
	</div>
</body>
</html>