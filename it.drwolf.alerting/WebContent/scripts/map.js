
var marker;  
var map;

var latField;
var lngField;
var addressField;

function initialize() {
	latField = document.getElementById('mainform:inddec:lat');
	lngField = document.getElementById('mainform:inddec:lng');
	addressField = document.getElementById("mainform:inddec:pac-input");
	
	
	var mapOptions = {
			center: getCenter(),
			zoom: 14,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map-canvas"),
			mapOptions);

	geocoder = new google.maps.Geocoder();

	google.maps.event.addListener(map, 'click', function(event) {
		placeMarker(event.latLng); // aggiungo un marker
		updateAddress(event.latLng); // aggiorno indirizzo nella pagina
	});

	

	
	
	//aggiungo l'autocomlete al campo indirizzo
	 var autocomplete = new google.maps.places.Autocomplete(addressField);
	  autocomplete.bindTo('bounds', map);

	//aggiungo il listener all'autocomplete
	  autocomplete.addListener('place_changed', function() {

		    marker.setVisible(false);
		    var place = autocomplete.getPlace();
		    if (!place.geometry) {
		      window.alert("Autocomplete's returned place contains no geometry");
		      return;
		    }

		    // If the place has a geometry, then present it on a map.
		    if (place.geometry.viewport) {
		      map.fitBounds(place.geometry.viewport);
		    } else {
		      map.panTo(place.geometry.location);
		    }
		    updateAddress(place.geometry.location);
		    marker.setPosition(place.geometry.location);
		    marker.setVisible(true);
		}); 
	
	//il posizionamento del marker iniziale deve essere l'ultima cosa.
	if (latField.value > 0.0 && lngField.value > 0.0){
		placeMarker(map.getCenter());
	}
	

}

function getCenter(){
	var center;
	if (latField.value > 0.0 && lngField.value > 0.0){
		center = new google.maps.LatLng(latField.value, lngField.value);
	}
	else{
		center = new google.maps.LatLng(43.865280, 11.167173);		
	}
	return center;
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
           
}

function updateAddress(location){
	latField.value=location.lat();
	lngField.value=location.lng();
	getAddress(location);
}


function getAddress(latLng) {
	geocoder.geocode( {'latLng': latLng},
			function(results, status) {
		if(status == google.maps.GeocoderStatus.OK) {
			if(results[0]) {
				addressField.value = results[0].formatted_address;
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



        