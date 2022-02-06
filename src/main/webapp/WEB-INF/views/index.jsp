<%--
  Created by IntelliJ IDEA.
  User: kjh48
  Date: 2022-01-28
  Time: 오후 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>환율계산</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  </head>
  <body>
  <h1>환율 계산</h1>
  <div><label>송금 국가 :</label> <span>미국(USD)</span></div>
  <div><label>수취 국가 :</label>
    <select name="receive_country" class="custom-select col-sm-6" id="receive_country" onchange = "getExchangeRates()">
      <option selected value="dft">국가 선택</option>
      <option value="KRW">한국(KRW)</option>
      <option value="PHP">필리핀(PHP)</option>
      <option value="JPY">일본(JPY)</option>
    </select>
  </div>

  <div><label>환율 :</label> <span id="exchange_rate"></span> </div>
  <div><label>송금액 :</label><span><input type="number" id="send_amount"></span>USD</div>
  <div style="text-align : right"><button type="button" class="btn btn-primary btn-sm " onclick="getSendAmount()" >환율 계산</button></div>
  <div id="receiveResult" class="col"  style=" margin-top : 3%">
  </div>
  </body>
<script>
  $('#send_amount').keypress(function (e) {
    if (e.which === 13) {
      getSendAmount();
    }
  });
  function getExchangeRates(){
    var receive_country = $('#receive_country').val();
    console.log(receive_country);
    if(receive_country === "dft"){
      $('#exchange_rate').text("");
      return;
    }
    $.ajax({
      url : "api/exchange-rates?receiveCountry=" + receive_country,
      type : "GET",
      success : function(res){
        $('#exchange_rate').text(res + " " +receive_country + "/ USD");
      },
      error : function(err){
        if(err.status === 400){
          alert("잘못 입력된 값 입니다.")
        }else if(err.status === 500){
          alert("서버에 문제가 발생했습니다.")
          console.log(err)
        }
      }
    })
  }
  function validCheck() {
    var amount = $('#send_amount').val();
    return (amount.length <= 0 || amount < 0 || amount > 10000);
  }
  function dftCheck(){
    var receive_country = $('#receive_country').val();
    return (receive_country == "dft");
  }

  function getSendAmount(){
    var receivingCountry = $('#receive_country').val();
    var sendAmount = $('#send_amount').val();
    var inputData = {
      "receiveCountry" : receivingCountry,
      "sendAmount" : sendAmount
    }
    if (validCheck()) {
      $('#receiveResult').html("<div class=\"alert alert-danger\" style='color: red'><p>송금액이 바르지 않습니다.송금액은 0 ~ 10000 사이의 수를 입력하세요.</p></div>");
      return;
    }
    if(dftCheck()){
      $('#receiveResult').html("<div class=\"alert alert-danger\" style='color: red'><p>수취 국가를 선택해 주세요.</p></div>");
      return;
    }
    var jsonData = JSON.stringify(inputData);
    $.ajax({
      url : "/api/exchange-rates",
      data : jsonData,
      type : "POST",
      contentType : "application/json",
      success : function (res) {
        $('#receiveResult').html("<div class=\"alert alert-success\">" +
                "<p>수취금액은 " + res + " " + receivingCountry + " 입니다.</p>" +
                "</div>");
      },
      error : function(err){
        if(err.status === 400){
          alert("잘못 입력된 값 입니다.")
        }else if(err.status === 500){
          alert("서버에 문제가 발생했습니다.")
        }
      }
    })
  }
</script>
</html>
