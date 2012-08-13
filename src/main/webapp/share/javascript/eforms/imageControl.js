(function($) {
	var imagePathTag = /\$(\{|%7B)oscar_image_path(\}|%7D)/;
		
	/**
	 * Converts image paths with template style tag to URL format using 'cfg_isrc'
	 */
	$.fn.convertImagePaths = function(value) {
		valNode = $("<div>").append(value);
		valNode.find("img").each(function() {
			var that = $(this);
			var src = that.attr("src");
			if (src == undefined || src == "" || src.search(imagePathTag) == -1) { return; };
			that.attr("template", src);
			that.attr("src", src.replace(imagePathTag, cfg_isrc));
		});
		return valNode.html();
	};

	/**
	 * Restores template format image paths.
	 */
	$.fn.restoreImagePaths = function(value) {
		valNode = $("<div>").append(value);
		valNode.find("img").each(function() { 
			var that = $(this);
			var template = that.attr("template");
			if (!(template === undefined || template == "")) {
				that.attr("src", template);
				that.removeAttr("template");
			}
		});
		return valNode.html();
	};
})(jQuery);