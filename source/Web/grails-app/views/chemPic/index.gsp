<html>
<head>
	<theme:title><g:message code="chempic.title" default="Chemistry Picture Generator" /></theme:title>
	<nav:set path="app/chemistry/ChemPic"/>
	<theme:layout name="main"/>
	<r:script>
        function stopRKey(evt) {
        	var evt = (evt) ? evt : ((event) ? event : null);
        	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
          	if ((evt.keyCode == 13) && (node.type=="text"))  {return false;}
        }

        document.onkeypress = stopRKey;

		$(function(){
	        
        	$("#dnaStrandString").keydown(function(event){
           		if(event.keyCode == 13){
                    $("#showDNAStrandButton").click();
            	}
       		});

 			$("#exportDNAStrandButton").on('click', function(e) {
  				e.preventDefault();
			});
		});

		function exportDNAStrandAsSVG() {
			$.post('${createLink(controller: "chemPic", action: "exportDNAStrandAsSVG")}',
				{dnaStrandString: $('#dnaStrandString').val()}, exportCallback);		 
		}

		function exportCallback(data) {
			if (data.indexOf('illegal') == -1) {
				hideErrors();
				saveSVG(data)
			} else
				showErrorMessage(data)
		}

		function showCallback(data) {
			if (data.indexOf('illegal') == -1) {
				hideErrors();
			    $("#ImageDiv").html('<object data="data:image/svg+xml;base64,' + data + '" width="400" height="200"></object>');
			} else 
				showErrorMessage(data)
		}

		function saveSVG(svgXML) {
			saveAs('data:image/svg+xml;base64,' + svgXML, 'dna_strand-' + currentDateTimeAsString() + '.svg');
		}

	</r:script>
<head>
<body>
	<theme:zone name="body">
		<ui:form remote="true" name="showDNAStrandForm" url="[controller: 'chemPic', action: 'showDNAStrand']" onSuccess="showCallback(data);">

		    <div class="pull-right">
       			<a href="http://research.microsoft.com/en-us/projects/dna/manual.pdf" class="btn"><i class="icon-file"></i>Syntax</a>
        	</div>

			<ui:field label="DNA Strand String">
				<ui:fieldInput>
					<g:textField id="dnaStrandString" name="dnaStrandString" style="width: 300px" value="${dnaStrandString}" />
				</ui:fieldInput>
			</ui:field>

			<ui:field label="Image">
				<ui:fieldInput>
					<div id="ImageDiv">
						<g:if test="${svgXMLURI}">
							<object data="data:image/svg+xml;base64,${svgXMLURI}" width="400" height="200"></object>
						</g:if>
					</div>
				</ui:fieldInput>
			</ui:field>

       		<ui:actions>
				<ui:button id="showDNAStrandButton" type="submit" kind="button" mode="primary">
					${message(code: 'button.showDNAStrand.label', default: 'Show')}
				</ui:button>
				<ui:button id="exportDNAStrandButton" kind="button" type="cancel" onclick="exportDNAStrandAsSVG();">
					${message(code: 'button.exportDNAStrandAsSVG.label', default: 'Export As SVG')}
				</ui:button>
			</ui:actions>
		</ui:form>
	</theme:zone>
</body>
</html>