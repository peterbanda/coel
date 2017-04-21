<html>
<head>
	<theme:title><g:message code="symmetry.confenum.title" default="Configuration Enumeration" /></theme:title>
	<nav:set path="app/symmetry/Enumeration"/>
	<theme:layout name="main"/>
    <r:script>
		function showCallback(data) {
			if (data) {
				hideErrors();
			    $("#outputDiv").html(data.countFull);
			}
		}
    </r:script>
<head>
<body>
    <theme:zone name="body">
        <ui:form remote="true" name="enumerationForm" url="[controller: 'symmetry', action: 'enumerateUniformTwoDimConfs']" onSuccess="showCallback(data);" onFailure="showErrors(errors)">

            <ui:field label="Size">
                <ui:fieldInput>
                    <g:textField id="size" name="size" style="width: 300px" value="10" />
                </ui:fieldInput>
            </ui:field>

            <ui:field label="Output">
                <ui:fieldInput>
                    <div id="outputDiv">
                    </div>
                </ui:fieldInput>
            </ui:field>

            <ui:actions>
                <ui:button type="submit" kind="button" mode="primary">
                    Calculate
                </ui:button>
            </ui:actions>
        </ui:form>
    </theme:zone>
</body>