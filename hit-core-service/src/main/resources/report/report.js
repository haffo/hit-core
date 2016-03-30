window.toggle_visibility = function(id, elm) {
	var e = document.getElementById(id);
	if (elm.checked == true)
		e.style.display = '';
	else
		e.style.display = 'none';
};
window.toggle_visibilityC = function(cls, elm) {
	var e = document.getElementsByClassName(cls);
	for (var i = e.length - 1; i >= 0; i--) {
		if (elm.checked == true)
			e[i].style.display = '';
		else
			e[i].style.display = 'none';
	}
};
window.fi = function(id) {
	var div = document.getElementById(id);
	if (div.style.display !== 'none') {
		document.getElementById("btn").childNodes[0].nodeValue = "View";
		div.style.display = 'none';
	} else {
		document.getElementById("btn").childNodes[0].nodeValue = "Hide";
		div.style.display = '';
	}
};