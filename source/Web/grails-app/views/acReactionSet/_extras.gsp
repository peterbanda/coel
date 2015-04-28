	    <g:render template="dialogs_show"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showMassActionCopyDialog();">Copy as Mass-Action</a></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showDNASDCopyDialog();">Copy as DNA SD</a></li>
				<li role="presentation" class="divider"></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showReplaceSpeciesSetDialog();">Replace Species Set</a></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showAddReactionsDialog();">Add Reactions</a></li>
    			<li role="presentation" class="divider"></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showImportRatesDialog();">Import Rates</a></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showScaleForwardRateConstantsDialog();">Scale Forward Rates</a></li>
    			<li role="presentation" class="divider"></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'exportAsOctaveMatlab', id: instance.id)}">Export as Octave/Matlab</a></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showExportAsLatexTableDialog();">Export as Latex</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showReactionStructureSVGImages();">Show Structured Reactions</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="exportReactionStructureSVGImages();">Export Structured Reactions</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="exportSpeciesStructureSVGImages();">Export Structured Species</a></li>
				
  			</ul>
		</div>