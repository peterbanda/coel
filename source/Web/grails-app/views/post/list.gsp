<html>
<head>
    <nav:set path="app"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="title"/>
            <gui:column property="slug"/>
        </gui:table>
    </theme:zone>
</body>
</html>