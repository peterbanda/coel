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
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>D. Blount, P. Banda, C. Teuscher, and D. Stefanovic. Feedforward Chemical Neural Network: A Compartmentalized Chemical System that Learns XOR. Artificial Life (In submission), 2015</i>
                                        </td>
                                    </tr>
                                </table>
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
                                    <li><gui:actionLink action="getSpeciesStructureImages" id="4153" icon="icon-download" text="CHLP DNA SD Species Images"/></li>
                                    <li><gui:actionLink action="getReactionStructureImages" id="4153" icon="icon-download" text="CHLP DNA SD Reaction Images"/></li>
                                </ul>
                                <br/>
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>P. Banda, A. Goudarzi, C. Teuscher, and D. Stefanovic. Analytical Basis of Chemical Learning. PLOS Computational Biology (In submission), 2015</i>
                                        </td>
                                    </tr>
                                </table>

                                <hr/>
                            </li>

                            <li>
                                <b>Analog Asymmetric Signal Perceptron (AASP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3535" icon="icon-download" text="AASP"/></li>
                                </ul>
                                <br/>
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                             <i>P. Banda, and C. Teuscher. Learning Two-input Linear and Nonlinear Analog Functions with a Simple Chemical System. In Ibarra, O.H., et al. eds.: Unconventional Computing and Natural Computing Conference, Volume 8553 of Lecture Notes in Computer Science. Springer International Publishing Switzerland, 14-26, 2014, <a title="doi" href="http://dx.doi.org/10.1007/978-3-319-08123-6_2">doi</a>, <a title="arxiv" href="http://arxiv.org/abs/1404.0427">arxiv</a></i>
                                        </td>
                                    </tr>
                                </table>

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
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                             <i>P. Banda, C. Teuscher, and D. Stefanovic. Training an Asymmetric Signal Perceptron through Reinforcements in an Artificial Chemistry. Journal of the Royal Society, Interface, 11(93), 2014, <a title="doi" href="http://dx.doi.org/10.1098/rsif.2013.1100">doi</a></i>
                                        </td>
                                    </tr>
                                </table>

                                <hr/>
                            </li>
                            <li>
                                <b>Standard Asymmetric Signal Perceptron (SASP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="3454" icon="icon-download" text="SASP MM"/></li>
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="1033" icon="icon-download" text="SASP MA"/></li>
                                </ul>
                                </br>
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>P. Banda, C. Teuscher, and D. Stefanovic. Training an Asymmetric Signal Perceptron through Reinforcements in an Artificial Chemistry. Journal of the Royal Society, Interface, 11(93), 2014, <a title="doi" href="http://dx.doi.org/10.1098/rsif.2013.1100">doi</a></i>
                                        </td>
                                    </tr>
                                </table>

                                <hr/>
                            </li>
                            <li>
                                <b>Weight-Race Perceptron (WRP)</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="1022" icon="icon-download" text="WRP"/></li>
                                </ul>
                                <br/>
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>P. Banda, C. Teuscher, and M. R. Lakin. Online Learning in a Chemical Perceptron. Artificial Life, MIT Press, 19(2), 2013, <a title="doi" href="http://dx.doi.org/10.1162/ARTL_a_00105">doi</a>, <a title="free download" href="http://www.mitpressjournals.org/doi/pdf/10.1162/ARTL_a_00105">free download</a></i>
                                        </td>
                                    </tr>
                                </table>

                                <hr/>
                            </li>
                            <li>
                                <b>Weight-Loop Perceptron</b>
                                <ul class="inline spacedTop">
                                    <li><gui:actionLink class="export" action="exportReactionSet" id="1024" icon="icon-download" text="WLP"/></li>
                                </ul>
                                <br/>
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>P. Banda, C. Teuscher, and M. R. Lakin. Online Learning in a Chemical Perceptron. Artificial Life, MIT Press, 19(2), 2013, <a title="doi" href="http://dx.doi.org/10.1162/ARTL_a_00105">doi</a>, <a title="free download" href="http://www.mitpressjournals.org/doi/pdf/10.1162/ARTL_a_00105">free download</a></i>
                                        </td>
                                    </tr>
                                </table>

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
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>P. Banda, and C. Teuscher. An Analog Chemical Circuit with Parallel-Accessible Delay Line Learning Temporal Tasks. In Sayama, H., et al. eds.: ALIFE 14: Proceedings of the Fourteenth International Conference on the Synthesis and Simulation of Living Systems, MIT Press, 482-489, 2014, <a title="doi" href="http://mitpress.mit.edu/sites/default/files/titles/content/alife14/ch078.html">doi</a>, <a title="download" href="http://dx.doi.org/10.7551/978-0-262-32621-6-ch078">free download</a></i>
                                        </td>
                                    </tr>
                                </table>

                                <hr/>
                            </li>

                            <li>
                                <b>Backpropagation Signalling Delay Line (MDL)</b>
                                <ul class="inline spacedTop">
                                    <li>N/A</li>
                                </ul>
                                <br/>
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>J. Moles, P. Banda, and C. Teuscher. Delay Line as a Chemical Reaction Network. Parallel Processing Letters, 21(1), 2015, <a title="doi" href="http://dx.doi.org/10.1142/S0129626415400022">doi</a>, <a title="arxiv" href="http://arxiv.org/abs/1404.1152">arxiv</a></i>
                                        </td>
                                    </tr>
                                </table>
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
                                <table>
                                    <tr>
                                        <td style="vertical-align: top;">
                                            Reference:
                                        </td>
                                        <td>
                                            <i>J. Moles, P. Banda, and C. Teuscher. Delay Line as a Chemical Reaction Network. Parallel Processing Letters, 21(1), 2015, <a title="doi" href="http://dx.doi.org/10.1142/S0129626415400022">doi</a>, <a title="arxiv" href="http://arxiv.org/abs/1404.1152">arxiv</a></i>
                                        </td>
                                    </tr>
                                </table>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </theme:zone>
</body>