<html>
<head>
    <theme:title>Network Simulator</theme:title>
    <theme:layout name="main"/>
<head>
<body>
<theme:zone name="dialogs">
    <gui:modal id="no-network-modal" title="No Network">
        <p> No network to launch. First define one.</p>
    </gui:modal>
    <gui:modal id="no-topology-or-function-modal" title="No Topology or Function">
        <p> No topology or function defined. Proceed to the step 1 (and/or 2).</p>
    </gui:modal>
</theme:zone>
<theme:zone name="body">
    <p>Functionality of the network module includes definition of topologies, functions (transition tables), networks, interaction series,
    simulation execution, performance evaluation, Derrida analysis, and damage spreading.</p>
    <p>The common work-flow is described by the following steps.</p>

    <div class="accordion" id="accordion" style="margin: 25px 0px 0px 0px;">
        <div class="accordion-group">
            <div class="accordion-heading">
                <div class="row-fluid">
                    <div class="pull-right span5">
                        <a class="accordion-toggle" data-toggle="collapse" href="#collapseOne">Read more...</a>
                    </div>
                    <div class="span7">
                        <ui:h3>1.
                            <g:if test="${noTopologies}">
                                <a href="${g.createLink(uri:'/topology/createSpatial')}">Define Topology</a>
                            </g:if>
                            <g:else>
                                <a href="${g.createLink(uri:'/topology/list')}">Define Topology</a>
                            </g:else>
                        </ui:h3>
                    </div>
                </div>
            </div>
            <div id="collapseOne" class="accordion-body collapse">
                <div class="accordion-inner">
                    <div class="row-fluid" style="margin: 20px 20px 20px 20px;">
                        <div class="span8">
                            <p> COEL supports 3 types of topologies: spatial, template, and layered.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-group">
            <div class="accordion-heading">
                <div class="row-fluid">
                    <div class="pull-right span5">
                        <a class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">Read more...</a>
                    </div>
                    <div class="span7">
                        <ui:h3> 2.
                            <g:if test="${noNetworkFunctions}">
                                <a href="${g.createLink(uri:'/networkFunction/create')}">Define Network Function</a>
                            </g:if>
                            <g:else>
                                <a href="${g.createLink(uri:'/networkFunction/list')}">Define Network Function</a>
                            </g:else>
                        </ui:h3>
                    </div>
                </div>
            </div>
            <div id="collapseTwo" class="accordion-body collapse">
                <div class="accordion-inner">
                    <div class="row-fluid" style="margin: 20px 20px 20px 20px;">
                        <div class="span8">
                            <p> Network function can be currently defined only as a transition table.<p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="accordion-group">
            <div class="accordion-heading">
                <div class="row-fluid">
                    <div class="pull-right span5">
                        <a class="accordion-toggle" data-toggle="collapse" href="#collapseThree">Read more...</a>
                    </div>
                    <div class="span7">
                        <ui:h3> 3.
                            <g:if test="${noTopologies || noNetworkFunctions}">
                                <a href="javascript:void(0);" onclick="$('#no-topology-or-function-modal').modal();">Combine Topology and Function into Network</a>
                            </g:if>
                            <g:else>
                                <g:if test="${noNetworks}">
                                    <a href="${g.createLink(uri:'/network/create')}">Combine Topology and Function into Network</a>
                                </g:if>
                                <g:else>
                                    <a href="${g.createLink(uri:'/network/list')}">Combine Topology and Function into Network</a>
                                </g:else>
                            </g:else>
                        </ui:h3>
                    </div>
                </div>
            </div>
            <div id="collapseThree" class="accordion-body collapse">
                <div class="accordion-inner">
                    <div class="row-fluid" style="margin: 20px 20px 20px 20px;">
                        <div class="span8">
                            <p> TODO.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="accordion-group">
            <div class="accordion-heading">
                <div class="row-fluid">
                    <div class="pull-right span5">
                        <a class="accordion-toggle" data-toggle="collapse" href="#collapseFour">Read more...</a>
                    </div>
                    <div class="span7">
                        <ui:h3> 4.
                            <g:if test="${noNetworks}">
                                <a href="javascript:void(0);" onclick="$('#no-network-modal').modal();">Launch Network</a>
                            </g:if>
                            <g:else>
                                <a href="${g.createLink(uri:'/networkRun')}">Launch Network</a>
                            </g:else>
                        </ui:h3>
                    </div>
                </div>
            </div>
            <div id="collapseFour" class="accordion-body collapse">
                <div class="accordion-inner">
                    <div class="row-fluid" style="margin: 20px 20px 20px 20px;">
                        <div class="span8">
                            <p> Network is executed starting with a random configuration of ones and zeros.</p>
                            <p> User can inspect the execution outcome in the embedded chart with play, pause, and reverse control options.
                            If further post-processing is required space-time diagrams could be exported into SVG file(s).</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</theme:zone>
</body>
</html>