<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReactionSet.label', default: 'Reaction Set')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
    <r:require module="editable"/>
    <r:require module="jszip"/>
    <theme:layout name="show"/>
	<r:script>
		var reactionSvgImageDatas;

		function showReactionStructureSVGImages() {
            if (!reactionSvgImageDatas)
			    $.ajax({
			        url: '${createLink(action: "getReactionStructureImages")}?id=${instance.id}',
				    success: function(reactionSvgDatas) {
					    reactionSvgImageDatas = reactionSvgDatas;
    					var incorrectSvgImageCounter = 0;
	    				$.each(reactionSvgDatas, function(index, reactionSvgImageData) {
		    				var svgImage = reactionSvgImageData.image
			    			if (!svgImage) {
				    			incorrectSvgImageCounter++;
					    	} else if (svgImage.indexOf('illegal') == -1) {
						    	hideErrors();
							    var newRow = $('<tr>');
							    var content = $('<td id="svgImage" colspan="8">');
							    content.append('<object data="data:image/svg+xml;base64,' + svgImage + '"></object>');
							    newRow.append(content);
							    $("#acReactionTable > tbody > tr").eq(2 * index - incorrectSvgImageCounter).after(newRow);
						    } else {
							    incorrectSvgImageCounter++;
							    showErrorMessage(svgImage)
						    }
					    });
				    },
				    async:   false
			    });

		}

		function exportReactionStructureSVGImages() {
			if (!reactionSvgImageDatas)
				showReactionStructureSVGImages();

			exportSvgImageDataAsZip(reactionSvgImageDatas, 'reaction_set_${instance.id}.zip')
		}

		function exportSpeciesStructureSVGImages() {
			$.get('${createLink(action: "getSpeciesStructureImages")}?id=${instance.id}',
				function(speciesSvgImageDatas) {
					exportSvgImageDataAsZip(speciesSvgImageDatas, 'reaction_set_species_${instance.id}.zip')
				}
			);
		}

		function exportSvgImageDataAsZip(svgImageDatas, filename) {
			var zip = new JSZip();
			zip.file("info.txt", "Exported By COEL " + currentDateTimeAsString() + "\n------------------------------------\nReaction Set id : ${instance.id}\nReaction Set name : ${instance.label}");
			var img = zip.folder("images");
			$.each(svgImageDatas, function(i, svgImageData) {
				var svgImage = svgImageData.image
				if (svgImage || svgImage.indexOf('illegal') == -1) {
					img.file(svgImageData.label + ".svg", svgImage, {base64: true});
				}
	      	});
			var content = zip.generate();
			saveAs('data:application/zip;base64,' + content, filename)			
		}

	</r:script>
</head>
<body>
	<theme:zone name="extras">
		<g:render template="extras"/>
    </theme:zone>

    <theme:zone name="details">
    	<div class="row-fluid">
    	    <f:with bean="instance">
    			<div class="span5">
    				<f:display property="id"/>
        			<f:display property="createTime"/>
        			<f:ref property="createdBy" textProperty="username"/>
    			</div>
    			<div class="span5">
					<f:display property="label"/>
					<f:ref property="speciesSet" textProperty="name"/>
    			</div>
			</f:with>    		
		</div>
		<hr/>
		<div class="spacedLeft">			
			<g:render template="species"/>	
		</div>
        <div class="accordion" id="accordion">
  			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" href="#collapseOne">Reactions</a>
    			</div>
    			<div id="collapseOne" class="accordion-body collapse in">
      				<div class="accordion-inner">
						<g:render template="reactions"/>
      				</div>
      			</div>
    		</div>
			<g:if test="${!instance?.reactions.isEmpty()}">
  			    <div class="accordion-group">
    			    <div class="accordion-heading">
        			    <a class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">Reaction Groups</a>
    			    </div>
    			    <div id="collapseTwo" class="accordion-body collapse in">
      				    <div class="accordion-inner">
						    <g:render template="reactionGroups"/>
      				    </div>
      			    </div>
    		    </div>
            </g:if>
  		</div>
    </theme:zone>
</body>
</html>