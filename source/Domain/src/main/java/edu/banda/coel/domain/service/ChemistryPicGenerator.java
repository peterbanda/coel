package edu.banda.coel.domain.service;

import java.util.List;

/**
 * @author Peter Banda
 * @since 2013
 */
public interface ChemistryPicGenerator {

    /**
     * Creates an SVG XML document of given DNA strand specified in the Visual DSD syntax. 
     *
     * @param dnaStrand DNA strand to visualize
     * @return SVG XML document marshaled to String
     */
    String createDNAStrandSVG(String dnaStrand);

    /**
     * Creates an SVG XML document of given DNA strand specified in the Visual DSD syntax. 
     *
     * @param reactantDNAStrands Reactant DNA strands to visualize
     * @param productDNAStrands Product DNA strands to visualize
     * @return SVG XML document marshaled to String
     */
    String createDNAReactionSVG(List<String> reactantDNAStrands, List<String> productDNAStrands, boolean bidirectional);
}