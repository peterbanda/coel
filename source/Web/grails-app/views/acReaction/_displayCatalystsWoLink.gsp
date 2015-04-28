<%@ page import="com.banda.chemistry.domain.AcSpeciesAssociationType" %>
${it.getSpeciesAssociations(AcSpeciesAssociationType.Catalyst).collect{ assoc -> assoc.species.label }.join(', ')} 
