<%@ page import="com.banda.function.domain.TransitionTable" %>
<%@ page import="com.banda.function.domain.Expression" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkFunction.label', default: 'Network Function')}" scope="request" />
    <nav:set path="app/network/Function"/>
    <theme:layout name="show"/>
</head>
	<theme:zone name="extras">
        <g:if test="${instance.function.getClass() == TransitionTable.class}">
            <g:render template="dialogs_show"/>
            <div class="dropdown">
  			    <ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			    Extras
    			    <span class="caret"></span>
  			    </ui:button>
  			    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                    <li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showExportDialogTransitionTableDialog();">Export Transition Table</a></li>
  			    </ul>
		    </div>
         </g:if>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>

	        <hr>                

			<f:ref property="parentFunction" textProperty="name"/>
			<f:display property="name"/>

            <g:if test="${instance.function?.getClass() == TransitionTable.class}">
                <f:display property="function" label="Transition Table (Function)">${render(template:'displayTransitionTable', bean:value, model: [editable:"false"])}</f:display>
            </g:if>
            <g:if test="${it?.getClass() == Expression.class}">
                <f:display property="function" label="Expression (Function)">${value.formula}</f:display>
            </g:if>

			<f:display property="statesWeightsIntegratorType"/>
			<f:display property="multiComponentUpdaterType"/>
		</f:with>

		<g:render template="layerFunctions"/>
    </theme:zone>
</body>
</html>