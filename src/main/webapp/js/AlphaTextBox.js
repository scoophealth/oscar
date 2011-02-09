dojo.require("dojo.widget.ValidationTextbox");
dojo.require("dojo.validate.common");
dojo.provide("dojo.widget.AlphaTextBox");


/*
 	  ****** AlphaTextBox *

 	  A subclass of ValidationTextbox.
 	  Over-rides isValid to test if input is alpha characters only, no numbers allowed.
*/

dojo.widget.defineWidget(
        "dojo.widget.AlphaTextBox",
        dojo.widget.ValidationTextbox,
{
    mixInProperties: function(localProperties, frag){
        // First initialize properties in super-class.
        dojo.widget.AlphaTextBox.superclass.mixInProperties.apply(this, arguments);

        if (localProperties.length){
            this.flags.length = parseInt(localProperties.length);
        }
        if (localProperties.maxlength){
            this.flags.maxlength = parseInt(localProperties.maxlength);
        }
        if (localProperties.minlength){
            this.flags.minlength = parseInt(localProperties.minlength);
        }
    },

    isValid: function(){
        // summary: Over-ride for integer validation
        return dojo.validate.isText(this.textbox.value, this.flags) && this.textbox.value.match('[^0-9]');
    }
})

