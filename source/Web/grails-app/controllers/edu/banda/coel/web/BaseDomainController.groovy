package edu.banda.coel.web

import net.sf.beanlib.hibernate.HibernateBeanReplicator;

import org.apache.commons.lang.StringUtils
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.grails.plugin.filterpane.FilterPaneUtils
import org.hibernate.Criteria
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions

import com.banda.chemistry.domain.AcInteractionSeries;


import com.banda.core.Pair
import com.banda.core.reflection.GenericReflectionProvider;
import com.banda.core.util.ObjectUtil
import com.banda.math.domain.rand.RandomDistributionType
import com.banda.math.domain.rand.ShapeLocationDistribution
import com.banda.math.domain.rand.UniformDistribution
import com.banda.math.domain.rand.DiscreteDistribution
import com.banda.math.domain.rand.RandomDistribution
import com.banda.math.domain.rand.BooleanDensityUniformDistribution
import com.banda.core.util.ConversionUtil
import com.banda.core.util.ParseUtil
import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.domain.AcInteractionVariableAssignment

import java.util.HashSet

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException

import edu.banda.coel.web.SecureAccessService

import org.postgresql.util.PSQLException

abstract class BaseDomainController extends BaseController {

	static allowedMethods = [save: "POST", update: "POST"]

	static MAX_ITEMS_PER_PAGE = 20

	protected def doClazz
	protected def doClazzVariableName
	protected def doClazzMessageLabel
	def filterPaneService
	def SecureAccessService secureAccessService
	def HibernateBeanReplicator hibernateReplicator
	def GenericReflectionProvider genericReflectionProvider

	public BaseDomainController() {
		def doClazzName = StringUtils.substringBefore(getClass().getName(), "Controller")
		this.doClazz = Class.forName(doClazzName)
		this.doClazzVariableName = StringUtils.uncapitalize(doClazz.getSimpleName())
		this.doClazzMessageLabel = message(code: doClazzVariableName + ".label", default: doClazzVariableName)
	}

	public BaseDomainController(doClazz) {
		this.doClazz = doClazz
		this.doClazzVariableName = StringUtils.uncapitalize(doClazz.getSimpleName())
		this.doClazzMessageLabel = message(code: doClazzVariableName + ".label", default: doClazzVariableName)
	}

	def list(Integer max) {
		if (!doClazz) {
			return
		}
		params.max = Math.min(max ?: MAX_ITEMS_PER_PAGE, 100)
		if (!params.sort) {
			params.sort='id'
			params.order='desc'
		}

		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		def instanceListName = "list"
		def instanceListTotalName = "total"

		def instances = null
		def total = null
		if (params.createdBy) {
			if (params.projections)
				instances = doClazz.listWithParamsAndProjections(params)
			else
				instances = doClazz.findAllByCreatedBy(params.createdBy)
			total = doClazz.countByCreatedBy(params.createdBy)
		} else {
			if (params.projections)
				instances = doClazz.listWithParamsAndProjections(params)
			else
				instances = doClazz.list(params)
			total = doClazz.count()
		}

		[(instanceListName) : instances, (instanceListTotalName) : total, filterParams: FilterPaneUtils.extractFilterParams(params), className : doClazz.getName(), domainName : doClazzVariableName, domainNameLabel : doClazzMessageLabel]
	}

	def filter = {
		if (!isAdmin())
			params.createdBy = getCurrentUserOrError()

		if (params.fullList)
			params.max = null
		else
			if (!params.max) params.max = 20

		def ll = filterPaneService.filter( params,  doClazz ).unique()
		render(view:'list', model:[list: ll, total: filterPaneService.count( params, doClazz), filterParams: FilterPaneUtils.extractFilterParams(params), className : doClazz.getName(), domainName : doClazzVariableName, domainNameLabel : doClazzMessageLabel, params:params ])
	}

