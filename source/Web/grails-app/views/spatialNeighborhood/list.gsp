<html>
<head>
    <nav:set path="app/network/SpatialNeighborhood"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="name" editable="true"/>
            <gui:column label="Number of Neighbors">${bean.neighbors.size()}</gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>