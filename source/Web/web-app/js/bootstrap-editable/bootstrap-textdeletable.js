(function ($) {
    "use strict";
    
    var TextDeletable = function (options) {
        this.init('textdeletable', options, TextDeletable.defaults);
    };

    //inherit from text input
    $.fn.editableutils.inherit(TextDeletable, $.fn.editabletypes.text);

    $.extend(TextDeletable.prototype, {

        render: function() {
           var buttons = this.$input.parent().parent().find('.editable-buttons')
           var deleteButton = $('<button type="button" class="btn editable-cancel"><i class="icon-trash"></i></button>')
           buttons.append(deleteButton);
           deleteButton.click(this.options.deleteFun);
        }
    });

    TextDeletable.defaults = $.extend({}, $.fn.editabletypes.text.defaults, {
    	deleteFun : function() {
    		alert('Delete function not defined');
    	}
    });

//    console.log("Registering TextDeletable");
    $.fn.editabletypes.textdeletable = TextDeletable;
}(window.jQuery));