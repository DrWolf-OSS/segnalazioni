jQuery(document).ready(function() {

	jQuery('.message').slideDown(250);

	jQuery('.message').click(function() {
		jQuery(this).slideUp(250);
	});
});


