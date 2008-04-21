var initial_image_index = '0';
var image_pixels = [];
var image_pixel_els = [];
var title_image_src;
var pixel_highlight_inprogress = false;
var highlighted_pixels = [];
var liquidity = null;

var colors = {};
var colors_history = {};
var schemes_history = {};
var current_color;
var current_matching_color_index = null;
var current_matching_scheme_index = null;
var edit_scheme_el;
var matching_colors = [];
var matching_schemes = [];
var starred_colors = {};
var starred_schemes = {};
var list_entries = [];
var previous_list_entries = [];
var max_list_entries = 20;
var image_colors_active = false;
var default_max_timestamp = 9999999999;
var search_max_timestamp = default_max_timestamp;
var image_colors_inited = false;

var schemes = {};
//var base_url = document.location.href;
//var base_url = 'http://www.colr.org';

var edit_scheme_id;
var edit_scheme = {'id':-1,'colors':[],'tags':[],'timestamp':0};

var counts = {};
var color_offset = 0;
var scheme_offset = 0;
//var timestamp_offset = 0;
var older_than = '';
var newer_than = '';

var cookie_expire1 = new Date();
cookie_expire1.setDate(cookie_expire1.getDate()+365)

var colr_contextmenu = null;
var colr_moremenu = null;
var colr_swatchmenu = null;
var colr_hoverpane = null;
var colr_dialog_box = null;
var colr_messages = null;


// color select stuff
var cs0 = null;

var chunksize = 10;

function colr_onload() {
	colr_messages = new Messages({'id':'colr_messages','base_el':$('select_area')});
	colr_contextmenu = new dropdownMenu({'id':'colr_menu0','title':'','image_url':base_url});
	colr_swatchmenu = new dropdownMenu({'id':'colr_swatchmenu','image_url':base_url});
	colr_hoverpane = new hoverPane({'id':'colr_hoverpane0','image_url':base_url});
	colr_throbber = new Throbber({'image':base_url+'/throbber.gif'});

	connect(document, 'onkeypress', bind(colr_hoverpane.keypress, colr_hoverpane));

	colr_dialog_box = new DialogBox({'id':'colr_dialog_box','element_id':'colr_dialog_box','image_url':base_url});
	new Draggable('colr_dialog_box',{'handle':colr_dialog_box.element.id+'_header','starteffect':null,'endeffect':null});

	//createLoggingPane(false);

	preload_images(['dropdown_menu_arrow.gif','greystar.gif','star.gif','arrow_up.gif','paint_can_icon1.gif','brush_icon1.gif','brush_icon1_grey.gif','color_details.png','expand_scheme.png'], base_url);


	var starred_colors_text = getcookie('starred_colors');
	if (starred_colors_text != null)
		var starred_colors_array = starred_colors_text.split(escape('|'));
	if (starred_colors_array)
		for (var i=0;i<starred_colors_array.length;i++)
			starred_colors[starred_colors_array[i]] = 1;

	var starred_schemes_text = getcookie('starred_schemes');
	if (starred_schemes_text != null)
		var starred_schemes_array = starred_schemes_text.split(escape('|'));
	if (starred_schemes_array)
		for (var i=0;i<starred_schemes_array.length;i++)
			starred_schemes[starred_schemes_array[i]] = 1;

	update_list_entries();
	display_list_entries();
	display_list_nav();

	// color select control
	cs0 = new colorSelect({'id':'cs0',
		 'base_el':$("color_select_icon0"),
		 'onchange_function':cs0_change_update,
		 'onhide_function':cs0_hide_update,
		 'color':$("apptColor").value
	});
	cs0.update_color_box();

	$('apptColor').value = '';


	load_image_from_url(initial_image_index);

	$('list_title').innerHTML = 'Latest Colors & Schemes';


	connect($('site_image_surround'), 'onmouseover', function(evt) {show_image_colors(evt);});
	connect($('site_image_surround'), 'onmouseout', function(evt) {if(evt.target().target && evt.event().target.className == 'pixel_zoom') return;image_colors_active=false;setTimeout(function(){if (!image_colors_active) hide_image_colors()}),500;});
	connect($('site_image_area'), 'onmouseover', hide_image_colors);

	connect($('get_scheme_from_image_button'), 'onclick', get_scheme_from_image);
	new Draggable('liquidity_slider_knob',{'scroll':'liquidity_slider','zindex':null,'starteffect':null,'endeffect':null,'reverteffect':null,
											'onchange':liquidity_slider_onchange,
											'revert':set_liquidity});
	liquidity = getcookie('liquidity');
	if (liquidity == null)
		liquidity = 1;

	var c = getCoords($('liquidity_slider'));
	var kc = getCoords($('liquidity_slider_knob'));
	var x = Math.floor(liquidity *c.w/6 + c.x + 1);
	setElementPosition($('liquidity_slider_knob'), {'x':x});

	liquidity_slider_onchange();
	$('current_liquidity').innerHTML = liquidity;


	// if some color or scheme is already supplied at load time
	if ( matching_colors.length ==1 )
		set_edit_color(matching_colors[0]);

	if ( matching_schemes.length ==1 )
		set_edit_scheme(schemes[matching_schemes[0]]);


	// insert liquidity css
	// commented out after the generated css was placed in the stylesheet
	/*
	var csstext = '';
	for (var liquidity_level=0;liquidity_level<6;liquidity_level++) {	
	if (liquidity_level > 0) {
	var	color_liquidity_offset = 'background-position:-'+(60*(liquidity_level-1))+'px 0px;';
	var	scheme_liquidity_offset = 'background-position:-'+(25*(liquidity_level-1))+'px 0px;';
	}else {
	var	color_liquidity_offset = 'display:none;';
	var	scheme_liquidity_offset = 'display:none;';
	}

	for (var i=0;i<11;i++) {
	csstext += 'body.liquidity_'+liquidity_level+' .color_swatch_splat_'+i+' {'+color_liquidity_offset+'}\n';
	csstext += 'body.liquidity_'+liquidity_level+' .scheme_swatch_splat_'+i+' {'+scheme_liquidity_offset+'}\n';
	}
	}
	insert_inline_css(csstext,'liquidity_css');
	*/

}

function sync_liquidity_slider() {
	var c = getCoords($('liquidity_slider'));
	setElementPosition($('liquidity_slider_knob'), {'y':c.y});

}

function liquidity_slider_onchange(evt) {
	//log(evt);
	var c = getCoords($('liquidity_slider'));
	var kc = getCoords($('liquidity_slider_knob'));

	setElementPosition($('liquidity_slider_knob'), {'y':c.y});
	if (kc.x < c.x)
		setElementPosition($('liquidity_slider_knob'), {'x':c.x});
	if (kc.x > c.x + c.w - kc.w / 2)
		setElementPosition($('liquidity_slider_knob'), {'x':c.x + c.w - kc.w / 2});
	set_liquidity();	
}

function set_liquidity() {
	var c = getCoords($('liquidity_slider'));
	var kc = getCoords($('liquidity_slider_knob'));
	var l = Math.floor((kc.x - c.x) / c.w * 6)
	if (l == liquidity) return;

	liquidity = l;
	$('current_liquidity').innerHTML = liquidity;

	setElementClass($('body'),'liquidity_'+liquidity);

	setcookie('liquidity',liquidity,cookie_expire1,'/',null,null);
}


function hide_image_colors(evt) {
	removeElementClass($('site_image_surround'), 'show_blocks');
	for (var i=0;i<highlighted_pixels.length;i++) {
		hideElement(image_pixel_els[highlighted_pixels[i]]);
	}
	highlighted_pixels = [];
}


function init_image_colors() {
	var image = $("site_image");
	var zoom_width = 10;
	p = getElementPosition(image);

	for (var i in image_pixels) {
		if (!image_pixel_els[i]) {
			image_pixel_els[i] = document.createElement('div');
			image_pixel_els[i].id = 'image_pixel_'+i;
			image_pixel_els[i].className = 'pixel_zoom';
			$('site_image_surround').appendChild(image_pixel_els[i]);
		}
		disconnectAll(image_pixel_els[i]);

		image_pixel_els[i].style.backgroundColor = '#'+image_pixels[i].color;
		image_pixel_els[i].style.left = (p.x + image_pixels[i].x - (zoom_width))+'px';
		image_pixel_els[i].style.top = (p.y + image_pixels[i].y - (zoom_width))+'px';
		image_pixel_els[i].style.width = ((zoom_width)*2)+'px';
		image_pixel_els[i].style.height = ((zoom_width)*2)+'px';

		connect(image_pixel_els[i],'onmouseup', image_onclick);
	}
	image_colors_inited = true;
}

function show_image_colors(evt) {
	if (!image_colors_inited) {
		init_image_colors();
	}

	image_colors_active = true;

	addElementClass($('site_image_surround'), 'show_blocks');
	evt.stop();
}


function highlight_pixel(color, evt) {
  var image_pos = getElementPosition($("site_image"));
  for (var i in image_pixels) {
    if (!image_pixel_els[i]) continue;
    ip = image_pixels[i];
    if (color == ip.color)
      if (!pixel_highlight_inprogress) {
		highlighted_pixels.push(i);
        image_pixel_els[i].style.backgroundColor = '#'+image_pixels[i].color;
        showElement(image_pixel_els[i]);
        setTimeout(partial(draw_pixel_highlight,i,5,image_pos),100);
      }
  }
}


function draw_pixel_highlight(pixel_id, iteration, p) {
  var highlight_box = image_pixel_els[pixel_id];
  pixel_highlight_inprogress = true;
  
  var width_multiplier = 2;
  var ip = image_pixels[pixel_id];
  
  setElementPosition(highlight_box, {'x':p.x+ ip.x - (5-iteration)*width_multiplier,'y':p.y + ip.y - (8-iteration)*width_multiplier});
  setElementDimensions(highlight_box, {'w':(5-iteration)*width_multiplier*2,'h':(5-iteration)*width_multiplier*2});
  
  if (iteration <= 0) {
    pixel_highlight_inprogress = false;
    return;
  }
  
  setTimeout(partial(draw_pixel_highlight,pixel_id,iteration-1, p),50);
}

function reset_search() {
	older_than='';
	newer_than='';
	search_max_timestamp = default_max_timestamp;
	$('list_title').innerHTML = '';
}

function reset_edit_scheme() {
  current_matching_scheme_index = null;
  edit_scheme = {};
  edit_scheme_id = null;
  edit_scheme.id = -1;
  edit_scheme.colors = new Array();
  edit_scheme.tags = new Array();
}

function clear_messages() {
  colr_messages.show();
}

