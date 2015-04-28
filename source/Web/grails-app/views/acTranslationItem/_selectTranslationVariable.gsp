<g:select 
	name="variable.id" 
	from="${translationVariables}"
	noSelection= "['': 'Select One...']"
	optionKey="id" 
	value="${acTranslationItemInstance?.variable?.id}"
/>
