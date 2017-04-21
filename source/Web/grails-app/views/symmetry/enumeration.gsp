<html>
<head>
	<theme:title><g:message code="symmetry.confenum.title" default="Shift-Symmetric Configuration Enumeration" /></theme:title>
	<nav:set path="app/symmetry/Enumeration"/>
	<theme:layout name="main"/>
    <r:script>
		function showCallback(data) {
            $("body").css("cursor", "default");
            if (data.errors) {
                showErrors(data.errors);
            } else {
				hideErrors();

				var latticeSize = $("#latticeSize").val()
                var alphabetSize = $("#alphabetSize").val()

                $("#outputLabel").html("<p>The number of shift-symmetric configurations for the lattice " + latticeSize + "x" + latticeSize + " with the alphabet size (states per cell) " + alphabetSize + " is:</p>");
                $("#expCount").val(data.exp);
                $("#fullCount").val(data.full);

				$("#outputDiv").show();
			}
		}

        function showFull() {
            $("#showFullOutputButton").hide();
            $("#showExpOutputButton").show();
            $("#expCountDiv").hide();
            $("#fullCountDiv").show();
        }

        function showExp() {
            $("#showExpOutputButton").hide();
            $("#showFullOutputButton").show();
            $("#fullCountDiv").hide();
            $("#expCountDiv").show();
        }

        function hideOutput() {
            $("#outputDiv").hide();
            $("body").css("cursor", "wait");
        }

        function errorCallback(data) {
            $("#outputDiv").html(data);
        }

        function changeAlphabetSize() {
            $("#alphabetSize").val('2');
            if ($("#binaryAlphabetSize").is(":checked")) {
                $("#alphabetSize").hide();
            } else {
                $("#alphabetSize").show();
            }
        }
    </r:script>
<head>
<body>
    <theme:zone name="body">
        <ui:form remote="true" name="enumerationForm" url="[controller: 'symmetry', action: 'enumerateUniformTwoDimConfs']" before="hideOutput();" onSuccess="showCallback(data);">

            <ui:field label="Lattice Size">
                <ui:fieldInput>
                    <g:textField id="latticeSize" name="latticeSize" style="width: 300px" value="10" />
                </ui:fieldInput>
            </ui:field>

            <ui:field label="Alphabet Size">
                <ui:fieldInput>
                    <div class="row-fluid">
                        <g:checkBox name="binaryAlphabetSize" value="${true}" onchange="changeAlphabetSize();"></g:checkBox>Binary
                    </div>
                    <div class="row-fluid">
                        <g:textField id="alphabetSize" name="alphabetSize" style="width: 20px; display: none" value="2"/>
                    </div>
                </ui:fieldInput>
            </ui:field>

            <div id="outputDiv" class="control-group" style="display:none">
                <label for="" class="control-label"></label>
                <div class="controls">
                    <div id="outputLabel" class="row">
                    </div>
                    <div class="row-fluid">
                        <div id="expCountDiv" class="span5">
                            <input id="expCount" name="expCount" class="span12" value="" readonly="readonly" type="text">
                        </div>
                        <div id="showFullOutputButton" class="span1">
                            <a href="#" onclick="showFull();">Show Full</a>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div id="fullCountDiv" class="span5" style="display:none">
                            <g:textArea id="fullCount" name="fullCount" class="span12" value="" rows="4" cols="70" readonly="readonly"/>
                        </div>
                        <div id="showExpOutputButton" class="span1" style="display:none">
                            <a href="#" onclick="showExp();">Show Exp</a>
                        </div>
                    </div>
                </div>
            </div>

            <ui:actions>
                <ui:button type="submit" kind="button" mode="primary">
                    Calculate
                </ui:button>
            </ui:actions>
        </ui:form>
    </theme:zone>
</body>