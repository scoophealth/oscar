// Copyright (c) 2006 - 2007 Gabriel Lanzani (http://www.glanzani.com.ar)
// 
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
// 
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// SEE CHANGELOG FOR A COMPLETE CHANGES OVERVIEW
// VERSION 0.3

Autocompleter.SelectBox = Class.create();

Autocompleter.SelectBox.prototype = Object.extend(new Autocompleter.Base(), {
initialize: function(select, options) {
	this.element = "<input type=\"text\" id=\"" + $(select).id + "_combo\" />"
	new Insertion.Before(select, this.element)
	var inputClasses = Element.classNames(select);
	inputClasses.each(function(inputClass)
		{
			Element.addClassName($(select).id + "_combo", inputClass);
		});
	
	this.update = "<div id=\"" + $(select).id + "_options\" class=\"autocomplete\"></div>"	
	new Insertion.Before(select, this.update)
		
		
    this.baseInitialize($(select).id + "_combo", $(select).id + "_options", options);
    this.select = select;
	this.selectOptions = [];
		
	$(this.element.id).setAttribute('readonly','readonly');
	this.element.readOnly = true;
	if(this.options.debug)alert('input ' + this.element.id + ' and div '+ this.update.id + ' created, Autocompleter.Base() initialized');
	if(!this.options.debug)Element.hide(select);

	var optionList = $(this.select).getElementsByTagName('option');
	var nodes = $A(optionList);

	for(i=0; i<nodes.length;i++){
		this.selectOptions.push("<li id=\"" + nodes[i].value + "\">" + nodes[i].innerHTML + '</li>');
		if (nodes[i].getAttribute("selected")) this.element.value = nodes[i].innerHTML;
		
		if(this.options.debug)alert('option ' + nodes[i].innerHTML + ' added to '+ this.update.id);
	}
	
	Event.observe(this.element, "click", this.activate.bindAsEventListener(this));
	
	if ($(select).selectedIndex >= 0)this.element.value = $(select).options[$(select).selectedIndex].innerHTML;
	
	var self = this;
	this.options.afterUpdateElement = function(text, li) {
		var optionList = $(select).getElementsByTagName('option');
		var nodes = $A(optionList);

		var opt = nodes.find( function(node){
			return (node.value == li.id);
		});
		$(select).selectedIndex=opt.index;
		if(self.options.redirect) document.location.href = opt.value;
		if(self.options.autoSubmit) 
			$(self.options.autoSubmit).submit;
	}
  },

  getUpdatedChoices: function() {
  		this.updateChoices(this.setValues());
  },

  setValues : function(){
		return ("<ul>" + this.selectOptions.join('') + "</ul>");
  },

  setOptions: function(options) {
    this.options = Object.extend({
		//MORE OPTIONS TO EXTEND THIS CLASS
		redirect	: false, // redirects to option value
		debug		: false, //show alerts with information
		autoSubmit	: '' //form Id to submit after change
	}, options || {});
  }
});