	def show(Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFound(id)
			return
		}
		[instance: instance]
	}

	def edit(Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFound(id)
			return
		}
		[instance: instance]
	}

	def updatePropertyAjax(String value, String property, Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFoundAjax(id)
			return
		}

		instance."$property" = value

		saveFromAjax(instance, property)
	}

	protected saveFromAjax(instance, property) {
		def saved = false
		if (!instance.hasErrors())
			try {
				saved = instance.save(flush: true)
			} catch (e) {
				saved = false
				instance.errors.rejectValue(property,e.message)
			}

		if (!saved) {
			response.status = 400
			render message(error:instance.errors.allErrors.first(),encodeAs:'HTML')
			return
		}

		render(status: 200, text: 'Update successful.')
	}

	def deleteAjax(Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFoundAjax(id)
			return
		}
		def owner = getOwner(instance)
		def saved = true
		try {
			deleteInstance(instance)			
		} catch (DataIntegrityViolationException e) {
			saved = false
		} catch (PSQLException e) {
			saved = false
		}
		if (!saved) {
			response.status = 400
			render message(code: 'default.not.deleted.message', args: [doClazzMessageLabel, id], encodeAs:'HTML')
			return
		}

		render(status: 200, text: message(code: 'default.deleted.message', args: [doClazzMessageLabel, id]))
	}

	def delete(Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFound(id)
			return
		}
		def owner = getOwner(instance)
		try {
			deleteInstance(instance)
			flash.message = message(code: 'default.deleted.message', args: [doClazzMessageLabel, id])
			deleteSuccessfulRedirect(instance, owner)
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [doClazzMessageLabel, id])
			deleteFailedRedirect(instance)
		} catch (PSQLException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [doClazzMessageLabel, id]) + e.getServerErrorMessage()
			deleteFailedRedirect(instance)
		}
	}

	def copy(Long id) {
		def instance = getSafe(id)
		if (!instance) {
			handleObjectNotFound(id)
			return
		}
		try {
			def newInstance = copyInstance(instance)
			if (newInstance.hasErrors()) {
				copyFailedRedirect(instance)
				return
			}
			flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, newInstance.id])
			copySuccessfulRedirect(newInstance)
		} catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.copied.message', args: [doClazzMessageLabel, id])
			copyFailedRedirect(instance)
		} catch (PSQLException e) {
			flash.message = message(code: 'default.not.copied.message', args: [doClazzMessageLabel, id]) + e.getServerErrorMessage()
			copyFailedRedirect(instance)
		}
	}

	def deleteMultiple() {
		if (!params.ids) {
			flash.message = "No rows selected!"
			notFoundRedirect(null)
			return
		}
		def ids = ParseUtil.parseArray(params.ids, Long.class, doClazzVariableName + " id", ",")
		def error = false
		def instancesWithOwners = ids.collect{ id ->
			def instance = getSafe(id)
			if (!instance) {
				error = true
				handleObjectNotFound(id)
				return
			}
			def owner = getOwner(instance)
			try {
				deleteInstance(instance)
			} catch (DataIntegrityViolationException e) {
				flash.message = message(code: 'default.not.deleted.message', args: [doClazzMessageLabel, id])
				error = true
				deleteFailedRedirect(instance)
				return
			} catch (PSQLException e) {
				flash.message = message(code: 'default.not.deleted.message', args: [doClazzMessageLabel, id]) + e.getServerErrorMessage()
				error = true
				deleteFailedRedirect(instance)
				return
			}
			new Pair(instance, owner)
		}
		if (!error) {
			def size = ids.size()
			if (size == 1)
				flash.message = message(code: 'default.deleted.message', args: [doClazzMessageLabel, ids.get(0)])
			else 
				flash.message = message(code: 'default.multideleted.message', args: [doClazzMessageLabel, ids.join(", "), size])
			deleteMultiSuccessfulRedirect(instancesWithOwners)
		}
	}

	def copyMultiple() {
		if (!params.ids) {
			flash.message = "No rows selected!"
			notFoundRedirect(null)
			return
		}
		def ids = ParseUtil.parseArray(params.ids, Long.class, doClazzVariableName + " id", ",")
		Collections.sort(ids)

		def error = false
		def newInstances = ids.collect{ id ->
			def instance = getSafe(id)
			if (!instance) {
				error = true
				handleObjectNotFound(id)
				return
			}
			try {
				def newInstance = copyInstance(instance)
				if (newInstance.hasErrors()) {
					error = true
					copyFailedRedirect(instance)
					return
				}
				newInstance
			} catch (DataIntegrityViolationException e) {
				println e.getMessage()
				flash.message = message(code: 'default.not.copied.message', args: [doClazzMessageLabel, id])
				error = true
				copyFailedRedirect(instance)
				return
			} catch (PSQLException e) {
				flash.message = message(code: 'default.not.copied.message', args: [doClazzMessageLabel, id]) + e.getServerErrorMessage()
				error = true
				copyFailedRedirect(instance)
				return
			}
		}
		if (!error) {
			def newIds = newInstances*.id
			def size = newIds.size()
			if (size == 1)
				flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, newIds.get(0)])
			else
				flash.message = message(code: 'default.multicreated.message', args: [doClazzMessageLabel, newIds.join(", "), size])
			copyMultiSuccessfulRedirect(newInstances)
		}
	}

	def showNext(Long id) {
		def nextId = doClazz.nextId(id)

		if (!nextId) {
			handleObjectNotFound(id)
			return
		}
	
		redirect(action: "show", id: nextId)
	}

	def showPrevious(Long id) {
		def previousId = doClazz.previousId(id)
		
		if (!previousId) {
			handleObjectNotFound(id)
			return
		}
	
		redirect(action: "show", id: previousId)
	}

	protected def deleteInstance(instance) {
		doClazz.withTransaction{ status ->
			instance.delete(flush: true)
		}
	}

	protected def copyInstance(instance) {
		def newInstance = genericReflectionProvider.clone(instance)
		newInstance.id = null
		newInstance.version = null

		newInstance.save(flush:true)
		newInstance
	}

	protected def handleObjectNotFound(id) {
		flash.message = message(code: 'default.not.found.message', args: [doClazzMessageLabel, id])
		notFoundRedirect(id)
	}

	protected def handleObjectNotFoundAjax(id) {
		response.status = 400
		render message(code: 'default.not.found.message', args: [doClazzMessageLabel, id], encodeAs:'HTML') + ". Please refresh your browser."
	}
	
	protected def notFoundRedirect(id) {
		redirect(action: "list")
	}

	protected def copyMultiSuccessfulRedirect(newInstances) {
		redirect(action: "list")
	}

	protected def copySuccessfulRedirect(newInstance) {
		redirect(action: "list")
	}

	protected def copyFailedRedirect(instance) {
		redirect(action: "show", id: instance.id)
	}

	protected def deleteSuccessfulRedirect(instance, owner) {
		redirect(action: "list")
	}

	protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
		redirect(action: "list")
	}

	protected def deleteFailedRedirect(instance) {
		redirect(action: "show", id: instance.id)
	}
	
	protected def getOwner(instance) {
		null
	}

	protected def setOlcFailureMessage(instance) {
		instance.errors.rejectValue("version", "default.optimistic.locking.failure", [doClazzMessageLabel] as Object[], "Another user has updated this " + doClazzMessageLabel + " while you were editing")
	}

	protected def getSafe(Long id) {
		secureAccessService.get(doClazz, id)
	}

	protected def getWithProjections(Long id, properties) {
		secureAccessService.getWithProjections(doClazz, id, properties)
	}

	protected def updateSafe(object) {
		secureAccessService.save(object)
	}

	protected def updateRandomDistribution = {
		def randomDistribution = bindRandomDistribution(params.prefix)
		render(template:"/randomDistribution/refresh", model: ["randomDistribution": randomDistribution, "prefix" : params.prefix])
	}

	protected def updateRandomDistributionInt = {
		def randomDistribution = bindRandomDistributionInt(params.prefix)
		render(template:"/randomDistribution/refresh", model: ["randomDistribution": randomDistribution, "prefix" : params.prefix])
	}

	protected def bindRandomDistribution(String prefix) {
		def randomDistribution = null
		switch (params[prefix + 'DistributionType'] as RandomDistributionType) {
			case RandomDistributionType.Uniform:
				randomDistribution = new UniformDistribution()
				randomDistribution.from = params.double(prefix + 'From')
				randomDistribution.to = params.double(prefix + 'To')
				break
			case RandomDistributionType.Normal:
				randomDistribution = new ShapeLocationDistribution(RandomDistributionType.Normal)
				randomDistribution.shape = params.double(prefix + 'Shape')
				randomDistribution.location = params.double(prefix + 'Location')
				break
			case RandomDistributionType.LogNormal:
				randomDistribution = new ShapeLocationDistribution(RandomDistributionType.LogNormal)
				randomDistribution.shape = params.double(prefix + 'Shape')
				randomDistribution.location = params.double(prefix + 'Location')
				break
			case RandomDistributionType.PositiveNormal:
				randomDistribution = new ShapeLocationDistribution(RandomDistributionType.PositiveNormal)
				randomDistribution.shape = params.double(prefix + 'Shape')
				randomDistribution.location = params.double(prefix + 'Location')
				break
			case RandomDistributionType.Discrete:
				randomDistribution = new DiscreteDistribution()
			
				if (params[prefix + 'ValueTypeName']) { 
					def valueTypeClass = Class.forName(params[prefix + 'ValueTypeName'])
					randomDistribution.probabilities = ConversionUtil.convertToList(Double.class, params[prefix + 'Probabilities'], "Probabilities")
					randomDistribution.values = ConversionUtil.convertToList(valueTypeClass, params[prefix + 'Values'], "Probabilities")
				}
				break
			case RandomDistributionType.BooleanDensityUniform:
				randomDistribution = new BooleanDensityUniformDistribution()
				break
		}
		randomDistribution
	}

	protected def bindRandomDistributionInt(String prefix) {
		def randomDistribution = null
		switch (params[prefix + 'DistributionType'] as RandomDistributionType) {
			case RandomDistributionType.Uniform:
				randomDistribution = new UniformDistribution()
				randomDistribution.from = params.int(prefix + 'From')
				randomDistribution.to = params.int(prefix + 'To')
				break
			case RandomDistributionType.Normal:
				randomDistribution = new ShapeLocationDistribution(RandomDistributionType.Normal)
				randomDistribution.shape = params.double(prefix + 'Shape')
				randomDistribution.location = params.double(prefix + 'Location')
				randomDistribution.valueType = Integer.class
				break
			case RandomDistributionType.LogNormal:
				randomDistribution = new ShapeLocationDistribution(RandomDistributionType.LogNormal)
				randomDistribution.shape = params.double(prefix + 'Shape')
				randomDistribution.location = params.double(prefix + 'Location')
				randomDistribution.valueType = Integer.class
				break
			case RandomDistributionType.PositiveNormal:
				randomDistribution = new ShapeLocationDistribution(RandomDistributionType.PositiveNormal)
				randomDistribution.shape = params.double(prefix + 'Shape')
				randomDistribution.location = params.double(prefix + 'Location')
				randomDistribution.valueType = Integer.class
				break
			case RandomDistributionType.Discrete:
				def valueTypeClass = Class.forName(params[prefix + 'ValueTypeName'])

				randomDistribution = new DiscreteDistribution()
				randomDistribution.probabilities = ConversionUtil.convertToList(Double.class, params[prefix + 'Probabilities'], "Probabilities")
				randomDistribution.values = ConversionUtil.convertToList(valueTypeClass, params[prefix + 'Values'], "Probabilities")
				break
			case RandomDistributionType.BooleanDensityUniform:
				randomDistribution = new BooleanDensityUniformDistribution()
				break
		}
		randomDistribution
	}

	protected def isRandomDistributionEqual(RandomDistribution rd1, RandomDistribution rd2) {
		if (rd1 == null && rd2 == null) {
			return true
		}
		if (rd1 == null || rd2 == null || (rd1.type != rd2.type)) {
			return false
		}
		switch (rd1.type) {
			case RandomDistributionType.Uniform: 
				return ObjectUtil.areObjectsEqual([rd1.from, rd1.to] as Object[], [rd2.from, rd2.to] as Object[])
			case RandomDistributionType.Normal:
			case RandomDistributionType.LogNormal:
			case RandomDistributionType.PositiveNormal:
				return ObjectUtil.areObjectsEqual([rd1.shape, rd1.location] as Object[], [rd2.shape, rd2.location] as Object[])
			case RandomDistributionType.Discrete:
				return ObjectUtil.areObjectsEqual(rd1.probabilities, rd2.probabilities) && ObjectUtil.areObjectsEqual(rd1.values, rd2.values)
			case RandomDistributionType.BooleanDensityUniform:
				return true;
		}
	}

	// move elsewhere
	protected def saveActionSeries(AcInteractionSeries acInteractionSeriesInstance, AcInteractionSeries acInteractionSeriesInstanceClone) {
		def interactionVariableAssignmentMap = [ : ]
		acInteractionSeriesInstanceClone.actions.each{action ->
			interactionVariableAssignmentMap.put(action, action.variableAssignments)
			action.setVariableAssignments(new HashSet<AcInteractionVariableAssignment>())
		}
		if (!acInteractionSeriesInstanceClone.save(flush: true)) {
			render(view: "edit", model: [instance: acInteractionSeriesInstance])
			return
		}
		// now add variable assignments and save again
		interactionVariableAssignmentMap.entrySet().each { actionInteractionVariableAssignments ->
			def action = actionInteractionVariableAssignments.getKey()
			def interactionVariableAssignments = actionInteractionVariableAssignments.getValue()
			action.addVariableAssignments(interactionVariableAssignments)
			if (!action.save(flush: true)) {
				render(view: "edit", model: [instance: acInteractionSeriesInstance])
				return
			}
		}
	}

	protected def saveActionSeriesRecursively(acInteractionSeriesInstance, actionSeriesCloneMap) {
		def actionSeriesClone = actionSeriesCloneMap.get(acInteractionSeriesInstance)
		actionSeriesClone.removeAllSubActionSeries()

		acInteractionSeriesInstance.subActionSeries.each{
			saveActionSeriesRecursively(it, actionSeriesCloneMap)
		}
		acInteractionSeriesInstance.subActionSeries.each{
			def subActionSeriesClone = actionSeriesCloneMap.get(it)
			actionSeriesClone.addSubActionSeries(subActionSeriesClone)
		}
		saveActionSeries(acInteractionSeriesInstance, actionSeriesClone)
	}
}