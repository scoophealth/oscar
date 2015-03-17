
	jQuery(document).ready(function() {

		jQuery( "#jsonDxSearch" ).autocomplete({			
			source: function(request, response) {
				jQuery.ajax({
				    url: ctx + "/dxCodeSearchJSON.do",
				    type: 'POST',
				    data: 'method=search' + (jQuery( '#codingSystem' ).find(":selected").val()).toUpperCase()
				    				+ '&keyword=' 
				    				+ jQuery( "#jsonDxSearch" ).val(),
				  	dataType: "json",
				    success: function(data) {
						response(jQuery.map( data, function(item) { 
							return {
								label: item.description.trim() + ' (' + item.code + ')',
								value: item.code,
								id: item.id
							};
				    	}))
				    }			    
				})					  
			},
			delay: 100,
			minLength: 2,
			select: function( event, ui ) {
				event.preventDefault();
				jQuery( "#jsonDxSearch" ).val(ui.item.label);
				jQuery( '#codeTxt' ).val(ui.item.value);
			},
			focus: function(event, ui) {
		        event.preventDefault();
		        jQuery( "#jsonDxSearch" ).val(ui.item.label);
		    },
			open: function() {
				jQuery( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			},
			close: function() {
				jQuery( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
			}
		})

		jQuery( "#jsonDxSearch" )
		.val("Search")
		.css('color','grey')
		.focus(function(){
			if(this.value == "Search"){
		         this.value = "";
		         jQuery( "#jsonDxSearch" ).css('color','black');
		    } 			
		}).blur(function(){
		    if(this.value==""){
		         this.value = "Search";	
		         jQuery( "#jsonDxSearch" ).css('color','grey');
		    } 
		});
				
	})
