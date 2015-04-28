				<td colspan="2">
               		<table style="none; border: 0px">
                        <tbody>
                        	<tr>
                                <td valign="top" class="name">
                                    <label for="actionSeries"><g:message code="acRunTask.actionSeries.label" default="Interaction Series" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: acRunTaskInstance, field: 'actionSeries', 'errors')}">
									<g:select
	    								name="actionSeriesId"
    									from="${actionSeries}"
    									noSelection= "['': 'Select One...']"
    									optionKey="id"
    									value="${acRunTaskInstance?.actionSeries?.id}"
									/>
    	    					</td>
    	    				</tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="evaluatedActionSeries"><g:message code="acRunTask.evaluatedActionSeries.label" default="Evaluated Interaction Series" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: acRunTaskInstance, field: 'evaluatedActionSeries', 'errors')}">
									<g:select
	    								name="evaluatedActionSeriesId"
    									from="${evaluatedActionSeries}"
    									noSelection= "['': 'Select One...']"
    									optionKey="id"
    									value="${acRunTaskInstance?.evaluatedActionSeries?.id}"
									/>
    	    					</td>
    	    				</tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="acTask"><g:message code="acRunTask.acTask.label" default="AC Task" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: acRunTaskInstance, field: 'acTask', 'errors')}">
									<g:select
	    								name="acTaskId"
    									from="${acTasks}"
    									noSelection= "['': 'Select One...']"
    									optionKey="id"
    									value="${acRunTaskInstance?.acTask?.id}"
									/>
    	    					</td>
    	    				</tr>
						</tbody>
                    </table>
               	</td>
            </tr>