<r:script>

		$(function(){
			$("#coordinateDiffs").hide()

    		$("#coordinateDiffs").onEnter( function(event) {
				$.post('${createLink(controller: "spatialNeighbor", action: "saveAjax")}',
						{'parent.id': ${instance.id}, coordinateDiffs: $('#coordinateDiffs').val()},
						addNeighbors) 

    			event.preventDefault();
    		});
		});

		function addNeighborLabelTextField() {
			$("#coordinateDiffs").val('');
			$("#coordinateDiffs").show();
			$("#coordinateDiffs").focus();
		}

		function addNeighbors(data) {
			if ($("#neighborCoordinateDiffs:contains('None')").length == 1) {
				$("#neighborCoordinateDiffs").html($('<li>Neighbor</li>'));
			}
			$("#addNeighborLink").data('popover').options.content = 'Add New';
			$("#addNeighborLink").data('popover').options.placement = 'bottom';
			$.each(data.neighborInstances, function(index, neighborInstance) {
				var newItem = $('<li><a href="#" id="neighbor' + neighborInstance.id + '" class="plain" data-type="textdeletable" data-pk="' + + neighborInstance.id + '" data-mode="inline" data-url="/spatialNeighbor/updatePropertyAjax" data-title="Enter Coordinates">' + neighborInstance.coordinateDiffs + '</a></li>')
				$("#neighborCoordinateDiffs").append(newItem);
				$('#neighbor' + neighborInstance.id).editable({
    				params: function(params) {
    					var data = {};
    					data['id'] = params.pk;
    					data['property'] = 'neighborInstance';
    					data['value'] = params.value;
    					return data;
  					},
  					deleteFun: function() {deleteNeighbor(neighborInstance.id)}
				});
	      	});

			$("#coordinateDiffs").hide();
			hideErrors()
			showMessage(data.message);
		}

		function deleteNeighbor(id) {
 			$.post('${createLink(controller: "spatialNeighbor", action: "deleteAjax")}',
				{'id': id},
				function(data) {
					var neighborItem = $("#neighborCoordinateDiffs").find('#neighbor' + id).parent()
					neighborItem.remove()
					hideErrors()
					showMessage(data)
				}
			)
			.fail(function(data) {
				hideMessages()
				showErrorMessage(data.responseText);
			})
		}
</r:script>

<ul class="inline">
	<li>
		<ul id="neighborCoordinateDiffs" class="inline">
			<li>Neighbors</li>
			<g:if test="${!instance.neighbors.isEmpty()}">
				<g:each in="${instance.neighbors}" var="neighbor" >
					<li>
						<a href="#" id="neighbor${neighbor.id}" class="plain" data-type="textdeletable" data-pk="${neighbor.id}" data-mode="inline" data-url="/spatialNeighbor/updatePropertyAjax" data-title="Enter Coordinates">
							${neighbor.coordinateDiffs}
						</a>
						<r:script>
							$(document).ready(function() {
	    						$('#neighbor${neighbor.id}').editable({
    								params: function(params) {
    									var data = {};
    									data['id'] = params.pk;
    									data['property'] = 'coordinateDiffs';
    									data['value'] = params.value;
    									return data;
  									},
  									deleteFun: function() {deleteNeighbor(${neighbor.id})}
								});
							});
						</r:script>
					</li>
				</g:each>
			</g:if>
			<g:else>
				<li><i>None</i></li>
			</g:else>
		</ul>
	</li>
	<li>
		<g:if test="${!instance.neighbors.isEmpty()}">
			<gui:actionLink icon="icon-plus" onclick="addNeighborLabelTextField();" elementId="addNeighborLink" hint="Add New"/>
		</g:if>
		<g:else>
			<gui:actionLink icon="icon-plus" onclick="addNeighborLabelTextField();" elementId="addNeighborLink" hint="Start here to add new neighbors" hint-placement="top" hint-show="true"/>
		</g:else>
		<g:textField name="coordinateDiffs" style="width: 410px" placeholder="Enter (muliple) coordinate diffs as (x11,...,x1n),(x21,...,x2n), etc."/>
	</li>
</ul>