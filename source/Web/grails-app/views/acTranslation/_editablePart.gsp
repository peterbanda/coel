    <r:script>

		var type = "point"

		$(function(){
			if ($("#toTime").val())
				switchType();
			$("#fromTime").focus();			
		});

  		function switchType() {
  			if (type == "point")
  				switchTypeToRange();
			else
				switchTypeToPoint();
  		};

  		function switchTypeToRange() {
			$("#toTimeDiv").show();
			$("#typeDiv").html('Range');
			$("label[for='fromTime']").html('From Time');
			type = "range";
		}

  		function switchTypeToPoint() {
			$("#toTimeDiv").hide();
			$("#typeDiv").html('Point');
			$("label[for='fromTime']").html('Apply Time');
			$("#toTime").val('');			
			type = "point";
  		};

	</r:script>


        <f:display bean="instance" property="id" label="Type">
         	<div id="typeDiv" class="span2">
        		Point
        	</div>
	    	<div class="span3 offset1">
    	   		<a href="javascript:void(0);" onclick="switchType();">Change Type</a>
        	</div>
        </f:display>

		<ui:field bean="instance" name="fromTime" label="Apply Time"/>

		<div id="toTimeDiv" style="display:none;">
			<ui:field bean="instance" name="toTime"/>
		</div>

