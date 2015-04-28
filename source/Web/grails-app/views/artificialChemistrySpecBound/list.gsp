<html>
<head>
    <nav:set path="app/evolution/ArtificialChemistrySpecBound"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="createSymmetricInstance" icon="icon-plus" text="Symmetric Type"/>
    	<gui:actionButton action="createDNAStrandInstance" icon="icon-plus" text="DNA Strand Type"/>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="name"/>
            <gui:column property="timeCreated"/>
        </gui:table>
    </theme:zone>
</body>
</html>