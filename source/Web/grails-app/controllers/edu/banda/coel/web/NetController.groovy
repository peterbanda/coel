package edu.banda.coel.web

import com.banda.network.domain.Network
import com.banda.network.domain.NetworkFunction
import com.banda.network.domain.Topology

class NetController extends BaseController {

	def index = {
		def noTopologies = true
		def noNetworkFunctions = true
		def noNetworks = true
		if (isAdmin()) {
			noTopologies = Topology.count() == 0
			noNetworkFunctions = NetworkFunction.count() == 0
			noNetworks = Network.count() == 0
		} else {
			def user = getCurrentUserOrError()
			noTopologies = Topology.countByCreatedBy(user) == 0
			noNetworkFunctions = NetworkFunction.countByCreatedBy(user) == 0
			noNetworks = Network.countByCreatedBy(user) == 0
		}
		[noTopologies : noTopologies, noNetworkFunctions : noNetworkFunctions, noNetworks : noNetworks]
	}
}
