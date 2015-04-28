modules = {
	chart {
		resource url:[dir:'js', file:'chartUtils.js']
		resource url:'http://www.google.com/jsapi', attrs:[type: 'js']
		resource url:[dir:'js', file:'jquery.multiselect.min.js']
		resource url:[dir:'css', file:'jquery.multiselect.css']
	}

	select {
		resource url:[dir:'js/bootstrap-select', file:'bootstrap-select.min.js']
		resource url:[dir:'css/bootstrap-select', file:'bootstrap-select.min.css']
	}

	editable {
		resource url:[dir:'js/bootstrap-editable', file:'bootstrap-editable.min.js']
		resource url:[dir:'css/bootstrap-editable', file:'bootstrap-editable.css']
		resource url:[dir:'js/bootstrap-editable', file:'bootstrap-textdeletable.js']
	}

    wysiwyg {
        dependsOn 'jquery'
        defaultBundle false

        resource url: 'js/ckeditor/ckeditor.js', disposition: 'head', exclude: 'hashandcache'
		resource url: 'js/ckeditor/config.js'
		resource url: 'js/ckeditor/styles.js'
		resource url: 'js/ckeditor/lang/en.js'
		resource url: 'js/ckeditor/plugins/icons.png'
		resource url: 'js/ckeditor/contents.css'
		resource url: 'js/ckeditor/skins/moono/editor_gecko.css'
		resource url: 'js/ckeditor/skins/moono/dialog.css'
		resource url: 'js/ckeditor/plugins/image/dialogs/image.js'
		resource url: 'js/ckeditor/plugins/dialog/dialogDefinition.js'
		resource url: 'js/ckeditor/plugins/link/dialogs/anchor.js'
		resource url: 'js/ckeditor/plugins/link/dialogs/link.js'
		resource url: 'js/ckeditor/plugins/table/dialogs/table.js'
		resource url: 'js/ckeditor/plugins/tabletools/dialogs/tableCell.js'
		resource url: 'js/ckeditor/plugins/about/dialogs/about.js'
		resource url: 'js/ckeditor/plugins/specialchar/dialogs/specialchar.js'
        resource url: 'js/wysiwyg.js'
    }

	timer {
		resource url:[dir:'js', file:'jquery.timer.js']
	}

	moment {
		resource url:[dir:'js', file:'moment.min.js']
	}

	jszip {
		resource url:[dir:'js', file:'jszip.min.js']
	}

	ddslick {
		resource url:[dir:'js', file:'jquery.ddslick.min.js']
	}
	
	'theme.Bootstrap' {
		resource id:'styling', url:[dir:'css', file:'styling.css']
	}

	'theme.Bootstrap.sidebar' {
		// Add your 'sidebar' specific CSS/JS files here
	}

	'theme.Bootstrap.report' {
		// Add your 'report' specific CSS/JS files here
	}

	'theme.Bootstrap.dataentry' {
		// Add your 'dataentry' specific CSS/JS files here
	}

	'theme.Bootstrap.dialog' {
		// Add your 'form' specific CSS/JS files here
	}

	'theme.Bootstrap.main' {
		// Add your 'main' specific CSS/JS files here
	}

	'theme.Bootstrap.home' {
		// Add your 'main' specific CSS/JS files here
	}
	// Resources for your custom UI Set
	'ui.Bootstrap' {
		dependsOn 'jquery', 'bootstrap', 'bootstrap-js'
		
		resource id:'styling', url:[dir:'css', file:'ui-styling.css']
		resource id:'hooks', url:[dir:'js', file:'bootstrap-hooks.js']
		resource url:[dir:'js', file:'common.js']
	}
    application {
        resource url:'js/application.js'
    }
}