<html>
<head>
    <nav:set path="app/chemistry/SingleRunAnalysisResult"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="actions"/>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="timeCreated"/>
            <gui:column property="multiRunResult">
            	<g:link controller="multiRunAnalysisResult" action="show" id="${it.id}">${it.id}</g:link>
            </gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>