// 复选框的全选和全不选
function checkAll() {
	var checkboxs=document.getElementsByName("checkbox");
	var checked=checkboxs[0].checked;
	for(var i=1; i<checkboxs.length; i++) {
		checkboxs[i].checked=checked;
	}
}
// 获取已选的复选框序号
function getCheckedIndexes() {
	var checkedIndexes=[];
	var checkboxs=document.getElementsByName("checkbox");
	for(var i=1; i<checkboxs.length; i++) {
		if(checkboxs[i].checked) {
			checkedIndexes.push(i-1);
		}
	}
	return checkedIndexes;
}
// 判断是否登录
if(!sessionStorage.getItem("sessionId")) {
	parent.location.href="login.html"
}
// 封装Vue的post请求，包含headers
function post(url, jsonData, fun) {
	Vue.http.post(url, jsonData, {
		emulateJSON: true,
		headers: {
			sessionId: sessionStorage.getItem("sessionId")
		}
	}).then(fun, function(res){
	    console.log(res);
	});
}
// 封装Vue的get请求，包含headers
function get(url, fun) {
	Vue.http.get(url, {
		headers: {
			sessionId: sessionStorage.getItem("sessionId")
		}
	}).then(fun, function(res){
	    console.log(res);
	});
}
// 字符串转Date对象
function getDate(str) {
	var year=str.substring(0, 4);
	var month=str.substring(5, 7);
	var day=str.substring(8, 10);
	var hour=str.substring(11, 13);
	var minute=str.substring(14, 16);
	var second=str.substring(17, 19);
	return new Date(year, month-1, day, hour, minute, second);
}
