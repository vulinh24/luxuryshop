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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js"></script>
</head>
<body>
    <div class="wrapper">
        <div class="container">
            <div class="dashboard">
                <jsp:include page="/WEB-INF/views/back-end/common/menu.jsp"></jsp:include>
                <div class="right">
                    <div class="right__content">

                        <div class="row">
                            <canvas id="myChart"></canvas>
                        </div>
                    </div>

                    <div class="right__content">
                                        <div class="right__title" style="width: 500px;">Top Sản Phẩm Bán Chạy Nhất</div>
                                        <div class="right__table">
                                            <div class="right__tableWrapper">
                                                <table id="myTable" style="border:none;">
                                                    <thead>
                                                    <tr>
                                                        <th scope="col">STT</th>
                                                        <th scope="col">Tên sản phẩm</th>
                                                        <th scope="col">Giá bán</th>
                                                        <th scope="col">Giá Cũ</th>
                                                        <th scope="col">Số lượng đã bán</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var="product" items="${products }" varStatus="loop">
                                                        <tr>
                                                            <td data-label="STT" scope="row">${loop.index + 1}</td>
                                                            <td data-label="Tên sản phẩm">${product.title }</td>
                                                            <td data-label="Giá SP"><fmt:formatNumber value="${product.price }"
                                                                                                      type="number"/></td>
                                                            <td data-label="Giá Sale"><fmt:formatNumber value="${product.priceOld }"
                                                                                                        type="number"/></td>
                                                            <td data-label="Số lượng">${product.amount}</td>


                                                        </tr>
                                                    </c:forEach>

                                                    </tbody>
                                                </table>
                                            </div>
                                            </div>
                                        </div>
                                    </div>

                </div>
            </div>
        </div>
    </div>
    <%--    <script type="text/javascript">--%>
    <%--	    <sec:authorize access="hasRole('OPERATOR')">--%>
    <%--			alert("Để đảm bảo dữ liệu, vui lòng thêm dữ liệu mới để thực hiện chức năng!");--%>
    <%--		</sec:authorize>--%>
    <%--    </script>--%>
    <jsp:include page="/WEB-INF/views/back-end/common/js.jsp"></jsp:include>
    <script src="${pageContext.request.contextPath}/js/jquery-3.5.1.min.js"></script>
    <!-- chart -->
    <script>
        $.ajax({
            url: "/caculate",
            type: "get",
            dataType: "json",
            success: function (res) {
                var keyres = Object.keys(res);
			var valueres = Object.values(res);

			let myChart = document.getElementById('myChart').getContext('2d');
		    // Global Options
		    Chart.defaults.global.defaultFontFamily = 'Lato';
		    Chart.defaults.global.defaultFontSize = 16;
		    Chart.defaults.global.defaultFontColor = '#777';

		    let massPopChart = new Chart(myChart, {
		      type:'bar', // bar, horizontalBar, pie, line, doughnut, radar, polarArea
		      data:{
		        labels: keyres,
		        datasets:[{
		          label:'Doanh Thu',
		          data: valueres,
		          //backgroundColor:'green',
		          backgroundColor:[
		            'rgba(255, 99, 132, 0.6)',
		            'rgba(54, 162, 235, 0.6)',
		            'rgba(255, 206, 86, 0.6)',
		            'rgba(75, 192, 192, 0.6)',
		            'rgba(153, 102, 255, 0.6)',
		            'rgba(255, 159, 64, 0.6)',
		            'rgba(255, 99, 132, 0.6)'
		          ],
		          borderWidth:1,
		          borderColor:'#777',
		          hoverBorderWidth:3,
		          hoverBorderColor:'#000'
		        }]
		      },
		      options:{
		        title:{
		          display:true,
		          text:'Thống Kê Doanh Thu',
		          fontSize:25
		        },
		        legend:{
		          display:false,
		          position:'right',
		          labels:{
		            fontColor:'#000'
		          }
		        },
		        layout:{
		          padding:{
		            left:50,
		            right:0,
		            bottom:0,
		            top:0
		          }
		        },
		        tooltips:{
		          enabled:true
		        }
		      }
		    });
		},
		error : function(res) {

		}
	});

  </script>
	<!--  -->

</body>
</html>