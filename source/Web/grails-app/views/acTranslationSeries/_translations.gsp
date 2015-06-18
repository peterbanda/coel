      		<ul class="inline">
      			<li>Translations</li>
      			<li>
					<div class="spacedTop spacedLeft">
                        <g:if test="${!instance?.translations.isEmpty()}">
                            <gui:actionLink controller="acTranslation" action="create" params="['translationSeries.id':instance.id]" hint="Add New"/>
						    <gui:modal id="confirm-translation-delete-modal" title="Delete" onclick="doTableSelectionAction('acTranslationTable','deleteMultiple')" text="Are you sure?"/>
						    <gui:actionLink icon="icon-trash" onclick="openModal('confirm-translation-delete-modal')" hint="Delete"/>
                        </g:if>
                        <g:else>
                            <gui:actionLink controller="acTranslation" action="create" params="['translationSeries.id':instance.id]" hint="Start here to add a new translation" hint-placement="right" hint-show="true"/>
                        </g:else>
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