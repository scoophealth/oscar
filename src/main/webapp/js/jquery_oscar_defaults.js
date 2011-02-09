jQuery(document).ajaxError(function(event, request, options, error) { alert('Error contacting server, please try again. \n'+options.url); });
