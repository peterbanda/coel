package edu.banda.coel.web

import java.util.List
import java.util.Map

import grails.plugin.formfields.BeanPropertyAccessorFactory
import grails.plugin.formfields.BeanPropertyAccessor
import grails.plugin.formfields.FormFieldsTagLib
import grails.util.GrailsNameUtils

import org.codehaus.groovy.grails.web.pages.GroovyPage

import grails.util.Environment
import grails.util.GrailsNameUtils

import org.codehaus.groovy.grails.plugins.GrailsPlugin
import org.grails.plugin.platform.ui.UITagException
import org.grails.plugin.platform.util.TagLibUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import org.apache.commons.lang.StringUtils

import java.util.concurrent.ConcurrentHashMap

class UIExtTagLib {

	static final String BEAN_PAGE_SCOPE_VARIABLE = 'gui:trw:bean'
	static final namespace = 'gui'

	BeanPropertyAccessorFactory beanPropertyAccessorFactory

	def actionButton = { attrs, body ->
		if (!attrs.action) throwTagError("Tag [gui:actionButton] is missing required attribute [type]")
		def title = attrs.text
		if (!title && hasBody(body)) {
			title = body()
		}
		out << render(template: '/_fields/actionButton', model: [action:attrs.action, id:attrs.id, title:title, icon:attrs.icon, attrs:attrs])
	}

	def actionLink = { attrs, body ->
		if (!attrs.action) throwTagError("Tag [gui:actionLink] is missing required attribute [type]")
		def title = attrs.text
		if (!title && hasBody(body)) {
			title = body()
		}
		out << render(template: '/_fields/actionLink', model: [action:attrs.action, id:attrs.id, title:title, icon:attrs.icon, attrs:attrs])
	}

	def modal = { attrs, body ->
		if (!attrs.id) throwTagError("Tag [gui:modal] is missing required attribute [id]")
		def text = attrs.text
		if (!text && hasBody(body)) {
			text = body()
		}
		out << render(template: '/_fields/modal', model: [id:attrs.id, title:attrs.title, text:text, attrs:attrs])
	}

	def table = { attrs, body ->
		if (!attrs.domainName) {
			if (!attrs.list) {
				if (attrs.displayEmpty)
					throwTagError("Tag [gui:table] is missing required attribute [domainName] for [displayEmpty] since [list] is not available.")
				return
			}
			attrs.domainName = StringUtils.uncapitalize(attrs.list.first().getClass().getSimpleName())
		}

		def headerbuffer = []
		def tdbuffer = []
		// collect up the header and tds
		body(headerbuffer: headerbuffer, tdbuffer : tdbuffer, list : attrs.list, domainName : attrs.domainName)
		def tableId = attrs.id ? attrs.id : attrs.domainName + 'Table'
		def ids = []
		if (attrs.showEnabled || attrs.editEnabled || attrs.deleteEnabled || attrs.checkEnabled) {
			for (bean in attrs.list) {
				def propertyAccessor = resolveProperty(bean, "id")
				ids.add(propertyAccessor.value)
			}
		}
		def model = [headers: headerbuffer, tds: tdbuffer.transpose(), tableId: tableId, attrs : attrs, ids : ids]
		out << renderTable(model)
	}

	def column = { attrs, body ->
		def headerbuffer = pageScope.variables.headerbuffer
		def tdbuffer = pageScope.variables.tdbuffer
		def list = pageScope.variables.list
		def domainName = pageScope.variables.domainName
		if (headerbuffer == null || tdbuffer == null) {
			throwTagError("The [gui:column] tag can only be invoked inside a [table] tag body")
		}

		def label = attrs.remove('label')
		if (!label) {
			def property = attrs.property
			if (!property) throwTagError("Tag [gui:column] is missing attribute [property], since [label] hasn't been defined.")
			def propertyAccessor
			if (list) {
				propertyAccessor = resolveProperty(list.first(), property)
			} else {
				if (domainName)
					propertyAccessor = resolveProperty(null, domainName + "." + property)
				else
					throwTagError("Tag [gui:table] expects non-empty [list]")
			}
			label = resolveLabelText(propertyAccessor, attrs)
		}
		headerbuffer << label

		def columncontent = []
		def index = 0
		for (bean in list) {
			pageScope.variables[BEAN_PAGE_SCOPE_VARIABLE] = bean
			attrs.index = index
			if (attrs.editable == "true" || attrs.editableUrl) {
				def idPropertyAccessor = resolveProperty(bean, "id")
				attrs.id = idPropertyAccessor.value
				if (!attrs.editableUrl) {
					attrs.editableUrl = "/" + domainName + "/updatePropertyAjax"
				}
//				def versionPropertyAccessor = resolveProperty(bean, "version")
//				attrs.version = versionPropertyAccessor.value
		    }
			attrs.label = label
			columncontent << tdx(attrs, body)
			index++
		}
		tdbuffer << columncontent

		return null
	}

	def tr = { attrs, body ->		
		if (!attrs.bean) throwTagError("Tag [tr] is missing required attribute [bean]")
		pageScope.variables[BEAN_PAGE_SCOPE_VARIABLE] = attrs.bean
		out << ui.tr(attrs, body)
	}

	def tdx = { attrs, body ->
		def bean = resolveBean(attrs)
		if (!bean) throwTagError("Tag [$name] is missing required attribute [bean]")
		def property = attrs.property

		def value = ''
		if (property) {
			def propertyAccessor = resolveProperty(bean, property)
			def model = buildModel(propertyAccessor, attrs)
			value = propertyAccessor.value
			if (hasBody(body)) {
				value = body(value)
			} else {
				value = renderDefaultDisplay(model, attrs)
			}
		} else {
			value = body(bean : bean, index : attrs.index)
		}
		out << render(template: '/_fields/td', model: [value:value, attrs:attrs]) 
	}

	private String renderTable(Map model) {
		render template: '/_fields/table', model: model
	}

	private Map buildModel(BeanPropertyAccessor propertyAccessor, Map attrs) {
		def value = attrs.containsKey('value') ? attrs.remove('value') : propertyAccessor.value
		def valueDefault = attrs.remove('default')
		[
			bean: propertyAccessor.rootBean,
			property: propertyAccessor.pathFromRoot,
			type: propertyAccessor.propertyType,
			beanClass: propertyAccessor.beanClass,
			value: (value instanceof Number || value instanceof Boolean || value) ? value : valueDefault,
		]
	}

	private String renderDefaultDisplay(Map model, Map attrs = [:]) {
		switch (model.type) {
			case Boolean.TYPE:
			case Boolean:
				g.formatBoolean(boolean: model.value)
				break
			case Calendar:
			case Date:
			case java.sql.Date:
			case java.sql.Time:
				g.formatDate(date: model.value)
				break
			default:
				g.fieldValue bean: model.bean, field: model.property
		}
	}

	private Object resolveBean(attrs) {
		def bean = pageScope.variables[BEAN_PAGE_SCOPE_VARIABLE]
		if (!bean) bean = attrs.remove('bean')
		bean
	}

	private boolean hasBody(Closure body) {
		return !body.is(GroovyPage.EMPTY_BODY_CLOSURE)
	}

	private BeanPropertyAccessor resolveProperty(bean, String propertyPath) {
		beanPropertyAccessorFactory.accessorFor(bean, propertyPath)
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