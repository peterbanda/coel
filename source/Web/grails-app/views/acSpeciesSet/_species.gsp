    <r:script>

		$(function(){
			$("#labels").hide()

    		$("#main").onEnter( function(event) {
    			// to prevent submission of the main form
    			event.preventDefault();
    		});

    		$("#labels").onEnter( function(event) {
				$.post('${createLink(controller: "acSpecies", action: "saveAjax")}',
						{'parentSet.id': ${instance?.id}, labels: $('#labels').val()},
						addSpeciesRow) 

    			event.preventDefault();
    		});
		});

		function addSpeciesLabelTextField() {
			$("#labels").val('');
			$("#labels").show();
			$("#labels").focus();
		}

		function addSpeciesRow(data) {
			var rowCount = $('#acSpeciesTable tr').length + 1;
			$.each(data.acSpeciesInstances, function(index, acSpeciesInstance) {
				var clazz
				if ((rowCount + index) % 2 == 0)
					clazz = 'odd'
				else
					clazz = 'even'
				var newRow = $('<tr class="' + clazz + '">');

				var editLink = '${createLink(controller:"acSpecies", action: "edit")}?id=' + acSpeciesInstance.id

				var doColumn = $('<td>');
				doColumn.append('<a href="' + editLink + '"><i class="icon-edit"></i></a>');

				newRow.append('<input type="hidden" name="objectId" value="' + acSpeciesInstance.id + '" id="objectId" />');
				newRow.append(doColumn);
				newRow.append('<td><input type="hidden" name="_checked"/><input type="checkbox" name="checked" value="" id="checked"/></td>');
				newRow.append('<td><a href="#" id="label' + acSpeciesInstance.id + '" class="plain" data-type="text" data-pk="' + + acSpeciesInstance.id + '" data-mode="inline" data-url="/acSpecies/updatePropertyAjax" data-title="Enter Label">' + acSpeciesInstance.label + '</a></td>')
				$("#acSpeciesTable").append(newRow);

				$('#label' + acSpeciesInstance.id).editable({
    				params: function(params) {
    					var data = {};
    					data['id'] = params.pk;
    					data['property'] = 'label';
    					data['value'] = params.value;
    					return data;
  					}
				});
								
	      	});

			$("#labels").hide();
			showMessage(data.message);
		}
	</r:script>

   		<ui:field name="acSpeciesSet.species">
    		<ui:fieldInput>
				<div class="row-fluid span4">
					<div class="spacedTop spacedLeft">
						<gui:actionLink icon="icon-plus" onclick="addSpeciesLabelTextField();" hint="Add New"/>
						<g:textField name="labels" placeholder="Enter species label(s)"/>
						<gui:modal id="confirm-species-delete-modal" title="Delete" onclick="doTableSelectionAction('acSpeciesTable','deleteMultiple')" text="Are you sure?"/>
						<gui:actionLink icon="icon-trash" onclick="openModal('confirm-species-delete-modal')" hint="Delete"/>
					</div>
					<gui:table domainName="acSpecies" list="${instance?.variables}" editEnabled="true" checkEnabled="true" displayEmpty="true">
					    <gui:column property="label" editable="true"/>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>