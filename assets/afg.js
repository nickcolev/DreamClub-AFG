var Android1 = {	// test/debug (change to 'Android' under Linux)
	play : function() { console.log("Android.play()"); return true; },
	stop : function() { console.log("Android.stop()"); }
}
var sleep;
function playCtrl() {
	if (document.getElementById("ctrl").value == "Stop") {
		stop();
	} else {
		start();
	}
	return false;
}
function setFreq(o,x) {
	if (!x) x = "1";
	document.getElementById("freq").value = parseInt(x) * parseFloat(o.innerText);
}
function setSleep() {
	var o = document.getElementById("sleep");
	if (o.value)
		if (isNaN(o.value))
			alert("Invalid sleep value (Please enter sleep time in seconds");
		else
			sleep = window.setTimeout(stop,1000*o.value);
		    
}
function start() {
	setSleep();
	var f = document.getElementById("freq").value;
	if (isNaN(f)) alert("Invalid frequency (Please use numbers only)");
	else if (f < 1 || f > 15000) alert("Frequency should be between 1Hz and 15000Hz");
	else if (Android.play(document.getElementById("freq").value)) document.getElementById("ctrl").value = "Stop";
}
function stop() {
	Android.stop();
	document.getElementById("ctrl").value = "Play";
	if (sleep) window.clearTimeout(sleep);
}
