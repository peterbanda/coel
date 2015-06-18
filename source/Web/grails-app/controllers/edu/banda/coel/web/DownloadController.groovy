package edu.banda.coel.web

import com.banda.chemistry.business.OctaveGenerator
import com.banda.chemistry.domain.AcReactionSet
import com.banda.network.domain.NetworkFunction
import grails.converters.JSON
import org.springframework.security.access.AccessDeniedException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class DownloadController extends BaseController {

	private def reactionSetIds = [4154l,4153l,4152l,3674l,3673l,3672l,3671l,3611l,3548l,3536l,3537l,3538l,3531l,3547l,3546l,3545l,3544l,3535l,3465l,3466l,3454l,1033l,1024l,1022l]

    private def networkFunctionIds = [1087l,1086l,1056l,1052l,1083l,1041l,1012l,1020l,1021l,1022l,1024l]

    def NetworkCommonService networkCommonService

    def ChemistryCommonService chemistryCommonService

	def index = {}

	def chemistry = {}

	def network = {}

    def exportReactionSet(Long id) {
        if (!reactionSetIds.contains(id))
            throw new AccessDeniedException("Reaction set not available for download.")

        def reactionSet = AcReactionSet.get(id)
        def octaveOutput = OctaveGenerator.apply(reactionSet)
        response.setHeader("Content-disposition", "attachment; filename=" + "reactionSet_${id}_ode.m");
        render(contentType: "text", text: octaveOutput.toString());
    }

    def getReactionStructureImages(Long id) {
        if (!reactionSetIds.contains(id))
            throw new AccessDeniedException("Reaction set not available for download.")

        def reactionSet = AcReactionSet.get(id)
        def images = chemistryCommonService.getReactionStructureImages(reactionSet, false)

        response.setContentType('APPLICATION/OCTET-STREAM')
        response.setHeader('Content-Disposition', 'Attachment;Filename=reaction_set_' + id + '.zip')
        ZipOutputStream zip = new ZipOutputStream(response.outputStream);

        images.each{ reactionImage ->
            def file1Entry = new ZipEntry(reactionImage.label + '.svg');
            zip.putNextEntry(file1Entry);
            zip.write(reactionImage.image.bytes);
        }
        zip.close();
    }

    def getSpeciesStructureImages(Long id) {
        if (!reactionSetIds.contains(id))
            throw new AccessDeniedException("Reaction set not available for download.")

        def reactionSet = AcReactionSet.get(id)
        def images = chemistryCommonService.getSpeciesStructureImages(reactionSet, false)

        response.setContentType('APPLICATION/OCTET-STREAM')
        response.setHeader('Content-Disposition', 'Attachment;Filename=reaction_set_species_' + id + '.zip')
        ZipOutputStream zip = new ZipOutputStream(response.outputStream);

        images.each{ speciesImage ->
            def file1Entry = new ZipEntry(speciesImage.label + '.svg');
            zip.putNextEntry(file1Entry);
            zip.write(speciesImage.image.bytes);
        }
        zip.close();
    }

    def exportNetworkFunction(Long id) {
        if (!networkFunctionIds.contains(id))
            throw new AccessDeniedException("Network function not available for download.")

        def lsbFirst = (params.lsbfirst != null)
        def networkFunctionInstance = NetworkFunction.get(id)
        def text = networkCommonService.getTransitionTableInfo(networkFunctionInstance, lsbFirst)

        response.setHeader("Content-disposition", "attachment; filename=" + "transition_table_${id}");
        render(contentType: "text", text: text);
    }
}