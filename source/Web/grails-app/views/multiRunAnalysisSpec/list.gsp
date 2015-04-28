<html>
<head>
    <nav:set path="app/chemistry/MultiRunAnalysisSpec"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="name"/>
            <gui:column property="timeCreated"/>
            <gui:column property="singleRunSpec">
				<g:link controller="singleRunAnalysisSpec" action="show" id="${it.id}">${it.id}</g:link>
            </gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>
