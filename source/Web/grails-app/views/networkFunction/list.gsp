<html>
<head>
    <nav:set path="app/network/Function"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="name" editable="true"/>
            <gui:column property="multiComponentUpdaterType"/>
        </gui:table>
    </theme:zone>
</body>
</html>