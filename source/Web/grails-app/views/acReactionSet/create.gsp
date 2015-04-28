<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReactionSet.label', default: 'Reaction Set')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
    <theme:layout name="create"/>
    <r:script>
    	$("#speciesSet\\.id").hide();

      	function speciesSetChoice1Selected() {
			$("#speciesSet\\.id").hide();
  		};
      	function speciesSetChoice2Selected() {
			$("#speciesSet\\.id").show();
  		};
    </r:script>
</head>
<body>
    <theme:zone name="details">
    	<ui:field bean="instance" name="label"/>
    	<g:if test="${acSpeciesSets}">
			<ui:field label="Species Set">
	           	<ui:fieldInput>
	           		<div class="row-fluid">
	           			<div class="thumbnail span4">
							<label class="radio">
          						<input name="speciesSetChoice" value="1" checked="checked" type="radio" onchange="speciesSetChoice1Selected();">New
        					</label>
        				</div>
        			</div>
        			<div class="row-fluid">
        				<div class="thumbnail span4">
        					<label class="radio">
          						<input name="speciesSetChoice" value="2" type="radio" onchange="speciesSetChoice2Selected();">
          						Existing
        					</label>
        					<g:select class="span12" name="speciesSet.id" span="3" from="${acSpeciesSets}" optionKey="id"
	           					optionValue="${{it.id + ' : ' + it.name}}"
	           					value="${instance?.speciesSet}"/>
	           			</div>
	           		</div>
				</ui:fieldInput>
			</ui:field>
    	</g:if>
    	<g:else>
			<g:hiddenField name="speciesSetChoice" value="1" />
    	</g:else>
    </theme:zone>
</body>
</html>