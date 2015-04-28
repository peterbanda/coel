var CKEDITOR_BASEPATH = URL_ROOT + '/static/js/ckeditor/';

$("textarea.wysiwyg").each(function() {
    var name = $(this).attr("name");
    CKEDITOR.replace(name);
});