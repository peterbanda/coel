package edu.banda.coel.web

import com.banda.chemistry.business.OctaveGenerator
import com.banda.chemistry.domain.AcReactionSet
import com.banda.function.enumerator.ListEnumeratorFactory
import com.banda.math.business.sym.twodim.TwoDimSymmetricConfigurationEnumerator
import com.banda.network.domain.NetworkFunction
import org.springframework.security.access.AccessDeniedException

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class SymCAController extends BaseController {

    def NetworkCommonService networkCommonService

    def ListEnumeratorFactory listEnumeratorFactory

	def index = {}

	def enumeration = {}

	def probability = {}

    def enumerateTwoDimConfs(Long size) {
        def enumerator = new TwoDimSymmetricConfigurationEnumerator(size, 2, listEnumeratorFactory)
        def confCount = enumerator.enumerateAll3()
        render confCount
    }
}