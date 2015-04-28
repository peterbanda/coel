<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acCompartment.label', default: 'Compartment')}" scope="request" />
    <nav:set path="app/chemistry/Compartment"/>
    <theme:layout name="create"/>
    <r:script>
    	$("#reactionSet\\.id").hide();

      	function reactionSetChoice1Selected() {
			$("#reactionSet\\.id").hide();
  		};
      	function reactionSetChoice2Selected() {
			$("#reactionSet\\.id").show();
  		};
    </r:script>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="parentId" value="${parentId}" />
    	<ui:field bean="instance" name="label"/>
    	<g:if test="${acReactionSets}">
			<ui:field label="Reaction Set">
	           	<ui:fieldInput>
	           		<div class="row-fluid">
	           			<div class="thumbnail span4">
							<label class="radio">
          						<input name="reactionSetChoice" value="1" checked="checked" type="radio" onchange="reactionSetChoice1Selected();">New
        					</label>
        				</div>
        			</div>
        			<div class="row-fluid">
        				<div class="thumbnail span4">
        					<label class="radio">
          						<input name="reactionSetChoice" value="2" type="radio" onchange="reactionSetChoice2Selected();">
          						Existing
        					</label>
        					<g:select class="span12" name="reactionSet.id" span="3" from="${acReactionSets}" optionKey="id"
	           					optionValue="${{it.id + ' : ' + it.label}}"
	           					value="${instance?.reactionSet}"/>
	           			</div>
	           		</div>
				</ui:fieldInput>
			</ui:field>
    	</g:if>
    	<g:else>
			<g:hiddenField name="reactionSetChoice" value="1" />
    	</g:else>
    </theme:zone>
</body>
</html>