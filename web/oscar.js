
  function xcenter(width){
	var xpos;
	xpos = parseInt((screen.width - width) / 2);
	return(xpos);
  }

  function ycenter(height){
	var ypos;
	ypos = parseInt((screen.height - height) / 2);
	return(ypos);
  }

  function popup(page, width, height) {
      window.open(page, '', "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width="+width+", height="+height+", left="+xcenter(width)+", top="+ycenter(height));
  }