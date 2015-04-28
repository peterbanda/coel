<%@ page import="com.banda.chemistry.domain.AcSpeciesAssociationType" %>
${it.getSpeciesAssociations(AcSpeciesAssociationType.Inhibitor).collect{ assoc -> link(action:'show',controller:'acSpecies',id: assoc.species.id) { assoc.species.label } }.join(', ')} 
