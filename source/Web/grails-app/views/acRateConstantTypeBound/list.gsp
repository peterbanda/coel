<html>
<head>
    <nav:set path="app/chemistry/RateConstantTypeBound"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="rateConstantType"/>
            <gui:column property="from"/>
            <gui:column property="to"/>
        </gui:table>
    </theme:zone>
</body>
</html>