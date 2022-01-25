// 复选框的全选和全不选
function checkAll() {
	var checkboxs=document.getElementsByName("checkbox");
	var checked=checkboxs[0].checked;
	for(var i=1; i<checkboxs.length; i++) {
		checkboxs[i].checked=checked;
	}
}
