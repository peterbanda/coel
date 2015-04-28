<html>
<head>
   	<nav:set path="app/chemistry/MultiRunAnalysisResult"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="timeCreated"/>
            <gui:column property="spec">
				<g:link controller="multiRunAnalysisSpec" action="show" id="${it.id}">${it.id}</g:link>
            </gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>