<html>
<head>
	<nav:set path="app"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" deleteEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="name"/>
            <gui:column property="description"/>
        </gui:table>
    </theme:zone>
</body>
</html>