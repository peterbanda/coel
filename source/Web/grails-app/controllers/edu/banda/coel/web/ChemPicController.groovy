package edu.banda.coel.web

import edu.banda.coel.CoelRuntimeException
import edu.banda.coel.business.chempic.ChemistryPicGeneratorImpl
import edu.banda.coel.domain.service.ChemistryPicGenerator

class ChemPicController extends BaseController {

	def ChemistryPicGenerator chemPicGenerator = new ChemistryPicGeneratorImpl(true);

	def index = {
		def dnaStrandString = '{ls1}<us1>[ds]<us2>{ls2}'
		def svgXML = chemPicGenerator.createDNAStrandSVG(dnaStrandString)
		[dnaStrandString : dnaStrandString, svgXMLURI : svgXML]
	}

	def showDNAStrand = {
		def svgXMLURI;
		try {
			if (params.dnaStrandString == "") {
				throw new CoelRuntimeException("No DNA strand to show.")
			}
			svgXMLURI = chemPicGenerator.createDNAStrandSVG(params.dnaStrandString)
		} catch (Exception e) {
			render e.getMessage()
		}
		if (svgXMLURI) {			
			render svgXMLURI
		}
	}

	def exportDNAStrandAsSVG = {
		def svgXML
		try {
			if (params.dnaStrandString == "") {
				throw new CoelRuntimeException("No DNA strand to show.")
			}
			svgXML = chemPicGenerator.createDNAStrandSVG(params.dnaStrandString)
		} catch (Exception e) { 
			render e.getMessage()
		}
		if (svgXML) {
			render svgXML
		}
	}
}