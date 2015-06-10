<html>
<head>
    <theme:title><g:message code="download.network.title" default="Cellular Automaton (Network) Download" /></theme:title>
    <nav:set path="app/download/Network"/>
    <theme:layout name="main"/>
    <r:script>
        function showExportDialogTransitionTableDialog(functionId) {
            console.log(functionId)
            $('#export-transition-table-modal').find("#id").val(functionId);
            $('#export-transition-table-modal').modal({ keyboard: true });
        }
    </r:script>
<head>
<body>
<theme:zone name="body">
    <gui:modal id="export-transition-table-modal" title="Export Transition Table" action="exportNetworkFunction">
        <g:hiddenField name="id" value="" />
        <ul class="inline">
            <li><div class="spacedTop">Least Significant Bit First</div></li>
            <li><g:checkBox id="lsbfirst" name="lsbfirst" checked="true"></g:checkBox></li>
        </ul>
    </gui:modal>
    <div class="accordion" id="accordion">
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" href="#collapseOne">Two-dimensional Leader Election</a>
            </div>
            <div id="collapseOne" class="accordion-body collapse in">
                <div class="accordion-inner">
                    <ul class="spacedTop">
                        <li>
                            <b>Leader Election Targeting 29^2 (LE 29^2)</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1056); return false;" text="LE 29^2"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>P. Banda. Anonymous Leader Election in One- and Two-Dimensional Cellular Automata. Dissertation, Comenius University, 2014</i>
                            <hr/>
                        </li>
                        <li>
                            <b>Leader Election Targeting 19^2 (LE 19^2)</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1052); return false;" text="LE 19^2"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>P. Banda. Anonymous Leader Election in One- and Two-Dimensional Cellular Automata. Dissertation, Comenius University, 2014</i>
                            <hr/>
                        </li>
                        <li>
                            <b>Density Minimization (DM)</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1083); return false;" text="DM"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>P. Banda. Anonymous Leader Election in One- and Two-Dimensional Cellular Automata. Dissertation, Comenius University, 2014</i>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">One-dimensional Leader Election</a>
            </div>
            <div id="collapseTwo" class="accordion-body collapse in">
                <div class="accordion-inner">
                    <ul class="spacedTop">
                        <li>
                            <b>Strategy of Mirror Particles (SMP)</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1041); return false;" text="SMP"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>P. Banda. Cellular Automata Evolution Of Leader Election. In Kampis, G., Karsai, I., Szathmáry, E., eds.: Advances in Artificial Life. Darwin Meets von Neumann. Volume 5778 of Lecture Notes in Computer Science. Springer Berlin / Heidelberg, 310-317, 2011</i>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" href="#collapseThree">Game of Life</a>
            </div>
            <div id="collapseThree" class="accordion-body collapse in">
                <div class="accordion-inner">
                    <ul class="spacedTop">
                        <li>
                            <b>Game of Life</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1012); return false;" text="GoL"/>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" href="#collapseFour">Two-dimensional Density Classification</a>
            </div>
            <div id="collapseFour" class="accordion-body collapse in">
                <div class="accordion-inner">
                    <ul class="spacedTop">
                        <li>
                            <b>Cenek Rule</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1020); return false;" text="Cenek"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>M. Cenek. Information Processing in Two-Dimensional Cellular Automata. Dissertation, Portland State University, 2011</i>
                            <hr/>
                        </li>
                        <li>
                            <b>Marques-Pita Rule</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1021); return false;" text="Marques"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>N/A</i>
                            <hr/>
                        </li>
                        <li>
                            <b>Wolz and deOliveira Rule</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1022); return false;" text="Wolz"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>D. Wolz and P. P. B. de Oliveira. Very effective evolutionary techniques for searching cellular automata rule spaces. Journal of Cellular Automata, 3(4):289–312, 2008</i>
                            <hr/>
                        </li>
                        <li>
                            <b>2DGKL</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1086); return false;" text="2DGKL"/>
                            </ul>
                            <hr/>
                        </li>
                        <li>
                            <b>Local Majority (LM)</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1087); return false;" text="LM"/>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" href="#collapseFive">Two-dimensional Synchronization</a>
            </div>
            <div id="collapseFive" class="accordion-body collapse in">
                <div class="accordion-inner">
                    <ul class="spacedTop">
                        <li>
                            <b>Cenek Rule</b>
                            <ul class="inline spacedTop">
                                <li><gui:actionLink action="exportNetworkFunction" icon="icon-download" onclick="showExportDialogTransitionTableDialog(1024); return false;" text="Cenek"/>
                            </ul>
                            <br/>
                            Reference:
                            <i>M. Cenek. Information Processing in Two-Dimensional Cellular Automata. Dissertation, Portland State University, 2011</i>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</theme:zone>
</body>