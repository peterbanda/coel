	<ui:field bean="instance" name="asRepetitions"/>
	<ui:field bean="instance" name="network" from="${networks}" optionValue="${{it.id + " : " + it.name}}" required="${true}" />
	<ui:field bean="instance" name="runTime"/>
	<ui:field bean="instance" name="fixedPointDetectionPeriodicity"/>
	<ui:field bean="instance" name="actionSeries">
		<ui:fieldInput>
			<g:select name="actionSeries" from="${networkActionSeries}"
				multiple="yes"
				optionKey="id"
				optionValue="${{it.id + " : " + it.name}}"
				size="10"
				value="${instance?.actionSeries*.id}" />
		</ui:fieldInput>
	</ui:field>
	<ui:field bean="instance" name="evaluation" from="${evaluations}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>