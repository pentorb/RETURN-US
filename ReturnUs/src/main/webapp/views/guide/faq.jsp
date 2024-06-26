<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Return:Us</title>
<style>
#big {
	background-color: #F5F9F1;
	height: 100%;
	padding: 0px 50px 0px 50px;
}

#sm {
	background-color: white;
	height: 100%;
	padding: 45px 50px 0px 50px;
}

.accordion input[type="radio"] {
	display: none;
	width: 90%;
	height: 8%;
}

.accordion label {
	display: block;
	cursor: pointer;
	padding: 10px 20px;
	margin-bottom: 2px;
	border: 1px solid #D9D9D9;
/* 	background-color:#59981a; */
}

.accordion div {
	display: none;
	padding: 10px 20px;
	background-color: #fff;
}
#emptyArea .btn {
	background: lightgray;
}

#emptyArea .select {
	background: lightblue;
}

.Q {
	color: rgb(26, 124, 255);
	/*     margin-right: 2px; */
	white-space: nowrap;
	font-size: 28px;
	margin-right: 2%;
}

.A {
	font-size: 28px;
	line-height: 20px;
	letter-spacing: -0.2px;
	font-weight: 700;
	color: rgb(255, 0, 21);
	white-space: nowrap;
	flex-grow: 1; /* 텍스트가 확장되어 영역을 채우도록 설정 */
	padding-bottom: 10px;
}

.title {
	font-style: noto-sans;
	font-size: 28px;
	color: black;
}

.redemp {
	color: red;
}


#emptyArea .select {
	background: lightblue;
}
#pageDiv {
	padding: 40px;
    display: flex;
    justify-content: center;
    align-items:center;
}
#pageDiv button {
    padding: 10px 16px;
    text-decoration: none;
    transition: 0.5s;
    border: 1px solid lightgray;
    margin: 0 4px;
     background: var(--bs-white); 
}
#pageDiv button:hover {
	color: white;
	background: #81c408;
	text-color:white;
}
/* #pageDiv a { */
/* 	border: 1px solid var(--bs-light); */
/* } */

</style>

</head>

<body class="noto-sans">
	<!-- 헤더 파일 include -->

	<%@include file="/views/common/header.jsp"%>




	<div class="container-fluid fruite py-5" style="margin: 38px 100px; width: 90%;">
		<div class="col-lg-12">
			<div class="row g-4">
				<div class="col-6"></div>
			</div>
			<div class="row g-4">
				<div class="col-lg-2 shadow-sm">
					<%@include file="/views/common/guideSideBar.jsp"%>
				</div>
				<div class="col-lg-10">
					<div style="height: 100%; padding: 0px 70px 0px 70px">
						<!--큰 card ** 여기서부터 코딩하시면 됩니다!!! ** -->
						<div id="big" class="card">
							<div style="padding: 50px 0px 30px; color: #3E6D10;">
								<h3 class="noto-sans" style="color: #3E6D10;">자주 묻는 질문</h3>
								<span style="color: #3E6D10;">&nbsp;</span>
							</div>
							
							<!--body ** 여기서부터 코딩하시면 됩니다!!! ** -->
							<div id="sm" class="card">
								<c:if test="${admin eq 'admin' }">
									<a href="writefaq"><button class="button">글쓰기</button></a>
								</c:if>
								<c:forEach items="${faqList }" var="faq">
									<div class="accordion" style="padding-top: 1%">
										<input type="radio" name="accordion"> 
										
										<label for="answer?faqNo=${faq.faqNo }" style="align-items: center; display: flex"> 
											<span class="Q">Q</span> 
											<span style="display: inline; font-style: bold; font-size: large; font-weight: 500">${faq.faqTitle }</span>
										</label>
										
										<div style="padding: 30px; background-color:rgb(176 203 149 / 24%); border-radius:15px">
											<span class="A">A</span> <br>
											<br> <span class="content">${faq.faqContent }</span>
											<c:if test="${admin eq 'admin' }">
												<form id="deleteForm" method="get" action="deletefaq?faqNo">
													<input type="hidden" name="faqNo" id="faqNoInput" value="${faq.faqNo }">
													<button type="button" class="button" style="float: right;" onclick="rudeletefaq()">삭제</button>
												</form>
												<a href="faqmodify?faqNo=${faq.faqNo }">
													<button class="button" style="float: right; margin-right: 1%;">수정</button>
												</a>
											</c:if>
										</div>
									</div>
								</c:forEach>
								<div id="pageDiv">
									<c:choose>
										<c:when test="${pageInfo.curPage == 1 }">
											<a class="rounded">&lt;</a>
										</c:when>
										<c:otherwise>
											<a href="faq&page=${pageInfo.curPage-1 }" class="rounded">&lt;</a>
										</c:otherwise>
									</c:choose>
								
									<c:forEach begin="${pageInfo.startPage }" end="${pageInfo.endPage }" var="i">
										<c:choose>
											<c:when test="${i==pageInfo.curPage}">
												<a href="faq?page=${i }">
													<button class="rounded">${i}</button>
												</a>
											</c:when>
											<c:otherwise>
												<a href="faq?page=${i }">
													<button class="rounded">${i}</button>
												</a>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									
									<c:choose>
										<c:when test="${pageInfo.curPage == pageInfo.allPage }">
											<a class="rounded">&gt;</a>
										</c:when>
										<c:otherwise>
											<a href="faq&page=${pageInfo.curPage+1 }" class="rounded">&gt;</a>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>



	<%@ include file="/views/common/footer.jsp"%>
	<script src="sweetalert2.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script>
function rudeletefaq() {
    var deleteForm = document.getElementById("deleteForm");
    var faqNo = document.getElementById("faqNoInput").value; 
    Swal.fire({
        title: "삭제 하시겠습니까?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "삭제",
        cancelButtonText: "취소",
    }).then((result) => {
        if (result.isConfirmed) {
        	deleteForm.method = "get";
            deleteForm.action = "deletefaq";
            var faqNoInput = document.createElement("input");
            faqNoInput.setAttribute("type", "hidden");
            faqNoInput.setAttribute("name", "faqNo");
            faqNoInput.setAttribute("value", faqNo);
            deleteForm.appendChild(faqNoInput);
            deleteForm.submit();
        }
    });
}

</script>

	<script>
document.addEventListener('DOMContentLoaded', function () {
    var labels = document.querySelectorAll('.accordion label');
    labels.forEach(function(label) {
        label.addEventListener('click', function() {
            var radio = this.previousElementSibling;
            var div = this.nextElementSibling;
            
            if (radio.checked) {
                radio.checked = false;
                div.style.display = 'none';
            } else {	
                var radios = document.querySelectorAll('input[type="radio"]');
                radios.forEach(function(radio) {
                    radio.checked = false;
                });
                var divs = document.querySelectorAll('.accordion div');
                divs.forEach(function(div) {
                    div.style.display = 'none';
                });
                radio.checked = true;
                div.style.display = 'block';
            }
        });
    });
});
</script>








</body>
</html>