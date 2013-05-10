var csv = space.childByNamePath(document.name+".csv");

var rows = csv.content.split("\n");  
var splittedLabels=rows[0].split(";");
var splittedValues=rows[1].split(";");
var result = "";

var props = new Array();
var date = "";
var time = "";
var dateFormatted = "";

for (var i = 0; i < splittedLabels.length; i++) {

   if(splittedLabels[i] == "segnalazione"){
    props["dw:segnalazione"] = splittedValues[i];
   }
   if(splittedLabels[i] == "deviceID"){
    props["dw:deviceID"] = splittedValues[i];
   }  
   if(splittedLabels[i] == "longitudine"){
    props["dw:longitudine"] = splittedValues[i];
   }
   if(splittedLabels[i] == "latitudine"){
    props["dw:latitudine"] = splittedValues[i];
   }
   if(splittedLabels[i] == "altitudine"){
    props["dw:altitudine"] = splittedValues[i];
   }
   
  if(splittedLabels[i] == "data"){
    date = splittedValues[i].split("/");
  }
  if(splittedLabels[i] == "ora"){
    time = splittedValues[i].split(":");
  }
    
  if (isNotEmpty(date) && isNotEmpty(time)){
    var hour = time[0];
    var min = time[1];
    var sec = time[2];
    var msec = "0";
    var day = date[0];
    var month = date[1];
    var year = date[2];
   
    if(month.toString().length==1) month = "0"+month;
    if(day.toString().length==1) day = "0"+day;
    if(hour.toString().length==1) hour = "0"+hour;
    if(min.toString().length==1) min = "0"+min;
    if(sec.toString().length==1) sec = "0"+sec;
    if(msec.toString().length==1) msec = "00"+msec;
    if(msec.toString().length==2) msec = "0"+msec;
  
    dateFormatted = year+"-"+month+"-"+day+"T"+hour+":"+min+":"+sec+"."+msec+"Z";
    props["dw:data"] = dateFormatted;
  
  }
}
 

document.addAspect("dw:caleearth", props);
document.save();


function isNotEmpty(str) {
    return !(!str || 0 === str.length);
}

renditionService.render(document,"cm:doclib");
renditionService.render(document,"cm:pdfpreview");
renditionService.render(document,"cm:largepreview");
