<html>
<head>
	<theme:title><g:message code="download.chemistry.title" default="Chemistry Octave/Matlab Download" /></theme:title>
	<nav:set path="app/download/Chemistry"/>
	<theme:layout name="main"/>
<head>
<body>
    <theme:zone name="body">
        <div class="accordion" id="accordion">
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" href="#collapseOne">Multicompartment Chemistry</a>
                </div>
                <div id="collapseOne" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <ul class="spacedTop">
                            <li>
                                <b>Feedforward Chemical Neural Network (FCNN)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink action="exportReactionSet" id="3611" icon="icon-download" text="OCN (AASP)"/>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3548" icon="icon-download" text="HCN (AASP)"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>D. Blount, P. Banda, C. Teuscher, and D. Stefanovic. Feedforward Chemical Neural Network: A Compartmentalized Chemical System that Learns XOR. Artificial Life (In submission), 2015</i>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">Analog Chemical Perceptron</a>
                </div>
                <div id="collapseTwo" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <ul class="spacedTop">
                            <li>
                                <b>Chemical Linear Perceptron (CHLP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink action="exportReactionSet" id="4154" icon="icon-download" text="CHLP"/></li>
                                    <li><gui:actionLink action="exportReactionSet" id="4152" icon="icon-download" text="CHLP Scaled"/></li>
                                    <li><gui:actionLink action="exportReactionSet" id="4153" icon="icon-download" text="CHLP DNA SD"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>P. Banda, A. Goudarzi, C. Teuscher, and D. Stefanovic. Analytical Basis of Chemical Learning. PLOS Computational Biology (In submission), 2015</i>
                                <hr/>
                            </li>

                            <li>
                                <b>Analog Asymmetric Signal Perceptron (AASP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3535" icon="icon-download" text="AASP"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>P. Banda, and C. Teuscher. Learning Two-input Linear and Nonlinear Analog Functions with a Simple Chemical System. In Ibarra, O.H., et al. eds.: Unconventional Computing and Natural Computing Conference, Volume 8553 of Lecture Notes in Computer Science. Springer International Publishing Switzerland, 14-26, 2014</i>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" href="#collapseThree">Binary Chemical Perceptron</a>
                </div>
                <div id="collapseThree" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <ul class="spacedTop">
                            <li>
                                <b>Thresholded Asymmetric Signal Perceptron (TASP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3465" icon="icon-download" text="TASP MM"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3466" icon="icon-download" text="TASP MA"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>P. Banda, C. Teuscher, and D. Stefanovic. Training an Asymmetric Signal Perceptron through Reinforcements in an Artificial Chemistry. Journal of the Royal Society, Interface, 11(93), 2014</i>
                                <hr/>
                            </li>
                            <li>
                                <b>Standard Asymmetric Signal Perceptron (SASP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3454" icon="icon-download" text="SASP MM"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="1033" icon="icon-download" text="SASP MA"/></li>
                                </ul>
                                </br>
                                Reference:
                                <i>P. Banda, C. Teuscher, and D. Stefanovic. Training an Asymmetric Signal Perceptron through Reinforcements in an Artificial Chemistry. Journal of the Royal Society, Interface, 11(93), 2014</i>
                                <hr/>
                            </li>
                            <li>
                                <b>Weight-Race Perceptron (WRP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="1022" icon="icon-download" text="WRP"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>P. Banda, C. Teuscher, and M. R. Lakin. Online Learning in a Chemical Perceptron. Artificial Life, MIT Press, 19(2), 2013</i>
                                <hr/>
                            </li>
                            <li>
                                <b>Weight-Loop Perceptron</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="1024" icon="icon-download" text="WLP"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>P. Banda, C. Teuscher, and M. R. Lakin. Online Learning in a Chemical Perceptron. Artificial Life, MIT Press, 19(2), 2013</i>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" href="#collapseFour">Chemical Delay Line</a>
                </div>
                <div id="collapseFour" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <ul class="spacedTop">
                            <li>
                                <b>Parallel-Accessible Delay Line (PDL)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3536" icon="icon-download" text="PDL n = 2"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3537" icon="icon-download" text="PDL n = 3"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3538" icon="icon-download" text="PDL n = 4"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3531" icon="icon-download" text="PDL n = 5"/></li>
                                </ul>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3547" icon="icon-download" text="AASP w. PDL n = 2"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3546" icon="icon-download" text="AASP w. PDL n = 3"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3545" icon="icon-download" text="AASP w. PDL n = 4"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3544" icon="icon-download" text="AASP w. PDL n = 5"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>P. Banda, and C. Teuscher. An Analog Chemical Circuit with Parallel-Accessible Delay Line Learning Temporal Tasks. In Sayama, H., et al. eds.: ALIFE 14: Proceedings of the Fourteenth International Conference on the Synthesis and Simulation of Living Systems, MIT Press, 482-489, 2014</i>
                                <hr/>
                            </li>

                            <li>
                                <b>Backpropagation Signalling Delay Line (MDL)</b>
                                <ul class="inline spacedTop">
                                    <li>N/A</li>
                                </ul>
                                <br/>
                                Reference:
                                <i>J. Moles, P. Banda, and C. Teuscher. Delay Line as a Chemical Reaction Network. Parallel Processing Letters, 21(1), 2015</i>
                                <hr/>
                            </li>

                            <li>
                                <b>Manual Signalling Delay Line (MDL)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3671" icon="icon-download" text="MDL n = 2"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3672" icon="icon-download" text="MDL n = 3"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3673" icon="icon-download" text="MDL n = 4"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3674" icon="icon-download" text="MDL n = 5"/></li>
                                </ul>
                                <br/>
                                Reference:
                                <i>J. Moles, P. Banda, and C. Teuscher. Delay Line as a Chemical Reaction Network. Parallel Processing Letters, 21(1), 2015</i>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </theme:zone>
</body>