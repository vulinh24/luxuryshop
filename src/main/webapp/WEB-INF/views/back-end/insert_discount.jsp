<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- SPRING FORM -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/views/back-end/common/css.jsp"></jsp:include>
    <style>
    .checkBox{
    	display:inline-block;
    	font-size: 25px;
    }

    .checkBox span{
    	margin-right: 20px;
    	padding-right: 20px;

    }
    </style>
</head>
<body>
    <div class="wrapper">
        <div class="container">
            <div class="dashboard">
                <jsp:include page="/WEB-INF/views/back-end/common/menu.jsp"></jsp:include>
                <div class="right">
                    <div class="right__content">
                        <div class="right__title">Bảng điều khiển</div>
                        <p class="right__desc">Chèn mã giảm giá</p>
                        <div class="right__formWrapper">

                            <form:form action="${pageContext.request.contextPath}/admin/discount-add" method="post" modelAttribute="mgg" enctype="multipart/form-data">
                            	<form:hidden path="id"/>

                            	<div class="right__inputWrapper">
									<label>Tên</label>
									<form:input type="text" class="form-control" path="name"/>
									<small id="emailHelp" class="form-text text-muted">
											Tối đa 15 kí tự.
									</small>
								</div>

                                <div class="right__inputWrapper">
                                    <label>Giảm giá</label>
                                    <input type="text" class="form-control" name="num_discount"/>
                                </div>

                                <div class="right__inputWrapper">
                                    <label>Số lần sử dụng</label>
                                    <form:input type="text" class="form-control" path="remain" required="true"/>
                                </div>

                                <button class="btn" type="submit">Thêm</button>

                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
            </div>
        </div>
    <jsp:include page="/WEB-INF/views/back-end/common/js.jsp"></jsp:include>
    <script>

	$('#txtDetailDescription').summernote({
        placeholder: 'Hello stand alone ui',
        tabsize: 2,
        height: 120,
        toolbar: [
          ['style', ['style']],
          ['font', ['bold', 'underline', 'clear']],
          ['color', ['color']],
          ['para', ['ul', 'ol', 'paragraph']],
          ['table', ['table']],
          ['insert', ['link', 'picture', 'video']],
          ['view', ['fullscreen', 'codeview', 'help']]
        ]
      });
    </script>
</body>
</html>