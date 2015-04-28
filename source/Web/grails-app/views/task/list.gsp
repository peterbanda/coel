<html>
<head>
    <nav:set path="app/evolution/Task"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true">
        	<gui:column property="id"/>
            <gui:column label="Type">${bean?.getClass().getName()}</gui:column>
            <gui:column property="timeCreated"/>
            <gui:column property="repeat"><g:checkBox checked="${it}" readonly="readonly" /></gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>
