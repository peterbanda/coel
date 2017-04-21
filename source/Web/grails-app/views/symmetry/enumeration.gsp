<html>
<head>
	<theme:title><g:message code="download.chemistry.title" default="Enumeration and Probabilities" /></theme:title>
	<nav:set path="app/symConfs/Enum"/>
	<theme:layout name="main"/>
    <r:script>
		function showCallback(data) {
			if (data) {
				hideErrors();
			    $("#outputDiv").html(data);
			}
		}
    </r:script>
<head>
<body>
    <theme:zone name="body">
        <ui:form remote="true" url="[controller: 'symmetry', action: 'enumerateTwoDimConfs']" onSuccess="showCallback(data);">

            <ui:field label="Size">
                <ui:fieldInput>
                    <g:textField id="size" name="size" style="width: 300px" value="10" />
                </ui:fieldInput>
            </ui:field>

            <ui:field label="Output" style="display:none">
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
        <div class="row">
        </div>
    </theme:zone>
</body>