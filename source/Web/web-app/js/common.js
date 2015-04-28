$.fn.extend({
	populateKeyValues: function (keyValuePairs, optional) {
		$(this).html("");
		if (optional) $(this).append($('<option>', { value : '' }).text('---'));
		var that = $(this);
		$.each(keyValuePairs, function(key, val) {
			that.append($('<option>', { value : key }).text(val));
		});
	},
    populateAndSortKeyValues: function (keyValuePairs, optional, descOrder) {
        $(this).html("");
        if (optional) $(this).append($('<option>', { value : '' }).text('---'));
        var that = $(this);

        var items = []
        $.each(keyValuePairs, function(k, v) {
            items.push({key: k, value : v});
        });

        items.sort(function(o1, o2) {
            var t1 = o1.key, t2 = o2.key;
            var result = t1 > t2 ? 1 : t1 < t2 ? -1 : 0;
            if (!descOrder)
                return result;
            else
                return -result;
        });

        $.each(items, function(index, v) {
            that.append($('<option>', { value : v.key }).text(v.value));
        });
    }
});

function openModal(name) {
    $('#' + name).modal();
}

function currentDateTimeAsString() {
	var dNow = new Date();
	var dateAsString = dNow.getFullYear() + '-' + (dNow.getMonth()+1) + '-' + dNow.getDate() + '-' + dNow.getHours() + '-' + dNow.getMinutes() + '-' + dNow.getSeconds();
	return dateAsString
}

function saveAs(fileURL, fileName) {
    var save = document.createElement('a');
    save.href = fileURL;
    save.target = '_blank';
    save.download = fileName;

    var evt = document.createEvent('MouseEvents');
    evt.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);

    save.dispatchEvent(evt);
    (window.URL || window.webkitURL).revokeObjectURL(save.href);
}

function getCheckedIds(tableName) {
	var ids = []
	$('#' + tableName + ' tbody tr').each(function() {
		var id = $(this).find('#objectId').val();
		var checked = $(this).find("td input[type=checkbox]").attr("checked");
		if (checked) {
		   	ids.push(id)
		}
	});
	return ids
};

function doTableSelectionAction(tableName, actionName) {
	var ids = getCheckedIds(tableName)
	var controller = $('#' + tableName).find('#domainName').val();
	post('/' + controller + '/' + actionName, {ids: ids})
}

// Post to the provided URL with the specified parameters.
function post(path, parameters) {
	var form = $('<form></form>');

	form.attr("method", "post");
	form.attr("action", path);

	$.each(parameters, function(key, value) {
		var field = $('<input></input>');

		field.attr("type", "hidden");
		field.attr("name", key);
		field.attr("value", value);

		form.append(field);
	});

	// The form needs to be a part of the document in
	// order for us to be able to submit it.
	$(document.body).append(form);
	form.submit();
}

function checkAll(tableName, checkedFlag) {
	$('#' + tableName + ' tbody tr').each(function() {
		var id = $(this).find("td").eq(2).html();
		$(this).find("td input[type=checkbox]").attr("checked", checkedFlag);
	});
};

function showMessage(text) {
	var closeX = '<a class="close" data-dismiss="alert" href="#">×</a>'
	var messageBlock = $('<div class="alert alert-block alert-info">')
	messageBlock.append(closeX)
	messageBlock.append(text)
	$('#messageDiv').hide();
	$('#messageDiv').html(messageBlock);
	$('#messageDiv').fadeIn('2000');
}

function showErrorMessage(message) {
	var closeX = '<a class="close" data-dismiss="alert" href="#">x</a>'
	$('#errorDiv').hide();
	var ul = $('<ul class="errors" role="alert">');
	var innerDiv = $('<div class="alert alert-block alert-error">');
	innerDiv.append(closeX);
	innerDiv.append(message);
	ul.append(innerDiv);	
	$('#errorDiv').html(ul);
	$('#errorDiv').fadeIn('2000');
}

function hideMessages() {
	$('#messageDiv').fadeOut('2000');
	$('#messageDiv').html('');
}

function showErrors(errors) {
	if (errors.length > 0) {
		var closeX = $('<a class="close" data-dismiss="alert" href="#">x</a>');
		$('#errorDiv').hide();
		var ul = $('<ul class="errors" role="alert">');
		$.each(errors, function(index, error) {
			ul.append($('<div class="alert alert-block alert-error"></div>').text(closeX.html() + error.message));	
		});
		$('#errorDiv').html(ul);
		$('#errorDiv').fadeIn('2000');
	}
}

function hideErrors() {
	$('#errorDiv').fadeOut('2000');
	$('#errorDiv').html('');
}

(function($) {
    $.fn.onEnter = function(func) {
        this.bind('keypress', function(e) {
            if (e.keyCode == 13) func.apply(this, [e]);    
        });               
        return this; 
     };
})(jQuery);