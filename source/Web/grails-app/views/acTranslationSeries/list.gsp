<html>
<head>
    <nav:set path="app/chemistry/TranslationSeries"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
    	<gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
        	<gui:column property="name" editable="true"/>
            <gui:column property="timeCreated"/>
            <gui:column property="speciesSet"><g:link controller="acSpeciesSet" action="show" id="${it?.id}">${it?.name}</g:link></gui:column>
        </gui:table>
	</theme:zone>
</body>
</html>