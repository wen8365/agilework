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
	gotoLogin()
}
// 封装Vue的post请求，包含headers
function post(url, jsonData, fun) {
	Vue.http.post(url, jsonData, {
		emulateJSON: true,
		headers: {
			sessionId: sessionStorage.getItem("sessionId")
		}
	}).then(fun, handleError);
}
// 封装Vue的get请求，包含headers
function get(url, fun) {
	Vue.http.get(url, {
		headers: {
			sessionId: sessionStorage.getItem("sessionId")
		}
	}).then(fun, handleError);
}
// 字符串转Date对象
function getDate(str) {
    try {
        return new Date(str);
    } catch (error) {
        return null;
    }
}

function formatUTCTime(t) {
    if (!t) return null;
    var date = new Date(t);
    return date.getFullYear() + "-"
         + (date.getMonth() + 1) + "-"
         + (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + " "
         + (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":"
         + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes());
}

function handleError(res) {
    console.log(res);
    if (res.status==403) {
        gotoLogin()
    }
}

function gotoLogin() {
    sessionStorage.clear();
    alert("用户未登录！");
    parent.location.href="login.html";
}

// 获取课程表
function getSchoolTimetable(lessons) {
	var schoolTimetable=[];
	for(let i=0; i<7; i++) {
		schoolTimetable.push([]);
		for(var j=0; j<11; j++) {
			schoolTimetable[i].push([]);
		}
	}
	
	for(let lesson of lessons) {
		var dayOfWeek=lesson.dayOfWeek;
		var lessonNo=lesson.lessonNo;
		schoolTimetable[dayOfWeek-1][lessonNo-1].push(lesson);
	}
	
	return schoolTimetable;
}