<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   
<!-- JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
   
<!DOCTYPE html>
<html>
<head>
    <title>Cửa hàng</title>
    <jsp:include page="/WEB-INF/views/front-end/common/css.jsp"></jsp:include>
	<style>
		.shop__sidebar__categories .active {
			color: black;
		}

		.shop__sidebar__price .active {
			color: black;
		}
	</style>
	<style>
		.phppot-container {
			-webkit-font-smoothing: antialiased;
			font-family: Arial, "Helvetica Neue", Helvetica, sans-serif;
			font-size: .9em;
			color: #1e2a28;
			width: 740px;
			margin: 0 auto;
			padding: 0px 20px 20px 20px;
		}

		.header-search {
			padding: 40px;
			max-width: 500px;
			margin: 0 auto;
		}

		#search-list {
			max-width: 500px;
			border-bottom: #E0E0E0 1px solid;
			border-left: #E0E0E0 1px solid;
			border-right: #E0E0E0 1px solid;
		}

		#search-box {
			padding: 20px;
			border: #E0E0E0 1px solid;
			border-radius: 0px;
			width: 100%;
		}

		.search-section {
			padding: 10px;
			border-bottom: 1px solid #E0E0E0;
		}

		.product-row {
			border-bottom: 2px;
			clear: both;
		}

		.description {
			line-height: 20px;
			display: inline-block;
			font-size: 14px;
		}

		.image-search-result {
			height: 36px;
			width: 36px;
			padding: 0px 0px 0px 0px;
			cursor: pointer;
			border: #E0E0E0 1px solid;
			margin-right: 10px;
		}

		#search-list {
			overflow: scroll;
			height: 300px;
		}

		#suggesstion-box {
			position: absolute;
			z-index: 10000;
			background-color: white;
		}
	</style>
</head>
<body>
<jsp:include page="/WEB-INF/views/front-end/common/header.jsp"></jsp:include>
    <section class="breadcrumb-option">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb__text">
                        <h4>Cửa hàng</h4>
                        <div class="breadcrumb__links">
                            <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                            <img src="${pageContext.request.contextPath}/images/right-arrow.png">
                            <span>Cửa hàng</span>
                        </div>
                    </div>
                </div>
			</div>
		</div>
	</section>
