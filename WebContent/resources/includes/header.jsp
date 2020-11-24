<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./resources/styles/main.css" />
<title>Lend-a-Book</title>
</head>
<body>
    <div class="banner">

        <div id="bannerRight">
            <p id="bannerTitle">Lend-a-Book</p>
        </div>
    </div>
    <hr />

    <c:import url="/resources/includes/options.jsp" />

    <c:if test="${member.isLoggedIn() == true}">
        <h1>You are logged in as ${member.userName}</h1>
        <hr />
    </c:if>