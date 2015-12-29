
      var marker;  
      var map;

function initialize() {
    var mapOptions = {
      center: new google.maps.LatLng(43.865280, 11.167173),
      zoom: 14,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
   map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);
    
    
	  geocoder = new google.maps.Geocoder();

      google.maps.event.addListener(map, 'click', function(event) {
          placeMarker(event.latLng);
      });

      
  }




      
      function placeMarker(location) {
          if(marker){ // se il marker esiste già
              marker.setPosition(location); // si aggiorna la sua posizione
          }else{
              marker = new google.maps.Marker({ // se no si crea un nuovo marker
                  position: location, 
                  map: map
              });
          }
          //document.getElementById('lat').value=location.lat();
          //document.getElementById('lng').value=location.lng();
          getAddress(location);           
      }
      
      
      
      
      function getAddress(latLng) {
          geocoder.geocode( {'latLng': latLng},
            function(results, status) {
              if(status == google.maps.GeocoderStatus.OK) {
                if(results[0]) {
                	alert(results[0].formatted_address);
                }
                else {
                	alert("No results");
                }
              }
              else {
            	  alert(status);
              }
            });
          }
      
      google.maps.event.addDomListener(window, 'load', initialize);      
        