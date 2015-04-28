      		<ul class="inline">
      			<li>Translations</li>
      			<li>
					<div class="spacedTop spacedLeft">
    					<gui:actionLink controller="acTranslation" action="create" params="['translationSeries.id':instance.id]" hint="Add New"/>
    		   			<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acTranslationTable','deleteMultiple');return false;" hint="Delete"/>
    		   		</div>
           		    <gui:table list="${instance?.translations}" showEnabled="true" editEnabled="true" checkEnabled="true">
           		        <gui:column label="Time">
           		    		<g:if test="${bean.toTime}">
           		        		${bean.fromTime + ' - ' + bean.toTime}
           		        	</g:if>
           		        	<g:else>
           		        		${bean.fromTime}
           		        	</g:else>
           		       	</gui:column>
						<gui:column label="Items">
                       		<ul>
                            	<g:each in="${bean.translationItems.sort { it.variable.label }}" var="item">
                                    <li>
                        	    		<g:link controller="acTranslationItem" action="show" id="${item.id}">
											<g:render template="/acTranslation/displayTranslationItemFunction" bean="${item}" />
										</g:link>
									</li>
                                </g:each>
							</ul>
						</gui:column>
						<gui:column label="Ref Species" property="translationItems">
                       		<ul>
                            	<g:each in="${it}" var="item">
                                    <li>
										<g:render template="/acTranslation/displayTranslationItemRefSpecies" bean="${item}" />
									</li>
                                </g:each>
							</ul>
						</gui:column>
						<g:if test="${instance.variablesReferenced}">
							<gui:column label="Ref Variables" property="translationItems">
                       			<ul>
                            		<g:each in="${it}" var="item">
                                    	<li>
											<g:render template="/acTranslation/displayTranslationItemRefVariables" bean="${item}" />
										</li>
                                	</g:each>
								</ul>
							</gui:column>
						</g:if>
						<gui:column property="id" label="Add">
							<gui:actionLink controller="acTranslationItem" action="create" params="['translation.id':it]">
								<g:message code="acTranslation.addItem.label" default="Item" />
							</gui:actionLink>
						</gui:column>
    	        	</gui:table>
            	</li>
            </ul>