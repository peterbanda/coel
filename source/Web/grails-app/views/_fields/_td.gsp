<g:if test="${attrs.editableUrl}">
	<a href="#" id="${attrs.property ? attrs.property : attrs.label.replaceAll("\\s","")}${attrs.id}" class="plain" data-type="text" data-pk="${attrs.id}" data-mode="inline" data-url="${attrs.editableUrl}" data-title="Enter ${attrs.label}">
    	${value}
    </a>
    <r:script>
		$(document).ready(function() {
    		$('#${attrs.property ? attrs.property : attrs.label.replaceAll("\\s","")}${attrs.id}').editable({
    			tpl: "<input type='text' style='width: 450px'>",
    			params: function(params) {
    				var data = {};
    				data['id'] = params.pk;
    				data['property'] = '${attrs.property}';
    				data['value'] = params.value;
    				return data;
  				}
  				
// 				success: function(response, newValue) {
//					if(response.status == 'error') return response.msg; //msg will be shown in editable form
//				}
    		});
    	});
	</r:script>
</g:if>
<g:else>
	${value}
</g:else>