<html>
<head>
    <nav:set path="app"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" deleteEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="username"/>
            <gui:column property="firstName"/>
            <gui:column property="lastName"/>
            <gui:column property="email"/>
            <gui:column property="createTime"/>
            <gui:column property="accountEnabled" label="Enabled">
            	<g:checkBox checked="${it}" readonly="readonly" disabled="disabled"/>
            </gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>