		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="timeStepLength">
			<ui:fieldInput>
				<g:textField name="timeStepLength" value="${formatNumber(number: instance.timeStepLength, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="iterations">
			<ui:fieldInput>
				<g:textField name="iterations" value="${formatNumber(number: instance.iterations, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="lyapunovPerturbationStrength">
			<ui:fieldInput>
				<g:textField name="lyapunovPerturbationStrength" value="${formatNumber(number: instance.lyapunovPerturbationStrength, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="derridaPerturbationStrength">
			<ui:fieldInput>
				<g:textField name="derridaPerturbationStrength" value="${formatNumber(number: instance.derridaPerturbationStrength, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="derridaTimeLength">
			<ui:fieldInput>
				<g:textField name="derridaTimeLength" value="${formatNumber(number: instance.derridaTimeLength, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="timeStepToFilter">
			<ui:fieldInput>
				<g:textField name="timeStepToFilter" value="${formatNumber(number: instance.timeStepToFilter, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="fixedPointDetectionPrecision">
			<ui:fieldInput>
				<g:textField name="fixedPointDetectionPrecision" value="${formatNumber(number: instance.fixedPointDetectionPrecision, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="derridaResolution">
			<ui:fieldInput>
				<g:textField name="derridaResolution" value="${formatNumber(number: instance.derridaResolution, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>