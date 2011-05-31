 var targetTime = new Date().getTime();
 var inPomodoro = false;
 function setTargetTime(remaining, inPomodoroS) {
   targetTime = new Date().getTime() + parseInt(remaining);
   var newInPomodoro = "true" == String(inPomodoroS);
   inPomodoro = newInPomodoro;
   updateClock();
 };

 function updateClock() {

   var now = new Date().getTime();

   var allSeconds = Math.floor((Math.abs(targetTime - now)) / 1000);
   var minutes = Math.floor(allSeconds / 60);
   var seconds = allSeconds % 60;
   var colour = "Y";
   if (targetTime <= now) {
     colour = "G";
   } else if (allSeconds <= 60) {
     colour = "R";
   }
   var m1 = Math.floor(minutes / 10);
   var m2 = minutes % 10;
   var s1 = Math.floor(seconds / 10);
   var s2 = seconds % 10;
   var size = "M";

   $("#digit1").attr("src", "images/" + m1 + colour + size + ".png");
   $("#digit2").attr("src", "images/" + m2 + colour + size + ".png");
   $("#colon").attr("src", "images/d" +      colour + size + ".png");
   $("#digit3").attr("src", "images/" + s1 + colour + size + ".png");
   $("#digit4").attr("src", "images/" + s2 + colour + size + ".png");

   if (inPomodoro && targetTime >= now) {
     $("#clock").show("slow");
     //$("#clock").dropShadow();
     $("#message").html("<p></p>")
     document.title = "[" + m1 + m2 + ":" + s1 + s2 + "]";
   } else {
     //$("#clock").removeShadow();
     $("#clock").hide("slow");
     $("#message").html("<p>Matt is slacking off!</p>")
     document.title = "Slacking off!";
   } 
 };
 setInterval("updateClock();", 990);