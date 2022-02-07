// 复选框的全选和全不选
function checkAll() {
	var checkboxs=document.getElementsByName("checkbox");
	var checked=checkboxs[0].checked;
	for(var i=1; i<checkboxs.length; i++) {
		checkboxs[i].checked=checked;
	}
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
