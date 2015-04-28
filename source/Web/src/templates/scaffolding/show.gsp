<% import grails.persistence.Event %>
<%=packageName%>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:applyLayout name="\${layout}" />
        <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div id="pageBody">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <div id="messageTop" >
            	<g:if test="\${flash.message}">
            		<div class="message">\${flash.message}</div>
            	</g:if>
            </div>
			<div class="dialog">
                <table>
                    <tbody>
					<%  excludedProps = Event.allEvents.toList() << 'id' << 'version'
						allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
						props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) }
						Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
						props.each { p -> %>
						<g:if test="\${${propertyName}?.${p.name}}">
                        <tr class="prop">
							<td valign="top" class="name"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></td>
							<%  if (p.isEnum()) { %>
							<td valign="top" class="value"><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></td>
							<%  } else if (p.oneToMany || p.manyToMany) { %>
							<td valign="top" style="text-align: left;" class="value">
							    <ul>
								<g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
									<li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
								</g:each>
								</ul>
							</td>
							<%  } else if (p.manyToOne || p.oneToOne) { %>
								<td valign="top" class="value"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link></td>
							<%  } else if (p.type == Boolean || p.type == boolean) { %>
								<td valign="top" class="value"><g:formatBoolean boolean="\${${propertyName}?.${p.name}}" /></td>
							<%  } else if (p.type == Date || p.type == java.sql.Date || p.type == java.sql.Time || p.type == Calendar) { %>
								<td valign="top" class="value"><g:formatDate date="\${${propertyName}?.${p.name}}" /></td>
							<%  } else if(!p.type.isArray()) { %>
								<td valign="top" class="value"><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></td>
							<%  } %>
					    </tr>
						</g:if>
						<%  } %>
                    </tbody>
                </table>
            </div>
            <div class="buttons">
				<g:form>
					<g:hiddenField name="id" value="\${${propertyName}?.id}" />
					<g:actionSubmit class="edit" action="edit" value="\${message(code: 'default.button.edit.label', default: 'Edit')}"/>
					<g:actionSubmit class="delete" action="delete" value="\${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</g:form>
			</div>
        </div>
    </body>
</html>
