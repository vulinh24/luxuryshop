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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.16.2/xlsx.full.min.js"></script>
    <style>
        /* Styling for popup container */
                        #popup-container {
                            display: none;
                            position: fixed;
                            z-index: 1;
                            left: 0;
                            top: 0;
                            width: 100%;
                            height: 100%;
                            overflow: auto;
                            background-color: rgba(0, 0, 0, 0.5);
                        }

                        /* Styling for popup content */
                        #popup-content {
                            background-color: #fff;
                            margin: 15% auto;
                            padding: 20px;
                            border: 1px solid #888;
                            width: 30%;
                            text-align: center;
                        }
                        #btn_bc {
                            float:right;
                            cursor:pointer;border: 2px solid black;padding: 3px; background-color: #3d4ecc;color:white;border-radius: 3px;
                            margin-top : 10px; margin-right: 30px;
                            height: 30px; width: 20%;
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

                        <div class="row">
                            <canvas id="myChart"></canvas>
                        </div>

                        <!-- The popup -->
                        <div id="popup-container">
                            <div id="popup-content">
                                <h1>Xuất Báo Cáo</h1>

                                  <form id="date-form">
                                        <table style="border:0px;">
                                            <tr style="border:0px;">
                                                <td style="border:0px;">
                                                    <label for="datepicker"><b>Ngày đầu:</b></label>
                                                    <input type="date" id="start_date" name="date" style="border: 1px solid black">
                                                </td>
                                                <td style="border:0px;">
                                                    <label for="datepicker"><b>Ngày cuối:</b></label>
                                                    <input type="date" id="end_date" name="date" style="border: 1px solid black">
                                                </td>

                                            </tr>
                                        </table>
                                        <button id="date_btn" type="button" onclick="xuatbaocao();"
                                        style="cursor:pointer;border: 2px solid black;padding: 3px; background-color: #3240a8;color:white;border-radius: 3px;">Xuất báo cáo</button>
                                  </form>
                                <button onclick="hidePopup()" style="margin-top:3px;cursor:pointer;">Thoát</button>
                            </div>
                        </div>

                        <h4><button id="btn_bc" onclick="showPopup(${order.id})"><h4>Xuất Báo Cáo</h4></button></h4>

                    </div>

                    <div class="right__content">
                        <div class="right__title" style="width: 500px;">Top 20 Sản Phẩm Bán Chạy Nhất</div>
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
                                    <c:forEach var="product" items="${sell_products }" varStatus="loop">
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

                    <div class="right__content">
                        <div class="right__title" style="width: 500px;">20 Sản Phẩm Được Quan Tâm Nhất</div>
                        <div class="right__table">
                            <div class="right__tableWrapper">
                                <table id="myTable" style="border:none;">
                                    <thead>
                                    <tr>
                                        <th scope="col">STT</th>
                                        <th scope="col">Tên sản phẩm</th>
                                        <th scope="col">Giá bán</th>
                                        <th scope="col">Giá Cũ</th>
                                        <th scope="col">Số lần quan tâm</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="product" items="${view_products }" varStatus="loop">
                                        <tr>
                                            <td data-label="STT" scope="row">${loop.index + 1}</td>
                                            <td data-label="Tên sản phẩm">${product.title }</td>
                                            <td data-label="Giá SP"><fmt:formatNumber value="${product.price }"
                                                                                      type="number"/></td>
                                            <td data-label="Giá Sale"><fmt:formatNumber value="${product.priceOld }"
                                                                                        type="number"/></td>
                                            <td data-label="Số lượng quan tâm">${product.amount}</td>


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
    <script>

        /* Function to show the popup */
        function showPopup() {
            document.getElementById("popup-container").style.display = "block";
        }

        /* Function to hide the popup */
        function hidePopup() {
            document.getElementById("popup-container").style.display = "none";
        }
    </script>

    <script>
        function xuatbaocao() {
              var sdate = $("#start_date").val();
              var edate = $("#end_date").val();
              $.ajax({
                url: "/statistic-revenue?startDate=" + sdate + "&endDate=" + edate,
                type: "GET",
                success: function(response) {
                downloadAsExcel(response);
                  hidePopup();
                },
                error: function(xhr, textStatus, errorThrown) {
                  console.log("Error:", errorThrown);
                  hidePopup();
                  window.alert("Đã có lỗi xảy ra!");
                }
              });
        }
    </script>
    <script>
        const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
        const EXCEL_EXTENSION = '.xlsx';
        function downloadAsExcel(data){
            const worksheet = XLSX.utils.json_to_sheet(data);
            const workbook = {
                Sheets: {
                    'baocao' : worksheet
                },
                SheetNames: ['baocao']
            };
            const excelBuffer = XLSX.write(workbook, {bookType: 'xlsx', type: 'array'});
            saveAsExcel(excelBuffer, 'baocao'+new Date().getTime());
        }

        function saveAsExcel(buffer, filename){
            const data = new Blob([buffer], {type: EXCEL_TYPE});
            saveAs(data, filename+EXCEL_EXTENSION);
        }
    </script>


    <script>
        /*
        * FileSaver.js
        * A saveAs() FileSaver implementation.
        *
        * By Eli Grey, http://eligrey.com
        *
        * License : https://github.com/eligrey/FileSaver.js/blob/master/LICENSE.md (MIT)
        * source  : http://purl.eligrey.com/github/FileSaver.js
        */

        // The one and only way of getting global scope in all environments
        // https://stackoverflow.com/q/3277182/1008999
        var _global = typeof window === 'object' && window.window === window
          ? window : typeof self === 'object' && self.self === self
          ? self : typeof global === 'object' && global.global === global
          ? global
          : this

        function bom (blob, opts) {
          if (typeof opts === 'undefined') opts = { autoBom: false }
          else if (typeof opts !== 'object') {
            console.warn('Deprecated: Expected third argument to be a object')
            opts = { autoBom: !opts }
          }

          // prepend BOM for UTF-8 XML and text/* types (including HTML)
          // note: your browser will automatically convert UTF-16 U+FEFF to EF BB BF
          if (opts.autoBom && /^\s*(?:text\/\S*|application\/xml|\S*\/\S*\+xml)\s*;.*charset\s*=\s*utf-8/i.test(blob.type)) {
            return new Blob([String.fromCharCode(0xFEFF), blob], { type: blob.type })
          }
          return blob
        }

        function download (url, name, opts) {
          var xhr = new XMLHttpRequest()
          xhr.open('GET', url)
          xhr.responseType = 'blob'
          xhr.onload = function () {
            saveAs(xhr.response, name, opts)
          }
          xhr.onerror = function () {
            console.error('could not download file')
          }
          xhr.send()
        }

        function corsEnabled (url) {
          var xhr = new XMLHttpRequest()
          // use sync to avoid popup blocker
          xhr.open('HEAD', url, false)
          try {
            xhr.send()
          } catch (e) {}
          return xhr.status >= 200 && xhr.status <= 299
        }

        // `a.click()` doesn't work for all browsers (#465)
        function click (node) {
          try {
            node.dispatchEvent(new MouseEvent('click'))
          } catch (e) {
            var evt = document.createEvent('MouseEvents')
            evt.initMouseEvent('click', true, true, window, 0, 0, 0, 80,
                                  20, false, false, false, false, 0, null)
            node.dispatchEvent(evt)
          }
        }

        // Detect WebView inside a native macOS app by ruling out all browsers
        // We just need to check for 'Safari' because all other browsers (besides Firefox) include that too
        // https://www.whatismybrowser.com/guides/the-latest-user-agent/macos
        var isMacOSWebView = /Macintosh/.test(navigator.userAgent) && /AppleWebKit/.test(navigator.userAgent) && !/Safari/.test(navigator.userAgent)

        var saveAs = _global.saveAs || (
          // probably in some web worker
          (typeof window !== 'object' || window !== _global)
            ? function saveAs () { /* noop */ }

          // Use download attribute first if possible (#193 Lumia mobile) unless this is a macOS WebView
          : ('download' in HTMLAnchorElement.prototype && !isMacOSWebView)
          ? function saveAs (blob, name, opts) {
            var URL = _global.URL || _global.webkitURL
            var a = document.createElement('a')
            name = name || blob.name || 'download'

            a.download = name
            a.rel = 'noopener' // tabnabbing

            // TODO: detect chrome extensions & packaged apps
            // a.target = '_blank'

            if (typeof blob === 'string') {
              // Support regular links
              a.href = blob
              if (a.origin !== location.origin) {
                corsEnabled(a.href)
                  ? download(blob, name, opts)
                  : click(a, a.target = '_blank')
              } else {
                click(a)
              }
            } else {
              // Support blobs
              a.href = URL.createObjectURL(blob)
              setTimeout(function () { URL.revokeObjectURL(a.href) }, 4E4) // 40s
              setTimeout(function () { click(a) }, 0)
            }
          }

          // Use msSaveOrOpenBlob as a second approach
          : 'msSaveOrOpenBlob' in navigator
          ? function saveAs (blob, name, opts) {
            name = name || blob.name || 'download'

            if (typeof blob === 'string') {
              if (corsEnabled(blob)) {
                download(blob, name, opts)
              } else {
                var a = document.createElement('a')
                a.href = blob
                a.target = '_blank'
                setTimeout(function () { click(a) })
              }
            } else {
              navigator.msSaveOrOpenBlob(bom(blob, opts), name)
            }
          }

          // Fallback to using FileReader and a popup
          : function saveAs (blob, name, opts, popup) {
            // Open a popup immediately do go around popup blocker
            // Mostly only available on user interaction and the fileReader is async so...
            popup = popup || open('', '_blank')
            if (popup) {
              popup.document.title =
              popup.document.body.innerText = 'downloading...'
            }

            if (typeof blob === 'string') return download(blob, name, opts)

            var force = blob.type === 'application/octet-stream'
            var isSafari = /constructor/i.test(_global.HTMLElement) || _global.safari
            var isChromeIOS = /CriOS\/[\d]+/.test(navigator.userAgent)

            if ((isChromeIOS || (force && isSafari) || isMacOSWebView) && typeof FileReader !== 'undefined') {
              // Safari doesn't allow downloading of blob URLs
              var reader = new FileReader()
              reader.onloadend = function () {
                var url = reader.result
                url = isChromeIOS ? url : url.replace(/^data:[^;]*;/, 'data:attachment/file;')
                if (popup) popup.location.href = url
                else location = url
                popup = null // reverse-tabnabbing #460
              }
              reader.readAsDataURL(blob)
            } else {
              var URL = _global.URL || _global.webkitURL
              var url = URL.createObjectURL(blob)
              if (popup) popup.location = url
              else location.href = url
              popup = null // reverse-tabnabbing #460
              setTimeout(function () { URL.revokeObjectURL(url) }, 4E4) // 40s
            }
          }
        )

        _global.saveAs = saveAs.saveAs = saveAs

        if (typeof module !== 'undefined') {
          module.exports = saveAs;
        }
    </script>
</body>
</html>