<section class="shop spad">
	<div class="container">
		<div class="row">
			<div class="col-lg-3">
				<div class="shop__sidebar">
					<div class="shop__sidebar__search">
						<form action="/shop/search" method="post">
							<input id="search-box" type="text" placeholder="Tìm kiếm..." name="keyword">
							<div id="suggesstion-box"></div>
						</form>

					</div>
					<div class="shop__sidebar__accordion">
						<div class="accordion" id="accordionExample">
							<c:if test="${not empty USER }">
                                		<div class="card">
                                		<div class="card-heading" style="margin-bottom: 20px;">
                                        <a href="${pageContext.request.contextPath}/shop/search?favorite=true" >Sản phẩm yêu thích</a>
                                    	</div>
                                    	</div>
                                </c:if>
                                <c:if test="${not empty favorite }">
                                	<div class="card-heading">
	                                    <a href="${pageContext.request.contextPath}/shop" >Sản phẩm </a>
	                                </div>
                                </c:if>
                                <c:if test="${empty favorite }">
                                	<div class="card">
	                                    <div class="card-heading">
	                                        <a data-toggle="collapse" data-target="#collapseOne">Danh mục
	                                        <img src="${pageContext.request.contextPath}/images/arrow-down.png"></a>
	                                    </div>
	                                    <div id="collapseOne" class="collapse show" data-parent="#accordionExample">
	                                        <div class="card-body">
	                                            <div class="shop__sidebar__categories">
	                                                <ul class="nice-scroll">
	                                                	<li><a href="${pageContext.request.contextPath}/shop" <c:if test="${empty currentCategoryId }">class="active"</c:if> >Sản phẩm</a></li>
		                                                <c:forEach var = "category" items = "${categories }">
															<li><a href="${pageContext.request.contextPath}/shop/search?categoryid=${category.id}&page=1" class="${currentCategoryId==category.id ? 'active' : '' }"> ${category.name }</li>
														</c:forEach>
													</ul>
	                                            </div>
	                                        </div>
	                                    </div>
                               		 </div>
                                	<div class="card">
                                    <div class="card-heading">
                                        <a data-toggle="collapse" data-target="#collapseThree">Lọc giá sản phẩm
                                        <img src="${pageContext.request.contextPath}/images/arrow-down.png"></a>
                                    </div>
                                    <div id="collapseThree" class="collapse show" data-parent="#accordionExample">
                                        <div class="card-body">
                                            <div class="shop__sidebar__price">
                                                <ul>
                                                	<li><a <c:if test="${price == null }"> class="active"</c:if> href="${pageContext.request.contextPath}/shop/search?priceBegin=-1">Bỏ lọc</a></li>
                                                    <li><a <c:if test="${price == 0 }"> class="active"</c:if> href="${pageContext.request.contextPath}/shop/search?priceBegin=${'0'}&priceEnd=${'10000000'}&page=1">0đ - 10000000đ</a></li>
                                                    <li><a <c:if test="${price == 10000000 }"> class="active"</c:if> href="${pageContext.request.contextPath}/shop/search?priceBegin=${'10000000'}&priceEnd=${'20000000'}&page=1">10000000đ - 20000000đ</a></li>
                                                    <li><a <c:if test="${price == 20000000 }"> class="active"</c:if> href="${pageContext.request.contextPath}/shop/search?priceBegin=${'20000000'}&priceEnd=${'50000000'}&page=1">20000000đ - 50000000đ</a></li>
                                                    <li><a <c:if test="${price == 50000000 }"> class="active"</c:if> href="${pageContext.request.contextPath}/shop/search?priceBegin=${'50000000'}&priceEnd=${'100000000'}&page=1">50000000 - 100000000</a></li>
                                                    <li><a <c:if test="${price == 100000000 }"> class="active"</c:if> href="${pageContext.request.contextPath}/shop/search?priceBegin=${'100000000'}">100000000+</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>

									<div class="card">
										<div class="card-heading">
											<a data-toggle="collapse" data-target="#collapseOne">Bộ sưu tập
												<img src="${pageContext.request.contextPath}/images/arrow-down.png"></a>
										</div>
										<div id="collapseOne" class="collapse show" data-parent="#accordionExample">
											<div class="card-body">
												<div class="shop__sidebar__categories">
													<ul class="nice-scroll">
														<li>
															<a href="${pageContext.request.contextPath}/shop/search?collectionid=-1&page=1"
															   <c:if test="${empty currentCollectionId }">class="active"</c:if> >Bỏ
																lọc</a></li>
														<c:forEach var="collection" items="${collections }">
															<li>
																<a href="${pageContext.request.contextPath}/shop/search?collectionid=${collection.id}&page=1"
																   class="${currentCollectionId==collection.id ? 'active' : '' }"> ${collection.name }
															</li>
														</c:forEach>
													</ul>
												</div>
											</div>
										</div>
									</div>

									<%--                                <div class="card">--%>
									<%--                                    <div class="card-heading">--%>
									<%--                                        <a data-toggle="collapse" data-target="#collapseSix">Thẻ--%>
									<%--                                        <img src="${pageContext.request.contextPath}/images/arrow-down.png"></a>--%>
									<%--                                    </div>--%>
									<%--                                    <div id="collapseSix" class="collapse show" data-parent="#accordionExample">--%>
									<%--                                        <div class="card-body">--%>
									<%--                                            <div class="shop__sidebar__tags">--%>
									<%--                                            	<c:forEach var="tag" items="${tags}">--%>
									<%--                                            		<a href="${pageContext.request.contextPath}/shop/search?tag=${tag.upperName()}">${tag.upperName()}</a>--%>
									<%--                                            	</c:forEach>--%>
									<%--                                            </div>--%>
									<%--                                        </div>--%>
									<%--                                    </div>--%>
									<%--                                </div>--%>
								</c:if>
                                
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-9">
                    <div class="shop__product__option">
                        <div class="row">
                            <div class="col-lg-6 col-md-6 col-sm-6">
                                <div class="shop__product__option__left">
                                    <p>Hiển thị ${products.size()} / ${size } kết quả</p>
                                </div>
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-6">
                                <div class="shop__product__option__right">
                                    
                                    <c:if test="${empty favorite }">
                                    	<p>Sắp xếp giá:</p>
                                    	<select onchange="location = this.value;" style="width:20px;">
	                                    	<option></option>
	                                        <option value="${pageContext.request.contextPath}/shop/search?sort=asc&page=1">Thấp đến cao</option>
	                                        <option value="${pageContext.request.contextPath}/shop/search?sort=desc&page=1">Cao đến thấp</option>
                                    	</select>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <c:forEach var="product" items="${products }">
	                        <div class="col-lg-4 col-md-6 col-sm-6">
			                    <div class="product__item">
				                    <c:choose>
										<c:when test = "${empty product.productImages }">
											<div class="product__item__pic set-bg" data-setbg="http://placehold.it/700x400">
												<c:if test="${product.isHot == true }">
													<span class="label">Hot</span>
												</c:if>
												<c:if test="${product.isNew == true }">
													<span class="label">Mới</span>
												</c:if>
												<c:if test="${product.isSale == true }">
													<span class="label">Sale</span>
												</c:if>
												<ul class="product__hover">
					                               <c:choose>
														<c:when test="${product.userLiked(USER) == true }">
															<li onclick="favorite.favorite(${product.id});"><a><img class = "img_${product.id }" src="${pageContext.request.contextPath}/images/redheart.png" alt=""><span>Yêu thích</span></a></li>
														</c:when>
														<c:otherwise>
															<li onclick="favorite.favorite(${product.id});"><a><img class = "img_${product.id }" src="${pageContext.request.contextPath}/images/heart.png" alt=""><span>Yêu thích</span></a></li>
														</c:otherwise>
													</c:choose>
					                                <li><a href="#"><img src="${pageContext.request.contextPath}/images/compare.png" alt=""><span>So sánh</span></a></li>
					                                <li><a href="${pageContext.request.contextPath}/shop-details/${product.seo}"><img src="${pageContext.request.contextPath}/images/search.png" alt=""><span>Chi tiết</span></a></li>
					                            </ul>
					                        </div>
										</c:when>
										<c:otherwise>
											<div class="product__item__pic set-bg" data-setbg="${pageContext.request.contextPath}/file/upload/${product.productImages.get(0).path }">
											<c:if test="${product.isHot == true }">
													<span class="label">Hot</span>
												</c:if>
												<c:if test="${product.isNew == true }">
													<span class="label">Mới</span>
												</c:if>
												<c:if test="${product.isSale == true }">
													<span class="label">Sale</span>
												</c:if>
												<ul class="product__hover">
													<c:choose>
														<c:when test="${product.userLiked(USER) == true }">
															<li onclick="favorite.favorite(${product.id});"><a><img
																	class="img_${product.id }"
																	src="${pageContext.request.contextPath}/images/redheart.png"
																	alt=""><span>Yêu thích</span></a></li>
														</c:when>
														<c:otherwise>
															<li onclick="favorite.favorite(${product.id});"><a><img
																	class="img_${product.id }"
																	src="${pageContext.request.contextPath}/images/heart.png"
																	alt=""><span>Yêu thích</span></a></li>
														</c:otherwise>
													</c:choose>
													<li><a href="#"><img
															src="${pageContext.request.contextPath}/images/compare.png"
															alt=""><span>So sánh</span></a></li>
													<li>
														<a href="${pageContext.request.contextPath}/shop-details/${product.seo}"><img
																src="${pageContext.request.contextPath}/images/search.png"
																alt=""><span>Chi tiết</span></a></li>
												</ul>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="product__item__text">
										<h6>${product.title }</h6>
										<!-- code api in javascript.js -->
										<c:choose>
											<c:when test="${product.amount > 0}">
												<a href="javascript:void(0)" class="add-cart"
												   onclick="cart.choose_product_to_cart(${product.id}, 1)">+ Thêm vào
													giỏ hàng <span style="float: right">SL: ${product.amount}</span></a>
											</c:when>
											<c:otherwise>
												<a href="javascript:void(0)" class="add-cart">Sản phẩm hiện đã bán
													hết!</a>
											</c:otherwise>
										</c:choose>

										<div class="rating">
											<c:forEach begin="1" end="${product.rate }">
												<img width="15px"
													 src="${pageContext.request.contextPath}/images/yellow-star.png">
											</c:forEach>
											<c:forEach begin="${product.rate + 1 }" end="5">
												<img width="15px"
													 src="${pageContext.request.contextPath}/images/grey-star.png">
											</c:forEach>
										</div>
										<h5><fmt:formatNumber value="${product.price }" type="number"/> <span
												style="text-decoration:line-through; font-size:14px; color:grey;"><fmt:formatNumber
												value="${product.priceOld }" type="number"/></span></h5>
										<div class="product__color__select">
											<label class="silver" for="pc-1">
												<input type="radio" id="pc-1">
											</label>
											<label class="active grey" for="pc-2">
												<input type="radio" id="pc-2">
											</label>
										</div>
									</div>
								</div>
			                 </div>
                        </c:forEach>
                    </div>
                     
                    <div class="row">
                        <div class="col-lg-12">
                            	
								  <div class="product__pagination">
								  		<c:choose>
								  			<c:when test="${totalPage == 1 }">
								  				<a href="${pageContext.request.contextPath}/shop/search?page=1" class="active"><button type="button" class="btn btn-outline-dark">1</button></a>
								  			</c:when>
								  			<c:otherwise>
								  				<c:if test="${currentPage > 1 && currentPage <= totalPage }">
								  					<a href="${pageContext.request.contextPath}/shop/search?page=${currentPage - 1}"><button type="button" class="btn btn-outline-dark">&laquo;</button></a>
								  				</c:if>
								  				<c:forEach begin="1" end="${totalPage}" varStatus="loop"> 
										  			<c:if test="${loop.index == currentPage }">
										  			<a href="${pageContext.request.contextPath}/shop/search?page=${loop.index}" class="active"><button type="button" class="btn btn-outline-dark">${loop.index }</button></a>
										  			</c:if>
										  			<c:if test="${currentPage != loop.index }">
										  			<a href="${pageContext.request.contextPath}/shop/search?page=${loop.index}"><button type="button" class="btn btn-outline-dark">${loop.index }</button></a>
										  			</c:if>
									  			</c:forEach>
									  			<c:if test="${currentPage >= 1 && currentPage < totalPage }">
									  				<a href="${pageContext.request.contextPath}/shop/search?page=${currentPage + 1}"><button type="button" class="btn btn-outline-dark">&raquo;</button></a>
									  			</c:if>
								  			</c:otherwise>
								  		</c:choose>
                            	</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <jsp:include page="/WEB-INF/views/front-end/common/footer.jsp"></jsp:include>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/page.js"></script>
    <jsp:include page="/WEB-INF/views/front-end/common/js.jsp"></jsp:include>


<script>
	$(document).ready(function () {
		$("#search-box").keyup(function () {
			$(".img-url").show();
			$.ajax({
				url: "/suggest-search",
				type: "post",
				crossDomain: true,
				contentType: "application/json", // dữ liệu gửi lên web-service có dạng là json.
				data: JSON.stringify('keyword=' + $(this).val()), // object json -> string json
				dataType: "text", // dữ liệu từ web-service trả về là json.
				success: function (resp) {
					$("#suggesstion-box").show();
					$("#suggesstion-box").html(resp);
					$("#search-box").css("background", "#FFF");
				}
			});
		});
	});
	// $(document).ready(function() {
	// 	$("#search-box").blur(function() {
	// 		$("#suggesstion-box").hide();
	// 	});
	// });
	$.ajaxSetup({headers: {'csrftoken': '{{ csrf_token() }}'}});
</script>

</body>
</html>