if (!window.Android) {
	var Android = {	// test/debug
		play : function(f,w) { console.log("Android.play('f:',"+f+",'w:',"+w+")"); return true; },
		stop : function() { console.log("Android.stop()"); }
	}
}

var timer = null;

function playCtrl() {
	if (running())
		stop();
	else
		start();
	return false;
}

function inFreq(o) {
	localStorage.setItem("freq",o.value);
}

function setFreq(o) {
	var m=1, s=o.innerText,
		c = s.substring(s.length-1);
	switch(c) {
		case 'K':
		case 'k':
			m = 1000;
			s = s.substring(0,s.length-1);
			break;
		case 'M':
		case 'm':
			m = 1000000;
			s = s.substring(0,s.length-1);
			break;
		default:
	}
	var f = parseFloat(s) * m;
	document.getElementById("freq").value = f;
}

function setSleep() {
	timer = setTimeout(stop,1000*parseInt(localStorage.getItem("sleep")));
}

function start() {
	setSleep();
	var f = document.getElementById("freq").value;
	if (isNaN(f)) alert("Invalid frequency (Please use numbers only)");
	else if (f < 1 || f > 15000) alert("Frequency should be between 1Hz and 15000Hz");
	else if (Android.play(f,localStorage.getItem("wave")))
		document.getElementById("ctrl").src = "ic_action_stop.png";
}

function stop() {
	Android.stop();
	document.getElementById("ctrl").src = "ic_action_play.png";
	if (timer != null) { clearTimeout(timer); timer = null; }
}

function running() {
	return (timer == null ? false : true);
}