function search_by_tag(tag_names, random_tag, evt) {
	if (!tag_names && !random_tag) tag_names = $('tag_input').value;
	counts['matching_colors'] = 0;
	counts['matching_schemes'] = 0;

	$('tag_input').value = tag_names;
	color_offset = (color_offset <0)? 0 : color_offset;
	scheme_offset = (scheme_offset <0)? 0 : scheme_offset;
	colr_messages.show('Fetching...');

	var data = queryString(['color_offset','scheme_offset','older_than','newer_than'],[color_offset,scheme_offset,older_than,newer_than]);

	if (random_tag)
		var url = base_url+'/json/search/random';
	else
		var url = base_url+'/json/search/'+tag_names+'?'+data;

	var req = getXMLHttpRequest();
	req.open("GET", url, true);
	var d = sendXMLHttpRequest(req);
	d.addCallback(function(rsp) {
		colr_messages.hide();
		if (rsp.responseText.match(/DOCTYPE/))
			new_window_show(rsp.responseText);

		try {
			jsondata = eval('(' + rsp.responseText + ')');
		} catch (e) {log(e);}

		if (jsondata.success == true) {
			if (jsondata.new_tag_name != null && jsondata.new_tag_name != '')
				$('tag_input').value = jsondata.new_tag_name;

			if ($('tag_input').value != '') 
				$('list_title').innerHTML = '"'+$('tag_input').value + '" matches '+ jsondata.counts['matching_colors'] + ' colors and ' + jsondata.counts['matching_schemes'] + ' schemes';

			matching_colors = [];
			matching_schemes = [];
			update_matching_colors(jsondata.colors);
			update_matching_schemes(jsondata.schemes);
			update_stuff(jsondata);
			current_matching_color_index = null; // no current color
			update_list_entries();
			display_list_entries();
			display_list_nav();
		} else {
			colr_messages.show('Unable to load colors from URL!');
		}

  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function get_random_tag() {
  search_by_tag('',true);
}

function load_colors_from_site(site_url) {
	colr_messages.show('Fetching...');
	var url = base_url+'/json/from_web?url='+site_url;

	var req = getXMLHttpRequest();
	req.open("GET", url, true);
	var d = sendXMLHttpRequest(req);
	d.addCallback(function(rsp) {
		colr_messages.hide();
		try {
			jsondata = eval('(' + rsp.responseText + ')');
		} catch (e) {log(e);}
		if (jsondata.success == true) {
			results = draw_site_color_results(jsondata.urls);
			$('site_color_results').innerHTML = results;
			sync_liquidity_slider();

			var url_color_index = 0;
			for (url in jsondata.urls)
				for (var l1=0;l1<jsondata.urls[url].length;l1++) {
					connect($('url_color_'+url_color_index), 'onclick', site_color_onclick)
					url_color_index++;
				}
		} else {
			colr_messages.show('Unable to load colors from URL!');
		}

	});

	d.addErrback(function(err) {
	new_window_show(err.req.responseText);
	});
  
}

function site_color_onclick(evt) {
	var color = Color.fromBackground(evt.src()).toHexString().replace(/#/,'');
	var el1 = evt.src();
	var el2 = getFirstElementByTagAndClassName('div', 'swatch', $('edit_color'));
	if (!el2)
		el2 = $('edit_color');


	var ghost_el = $('ghost_0');
	var c1 = getCoords(el1);
	var c2 = getCoords(el2);

	var percent_x = Math.floor(c2.w / c1.w * 100) - 25;
	var percent_y = Math.floor(c2.h / c1.h * 100) - 25;
	setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
	setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
	ghost_el.style.backgroundColor = '#'+color;
	showElement(ghost_el);
	Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':.5, 'afterFinish':partial(off_ghost,0)});
	Scale(ghost_el,percent_x,{'scaleY':false, 'duration':.5});
	Scale(ghost_el,percent_y,{'scaleX':false, 'duration':.5});
	setTimeout(function() {ScrollTo($('site_header'),{'duration':.5})},200);

	setTimeout(partial(set_edit_color,color),500);
}



function draw_site_color_results(urls) {
  var results = '';
  var url_color_index = 0;
  for (url in urls) {
    results += '<div class="site_url_link"><a target="_new" href="'+url+'">'+url+'</a></div>';
    results += '<div class="site_url_colors">';
    if (urls[url].length == 0)
      results += '<span class="subdued">None!</span>';
    else {
      urls[url].sort(hsvsort);
      for (var l1=0;l1<urls[url].length;l1++) {
        var color = urls[url][l1];
        results += '<div id="url_color_'+url_color_index+'" class="site_color" style="background-color:#'+color+';">&nbsp;&nbsp;&nbsp;';
        results += '</div>';
        url_color_index++;
      }
    }
    results += '</div>';
  }
  return results;
}


function load_random_flickr_image() {
  colr_throbber.throb($('load_random_flickr_image_button'), 'Fetching Image...');
  var url = base_url+'/random_flickr/?rand='+randomString(4);
  
  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(function(rsp) {
    try {
      //colr_throbber.unthrob($('load_random_flickr_image_button'));
      colr_throbber.throb($('load_random_flickr_image_button'), 'Extracting Colors...');

      jsondata = eval('(' + rsp.responseText + ')');
      if (jsondata.success == true) {
        var image_url = jsondata.url;
        var image_title = jsondata.title;
  		if (image_url.match(/\./))
    		image_url = 'http://'+image_url.replace(/http:\/\//,'');
 
        load_image_from_url(image_url);
      } else {
        colr_messages.show('Error loading flickr image');
      }

    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}

function http_add(url) {
	return 'http://'+url.replace(/http:\/\//,'');
}


function load_image_from_url(image_url, opts) {
	var callback = (opts && opts['callback']) ? opts['callback'] : null;

	hide_image_colors();

	var url = base_url+'/image_pixels.php?image_url='+image_url;

	var req = getXMLHttpRequest();
	req.open("GET", url, true);
	var d = sendXMLHttpRequest(req);
	d.addCallback(function(rsp) {
		try {
			jsondata = eval('(' + rsp.responseText + ')');
		} catch (e) {colr_messages.show('Error loading image color data: '+e);return;}

		if (jsondata.success == true) {
			$("site_image").src = (jsondata.image_src == '')  ? base_url+'/'+image_url : jsondata.image_src;
			$("site_image").style.width = 'auto';
			$("site_image").style.height = 'auto';
			$("image_url").value = jsondata.image_src;
			if (jsondata.image_src != '')
				$("image_url").style.width = jsondata.image_src.length+"ex";
			image_pixels = jsondata.image_pixels;
			init_image_colors();
			setTimeout(sync_liquidity_slider,500);
			if (callback)
				callback();
		} else {
			colr_messages.show('Error loading image color data');
		}
		setTimeout(function() {colr_throbber.unthrob($('load_random_flickr_image_button'))}, 200);
	});

	d.addErrback(function(err) {
		new_window_show(err.req.responseText);
	});
}


function get_scheme_from_image(evt) {
	var num_colors = rand(4)+2;
	clear_edit_scheme();

	if (!image_pixels || image_pixels.length == 0)
		return;

	var chosen_pixels = [];
	for (var i=0;i<num_colors;i++) {
		//alert(image_pixels.length);
		var pixel_index = rand(image_pixels.length-1)
		p = image_pixels[pixel_index];

		if (!array_contains(edit_scheme.colors, p.color)) {
			edit_scheme.colors.push(p.color);
			chosen_pixels.push(pixel_index);
		}
	}
	
	for (var i=0;i<chosen_pixels.length;i++) {
		var pixel_index = chosen_pixels[i];
		var color = image_pixels[pixel_index]['color'];
		var el1 = $('image_pixel_'+pixel_index);
		var el2 = $('add_color_to_scheme');

		var ghost_el = $('ghost_'+i);
		var c1 = getCoords(el1);
		var c2 = getCoords(el2);

		var percent_x = Math.floor(c2.w / c1.w * 100) - 25;
		var percent_y = Math.floor(c2.h / c1.h * 100) - 25;
		setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
		setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
		ghost_el.style.backgroundColor = '#'+color;
		showElement(ghost_el);
		Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':.5, 'afterFinish':partial(off_ghost,i)});
		Scale(ghost_el,percent_x,{'scaleY':false, 'duration':.5});
		Scale(ghost_el,percent_y,{'scaleX':false, 'duration':.5});
		setTimeout(display_edit_scheme,500);
	}
	setTimeout(sync_edit_scheme,2000);
  
	evt.stop();
}

function get_colors_from_image() {
  colr_throbber.throb($('get_colors_from_image_button'),'Getting...');
  var num_colors = rand(4)+2;
  
  if (!image_pixels || image_pixels.length == 0)
    return;
  
  got_colors = new Array();
  for (var i=0;i<num_colors;i++) {
    var pixel_index = rand(image_pixels.length-1)
    p = image_pixels[pixel_index];
    got_colors.push(p.color);
  }
  load_colors({colors_array:got_colors});
}

function expand_scheme(scheme, props) {
	var result_id = props['result_id'];

	var scheme_el = $('match_'+result_id);

	if (result_id == 'edit_scheme')
		scheme_el = $('edit_scheme');
	//var tags_el = 
	var swatch_els = getElementsByTagAndClassName('div', 'scheme_swatch', scheme_el);

	previous_list_entries = list_entries;
	list_entries = [];

	for (var i=0;i<swatch_els.length;i++) {
		if (!$('ghost_'+i)) break;
		var swatch_el = swatch_els[i];

		//var color = MochiKit.Style.computedStyle(swatch_el, 'background-color');
		var color = scheme.colors[i];
	
		var el1 = swatch_el;
		var el2 = $('match_'+i);

		var ghost_el = $('ghost_'+i);
		var c1 = getCoords(el1);
		var c2 = getCoords(el2);

		var percent_x = Math.floor(c2.w / c1.w * 100) - 25;
		var percent_y = Math.floor(c2.h / c1.h * 100) - 25;
		setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
		setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
		ghost_el.style.backgroundColor = '#'+color;
		showElement(ghost_el);
		Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':.5, 'afterFinish':partial(off_ghost,i)});
		Scale(ghost_el,percent_x,{'scaleY':false, 'duration':.5});
		Scale(ghost_el,percent_y,{'scaleX':false, 'duration':.5});

		list_entries.push({'type':'color','id':color,'timestamp':1000-i});
	}


	setTimeout(function(){
		display_list_entries();
		load_colors({colors_array:scheme.colors});
		ScrollTo($('tagged_colors'),{'duration':.5});
		$('list_title').innerHTML = 'Expanded scheme colors <span class="button" id="undo_expand_colors">undo</span>';
		connect( $('undo_expand_colors'), 'onclick', restore_list_entries);
	},500);

}

function restore_list_entries() {
	list_entries = previous_list_entries;
	display_list_entries();
}

 // this looks like crap. Need a better highlight
function highlight_image(tf) {
  var im_el = $("site_image_surround");
  if (tf)
    im_el.style.borderColor = '#ff0000';
  else
    im_el.style.borderColor = '#999999';
}


function update_scheme(scheme_id, properties) {
  var el_id = get_property(properties,'el_id',null);
  if (!el_id) return;
  $(el_id).innerHTML = draw_scheme(scheme_id, properties);
}



function sync_edit_scheme() {
  var scheme_colors = '';

  for (i=0;i<edit_scheme.colors.length;i++) {
    scheme_colors += edit_scheme.colors[i];
    if (i<edit_scheme.colors.length-1)
      scheme_colors += ',';
  }
  

  var url = base_url+'/json/scheme/?colors='+scheme_colors;
  
  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(function(rsp) {
    try {
      //new_window_show(rsp.responseText);
      jsondata = eval('(' + rsp.responseText + ')');
      if (jsondata.success == true) {
        // if scheme already present, replace current scheme with this one
        if (jsondata.new_scheme_id == -1) {
          edit_scheme_id = -1;
          edit_scheme.id = -1;
          edit_scheme.tags = []
        } else {
          update_stuff(jsondata);
          edit_scheme_id = jsondata.new_scheme_id;
          edit_scheme = schemes[edit_scheme_id];
        }
        display_edit_scheme(edit_scheme, {'allow_interaction':true,'el_id':'edit_scheme'});
      } else {
        colr_messages.show('Error syncing scheme');
      }

    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });

}

function display_list_entries() {
	var match_results = '';
	for (var result_index=0;result_index<list_entries.length;result_index++) {
		var result = list_entries[result_index];
		//var el_id = 'match_'+result_index+'_'+result['timestamp'];
		var el_id = 'match_'+result_index;
		if (result['type'] == 'color') {
			match_results += '<div class="color_match" id="'+el_id+'"></div>';
		} else if (result['type'] == 'scheme') {
			match_results += '<div class="scheme_match" id="'+el_id+'"></div>';
		}
	}

	$("list_entries").innerHTML = match_results;
	if (list_entries.length > 0)
		rnd.seed = list_entries[0]['id'].charCodeAt(0);

	for (var result_index=0;result_index<list_entries.length;result_index++) {
		var result = list_entries[result_index];
		//var el_id = 'match_'+result_index+'_'+result['timestamp'];
		var el_id = 'match_'+result_index;
		if (result['type'] == 'color') {
			display_color(result_index,{'allow_interaction':true,'result_id':result_index});
		} else if (result['type'] == 'scheme') {
			display_scheme(result_index,{'allow_interaction':true,'result_id':result_index});
		}
	}
}

function display_list_nav() {
	var prev_results = '';
	var next_results = '';

	if (list_entries.length > 0 && 
			( counts['matching_schemes']+ counts['matching_colors'] > list_entries.length ||
			  counts['matching_schemes'] == 0 && counts['matching_colors'] == 0 && list_entries.length > 0 )) {
		next_results = '';
	}

	if (list_entries.length > 0 && list_entries[0]['timestamp'] < search_max_timestamp && search_max_timestamp != default_max_timestamp) {
		prev_results = '';
	}

	if (counts['matching_schemes'] == 0 && counts['matching_colors'] == 0 && list_entries.length > 0 && search_max_timestamp != default_max_timestamp) {
		prev_results = '';
	}
	
	if (counts['matching_schemes'] + counts['matching_colors'] > 0 && list_entries.length == 0) {
		prev_results = '';
	}

}

function continue_search() {
  matching_colors = [];
  matching_schemes = [];
  search_by_tag($('tag_input').value);
}


function display_colors() {
  var color_results = '';
  
  for (var color_index=0;color_index<matching_colors.length;color_index++) {
    var el_id = 'color_match_'+color_index;
    color_results += '<div class="color_match" id="'+el_id+'"></div>';
  }

  $("color_matches").innerHTML = color_results;
  
  for (var color_index=0;color_index<matching_colors.length;color_index++) {
    var el = $('color_match_'+color_index);

    var color_contents = '';
    if (matching_colors.length == 1 || current_matching_color_index == color_index) {
      display_color(color_index,{'allow_interaction':true});
      current_color_el = el_id;
    } else
      display_color(color_index,{'allow_interaction':false});
  }
}


function display_color(match_index, props) {
	var el = $('match_'+match_index);
	if ( props['edit'] )
		el = $('edit_color');

	var color = list_entries[match_index]['id'];
	el.innerHTML = draw_color(color, props);

	hookup_color_events(el, color, props);
}


function view_color(color, props, evt) {
	if (!props['skip_animation']) {  // Animated transition
		var ghost_el = $('ghost_0');
		ghost_el.style.backgroundColor = '#cccccc';

		var target_el = props['target_el'];
		var target_el = getFirstElementByTagAndClassName('div', 'swatch', $('edit_color'));
		if (!target_el)
			target_el = $('edit_color');

		var duration = ( props['duration'] ) ? props['duration'] : .5;

		var c1 = getCoords(props['source_el']);
		var c2 = getCoords(target_el);
		var percent = Math.floor(c2.w / c1.w * 100);
		setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
		setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
		showElement(ghost_el);
		Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':(duration*2), 'afterFinish':partial(poof_ghost,0)});
		Scale(ghost_el,percent,{'scaleY':false, 'duration':(duration*2)});
		setTimeout(function() {ScrollTo($('site_header'),{'duration':duration})},500);
		setTimeout(partial(display_edit_color,color), duration*1000);
	} else {
		display_edit_color(color);
	}
}


function edit_color(color, props) {
	if (!props['skip_animation']) {  // Animated transition
		var ghost_el = $('ghost_0');
		ghost_el.style.backgroundColor = '#cccccc';

		var target_el = getFirstElementByTagAndClassName('div', 'swatch', $('edit_color'));
		if (!target_el)
			target_el = $('edit_color');

		var c1 = getCoords($('match_'+props['result_id']));
		var c2 = getCoords(target_el);
		var percent = Math.floor(c2.w / c1.w * 100);

		setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
		setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
		showElement(ghost_el);
		Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':1, 'afterFinish':partial(poof_ghost,0)});
		Scale(ghost_el,percent,{'scaleY':false, 'duration':1});
		setTimeout(function() {ScrollTo($('site_header'),{'duration':.5})},500);
	}
	display_edit_color(color);
}

function do_edit_scheme(scheme, props) {
	edit_scheme = scheme;

	if (!props['skip_animation']) {  // Animated transition
		var ghost_el = $('ghost_0');
		ghost_el.style.backgroundColor = '#cccccc';
		var c1 = getCoords($('match_'+props['result_id']));
		var c2 = getCoords($('edit_scheme'));
		var percent = Math.floor(c2.w / c1.w * 100);
		setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
		setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
		showElement(ghost_el);
		Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':1, 'afterFinish':partial(poof_ghost,0)});
		Scale(ghost_el,percent,{'scaleY':false, 'duration':1});
		setTimeout(function() {ScrollTo($('site_header'),{'duration':.5})},500);
	}
	display_edit_scheme();
}

function move_ghost(start_el, end_el, opts) {
	var ghost_el = $('ghost_0');
	var c1 = getCoords(start_el);
	var c2 = getCoords(end_el);
	var percent = Math.floor(c2.w / c1.w * 100);
	setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
	setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
	showElement(ghost_el);
	Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':1, 'afterFinish':poof_ghost});
	Scale(ghost_el,percent,{'scaleY':false, 'duration':1});
	setTimeout(function() {ScrollTo($('site_header'),{'duration':.5})},500);
}


function poof_ghost(ghost_index) {
	if (!ghost_index) ghost_index = '0';
	fade($('ghost_'+ghost_index), {'duration':.4});
}

function off_ghost(ghost_index) {
	hideElement($('ghost_'+ghost_index));
}

function display_edit_color(color) {
	var props = {'edit':true};
	$('edit_color').innerHTML = draw_color(color, props);
 
	hookup_color_events($('edit_color'), color, props);

	cs0.setrgb('#'+color.toUpperCase());
	$('apptColor').value = '#'+color.toUpperCase();
}



function hookup_color_events(el, color, props) {

	var result_id = props['result_id'];
	if (props['edit'])
		result_id = 'edit_color';

  
	// hook up tags 
	var tags_el = getFirstElementByTagAndClassName('div', 'tags', el);
	var tag_els = getElementsByTagAndClassName('span', 'tag', tags_el);
	for (var l1=0;l1<tag_els.length;l1++) {
		var tag_contents = tag_els[l1].innerHTML;
		connect(tag_els[l1],'onclick',partial(search_by_tag, tag_contents, false));
		connect(tag_els[l1],'oncontextmenu',partial(color_current_tag_contextmenu, color, tag_contents));
	}

	// Add tags button 
	var add_tags_el = getFirstElementByTagAndClassName('span', 'button', tags_el);
	connect(add_tags_el,'onclick',partial(add_tags_to_color, color));

	var more_button_el = $('more_button_'+result_id);
	connect(more_button_el,'onclick',partial(toggle_more, 'color', color, props));

	// Edit button
	if (!props['edit']) {
		var edit_button_el = $('edit_button_'+result_id);
		connect(edit_button_el,'onclick',partial(edit_color, color, {'edit':true, 'result_id':props['result_id'] }));
	}


	// hook up star icon
	var favorite_el = getFirstElementByTagAndClassName('span', 'favorite', el);
	connect(favorite_el,'onclick',partial(toggle_star_color, color));

}


function toggle_more(type, id, props) {
	var result_id = props['result_id'];
	if (props['edit'])
		result_id = 'edit_'+type; 

	var more_button_el = $('more_button_'+result_id);
	var more_stuff_el = $('more_stuff_'+result_id);
 
	//if (more_stuff_el.innerHTML == '') {
	if (MochiKit.Style.computedStyle(more_stuff_el, 'display') == 'none') {
		results = '';
		if (type == 'color') {
			var color = id;
			results += '<div style="float:right;text-align:right;">';
			results += '<span id="matching_paints_button_'+result_id+'" class="button small"><img src="'+base_url+'/brush_icon1.gif"/> Matching Paints</span>';
			results += '<br/>';
			results += '<span id="matching_idee_images_button_'+result_id+'" class="button small"><img src="'+base_url+'/idee.gif"/> Matching Id&#233;e images</span>';
			results += '<br/>';
			results += '<span id="matching_istockphotos_button_'+result_id+'" class="button small"><span class="more_swatch" style="background-color:#'+color+';"><img src="'+base_url+'/istockphoto_logo_trans.png"/></span>Matching IstockPhotos</span>';
			results += '</div>';


			results += '<div class="color_hex">'+Color.fromHexString(color).toRGBString()+'</div>';
			results += '<br style="clear:both;"/>';
			results += '<div id="tag_history_'+result_id+'" class="tag_history">';
			results += '<span style="color:#000000;">Tag history: </span>';

			var color_history = colors_history[color];
			for (var history_index in color_history) {
				var historytag = color_history[history_index];

				var tag_id_results = (historytag.d_count >= historytag.a_count) ? '<span class="deleted">'+historytag.name+'</span>' : historytag.name;

				tag_id_results +=" (+"+historytag.a_count;
				if (historytag.d_count)
					tag_id_results += ' -' + historytag.d_count + ')&nbsp;&nbsp;&nbsp;';
				else
					tag_id_results += ')&nbsp;&nbsp;&nbsp;';
				  
				results += '<span id="history_tag_'+history_index+'" class="tag">'+tag_id_results+'</span>';
			}
			results += '</div>'; // history
			
			results += '<br style="clear:both;"/>';

			more_stuff_el.innerHTML = results;
    		connect($('matching_idee_images_button_'+result_id),'onclick',partial(idee_search_color, color));
    		connect($('matching_paints_button_'+result_id),'onclick',partial(get_paint_colors, color));
    		connect($('matching_istockphotos_button_'+result_id),'onclick',partial(istockphoto, color));

		  	// history contextmenu stuff
			var history_el = $('tag_history_'+result_id);
		  	
			var tag_els = getElementsByTagAndClassName('span', 'deleted', history_el);
		  	for (var l1=0;l1<tag_els.length;l1++) {
				var tag_contents = tag_els[l1].innerHTML;
				connect(tag_els[l1],'oncontextmenu',partial(color_removed_tag_contextmenu, color, tag_contents));
		  	}

		} else if (type == 'scheme') {
			var scheme = id;
			var scheme_id = scheme['id'];

			results += '<div style="float:right;text-align:right;">';
			results += '<span id="expand_scheme_colors_button_'+result_id+'" class="button small"><img src="'+base_url+'/expand_scheme.png"/> Expand scheme colors</span>';
			results += '<br/>';
			results += '<span id="matching_idee_images_button_'+result_id+'" class="button small"><img src="'+base_url+'/idee.gif"/> Matching Id&#233;e images</span>';
			results += '<br/>';
			results += '<span id="download_aco_button_'+result_id+'" class="button small">Download scheme as <span class="scheme2aco">.aco</span></span>';
			results += '</div>';

			results += '<div id="tag_history'+result_id+'" class="tag_history">';
			results += '<span style="color:#000000;">Tag history: </span>';

			var scheme_history = schemes_history[scheme_id];
			for (var history_index in scheme_history) {
				var historytag = scheme_history[history_index];

				var tag_id_results = (historytag.d_count >= historytag.a_count) ? '<span class="deleted">'+historytag.name+'</span>' : historytag.name;

				tag_id_results +=" (+"+historytag.a_count;
				if (historytag.d_count)
					tag_id_results += ' -' + historytag.d_count + ')&nbsp;&nbsp;&nbsp;';
				else
					tag_id_results += ')&nbsp;&nbsp;&nbsp;';
				  
				results += '<span id="history_tag_'+history_index+'" class="tag">'+tag_id_results+'</span>';
			}
			results += '</div>'; // scheme_history
	
			results += '<br style="clear:both;"/>';
			more_stuff_el.innerHTML = results;

		  	// history contextmenu stuff
			var history_el = $('tag_history_'+result_id);
		  
		  	var tag_els = getElementsByTagAndClassName('span', 'deleted', history_el);
		  	for (var l1=0;l1<tag_els.length;l1++) {
				var tag_contents = tag_els[l1].innerHTML;
				connect(tag_els[l1],'oncontextmenu',partial(scheme_removed_tag_contextmenu, scheme, tag_contents));
		  	}

    		connect($('matching_idee_images_button_'+result_id),'onclick',partial(idee_search_scheme, scheme));
    		connect($('expand_scheme_colors_button_'+result_id),'onclick',partial(expand_scheme, scheme, {'result_id':result_id}));
    		connect($('download_aco_button_'+result_id),'onclick',partial(scheme2aco, scheme));
		}
		more_button_el.innerHTML = 'Less &#9652;';
		showElement(more_stuff_el);
		//blindDown(more_stuff_el, {'duration':.5,'fps':5,'delay':0,'afterFinish':partial(function(el){el.innerHTML = 'Less &#9652;';},more_button_el)});
		//fade(more_stuff_el, {'duration':.5,'from':0.1,'to':1.0});	
		//setTimeout(partial(function(el){blindDown(el, {'duration':.5})},more_stuff_el),1000);	
  	} else {
		blindUp(more_stuff_el, {'duration':.5,'afterFinish':partial(function(el){el.innerHTML = 'More &#9662;';},more_button_el)});	
		//more_stuff_el.innerHTML = '';
		//more_button_el.innerHTML = 'More &#9662;';
	}
}


function display_schemes() {
	return;
  var scheme_results = '';
  
  for (var scheme_index=0;scheme_index<matching_schemes.length;scheme_index++) {
    var el_id = 'scheme_match_'+scheme_index;
    scheme_results += '<div class="scheme_match" id="'+el_id+'"></div>';
  }

  $("scheme_matches").innerHTML = scheme_results;
  
  for (var scheme_index=0;scheme_index<matching_schemes.length;scheme_index++) {
    var el = $('scheme_match_'+scheme_index);

    var scheme_contents = '';
    if (matching_schemes.length == 1 || current_matching_scheme_index == scheme_index) {
      display_scheme(scheme_index,{'allow_interaction':true});
      edit_scheme_el = el_id;
    } else
      display_scheme(scheme_index,{'allow_interaction':false});
  }
}

function display_scheme(match_index, props) {
  var el = $('match_'+match_index);
  var scheme_id = list_entries[match_index]['id'];
  var scheme = schemes[scheme_id];
  el.innerHTML = draw_scheme(scheme_id, props);
  
  // hook up star icon
  //var tags_el = getFirstElementByTagAndClassName('div', 'tags', el);
  //var icon_els = getElementsByTagAndClassName('img', 'tag_icon', tags_el);
  //if (icon_els[0])
  //connect(icon_els[0],'onclick',partial(toggle_star_scheme, scheme_id, props));
    
    hookup_scheme_events(el, scheme, props);
}

function display_edit_scheme() {
	var props = {'edit':true};
	$('edit_scheme').innerHTML = draw_scheme(-1, props);
	hookup_scheme_events($('edit_scheme'), edit_scheme, props);
}



function set_active_matching_scheme(scheme_index, evt) {
  if (colr_hoverpane.active == true) return;

  if (edit_scheme == matching_schemes[scheme_index] && current_matching_scheme_index != null) return;  // already done!
  
  // remove interaction from previous scheme
  if (current_matching_scheme_index != null)
    display_scheme(current_matching_scheme_index, {'allow_interaction':false});

  // draw active matching scheme "active"
  current_matching_scheme_index = scheme_index;
  edit_scheme = matching_schemes[scheme_index];
  display_scheme(scheme_index, {'allow_interaction':true});

  // display active matching scheme in edit_scheme area
  display_edit_scheme(edit_scheme, {'allow_interaction':false});
}



function hookup_scheme_events(el, scheme, props) {
	// hook up history stuff
	var history_toggle = getFirstElementByTagAndClassName('span', 'history_toggle', el);
	var history_box = getFirstElementByTagAndClassName('div', 'scheme_history', el);
	var result_id = (props['edit']) ? 'edit_scheme' : props['result_id']; 


	// history contextmenu stuff
	var tag_els = getElementsByTagAndClassName('span', 'deleted', history_box);
	for (var l1=0;l1<tag_els.length;l1++) {
		var tag_contents = tag_els[l1].innerHTML;
		connect(tag_els[l1],'oncontextmenu',partial(scheme_removed_tag_contextmenu, scheme, tag_contents));
	}

	// hook up tags 
	var tags_el = getFirstElementByTagAndClassName('div', 'tags', el);
	var tag_els = getElementsByTagAndClassName('span', 'tag', tags_el);
	for (var l1=0;l1<tag_els.length;l1++) {
		var tag_contents = tag_els[l1].innerHTML;
		connect(tag_els[l1],'onclick',partial(search_by_tag, tag_contents, false));
		connect(tag_els[l1],'oncontextmenu',partial(scheme_current_tag_contextmenu, scheme, tag_contents));
	}

	// Add tags button 
	var add_tags_el = getFirstElementByTagAndClassName('span', 'button', tags_el);
	connect(add_tags_el,'onclick',partial(add_tags_to_scheme, scheme));


	var more_button_el = $('more_button_'+result_id);
	connect(more_button_el,'onclick',partial(toggle_more, 'scheme', scheme, props));


	// hook up star icon
	var favorite_el = getFirstElementByTagAndClassName('span', 'favorite', el);
	connect(favorite_el,'onclick',partial(toggle_star_scheme, scheme['id']));


/* 
  var more_base_el = getFirstElementByTagAndClassName('span', 'more_menu_base', el);
  // hook up 'more' menu
  colr_moremenu = new dropdownMenu({'id':'colr_menu1','image_url':base_url,'base_element':more_base_el});
  colr_moremenu.additem({'id':'scheme_colors','text':'<img src="'+base_url+'/expand_scheme.png"/> See scheme colors','action':partial(expand_scheme, scheme)});
  colr_moremenu.additem({'id':'idee_search_scheme','text':'<img src="'+base_url+'/idee.gif"/> Matching Id&#233;e images','action':partial(idee_search_scheme, scheme)});
  colr_moremenu.additem({'id':'paint','text':'<span style="margin-left:21px;">Download scheme as <span class="scheme2aco">.aco</span><span> ','action':partial(scheme2aco, scheme)});
*/
  
  // hook up swatches
	var swatch_els = getElementsByTagAndClassName('div', 'scheme_swatch', el);
	for (var l1=0;l1<swatch_els.length;l1++) {
		var swatch_el = swatch_els[l1];
		var color = Color.fromBackground(swatch_el).toHexString().toLowerCase().substring(1,7);
		connect(swatch_el,'onmouseover',partial(highlight_pixel, color));
		connect(swatch_el,'onclick',partial(swatch_click, color, swatch_el, props));
		if (props['el_id'] == 'edit_scheme') {
			var remove_swatch_el = swatch_el.nextSibling;
			connect(remove_swatch_el,'onclick',partial(remove_color_from_edit_scheme, color));
		}
	}
  
	if (props['edit']) {

		var add_color_el = $('add_color_to_scheme');

		if (add_color_el)
			connect(add_color_el,'onclick',partial(add_color_to_scheme_start, scheme));
	}

	// Edit button
	if (!props['edit']) {
		var edit_button_el = $('edit_button_'+result_id);
		connect(edit_button_el,'onclick',partial(do_edit_scheme, scheme, {'edit':true, 'result_id':props['result_id'] }));
	}
}

function swatch_click(color, swatch_el, props, evt) {
	var duration = (props['edit']) ? .25 : .5;

	colr_swatchmenu.base_el = swatch_el;
	colr_swatchmenu.clearitems();
	//colr_swatchmenu.additem({'id':'scheme_colors','text':' '});
	colr_swatchmenu.additem({'id':'view_color','text':'View','action':partial(view_color, color, {'source_el':swatch_el,'duration':duration})});
	if (props['edit'])
		colr_swatchmenu.additem({'id':'remove_color','text':'Remove','action':partial(remove_color_from_edit_scheme, color)});
	colr_swatchmenu.show(evt);
  
}

function set_edit_scheme(scheme, evt) {
  if (colr_hoverpane.active == true) return;

  edit_scheme = scheme;
  for (var i=0;i<matching_schemes.length;i++)
    if (matching_schemes[i] == scheme.id) {
      current_matching_scheme_index = i;
      break;
    }

  display_edit_scheme(edit_scheme, {'allow_interaction':false});
}


function image_onclick(evt) {
	if (colr_hoverpane.active == true) return;

	var pixel_el = evt.src();

	var color = Color.fromBackground(pixel_el).toHexString().toLowerCase().substring(1,7);

	var swatch_el = getFirstElementByTagAndClassName('div', 'swatch', $('edit_color'));

	var ghost_el = $('ghost_0');
	var c1 = getCoords(pixel_el);
	var c2 = getCoords(swatch_el);
	var percent = Math.floor(c2.w / c1.w * 100) - 25;
	setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
	setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
	ghost_el.style.backgroundColor = '#'+color;
	showElement(ghost_el);
	Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':.3, 'afterFinish':partial(poof_ghost,0)});
	Scale(ghost_el,percent,{'scaleY':false, 'duration':.3});
	setTimeout(partial(set_edit_color,color),500);

	//set_edit_color(color);
}

function toggle_star_color(color, evt) {
	if (starred_colors[color] && starred_colors[color] == 1) // already starred
		starred_colors[color] = null;
	else
		starred_colors[color] = 1;

	var starred_colors_array = new Array();
	for (var starred_color in starred_colors)
		if (starred_colors[starred_color] == 1)
			starred_colors_array.push(starred_color);

	var starred_colors_text = starred_colors_array.join("|");
	setcookie('starred_colors',starred_colors_text,cookie_expire1,'/',null,null);

	display_list_entries();
	display_edit_color(color);
	evt.stop();
}

function toggle_star_scheme(scheme_id, properties, evt) {

	var el_id = get_property(properties,'el_id','');

	if (starred_schemes[scheme_id] && starred_schemes[scheme_id] == 1) // already starred
		starred_schemes[scheme_id] = null;
	else
		starred_schemes[scheme_id] = 1;

	var starred_schemes_array = new Array();
	for (var starred_scheme_id in starred_schemes)
		if (starred_schemes[starred_scheme_id] == 1 && !starred_scheme_id.match(/\D/))  // id must be numeric
			starred_schemes_array.push(starred_scheme_id);

	var starred_schemes_text = starred_schemes_array.join("|");
	setcookie('starred_schemes',starred_schemes_text,cookie_expire1,'/',null,null);

	display_list_entries();
	display_edit_scheme();
}

function draw_color(color, props) {  // color is a hex string
	var allow_interaction = get_property(props,'allow_interaction',false);
	var show_history = get_property(props,'show_history',false);
	var result_id = get_property(props,'result_id',false);
	if (props['edit'])
		result_id = 'edit_color';

	var results = "";
	if (!color) return;

	if (!colors[color]) { // color not present (not in db?)
		colors[color] = {};
	}
	//var active_text = (allow_interaction) ? ' class="selected"' : '';
	results += '<div class="color">';


	results += '<div class="buttons">';

	if (!props['edit']) {
		results += '<span id="edit_button_'+result_id+'" class="button small"> Edit</span>';
	}
	var imgsrc = (starred_colors[color] && starred_colors[color] == 1) ? 'star.gif' : 'greystar.gif';
	results += '<br/><span class="button small favorite"> <img class="tag_icon" src="'+base_url+'/'+imgsrc+'" />Favorite</span>';
	results += '<br/><span id="more_button_'+result_id+'" class="button small"> More &#9662;</span>';
	results += '</div>';


	var which_splat = Math.floor(rand(10));
	results += '<div class="swatch" style="background-color:#'+color+';float:left;width:60px;">';
	results += '<div class="splat color_swatch_splat_'+which_splat+'" style="background-image:url(\''+base_url+'/splats/splat'+which_splat+'_60x25.png\');background-repeat:no-repeat;width:60px;">&nbsp;';
	results += '</div>'; // splat
	results += '</div>'; // swatch
	results += "<br style=\"clear:left;\"/>";


	var wrap_text = (colors[color].tags && colors[color].tags.length > 4) ? 'style="white-space:normal;"' : '';
	//results += '<br/>'+list_entries[result_id].timestamp+'<br/>';

	results += '<div class="stuff">';
	results += '<div class="tags"'+wrap_text+'>';

	if (colors[color].tags && colors[color].tags.length > 0) {
		for (var tag_index in colors[color].tags) {
			var tag = colors[color].tags[tag_index]
			results += '<span class="tag">'+tag.name+'</span> ';
		}
	} else {
		results += "<i>no tags</i>&nbsp;&nbsp;";
	}

	results += '<span class="button small"> + Add tags</span>';
	results += '</div>';  // tags

	if (!props['edit'])
		results += '<div class="color_hex">#'+color+'</div>';


//	results += '<div style="float:right;text-align:right;padding-top:2px;">';
//	results += '</div>';
	results += '</div>';  // stuff
	
	results += '<div id="more_stuff_'+result_id+'" class="more_stuff">';
	results += '</div>';  // more_stuff
	results += '<br style="clear:both;"/>';



	results += '</div>';  // color


	//results += '<div class="more">';


	return results;
}

function rnd() {
        rnd.seed = (rnd.seed*9301+49297) % 233280;
        return rnd.seed/(233280.0);
};

function rand(number) {
        return Math.ceil(rnd()*number);
};

function draw_scheme(scheme_id, props) {
	var allow_interaction = get_property(props,'allow_interaction',false);
	var show_history = get_property(props,'show_history',false);
	var el_id = get_property(props,'el_id','');
	var result_id = get_property(props,'result_id','');
	if (props['edit'])
		result_id = 'edit_scheme';

	scheme = schemes[scheme_id];
	if (!scheme)
	scheme = edit_scheme;

	var history_style = (show_history) ? 'block': 'none';
	var active_text = (allow_interaction) ? ' selected' : '';

	var results = '';
	results += '<div class="scheme'+active_text+'">';

	results += '<div class="buttons">';
	results += '<span id="edit_button_'+result_id+'" class="button small"> Edit</span>';

	var imgsrc = (starred_schemes[scheme_id] && starred_schemes[scheme_id] == 1) ? 'star.gif' : 'greystar.gif';
	results += '<br/><span class="button small favorite"> <img class="tag_icon" src="'+base_url+'/'+imgsrc+'" />Favorite</span>';
	results += '<br/><span id="more_button_'+result_id+'" class="button small"> More &#9662;</span>';
	results += '</div>';


	results += '<div class="colors">';
  
  	var color_results = '';
  	for (i=0;i<scheme.colors.length;i++) {
		var which_splat = Math.floor(rand(10));
		results += '<div class="scheme_swatch" style="background-color:#'+scheme.colors[i]+';float:left;">';
		results += '<div class="splat scheme_swatch_splat_'+which_splat+'" style="background-image:url(\''+base_url+'/splats/splat'+which_splat+'_25x25.png\');background-repeat:no-repeat;width:25px;">&nbsp;';
		results += '</div>'; // splat
		results += '</div>'; // swatch
	}

    if (el_id == 'edit_scheme' && allow_interaction)
		results += '<div class="remove_swatch">x</div>';
    
    color_results += " '"+scheme.colors[i]+"' ";

	if ( props['edit'] ) {
		var no_colors = '';
		var no_colors_style = '';
		if (!scheme.colors.length || scheme.colors.length == 0) {
			no_colors = '(no colors)&nbsp;&nbsp;';
			no_colors_style = ' style="width:auto;padding:2px;" ';
		}
		results += '<div id="add_color_to_scheme"'+no_colors_style+'>'+no_colors+'<span style="font-size:x-large;font-weight:bold;">+</span></div>';
	}

	results += '<br style="clear:left;"/>';
	results += '</div>';  // colors div


	results += '<div class="stuff">';

	var wrap_text = (scheme.tags && scheme.tags.length > 4) ? 'style="white-space:normal;"' : '';
	results += '<div class="tags"'+wrap_text+'>';

	//results += '<br/>'+list_entries[result_id].timestamp+'<br/>';
	//results += '<br/>'+list_entries[result_id].id+'<br/>';

	if (scheme.tags && scheme.tags.length > 0)
		for (var tag_index in scheme.tags) {
			var tag = scheme.tags[tag_index]
			results += '<span class="tag">'+tag.name+'</span> ';
		}
		else
    		results += "<i>no tags</i>&nbsp;&nbsp;";
  
	results += '<span class="button small"> + Add tags</span>';
	results += '</div>';  // tags
	
	results += '<div id="more_stuff_'+result_id+'" class="more_stuff">';
	results += '</div>';  // more_stuff
	results += '<br style="clear:both;"/>';
	results += '</div>';  // stuff
 
	results += '</div>';  // scheme div
	return results;
}






function get_random_scheme() {
  colr_throbber.throb($('get_random_scheme_button'),'Getting...');
  var url = base_url+'/json/scheme/random';
  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(function(rsp) {
    try {
      colr_throbber.unthrob($('get_random_scheme_button'));
      jsondata = eval('(' + rsp.responseText + ')');

      if (jsondata.success == true) {
        colr_messages.show(jsondata.messages);
        update_stuff(jsondata);
        scheme = schemes[jsondata.new_scheme_id];
        
        if ($('tag_input').value == '' && !array_contains(matching_schemes,scheme.id))
          matching_schemes = array_unshift(matching_schemes,scheme.id);
          
        set_edit_scheme(scheme);
        display_schemes();
      } else {
        colr_messages.show('Error adding tags! '+jsondata.messages);
      }
    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function show_favorites() {
	colr_throbber.throb($('favorites_button'),'Getting...');
	var starred_colors_array = new Array();
	var starred_schemes_array = new Array();

	for (var color in starred_colors)
		if (starred_colors[color] == 1 && color != '')
			starred_colors_array.push(color);

	for (var scheme in starred_schemes)
		if (starred_schemes[scheme] == 1)
			starred_schemes_array.push(scheme);
	
	info(starred_colors_array);

	if ( starred_colors_array.length > 0 ) 
		load_colors({colors_array:starred_colors_array});

	if ( starred_schemes_array.length > 0 ) {
		matching_schemes = [];
		var f = function(jsondata) {
			update_matching_colors(jsondata.colors);
			update_matching_schemes(jsondata.schemes);
			update_stuff(jsondata);
			current_matching_color_index = null; // no current color
			update_list_entries({ignore_max:true});
			display_list_entries();
		}

		load_schemes(starred_schemes_array, f);
	}

	$('list_title').innerHTML = 'Favorites';
	colr_throbber.unthrob($('favorites_button'));
}

function get_starred_colors() {
  colr_throbber.throb($('favorites_button'),'Getting...');
  var starred_colors_array = new Array();
  for (var color in starred_colors)
    if (starred_colors[color] == 1)
      starred_colors_array.push(color);
      
  load_colors({colors_array:starred_colors_array});

  $('list_title').innerHTML = 'Starred colors';
  colr_throbber.unthrob($('get_starred_colors_button'));
}

function get_starred_schemes() {
  colr_throbber.throb($('get_starred_schemes_button'),'Getting...');
  var starred_schemes_array = new Array();
  for (var scheme in starred_schemes)
    if (starred_schemes[scheme] == 1)
      starred_schemes_array.push(scheme);
  load_schemes(starred_schemes_array);
  $('scheme_match_title').innerHTML = 'Starred schemes';
  colr_throbber.unthrob($('get_starred_schemes_button'));
}


function remove_color_from_edit_scheme(color, evt) {
	if (array_contains(edit_scheme.colors,color)) {
		var temp_colors = new Array;

		for (var i=0;i<edit_scheme.colors.length;i++)
			if (edit_scheme.colors[i] != color)
				temp_colors.push(edit_scheme.colors[i]);

		edit_scheme.colors = temp_colors;

		if (edit_scheme.colors.length > 0){
			display_edit_scheme();
			sync_edit_scheme();
		} else
			clear_edit_scheme();
	}
}

function clear_edit_scheme() {
  reset_edit_scheme();
  display_edit_scheme(edit_scheme, {'allow_interaction':true,'el_id':'edit_scheme'});
  //$('edit_scheme').innerHTML = draw_scheme(edit_scheme_id, {'allow_interaction':true,'el_id':'edit_scheme'});
}


function add_color_to_scheme_start(scheme, evt) {
	colr_hoverpane.show(evt);
}

function add_tags_to_color(color, evt) {
	var tags = prompt("(alphanumeric only, separate by spaces)\n\nAdd new tag(s):");
	if (!tags || tags == undefined || tags == 'undefined') {
		colr_messages.show("no tags added!");
		return;
	}

	colr_messages.show('<img class="minithrobber" style="vertical-align:middle;" src="'+base_url+'/throbber.gif"> Adding...');

	var url = base_url+'/json/color/'+color+'/addtag/';
	var req = getXMLHttpRequest();
	req.open("POST", url, true);
	req.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');

	var data = queryString(['tags'],[tags]);
	var d = sendXMLHttpRequest(req, data);

	d.addCallback(function(rsp) {
		try {
			jsondata = eval('(' + rsp.responseText + ')');
		} catch (e) {log(e);}

		if (jsondata.success == true) {
			colr_messages.show(jsondata.messages);
			update_stuff(jsondata);

			if ($('tag_input').value == '' && !array_contains(matching_colors,color))
				matching_colors = array_unshift(matching_colors,color);
			
			display_list_entries();
			display_edit_color(color);
		} else {
			colr_messages.show('Error adding tags! '+jsondata.messages);
		}

  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function add_tags_to_scheme(scheme) {
  var tags = prompt("(alphanumeric only, separate by spaces)\n\nAdd new tag(s):");
  
  if (!tags || tags == undefined || tags == 'undefined') {
    colr_messages.show("[scheme] no tags added!");
    return;
  }
    
  var scheme_colors = '';
  for (i=0;i<scheme.colors.length;i++) {
    scheme_colors += scheme.colors[i];
    if (i<scheme.colors.length-1)
      scheme_colors += ',';
  }
  
  var url = base_url+'/json/scheme/'+scheme.id+'/addtag/';
  var req = getXMLHttpRequest();
  req.open("POST", url, true);
  req.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
  
  var data = queryString(['tags','colors'],[tags,scheme_colors]);
  var d = sendXMLHttpRequest(req, data);
  
  d.addCallback(function(rsp) {
    try {
      jsondata = eval('(' + rsp.responseText + ')');

      if (jsondata.success == true) {
        colr_messages.show(jsondata.messages);
        update_stuff(jsondata);
        scheme = jsondata.schemes[0];
        edit_scheme = scheme;

		list_entries = array_unshift(list_entries,{'type':'scheme','id':scheme['id'],'timestamp':scheme['timestamp']} );
		display_list_entries();
        display_edit_scheme();
            
      } else {
        colr_messages.show('Error adding tags! '+jsondata.messages);
      }
    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });  
}



function get_paint_colors(color, evt) {
  if (!color.match(/#?([0-9]|[A-F]){6}/i))  // valid hex #color?
    return;
  color = color.replace(/#/g,'');
  
  var url = base_url+'/json/get_paint/'+color;
  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(function(rsp) {
    try {
      //new_window_show(rsp.responseText);
      jsondata = eval('(' + rsp.responseText + ')');
      if (jsondata.success == true) {
        colr_messages.show(jsondata.messages);
        var paint_results = draw_paint_results(color, jsondata.paint_colors);
        colr_dialog_box.reset();
        colr_dialog_box.setContents(paint_results);
        colr_dialog_box.setTitle('<img src="'+base_url+'/brush_icon1.gif"> Matching Paints');
        colr_dialog_box.show();
        colr_dialog_box.anchor(null,[0,0],evt);
      } else {
        colr_messages.show('Error loading image color data');
      }
    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}

function info(txt) {
	try{console.info(txt)}catch(e){};
}

function log_paint_request(paint_url) {
  var url = base_url+'/paint_request/?url='+escape(paint_url);
  
  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(function(rsp) {
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function draw_paint_results(color, paint_colors) {
  var results = '';
  var duron_paint_results = '';
  var behr_paint_results = '';
  var glidden_paint_results = '';
  var l1 = 0;
  var hsv = Color.fromHexString(color).asHSV();
  var extra_style = (hsv.v < .5) ? 'color:#fff;' : '';

  results += '<div style="padding-top:20px;width:90%;text-align:left;white-space:nowrap;clear:left;">';
  results += '<a id="paint_tab0" href="javascript:tab_show(0,\'paint\');" class="tab active"><img src="duron_logo.gif"/></a>';
  results += '<a id="paint_tab1" href="javascript:tab_show(1,\'paint\');" class="tab"><img src="behr_logo.gif"/></a>';
  results += '<a id="paint_tab2" href="javascript:tab_show(2,\'paint\');" class="tab"><img src="glidden_logo.gif"/></a>';
  results += '</div>';
  
  for (paint_company in paint_colors) {
    paint_company_colors = paint_colors[paint_company];
    for (l1=0;l1<paint_company_colors.length;l1++) {
      var link_text = ''
      if (paint_company == 'duron') {
        var code = unescape(paint_company_colors[l1][1][0]);
        var rc = code.split(',');
        var url = 'http://www.duron.com/services/architects_designers/color_spaces/duronCOLOR/search.asp?group_id='+rc[0]+'&row_id='+rc[1];
        //link_text = 'onclick="log_paint_request(\''+url+'\');new_window_show(\''+url+'\')"';
        var paint_color_hex = paint_company_colors[l1][0];
        var paint_color_name = unescape(paint_company_colors[l1][1][1]);
        duron_paint_results += '<div class="paint_result" style="background-color:#'+paint_color_hex+';'+extra_style+'"'+link_text+'>'+paint_color_name+'</div>';
      } else if (paint_company == 'behr') {
        var code = paint_company_colors[l1][1][0]
        var url = 'http://www.behr.com/behrx/colorsmart/colorByName.jsp?colorName='+code;
        //link_text = 'onclick="log_paint_request(\''+url+'\');new_window_show(\''+url+'\')"';
        var paint_color_hex = paint_company_colors[l1][0];
        var paint_color_name = unescape(paint_company_colors[l1][1][1]);
        behr_paint_results += '<div class="paint_result" style="background-color:#'+paint_color_hex+';'+extra_style+'"'+link_text+'>'+paint_color_name+'</div>';
      } else if (paint_company == 'glidden') {
        var code = paint_company_colors[l1][1][0]
        var url = 'http://www.glidden.com/colors/getStripeCard.do?stripecardid='+code;
        //link_text = 'onclick="log_paint_request(\''+url+'\');new_window_show(\''+url+'\')"';
        var paint_color_hex = paint_company_colors[l1][0];
        var paint_color_name = unescape(paint_company_colors[l1][1][1]);
        glidden_paint_results += '<div class="paint_result" style="background-color:#'+paint_color_hex+';'+extra_style+'"'+link_text+'>'+paint_color_name+'</div>';
      }
    }
  }
  
  results += '<div id="paint_tab_area0" class="tab_area">';
  results += duron_paint_results;
  results += '</div>';
  
  results += '<div id="paint_tab_area1" class="tab_area" style="display:none;">';
  results += behr_paint_results;
  results += '</div>';
    
  results += '<div id="paint_tab_area2" class="tab_area" style="display:none;">';
  results += glidden_paint_results;
  results += '</div>';
  
  results = '<div class="paint_results" style="height:'+(l1*2+1)+'em;">'+results+'</div><br style="clear:both;"/>';
  
  return results;
}



function tab_show(tab_num,tab_group_id) {
  var elList, i;
  i=0;
  // update all tabs.
  while ($(tab_group_id+'_tab'+i) && i<100) {
    if (i == tab_num) {
    // If the tab is the new active tab, activate it. 
      addElementClass($(tab_group_id+'_tab'+i), 'active');
      showElement($(tab_group_id+'_tab_area'+i));
      $(tab_group_id+'_tab'+i).blur();
    } else {
      // Otherwise, make sure the tab is deactivated.
      removeElementClass ($(tab_group_id+'_tab'+i),'active');
      hideElement($(tab_group_id+'_tab_area'+i));
    }
    i++;
  }
}

function update_stuff(jsondata) {
	if (jsondata.counts) counts = jsondata.counts;


	if ( jsondata.max_timestamp && search_max_timestamp == default_max_timestamp &&
          ( older_than != '' || newer_than != '') ) {
		search_max_timestamp = jsondata.max_timestamp;
	}

	if (jsondata.max_timestamp && older_than == '') {
		older_than = jsondata.max_timestamp;
	}

	update_colors(jsondata.colors);
	update_colors_history(jsondata.colors_history);
	update_schemes(jsondata.schemes);
	update_schemes_history(jsondata.schemes_history);
	update_list_entries();
}

function update_colors(c) {
  if (!c || !c.length) return;
  for (var l1=0;l1<c.length;l1++)
    colors[c[l1]['hex']] = c[l1];
}

function update_colors_history(ch) {
  for (color_hex in ch)
    colors_history[color_hex] = ch[color_hex];
}

function update_matching_colors(c) {
  if (!c || !c.length) return;
  
  matching_colors = [];
  for (var l1=0;l1<c.length;l1++)
    matching_colors.push(c[l1]['hex']);
}

function update_schemes(s) {
  if (!s || !s.length) return;
  for (var l1=0;l1<s.length;l1++)
    schemes[s[l1]['id']] = s[l1];
}

function update_schemes_history(sh) {
  for (scheme_id in sh)
    schemes_history[scheme_id] = sh[scheme_id];
}


function update_matching_schemes(s) {
  if (!s || !s.length) return;
  
  matching_schemes = [];
  for (var l1=0;l1<s.length;l1++)
    matching_schemes.push(s[l1]['id']);
}

function update_list_entries(args) {
	if ( !args ) args={};
	list_entries = [];

	for (i=0;i<matching_colors.length;i++) {
		var c = colors[matching_colors[i]];
		if (newer_than != '' && c['timestamp'] > newer_than) next;
		list_entries.push({'type':'color','id':c['hex'],'timestamp':c['timestamp']});
	}

	for (i=0;i<matching_schemes.length;i++) {
		var s = schemes[matching_schemes[i]];
		//log('matching scheme: '+s['id'] + ' '+s['timestamp']);
		//if (s['timestamp'] > timestamp_offset) next;
		if (newer_than != '' && s['timestamp'] < newer_than) next;
		list_entries.push({'type':'scheme','id':s['id'],'timestamp':s['timestamp']});
	}

	list_entries.sort(listsort);

	if (newer_than != '')
		list_entries.reverse();

	if ( !args.ignore_max )
		list_entries = list_entries.slice(0,max_list_entries);

	if (newer_than != '')
		list_entries.reverse();
	
}


function update_edit_color(color) {
  if (!color.match(/#?([0-9]|[A-F]){6}/i))  // valid hex #color?
    return;
  color = color.replace(/#/g,'');
  current_color = color;

  var url = base_url+'/json/color/'+color;


  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(function(rsp) {
    try {
      jsondata = eval('(' + rsp.responseText + ')');
      if (jsondata.success == true) {
        //new_window_show(rsp.responseText);
        update_stuff(jsondata);
        
        //display_current_color(color);
		display_edit_color(color);

      } else {
        colr_messages.show('Error loading image color data');
      }

    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });

  return;
}

function load_colors(args) {
	colors_array = args.colors_array;

	colors_string = colors_array.join(',');
	colors_string = colors_string.replace(/#/g,'');

	var url = base_url+'/json/colors/'+colors_string;
	var req = getXMLHttpRequest();
	req.open("GET", url, true);
	var d = sendXMLHttpRequest(req);
	d.addCallback(function(rsp) {
		try {
			jsondata = eval('(' + rsp.responseText + ')');
		} catch (e) {log(e);}

		if (jsondata.success == true) {
			colr_messages.show(jsondata.messages);

			matching_colors = [];
			update_matching_colors(jsondata.colors);
			update_matching_schemes(jsondata.schemes);
			update_stuff(jsondata);
			current_matching_color_index = null; // no current color

			update_list_entries();
			display_list_entries();
		} else {
			colr_messages.show('Error loading image color data');
		}
	});

	d.addErrback(function(err) {
		new_window_show(err.req.responseText);
	});
}



function load_schemes(scheme_ids_array, callback) {
  scheme_ids_string = scheme_ids_array.join(',');
  scheme_ids_string = scheme_ids_string.replace(/#/g,'');

  var url = base_url+'/json/schemes/'+scheme_ids_string;
  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(partial(function(callback, rsp) {
    try {
      jsondata = eval('(' + rsp.responseText + ')');
      if (jsondata.success == true) {
	  	callback(jsondata);
   	  } else {
        colr_messages.show('Error loading image scheme data');
      }
    } catch (e) {log(e);}
  }, callback));
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}

function load_latest_schemes() {
  colr_messages.show('Getting schemes...');
  var url = base_url+'/json/schemes/latest?offset='+scheme_offset;
  var req = getXMLHttpRequest();
  req.open("GET", url, true);
  var d = sendXMLHttpRequest(req);
  d.addCallback(function(rsp) {
    try {
      jsondata = eval('(' + rsp.responseText + ')');
      if (jsondata.success == true) {
	  	colr_messages.hide();
        update_stuff(jsondata);
        update_matching_schemes(jsondata.schemes);
        current_matching_scheme_index = null; // no current scheme
        display_schemes();
        display_list_nav();
        colr_messages.show(jsondata.messages);
      } else {
        colr_messages.show('Error loading image scheme data');
      }
    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function color_current_tag_contextmenu(color, tag, evt) {
  if (evt.modifier().ctrl) return true;
  colr_contextmenu.clearitems();
  colr_contextmenu.additem({'id':'remove_tag_title','text':'Tag "'+tag+'"','disabled':true});
  colr_contextmenu.additem({'id':'remove_tag','text':'Remove this tag','confirm':true,'action':partial(remove_tag_from_color, color, tag)});
  colr_contextmenu.show(evt);
}


function color_removed_tag_contextmenu(color, tag, evt) {
  if (evt.modifier().ctrl) return true;
  colr_contextmenu.clearitems();
  colr_contextmenu.additem({'id':'restore_tag_title','text':'Tag "'+tag+'"','disabled':true});
  colr_contextmenu.additem({'id':'restore_tag','text':'Restore this tag','confirm':true,'action':partial(restore_tag_to_color, color, tag)});
  colr_contextmenu.show(evt);
}


function set_edit_color(new_color) {
	//var new_color = $('apptColor').value.toLowerCase();
	//new_color = new_color.replace(/#/, ''); // remove #
	$('apptColor').value = new_color.toUpperCase();
	display_edit_color(new_color);
	update_edit_color(new_color); // checks back-end for color 
}


function toggle_history(history_toggle_el,history_el, evt) {
  if (MochiKit.Style.computedStyle(history_el, 'display') == 'none') {
    showElement(history_el);
    history_toggle_el.innerHTML = 'History:';
  } else {
    hideElement(history_el);
    history_toggle_el.innerHTML = 'H';
  }
  evt.stop();
}

function toggleElement(el) {
  if (!el) return;
  
  if (el.style.display == 'none')
    showElement(el)
  else
    hideElement(el);
}

function show_color_details(color, evt) {

	colr_dialog_box.reset();
	colr_dialog_box.setTitle('Color Details ');

	var contents = '';
	contents += '<div class="color_details_swatch" style="background-color:#'+color+';">&nbsp;</div>';
	contents += '<br/>#'+color
	contents += '&nbsp;&nbsp;&nbsp;'+Color.fromHexString(color).toRGBString();
	contents += '<br/>Tag history';
	contents += '<div class="tag_history">';

	var color_history = colors_history[color];
	for (var history_index in color_history) {
		var historytag = color_history[history_index];

		var tag_id_contents = (historytag.d_count >= historytag.a_count) ? '<span class="deleted">'+historytag.name+'</span>' : historytag.name;

		tag_id_contents +=" (+"+historytag.a_count;
		if (historytag.d_count)
			tag_id_contents += ' -' + historytag.d_count + ')&nbsp;&nbsp;&nbsp;';
		else
			tag_id_contents += ')&nbsp;&nbsp;&nbsp;';
		  
		contents += '<span id="history_tag_'+history_index+'" class="tag">'+tag_id_contents+'</span>';
	}
	contents += '</div>'; // color_history


    colr_dialog_box.setContents(contents);
    colr_dialog_box.show();
    colr_dialog_box.anchor(null,[0,0],evt);
    $(colr_dialog_box.id+'_header').style.backgroundcolor = '#'+color;

	var history_box = getFirstElementByTagAndClassName('div', 'tag_history', colr_dialog_box.element);

	// history contextmenu stuff
/*
	var history_tag_els = getElementsByTagAndClassName('span', 'deleted', history_box);
	for (var l1=0;l1<history_tag_els.length;l1++) {
	var tag_contents = history_tag_els[l1].innerHTML;
		connect(history_tag_els[l1],'oncontextmenu',partial(color_removed_tag_contextmenu, color, tag_contents));
	}
	*/

	for (var history_index in color_history) {
		var history_tag = $('history_tag_'+history_index);
		var tag_contents = history_tag.innerHTML;

		var history = color_history[history_index];
		connect(history_tag,'oncontextmenu',partial(color_removed_tag_contextmenu, color, tag_contents));
	}  


}

function istockphoto(color) {
  var r = h2d(color.substr(0,2));
  var g = h2d(color.substr(2,2));
  var b = h2d(color.substr(4,2));
  
  var url = 'http://www.istockphoto.com/file_search.php?action=file&color='+r+','+g+','+b;
  new_window_show(url);
}

function idee_search_scheme(scheme) {
  var url = 'http://labs.ideeinc.com/multicolour/#colors=' + scheme.colors.join(",");
  new_window_show(url);
}

function idee_search_color(color) {
  var url = 'http://labs.ideeinc.com/multicolour/#colors=' + color;
  new_window_show(url);
}

function h2d(h) {return parseInt(h,16);}


function scheme2aco(scheme) {
  //var scheme = (scheme_id) ? schemes[scheme_id] :edit_scheme;

  var url = base_url + "/scheme2aco.php?scheme_id="+scheme['id'];
  
  for (i=0;i<scheme.colors.length;i++) {
    url += "&color[]=" + scheme.colors[i];
  }
  
  aco_window = window.open(url, "aco_window", "resizable=yes,status=yes,toolbar=yes,menubar=yes,location=yes,scrollbars=yes width=400,height=200");
}


function scheme_current_tag_contextmenu(scheme, tag, evt) {
  if (evt.modifier().ctrl) return true;
  colr_contextmenu.clearitems();
  colr_contextmenu.additem({'id':'remove_tag_title','text':'Tag "'+tag+'"','disabled':true});
  colr_contextmenu.additem({'id':'remove_tag','text':'Remove this tag','confirm':true,'action':partial(remove_tag_from_scheme, scheme, tag)});
  colr_contextmenu.show(evt);
}


function scheme_removed_tag_contextmenu(scheme, tag, evt) {
  if (evt.modifier().ctrl) return true;
  colr_contextmenu.clearitems();
  colr_contextmenu.additem({'id':'restore_tag_title','text':'Tag "'+tag+'"','disabled':true});
  colr_contextmenu.additem({'id':'restore_tag','text':'Restore this tag','confirm':true,'action':partial(restore_tag_to_scheme, scheme, tag)});
  colr_contextmenu.show(evt);
}


function remove_tag_from_scheme(scheme, tag, evt) {
  colr_messages.show('<img class="minithrobber" style="vertical-align:middle;" src="'+base_url+'/throbber.gif"> Removing...');
  
  var url = base_url+'/json/scheme/'+scheme.id+'/removetag/';
  var req = getXMLHttpRequest();
  req.open("POST", url, true);
  req.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
  
  var data = queryString(['tag'],[tag]);
  var d = sendXMLHttpRequest(req, data);
  
  d.addCallback(function(rsp) {
    try {
      jsondata = eval('(' + rsp.responseText + ')');

      if (jsondata.success == true) {
        colr_messages.show(jsondata.messages);
        update_stuff(jsondata);
        
      //  if ($('tag_input').value == '' && !array_contains(matching_schemes,scheme.id))
      //    matching_schemes = array_unshift(matching_schemes,scheme.id);
            
        display_list_entries();
        display_edit_scheme(scheme);
            
      } else {
        colr_messages.show('Error adding tags! '+jsondata.messages);
      }

    } catch (e) {log(e);}
  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function restore_tag_to_scheme(scheme, tag, evt) {
  colr_messages.show('<img class="minithrobber" style="vertical-align:middle;" src="'+base_url+'/throbber.gif"> Adding...');
  
  var url = base_url+'/json/scheme/'+scheme.id+'/addtag/';
  var req = getXMLHttpRequest();
  req.open("POST", url, true);
  req.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
  
  var data = queryString(['tags'],[tag]);
  var d = sendXMLHttpRequest(req, data);
  
  d.addCallback(function(rsp) {
	try {
	  jsondata = eval('(' + rsp.responseText + ')');
	} catch (e) {log(e);}

	if (jsondata.success == true) {
		colr_messages.show(jsondata.messages);
		update_stuff(jsondata);

		if ($('tag_input').value == '' && !array_contains(matching_schemes,scheme.id))
	  	matching_schemes = array_unshift(matching_schemes,scheme.id);
			
		display_edit_scheme(scheme);
		display_list_entries();
		colr_messages.hide();
		
	} else {
		colr_messages.show('Error adding tags! '+jsondata.messages);
	}

  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function remove_tag_from_color(color, tag, evt) {
  colr_messages.show('<img class="minithrobber" style="vertical-align:middle;" src="'+base_url+'/throbber.gif"> Removing...');
  
  var url = base_url+'/json/color/'+color+'/removetag/';
  var req = getXMLHttpRequest();
  req.open("POST", url, true);
  req.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
  
  var data = queryString(['tag'],[tag]);
  var d = sendXMLHttpRequest(req, data);
  
  d.addCallback(function(rsp) {
    try {
      jsondata = eval('(' + rsp.responseText + ')');
    } catch (e) {log(e);}

    if (jsondata.success == true) {
        colr_messages.show(jsondata.messages);
        update_stuff(jsondata);
        
        if ($('tag_input').value == '' && !array_contains(matching_colors,color))
          matching_colors = array_unshift(matching_colors,color);
            
        update_matching_colors(jsondata.colors);
  		update_list_entries();
        display_list_entries();
        display_current_color(color);
            
    } else {
      colr_messages.show('Error adding tags! '+jsondata.messages);
    }

  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}


function restore_tag_to_color(color, tag, evt) {
  colr_messages.show('<img class="minithrobber" style="vertical-align:middle;" src="'+base_url+'/throbber.gif"> Adding...');
  
  var url = base_url+'/json/color/'+color+'/addtag/';
  var req = getXMLHttpRequest();
  req.open("POST", url, true);
  req.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
  
  var data = queryString(['tags'],[tag]);
  var d = sendXMLHttpRequest(req, data);
  
  d.addCallback(function(rsp) {
    try {
      jsondata = eval('(' + rsp.responseText + ')');
    } catch (e) {log(e);}

    if (jsondata.success == true) {
        colr_messages.show(jsondata.messages);
        update_stuff(jsondata);
        
        if ($('tag_input').value == '' && !array_contains(matching_colors,color))
          matching_colors = array_unshift(matching_colors,color);
        display_current_color(color);
  		update_list_entries();
		display_list_entries();
    } else {
        colr_messages.show('Error adding tags! '+jsondata.messages);
    }

  });
  
  d.addErrback(function(err) {
    new_window_show(err.req.responseText);
  });
}





function cs0_hide_update(new_color) {
  $('apptColor').value = new_color;
  set_edit_color(new_color);
}


function cs0_change_update(new_color) {
  return; // turned off to decrease load on server
  $('apptColor').value = new_color;
}



rnd.today=new Date();
rnd.seed=rnd.today.getTime();
function rnd() {
        rnd.seed = (rnd.seed*9301+49297) % 233280;
        return rnd.seed/(233280.0);
}

function rand(number) {
  return Math.ceil(rnd()*number);
}

function randomString(string_length) {
  var chars = "0123456789abcdefghiklmnopqrstuvwxyz";
  var randomstring = '';
  for (var i=0; i<string_length; i++) {
    var rnum = Math.floor(Math.random() * 36);  // 36 = chars.length
    randomstring += chars.substring(rnum,rnum+1);
  }
  return randomstring;
}



function new_window_show(text) {
  info_window = this.open('', 'info_window', 'resizable=yes,status=yes,scrollbars=yes,locationbar=yes,location=yes,menubar=yes,toolbar=yes,width=1000,height=1000');
  
  if (text.match(/^http/)) {
    info_window.location = text;
  } else {
    doc = info_window.document;
    doc.open('text/html');
    doc.write(text);
    doc.close();
  }
  info_window.focus();
}

function preload_images(images, base_url) {
  var txt = '';
  for (var i=0;i<images.length;i++) {
    txt += '<img src="'+base_url+'/'+images[i]+'"/>'
  }
  
  var imgdiv = createDOM('div', {'id':'preload_images','style':{'display':'none'}});

  document.getElementsByTagName('body').item(0).insertBefore(imgdiv, document.getElementsByTagName('body').item(0).firstChild);
  imgdiv.innerHTML = txt;
}

function listsort(a,b) {
	//return a['timestamp'] - b['timestamp'];
	var d1 = b['timestamp'] - a['timestamp'];
	if (d1 != 0) return d1;

	if (b['id'] > a['id']) return 1
	if (b['id'] < a['id']) return -1
	return 0; 
}

function schemesort(a_id,b_id) {
	var a = schemes[a_id];
	var b = schemes[b_id];

	var d1 = b['timestamp'] - a['timestamp'];
	if (d1 != 0) return d1;

	if (b['id'] > a['id']) return 1
	if (b['id'] < a['id']) return -1
	return 0; 
}

function colorsort(a_id,b_id) {
	var a = colors[a_id];
	var b = colors[b_id];

	var d1 = b['timestamp'] - a['timestamp'];
	if (d1 != 0) return d1;

	if (b_id > a_id) return 1
	if (b_id < a_id) return -1
	return 0; 
}


function hsvsort(a, b) {
  if (a == '000000') return 1;
  if (a == 'ffffff') return -1;
  
  var a1 = Color.fromHexString(a).asHSV();
  var b1 = Color.fromHexString(b).asHSV();
  
  var hyst =.055;

  if (a1.h < b1.h-hyst) return -1;
  if (a1.h > b1.h+hyst) return 1;
  
  if (a1.s < b1.s) return -1;
  if (a1.s > b1.s) return 1;
  
  if (a1.s < b1.s) return -1;
  if (a1.s > b1.s) return 1;
  
  return 0;
}


/**
 * Sets a Cookie with the given name and value.
 *
 * name       Name of the cookie
 * value      Value of the cookie
 * [expires]  Expiration date of the cookie (default: end of current session)
 * [path]     Path where the cookie is valid (default: path of calling document)
 * [domain]   Domain where the cookie is valid
 *              (default: domain of calling document)
 * [secure]   Boolean value indicating if the cookie transmission requires a
 *              secure transmission
 */
function setcookie(name, value, expires, path, domain, secure) {
    document.cookie = name + "=" + escape(value) +
        ((expires) ? "; expires=" + expires.toGMTString() : "") +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        ((secure) ? "; secure" : "");
}

function getcookie(name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(';');
  for(var i=0;i < ca.length;i++) {
    var c = ca[i];
    while (c.charAt(0)==' ') c = c.substring(1,c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
  }
  return null;
}

function array_contains(ar, el) {
  if (!ar || !el) return;
  for(var i=0;i<ar.length;i++)
    if(el == ar[i]) return true;
  return false;
}

function array_unshift(ar, el) {
  var copy = [];
  copy.push(el);
  for (var i = 0; i < ar.length; i++)
     copy.push(ar[i]);
  return copy;
}

function get_property(p, k, d) {
 try {
    if (p === null) return d;
 
    if (typeof(p[k]) == 'boolean' || typeof(p[k]) == 'number')
      return p[k];
 
    return (p[k]) ? p[k] : d;
  } catch (e) {
    //alert(p);
    //alert(e);
    return d;
  }
}

// global variables used for color selects
var color_selects = new Array(0);


function colorSelect(properties) {
  this.id = (properties['id']) ? properties['id'] : null;
  this.base_el = (properties['base_el']) ? properties['base_el'] : null;
  this.onchange_function = (properties['onchange_function']) ? properties['onchange_function'] : null;
  this.onhide_function = (properties['onhide_function']) ? properties['onhide_function'] : null;
  var init_color = (properties['color']) ? properties['color'] : null;

  // current control position
  this.sv_image="";
  this.hexcolor=""
  this.color = null;
  this.hsv = null;
  
  // Time to get funky with the DOM.  Unh.

  // Create a DOM element to hold the color select
  this.color_select_box = document.createElement('div');     // the box around the entire color select
  this.color_select_box.id = this.id+"_color_select_box";
  this.color_select_box.className = "color_select_box";
  this.color_select_box.style.display = "none";
  
  //document.body.appendChild(this.color_select_box);
  document.getElementsByTagName("body").item(0).appendChild(this.color_select_box);
  
  // horizontal & vertical s-v cursors
  this.sv_crosshair_horiz_cursor = createDOM('div', {'id':this.id+'_sv_crosshair_horiz_cursor','class':'sv_crosshair_horiz_cursor','style':{'visibility':'hidden'}});
  this.color_select_box.appendChild(this.sv_crosshair_horiz_cursor);
  
  this.sv_crosshair_vert_cursor = createDOM('div', {'id':this.id+'_sv_crosshair_vert_cursor','class':'sv_crosshair_vert_cursor','style':{'visibility':'hidden'}});
  this.color_select_box.appendChild(this.sv_crosshair_vert_cursor);

  this.sv_select_box_bg = createDOM('div', {'id':this.id+'_sv_select_box_bg','class':'sv_select_box_bg','style':{'height':'256px','width':'256px'}});
  this.color_select_box.appendChild(this.sv_select_box_bg);
  
  this.sv_select_box = createDOM('div', {'id':this.id+'_sv_select_box','class':'sv_select_box','style':{'height':'256px','width':'256px'}});
  this.sv_select_box_bg.appendChild(this.sv_select_box);
  
  this.h_select_box = createDOM('div', {'id':this.id+'_h_select_box','class':'h_select_box'});
  this.color_select_box.appendChild(this.h_select_box);

  this.color_box = createDOM('div', {'id':this.id+'_color_box','class':'color_box'});
  this.color_select_box.appendChild(this.color_box);
  
  this.color_value_box = createDOM('div', {'id':this.id+'_color_value_box','class':'color_value_box'});
  this.color_box.appendChild(this.color_value_box);
  
  this.ok_button = createDOM('div', {'id':this.id+'_ok_button','class':'ok_button'});
  this.color_select_box.appendChild(this.ok_button);
  this.ok_button.innerHTML = "ok";
  
  this.hue_cursor = createDOM('div', {'id':this.id+'_hue_cursor','class':'hue_cursor'});
  this.h_select_box.appendChild(this.hue_cursor);
  

  // used for mapping between mouse positions and color parameters
  this.hue_offset=0;
  this.sat_offset=0;
  this.val_offset=0;
  this.color_select_bounding_box = new Array(4);    // upper-left corner x, upper-left corner y, width, height

  // state information for the controls
  this.initialized=false;
  this.active=false;
  //alert('this should appear only twice! ' + this.id);
  this.h_select_box_focus=false;
  this.sv_select_box_focus=false;

  this.sv_select_box_mousedown = function(evt) {
    this.sv_select_box_focus = true;
    this.sv_update(evt);
    color_select_update(evt);
  }

  this.h_select_box_mousedown = function(evt)  {
    this.h_select_box_focus = true;
    this.hue_cursor_to_color(evt);
    this.sv_update(evt);
    color_select_update(evt);
  }

  this.h_select_box_mouseup = function(evt) {
    this.h_select_box_focus = false;
    //color_select_update(event);
  }

  this.sv_select_box_mouseup = function(evt) {
    this.sv_select_box_focus = false;
  }
  

  // these functions are tied to events (usually).
  // they are the entry points for whatever color_select does

  this.show = function(evt)  {
    // alert ("show color select");
    this.color_select_bounding_box = new Array;
    
    // in mozilla, insert the saturation-value background image
    if (!document.all && this.sv_image)
      this.sv_select_box.style.backgroundImage = "url('"+this.sv_image+"')";

    // make them visible first so we can substract the position of offsetParent 
    this.color_select_box.style.visibility = "visible";
    this.color_select_box.style.display = "block";   
    this.color_select_box.style.position = "absolute";
    this.anchor();
    
    var sv_select_box_pos = getElementPosition(this.sv_select_box);
    this.sat_offset = sv_select_box_pos.y;
    this.val_offset = sv_select_box_pos.x;
    
    h_select_box_pos = getElementPosition(this.h_select_box);
    this.hue_offset = h_select_box_pos.y;
    
    p = getElementPosition(this.base_el);
    this.color_select_bounding_box = [p.x, p.y, 300, 300];
    this.sv_cursor_draw();
    
    // position hue cursor
    color_select_box_pos = getElementPosition(this.color_select_box);
    this.hue_cursor.style.left = h_select_box_pos.x - color_select_box_pos.x-1;
    this.hue_cursor_draw();
    
    this.initialized=true;
    this.active=true;
    if (!this.color_select_box.style)
      alert ("color select box style not found!");
    
    this.update_color_box();
  }
  
  this.anchor = function(el, evt) {
    if (this.base_el) {
      var p = getElementPosition(this.base_el);
      var d = getElementDimensions(this.base_el);
      setElementPosition(this.color_select_box,{'x':p.x,'y':p.y+d.h});
    } else {
      p = evt.mouse().page;
      setElementPosition(this.color_select_box,{'x':p.x-2,'y':p.y-5});
    }  
  }  
  
  this.hide = function(evt) {
    if (this.color_select_box)
      this.color_select_box.style.display = "none";
      
    this.active=false;
    this.unfocus();
    
    try {  // because the hide_update_function might not be defined
      if (this.onhide_function) 
        this.onhide_function(this.hexcolor);
    } catch (e) {}

  }
  
  this.toggle_color_select = function(evt) {
    if (this.active)
      this.hide();
    else
      this.show();
  }

      
  this.select_disable = function() {
    // This function is IE-only!  Moz doesn't need it, and ignores it.
    // When the mouse is dragged, IE attempts to select the DOM objects, 
    // which would be ok, but IE applies a dark highlight to the selection.  
    // This makes the color select look flickery and bad.
    // The solution is to disable IE's selection event when it occurs when the mouse is over
    // the color select.
    
    // But that's not quite enough.  We *do* want to be able to select the #FFFFF hex
    // value.  So we only disable IE selection when the mouse is moving the s-v or hue
    // cursor.
          
    if (this.h_select_box_focus || this.sv_select_box_focus)
      return true;
    else
      return false;
  }

  this.hue_cursor_to_color = function(evt) {
    if (!this.h_select_box_focus) return;
    
    var new_hue_cursor_pos = evt.mouse().page.y - this.hue_offset;
    
    // keep the value sensible
    if (new_hue_cursor_pos > 255)
      new_hue_cursor_pos=255;
    if (new_hue_cursor_pos < 0)
      new_hue_cursor_pos=0;
  
    this.hue_cursor_pos = new_hue_cursor_pos;
    this.hue_cursor_draw(evt);
    this.cursor_to_color(evt);
  }
    
  this.sv_update = function(evt) {
    // map from the mouse position to the new s-v values
    
    // might be possible to get rid of this
    if (!this.sv_select_box_focus) return;
    
    var new_sat_cursor_pos = evt.mouse().page.y - this.sat_offset;
    var new_val_cursor_pos = evt.mouse().page.x - this.val_offset;
    
    // keep the values sensible
    if (new_sat_cursor_pos > 255)
      new_sat_cursor_pos = 255;
    if (new_sat_cursor_pos < 0)
      new_sat_cursor_pos = 0;
      
    if (new_val_cursor_pos > 255)
      new_val_cursor_pos = 255;
    if (new_val_cursor_pos < 0)
      new_val_cursor_pos = 0;
    
    this.sat_cursor_pos = new_sat_cursor_pos;
    this.val_cursor_pos = new_val_cursor_pos;
    
    this.sv_cursor_draw();
    this.cursor_to_color();
  }

  this.hue_cursor_draw = function(evt) {
    if (!this.hue_cursor.style) return;
    if (!this.sv_select_box_bg.style) return;
  
    this.hue_cursor.style.top = this.hue_cursor_pos+1 + "px";
    this.hue_cursor.style.visibility = "visible";
    new_color = Color.fromHSV(this.hsv.h, 1, 1).toRGBString();
    this.sv_select_box_bg.style.background = new_color;
  }
  
  this.sv_cursor_draw = function() {
    if (!this.sv_crosshair_horiz_cursor.style) return;
    if (!this.sv_crosshair_vert_cursor.style) return;
    
    // this is sort of a seat-of-the-pants algorithm for keeping the cursor
    // visible against the s-v background.  There may be better methods.
    var cursor_color=this.val_cursor_pos;
    if (cursor_color==0) cursor_color=.001;
    cursor_color = Math.round(255/(cursor_color/30));
    if (cursor_color > 255) cursor_color = 255;
    if (cursor_color < 0) cursor_color = 0;
    
    this.sv_crosshair_vert_cursor.style.backgroundColor = "rgb("+cursor_color+","+cursor_color+","+cursor_color+")";
    this.sv_crosshair_horiz_cursor.style.borderColor = "rgb("+cursor_color+","+cursor_color+","+cursor_color+")";
    
    // place the s-v cursors.
    this.sv_crosshair_horiz_cursor.style.top = this.sat_cursor_pos+3 + "px";
    this.sv_crosshair_horiz_cursor.style.left = 2 + "px";
    this.sv_crosshair_horiz_cursor.style.visibility = "visible";
  
    this.sv_crosshair_vert_cursor.style.left = this.val_cursor_pos+3 + "px";
    this.sv_crosshair_vert_cursor.style.visibility = "visible";
  }
  
  this.cursor_to_color = function() {
    //calculate real h, s & v
    var h = (255 - this.hue_cursor_pos)/255;
    var s = (255 - this.sat_cursor_pos)/255+.00001;
    var v = this.val_cursor_pos/255;
    this.color = Color.fromHSV(h,s,v);
    this.hsv = {'h':h,'s':s,'v':v};
    
    this.update_color_box();
  }
  
  
  this.unfocus = function()  {
    this.sv_select_box_focus=false;
  }


  this.setrgb = function(hexstring) {
    if (!hexstring) return;

    if (!hexstring.match(/#([0-9]|[A-F]){6}/i))  // valid hex #color?
      return false;
      
    this.color = Color.fromHexString(hexstring)
    this.hsv = this.color.asHSV();

    this.hue_cursor_pos = Math.round(255 - 255*this.hsv.h);
    this.sat_cursor_pos = Math.round(255 - 255*this.hsv.s);
    this.val_cursor_pos = Math.round(this.hsv.v*255);
    
    this.update_color_box();    
    return true;
  }

  
  this.update_color_box = function() {
    if (!this.color) return;
    this.hexcolor = this.color.toHexString().toUpperCase();
    
    try {  // because the change_update_function might not be defined
      if (this.onchange_function)
        this.onchange_function(this.hexcolor);
    } catch (e) {}
    
    // display it!
    if (this.color_value_box)
      this.color_value_box.innerHTML = this.hexcolor;
    
    if (this.color_value_box.style)
      this.color_box.style.background = this.color.toRGBString();
  }

  // push the new color select object onto the
  // global array of color select objects.
  // The array.push() method 
  // doesn't work with objects, only with primitives.
  color_selects[color_selects.length] = this;
  
  // initial values
  if (init_color)
    this.setrgb(init_color)
  else
    this.setrgb("#ffffff");
  
// hook up the appropriate browser events.
  connect(this.sv_select_box, 'onmousedown', bind(this.sv_select_box_mousedown, this));
  connect(this.sv_select_box, 'onmouseup', bind(this.sv_select_box_mouseup, this));
  connect(this.h_select_box, 'onmousedown', bind(this.h_select_box_mousedown, this));
  connect(this.h_select_box, 'onmouseup', bind(this.h_select_box_mouseup, this));
  connect(this.ok_button, 'onmouseup', bind(this.hide, this));
  connect(document, 'onmousemove', color_select_update);
  connect(document, 'onmousedown', color_select_mousedown);
  connect(document, 'onmouseup', color_select_mouseup);
  connect(document, 'onresize', color_select_hideall);
}

function color_select_mousedown(evt) {
  var cs_active = false;

  for (var l1=0;l1<color_selects.length;l1++) {
    var cs = color_selects[l1];
    if (cs.active == false) continue;
    cs_active = true;
  
    // if the mousedown is outside the color_select_box, close it.
    
    mc = evt.mouse().page;
    
    if  (mc.x < cs.color_select_bounding_box[0] ||
        mc.y < cs.color_select_bounding_box[1] ||
        mc.x > (cs.color_select_bounding_box[0]+cs.color_select_bounding_box[2]) ||
        mc.y > (cs.color_select_bounding_box[1]+cs.color_select_bounding_box[3]))
    {
      cs.hide();
    }
  }
  
  if (cs_active && evt && evt.stop) {
    evt.stop();
  }
}


function color_select_hideall() {
  //alert("hiding all color selects!");
  for (var l1=0;l1<color_selects.length;l1++)
  {
    var ob=color_selects[l1];
    ob.hide();
   }
}


function color_select_mouseup() {
  for (var l1=0;l1<color_selects.length;l1++) {
   cs = color_selects[l1];
    cs.unfocus();
  }
}


function color_select_update(evt) {
  var cs_active = false;

  for (var l1=0;l1<color_selects.length;l1++) {
    cs = color_selects[l1];
    if (cs.active == true) {
      cs_active = true;
      cs.sv_update(evt);
      cs.hue_cursor_to_color(evt);
    }
  }
  
  if (evt && evt.stop && cs_active)
    evt.stop();
}




function DialogBox(properties) {
  this.id = (properties['id']) ? properties['id'] : null;
  this.title = (properties['title']) ? properties['title'] : '';
  this.image_url = (properties['image_url']) ? properties['image_url'] : '';
  this.element_id = (properties['element_id']) ? properties['element_id'] : null;
  this.contents = properties['contents'];
  
  var el_id = ''
  if (this.element_id) {
    el_id = this.element_id;
  } else {
    el_id = 'dialog_box';
    if (this.id) el_id += "_"+this.id;
  }
  this.element = createDOM('div', {'id':el_id,'class':'dialog_box','style':{'display':'none','position':'absolute'}});
  
  document.getElementsByTagName("body").item(0).appendChild(this.element);
  var temp = '<div class="header" id="'+this.element.id+'_header"> <span id="'+this.element.id+'_hide" class="close"><img class="dialog_box_close_icon" src="'+this.image_url+'/close_button1.gif" alt="[X]"/></span><div class="title" id="'+this.element.id+'_title">'+this.title+'</div></div>';
  temp += '<div class="contents" id="'+this.element.id+'_contents"></div>';
  temp += '<br style="clear:both;"/>';
  temp += '</div>';
  this.element.innerHTML = temp;
  
  this.hide_element = document.getElementById(this.element.id+'_hide');
  
  this.reset = function()  {
    document.getElementById(this.element.id+'_title').innerHTML = '';
    document.getElementById(this.element.id+'_contents').innerHTML = '';
  }
  
  this.setContents = function(contents) {
    document.getElementById(this.element.id+'_contents').innerHTML = contents;
  }
  
  this.setTitle = function(title) {
    document.getElementById(this.element.id+'_title').innerHTML = title;
  }
  
  this.hide = function(evt) {
    hideElement(this.element);
  }
  
  this.show = function(evt) {
    showElement(this.element);
  }
  
  this.anchor = function(el, offset, evt) {
    if (!el) { // anchor to mouse cursor
      if (evt)
        setElementPosition(this.element,{'x':evt.mouse().page.x+offset[0],'y':evt.mouse().page.y+offset[1]});
    } else {
      c = getCoords(el);
      setElementPosition(this.element, {'x':c.x+offset[0],'y':c.y+c.h+offset[1]});
    }
  }  
  // functions must be declared before being connected to 
  connect(this.hide_element, 'onclick', this, 'hide'); 
}



function hoverPane(properties) {
  this.id = (properties['id']) ? properties['id'] : null;
  this.el = createDOM('div', {'id':this.id+'_el','class':'hoverpane'});
  document.getElementsByTagName("body").item(0).appendChild(this.el);
  this.target_id = null;
  this.color = '';
  this.active = false;
  this.signals = [];
  
  this.hover_pane_text = '<div>Click a color </div>(esc or right-click to cancel)';
  this.hover_pane_invalid_text = '<div style="background-color:#f6cccc">Sorry, it doesn\'t work here</div>';
  
  this.show = function(evt) {
    this.active = true;
    this.signals[0] = connect(document, 'onmouseup', bind(this.add_color_to_scheme, this));
    this.signals[1] = connect(document, 'onmousemove', bind(this.update, this));
    this.signals[2] = connect(document, 'oncontextmenu', bind(this.hide, this));
    this.signals[3] = connect(document, 'onkeypress', bind(this.keypress, this));
    this.update(evt);
  }
  
  this.update = function(evt) {
    if (evt.target().id == 'site_image') {
      this.el.innerHTML = this.hover_pane_invalid_text;
    } else if (evt.target().id != 'site_image'){
      this.target_id = evt.target().id;
      this.color = Color.fromBackground(evt.target());
      this.el.innerHTML = this.hover_pane_text +'<div id="'+this.id+'_swatch" style="background-color:'+this.color.toHexString()+';">&nbsp;</div>';
    }
    
    showElement(this.el);
    setElementPosition(this.el,{'x':evt.mouse().page.x+5,'y':evt.mouse().page.y-5});
  }

	this.add_color_to_scheme = function(evt) {
		var color = this.color.toHexString().toLowerCase().substring(1,7);
		if (array_contains(edit_scheme.colors,color)) {
			var temp = this.el.innerHTML;
			this.el.innerHTML = "<div>Already in scheme!</div>";
			setTimeout(bind(function(txt) {this.el.innerHTML=txt}, this, temp),1000);
		} else {
			edit_scheme.colors.push(color);
			//this.hide(evt);


			var el1 = $(this.id+'_swatch');
			var el2 = $('add_color_to_scheme');

			var ghost_el = $('ghost_0');
			var c1 = getCoords(el1);
			var c2 = getCoords(el2);

			//setTimeout(bind(this.hide, this, evt),100);
			this.hide();

			var percent_x = Math.floor(c2.w / c1.w * 100) - 25;
			var percent_y = Math.floor(c2.h / c1.h * 100) - 25;
			setElementPosition(ghost_el,{'x':c1.x,'y':c1.y});
			setElementDimensions(ghost_el,{'w':c1.w,'h':c1.h});
			ghost_el.style.backgroundColor = '#'+color;
			showElement(ghost_el);
			Move(ghost_el,{'x':c2.x,'y':c2.y,'mode':'absolute', 'duration':.5, 'afterFinish':partial(poof_ghost,0)});
			Scale(ghost_el,percent_x,{'scaleY':false, 'duration':.5});
			Scale(ghost_el,percent_y,{'scaleX':false, 'duration':.5});
			setTimeout(display_edit_scheme,500);
			//display_edit_scheme();
			setTimeout(sync_edit_scheme,2000);
		}
	}
  
  this.keypress = function(evt) {
    if (evt.key() && evt.event().keyCode == 27)
    this.hide(evt);
  }
  
  this.hide = function(evt) {
    this.active = false;
    hideElement(this.el);
   
    for (signal in this.signals)
      disconnect(this.signals[signal]);

    try {
      evt.stop();
    } catch (e) {}  // do nothing

  }
}

function insert_inline_css(csstext, id) {
	csstext = '<!--\n'+csstext+'-->';
	if ($(id)) {
      removeElement($(id));
	}	
	var s = document.createElement('style');
	s.setAttribute("type", "text/css");
	s.setAttribute("id", id);
	if ( s.styleSheet ) {
	  s.styleSheet.cssText = csstext;
	} else {
	  var cssText = document.createTextNode(csstext);
	  s.appendChild(cssText);
	}
	document.getElementsByTagName('head').item(0).appendChild(s);
}

function getCoords(el, relative) {
  var p = getElementPosition(el, relative);
  var d = getElementDimensions(el);
  return {'x':p.x,'y':p.y,'w':d.w,'h':d.h};
}


function Messages(properties) {
  this.id = (properties['id']) ? properties['id'] : null;
  this.base_el = (properties['base_el']) ? properties['base_el'] : document.getElementsByTagName("body").item(0);
  this.el = createDOM('div', {'id':this.id+'_el','class':'messages'});
  document.getElementsByTagName("body").item(0).appendChild(this.el);

  
  this.draw = function(messages) {
      
    var message_text = '';
    for (var l1=0;l1<messages.length;l1++) {
      var message = messages[l1];
      message = message.replace(/\[E\](.+)/g, function($str, $1) { return '<div class="error_message"/>'+$1+'</div>';});
      message = message.replace(/\[I\](.+)/g, function($str, $1) { return '<div class="info_message"/>'+$1+'</div>';});
      message = message.replace(/\[W\](.+)/g, function($str, $1) { return '<div class="warning_message"/>"'+$1+'</div>';});
      message_text += message;
    }
  
    var results = '';
    results += '<div class="messages_color" style="float:left;padding:5px;">';
    results += message_text;
    results += '</div>';
    return results;
  }
  
  this.show = function(messages) {
    if (typeof(messages) == 'string')
      messages = [messages];
    
    if (!messages || messages.length == 0) return;
  
    this.el.innerHTML = this.draw(messages);
    showElement(this.el);
    this.position();
    connect(this.el, 'onclick', bind(this.hide, this));
  }
  
  this.position = function() {
    var d = getElementDimensions(this.el);
    var c = getCoords(this.base_el);
    var x = Math.floor((c.w - d.w) /2);
    var y = Math.floor(c.y + 20 + document.body.scrollTop);
    if (document.documentElement)
      y += document.documentElement.scrollTop;
    
    setElementPosition(this.el, {'x':x,'y':y});
  }
  
  this.hide = function() {
    hideElement(this.el);
  }
}

function Throbber(properties) {
  this.image = (properties['image']) ? properties['image'] : null; // throbber image

  this.throb_elements = {};  // currently active throbber elements
  this.throb_contents = {};  // pre-throb contents of the active throbber elements

  this.throb = function(el,text) {
      if (!el) return;
      
      if (!this.throb_elements[el.id]) {
        this.throb_elements[el.id] = el;
	this.throb_contents[el.id] = el.innerHTML;
      }
      el.innerHTML = '<img class="minithrobber" style="vertical-align:top;" src="'+this.image+'">'+text;
  }

  this.unthrob = function(el) {
      if (this.throb_elements[el.id])
          this.throb_elements[el.id].innerHTML = this.throb_contents[el.id];

    this.throb_elements[el.id] = null;
    this.throb_contents[el.id] = null;
  }
}






function dropdownMenu(properties) {
  this.id = (properties['id']) ? properties['id'] : null;
  this.title = (properties['title']) ? properties['title'] : null;
  this.image_url = (properties['image_url']) ? properties['image_url'] : '';
  this.base_el = (properties['base_element']) ? properties['base_element'] : null;
  
  
  this.el = document.createElement('div');     // the DOM element that holds the menu
  this.el.className += 'dropdownmenu';
  document.getElementsByTagName("body").item(0).appendChild(this.el);
 
  this.menuitems = [];
  
  if (this.base_el) {
    if (this.title != null)
      this.base_el.innerHTML = this.title;
    addElementClass(this.base_el, 'dropdownmenu_base');
  }

  this.toggle = function(evt) {
    if (this.el.style.display == 'block')
      this.hide(evt);
    else
      this.show(evt);
      
  }
  
  this.show = function(evt) {
    this.hide_others();
    this.render();
    
    if (this.base_el) {
      addElementClass(this.base_el, 'active');
      var p = getElementPosition(this.base_el);
      var d = getElementDimensions(this.base_el);
      setElementPosition(this.el,{'x':p.x,'y':p.y+d.h});
    } else {
      var p = evt.mouse().page;
      setElementPosition(this.el,{'x':p.x-2,'y':p.y-5});
    }
    
    showElement(this.el);
    
    for (var l1=0;l1<this.menuitems.length;l1++) {
      this.menuitems[l1]['awaiting_confirm'] = false;
      var item = this.menuitems[l1];
      var el_id = this.id+'_'+item.id;
      connect($(el_id), 'onmouseover', bind(this.menuitem_highlight, this, $(el_id)));
      connect($(el_id), 'onmouseout', bind(this.menuitem_unhighlight, this, $(el_id)));
      if (item['action']) {
        if (item['confirm'] == true) {
          connect($(el_id), 'onclick', bind(this.menuitem_confirm, this, $(el_id), item['action']));
        } else {
          connect($(el_id), 'onmousedown', item['action']);
        }
      }
    }
	this.toFront(100);
    evt.stop();
  }
  
  this.hide = function() {
    hideElement(this.el);
    if (this.base_el)
      removeElementClass(this.base_el, 'active');
    
    for (var l1=0;l1<this.menuitems.length;l1++) {
      var item = this.menuitems[l1];
      disconnectAll($(item.id));
    }
  }
  
  // to be overridden
  this.hide_others = function() {
  }
  
  
  // hook it up!
  if (this.base_el != null) {
    disconnectAll(this.base_el); // disconnect any previous signals connected to base_element (possibly from prior menu instantiations)
    connect(this.base_el, 'onclick', bind(this.toggle, this));
  }
  connect(document, 'onclick', bind(this.hide, this));
  
  
  this.menuitem_confirm = function(el, action, evt) {
    disconnectAll(el);
    connect(el, 'onmouseover', bind(this.menuitem_highlight, this, el));
    connect(el, 'onmouseout', bind(this.menuitem_unhighlight, this, el));
    el.innerHTML +='<span id="'+this.id+'_confirm" class="menuitem_confirm">  Are you sure?</span>';
    connect($(this.id+'_confirm'), 'onmousedown', action);
    this.blink_el(this.id+'_confirm',2,false);
    evt.stop();
  }
  
  this.menuitem_highlight = function(el, evt) {
    addElementClass(el, 'highlight');
  }
  
  this.menuitem_unhighlight = function(el, evt) {
    removeElementClass(el, 'highlight');
  }
  
  this.additem = function(item) {
    //item['id'] = this.id+'_menuitem_'+this.menuitems.length;
    this.menuitems.push(item);
  }
  
  this.setitem = function(item) {
    var el_id = this.id+'_'+item.id;
    if ($(el_id))
      removeElement($(el_id)); // remove previous menuitem
      
    for (var l1=0;l1<this.menuitems.length;l1++) {
      if (this.menuitems[l1].id != item.id) continue;
      this.menuitems[l1] = item;
      break;
    }
  }
  
  this.render = function() {
    var menu_contents = '';
    for (var l1=0;l1<this.menuitems.length;l1++) {
      var menuitem = this.menuitems[l1];
      var el_id = this.id+'_'+menuitem.id;
      var disabled = (menuitem.disabled == true) ? ' disabled':'';
      var invisible = (menuitem.visible == false) ? ' style="display:none;"':'';
      menu_contents += '<div id="'+el_id+'" class="menuitem'+disabled+'"'+invisible+'>';
      menu_contents += menuitem.text;
      menu_contents += '</div>';
    }
    
    this.el.innerHTML = menu_contents;
  }
  
  this.clearitems = function() {
    this.el.innerHTML = '';
    this.menuitems = [];
  }
  
  this.blink_el = function(el_id, times, onoff, evt) {
    var el = $(el_id);
    if (!el) return;
    if (times == 0) {
      if (!onoff)
        removeElementClass(el,'blink');
      if (onoff)
        addElementClass(el,'blink');
      return;
    }
 
    if (hasElementClass(el,'blink'))
      removeElementClass(el,"blink");
    else
      addElementClass(el,'blink');
 
    setTimeout(bind(this.blink_el, this, el_id, times-1, onoff), 100);
  }

  this.toFront = function(delay) {
    if (delay != true) {
      setTimeout(bind(this.toFront, this, true), 0);
      return;
    }

    var highest_zindex = 0;
    var highest_zindex_el = this.el;
    var dbs = getElementsByTagAndClassName('div', 'dialog_box');

    for (var l1=0;l1<dbs.length;l1++) {
      var db = dbs[l1];

      var zindex = getStyle(db,'z-index')*1;
      if (zindex > highest_zindex) {
        highest_zindex = zindex;
        highest_zindex_el = db;
      }
    }
    setStyle(this.el,{'z-index':(highest_zindex+100)});
  }
}

function file_upload_start() {
	colr_messages.show('<img src="'+base_url+'/throbber.gif"/>Uploading...');
}

function file_upload_complete(response) {
	try {
		jsondata = eval('('+response+')');
		info(jsondata);
		var f = function() {
			colr_messages.hide();
			ScrollTo($('site_header'),{'duration':.5});
			blindDown($('site_image'), {'duration':.5});	
		}
		colr_messages.show('<img src="'+base_url+'/throbber.gif"/>Extracting Colors...');
		
		load_image_from_url('image_files/'+jsondata['temp_filename'], {'callback':f} );

	} catch (e) {colr_messages.show('Error uploading file: '+e);return;}
}


/*
JAX IFRAME METHOD (AIM)
* http://www.webtoolkit.info/
*
**/

var AIM = {

    frame : function(c) {

        var n = 'f' + Math.floor(Math.random() * 99999);
        var d = document.createElement('DIV');
        d.innerHTML = '<iframe style="display:none" src="about:blank" id="'+n+'" name="'+n+'" onload="AIM.loaded(\''+n+'\')"></iframe>';
        document.body.appendChild(d);

        var i = document.getElementById(n);
        if (c && typeof(c.onComplete) == 'function') {
            i.onComplete = c.onComplete;
        }

        return n;
    },

    form : function(f, name) {
        f.setAttribute('target', name);
    },

    submit : function(f, c) {
        AIM.form(f, AIM.frame(c));
		$('file_ext').value = $('image_file').value.replace(/.+\./g,'');
		f.submit();
        if (c && typeof(c.onStart) == 'function') {
            return c.onStart();
        } else {
            return true;
        }
    },

    loaded : function(id) {
        var i = document.getElementById(id);
        if (i.contentDocument) {
            var d = i.contentDocument;
        } else if (i.contentWindow) {
            var d = i.contentWindow.document;
        } else {
            var d = window.frames[id].document;
        }
        if (d.location.href == "about:blank") {
            return;
        }

        if (typeof(i.onComplete) == 'function') {
            i.onComplete(d.body.firstChild.innerHTML);
        }
    }

}
