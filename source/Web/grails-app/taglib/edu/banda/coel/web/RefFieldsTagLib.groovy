package edu.banda.coel.web

import java.util.List;
import java.util.Map;

import grails.plugin.formfields.BeanPropertyAccessorFactory
import grails.plugin.formfields.BeanPropertyAccessor
import grails.plugin.formfields.FormFieldsTagLib
import grails.util.GrailsNameUtils
import org.codehaus.groovy.grails.web.pages.GroovyPage

class RefFieldsTagLib {

	static final namespace = 'f'
	static final templatePath = '_fields/ref'

	BeanPropertyAccessorFactory beanPropertyAccessorFactory

	def ref = { attrs, body ->
		def bean = resolveBean(attrs.remove('bean'))
		if (!bean) throwTagError("Tag [$name] is missing required attribute [bean]")
		if (!attrs.property) throwTagError("Tag [$name] is missing required attribute [property]")

		def property = attrs.remove('property')

		def propertyAccessor = resolveProperty(bean, property)
		if (propertyAccessor.value) {
			def model = buildModel(propertyAccessor, attrs, body)
			out << renderRef(model)
		}
	}

	private boolean hasBody(Closure body) {
		return !body.is(GroovyPage.EMPTY_BODY_CLOSURE)
	}

	private String renderRef(Map model) {
		render template: '/_fields/ref', model: model
	}

	private Map buildModel(BeanPropertyAccessor propertyAccessor, Map attrs, Closure body) {
		def ref = propertyAccessor.value
		def value = ''
		if (attrs.containsKey('textProperty')) {
			def propertyValueAccessor = resolveProperty(ref, attrs.remove('textProperty'))
			value = propertyValueAccessor.value
		}

		def id = attrs.containsKey('id') ? attrs.remove('id') : ref.id
		def model = [
			bean: propertyAccessor.rootBean,
			property: propertyAccessor.pathFromRoot,
			type: propertyAccessor.propertyType,
			beanClass: propertyAccessor.beanClass,

			label: resolveLabelText(propertyAccessor, attrs),
			controller: attrs.containsKey('controller') ? attrs.remove('controller') : GrailsNameUtils.getPropertyName(propertyAccessor.propertyType.simpleName),
			id: attrs.containsKey('id') ? attrs.remove('id') : ref.id,
			value: value,
			constraints: propertyAccessor.constraints,
			errors: propertyAccessor.errors.collect { message(error: it) },
		]
		if (hasBody(body)) {
			model.value = body(ref)
		}
		model
	}

	private BeanPropertyAccessor resolveProperty(bean, String propertyPath) {
		beanPropertyAccessorFactory.accessorFor(bean, propertyPath)
	}

	private Object resolveBean(beanAttribute) {
		def bean = pageScope.variables[FormFieldsTagLib.BEAN_PAGE_SCOPE_VARIABLE]
		if (!bean) {
			// Tomcat throws NPE if you query pageScope for null/empty values
			if (beanAttribute.toString()) {
				bean = pageScope.variables[beanAttribute]
			}
		}
		if (!bean) bean = beanAttribute
		bean
	}

	private String resolveLabelText(BeanPropertyAccessor propertyAccessor, Map attrs) {
		def labelText
		def label = attrs.remove('label')
		if (label) {
			labelText = message(code: label, default: label)
		}
		if (!labelText && propertyAccessor.labelKeys) {
			labelText = resolveMessage(propertyAccessor.labelKeys, propertyAccessor.defaultLabel)
		}
		if (!labelText) {
			labelText = propertyAccessor.defaultLabel
		}
		labelText
	}

	private String resolveMessage(List<String> keysInPreferenceOrder, String defaultMessage) {
		def message = keysInPreferenceOrder.findResult { key ->
			message(code: key, default: null) ?: null
		}
		message ?: defaultMessage
	}
}