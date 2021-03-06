-- FUN

DROP TABLE FUN_FUNCTION CASCADE;
DROP SEQUENCE FUN_FUNCTION_ID_SEQ;

DROP TABLE FUN_TRANSITION_TABLE CASCADE;

DROP TABLE FUN_EXPRESSION CASCADE;

DROP TABLE FUN_RANDOM_DISTRIBUTION CASCADE;
DROP SEQUENCE FUN_RANDOM_DISTRIBUTION_ID_SEQ;

-- NET

DROP TABLE NET_TOPOLOGY CASCADE;
DROP SEQUENCE NET_TOPOLOGY_ID_SEQ;

DROP TABLE NET_LAYER_TOPOLOGY_MAPPING CASCADE;

DROP TABLE NET_TEMPLATE_TOPOLOGY CASCADE;

DROP TABLE NET_SPATIAL_TOPOLOGY CASCADE;

DROP TABLE NET_SPATIAL_NEIGHBORHOOD CASCADE;
DROP SEQUENCE NET_SPATIAL_NEIGHBORHOOD_ID_SEQ;

DROP TABLE NET_SPATIAL_NEIGHBOR CASCADE;
DROP SEQUENCE NET_SPATIAL_NEIGHBOR_ID_SEQ;

DROP TABLE NET_WEIGHT_SETTING CASCADE;	
DROP SEQUENCE NET_WEIGHT_SETTING_ID_SEQ;

DROP TABLE NET_FUNCTION CASCADE;
DROP SEQUENCE NET_FUNCTION_ID_SEQ;

DROP TABLE NET_NETWORK CASCADE;
DROP SEQUENCE NET_NETWORK_ID_SEQ;

DROP TABLE NET_ACTION CASCADE;
DROP SEQUENCE NET_ACTION_ID_SEQ;

DROP TABLE NET_ACTION_SERIES CASCADE;
DROP SEQUENCE NET_ACTION_SERIES_ID_SEQ;

DROP TABLE NET_EVALUATION CASCADE;
DROP SEQUENCE NET_EVALUATION_ID_SEQ;

DROP TABLE NET_PERFORMANCE CASCADE;
DROP SEQUENCE NET_PERFORMANCE_ID_SEQ;

DROP TABLE NET_EVALUATION_ITEM CASCADE;
DROP SEQUENCE NET_EVALUATION_ITEM_ID_SEQ;

DROP TABLE NET_EVALUATION_VAR CASCADE;
DROP SEQUENCE NET_EVALUATION_VAR_ID_SEQ;

DROP TABLE NET_DERRIDA CASCADE;
DROP SEQUENCE NET_DERRIDA_ID_SEQ;

DROP TABLE NET_DAMAGE_SPREADING CASCADE;
DROP SEQUENCE NET_DAMAGE_SPREADING_ID_SEQ;

-- AC

DROP TABLE AC_ARTIFICIAL_CHEMISTRY CASCADE;
DROP SEQUENCE AC_ARTIFICIAL_CHEMISTRY_ID_SEQ;

DROP TABLE AC_SPECIES_REACTION_ASSOCIATION CASCADE;
DROP SEQUENCE AC_SPECIES_REACTION_ASSOCIATION_ID_SEQ;

DROP TABLE AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING CASCADE;

DROP TABLE AC_SPECIES CASCADE;
DROP SEQUENCE AC_SPECIES_ID_SEQ;

DROP TABLE AC_SPECIES_SET CASCADE;
DROP SEQUENCE AC_SPECIES_SET_ID_SEQ;

DROP TABLE AC_PARAMETER CASCADE;
DROP SEQUENCE AC_PARAMETER_ID_SEQ;

DROP TABLE AC_PARAMETER_SET CASCADE;

DROP TABLE AC_REACTION CASCADE;
DROP SEQUENCE AC_REACTION_ID_SEQ;

DROP TABLE AC_REACTION_GROUP CASCADE;
DROP SEQUENCE AC_REACTION_GROUP_ID_SEQ;

DROP TABLE AC_REACTION_SET CASCADE;
DROP SEQUENCE AC_REACTION_SET_ID_SEQ;

DROP TABLE AC_COMPARTMENT_CHANNEL CASCADE;
DROP SEQUENCE AC_COMPARTMENT_CHANNEL_ID_SEQ;

DROP TABLE AC_COMPARTMENT_CHANNEL_GROUP_MAPPING CASCADE;

DROP TABLE AC_COMPARTMENT_CHANNEL_GROUP CASCADE;
DROP SEQUENCE AC_COMPARTMENT_CHANNEL_GROUP_ID_SEQ;

DROP TABLE AC_COMPARTMENT CASCADE;
DROP SEQUENCE AC_COMPARTMENT_ID_SEQ;

DROP TABLE AC_SUB_COMPARTMENT_ASSOCIATION CASCADE;
DROP SEQUENCE AC_SUB_COMPARTMENT_ASSOCIATION_ID_SEQ;

DROP TABLE AC_SPECIES_ACTION CASCADE;
DROP SEQUENCE AC_SPECIES_ACTION_ID_SEQ;

DROP TABLE AC_CACHE_WRITE CASCADE;
DROP SEQUENCE AC_CACHE_WRITE_ID_SEQ;

DROP TABLE AC_ACTION_SERIES_IMMUTABLE_SPECIES_MAPPING CASCADE;

DROP TABLE AC_ACTION_VAR CASCADE;
DROP SEQUENCE AC_ACTION_VAR_ID_SEQ;

DROP TABLE AC_ACTION CASCADE;
DROP SEQUENCE AC_ACTION_ID_SEQ;

DROP TABLE AC_ACTION_SERIES CASCADE;
DROP SEQUENCE AC_ACTION_SERIES_ID_SEQ;

DROP TABLE AC_EVALUATED_SPECIES_ACTION CASCADE;
DROP SEQUENCE AC_EVALUATED_SPECIES_ACTION_ID_SEQ;

DROP TABLE AC_EVALUATED_ACTION CASCADE;
DROP SEQUENCE AC_EVALUATED_ACTION_ID_SEQ;

DROP TABLE AC_EVALUATED_ACTION_SERIES CASCADE;
DROP SEQUENCE AC_EVALUATED_ACTION_SERIES_ID_SEQ;

DROP TABLE AC_RUN CASCADE;
DROP SEQUENCE AC_RUN_ID_SEQ;

DROP TABLE AC_SPECIES_HISTORY CASCADE;
DROP SEQUENCE AC_SPECIES_HISTORY_ID_SEQ;

DROP TABLE AC_TRANSLATION_ITEM CASCADE;
DROP SEQUENCE AC_TRANSLATION_ITEM_ID_SEQ;

DROP TABLE AC_TRANSLATION_VAR CASCADE;
DROP SEQUENCE AC_TRANSLATION_VAR_ID_SEQ;

DROP TABLE AC_RANGE_TRANSLATION CASCADE;
DROP SEQUENCE AC_RANGE_TRANSLATION_ID_SEQ;

DROP TABLE AC_TRANSLATION_SERIES CASCADE;
DROP SEQUENCE AC_TRANSLATION_SERIES_ID_SEQ;

DROP TABLE AC_TRANSLATED_RUN CASCADE;
DROP SEQUENCE AC_TRANSLATED_RUN_ID_SEQ;

DROP TABLE AC_TRANSLATION_ITEM_HISTORY CASCADE;
DROP SEQUENCE AC_TRANSLATION_ITEM_HISTORY_ID_SEQ;

DROP TABLE AC_EVALUATION CASCADE;
DROP SEQUENCE AC_EVALUATION_ID_SEQ;

DROP TABLE AC_EVALUATED_RUN CASCADE;
DROP SEQUENCE AC_EVALUATED_RUN_ID_SEQ;

DROP TABLE AC_EVALUATED_PERFORMANCE CASCADE;
DROP SEQUENCE AC_EVALUATED_PERFORMANCE_ID_SEQ;

DROP TABLE AC_RATE_CONSTANT_TYPE_BOUND CASCADE;
DROP SEQUENCE AC_RATE_CONSTANT_TYPE_BOUND_ID_SEQ;

DROP TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC CASCADE;
DROP SEQUENCE AC_ARTIFICIAL_CHEMISTRY_SPEC_ID_SEQ;

DROP TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC_BOUND CASCADE;
DROP SEQUENCE AC_ARTIFICIAL_CHEMISTRY_SPEC_BOUND_ID_SEQ;

DROP TABLE AC_SIMULATION_CONFIG CASCADE;
DROP SEQUENCE AC_SIMULATION_CONFIG_ID_SEQ;

-- UM

DROP TABLE UM_USER_ROLE_MAPPING CASCADE;

DROP TABLE UM_ROLE CASCADE;
DROP SEQUENCE UM_ROLE_ID_SEQ;

DROP TABLE UM_USER CASCADE;
DROP SEQUENCE UM_USER_ID_SEQ;

-- EVO

DROP TABLE EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING CASCADE;

DROP TABLE EVO_AC_TASK_REACTION_MAPPING CASCADE;

DROP TABLE EVO_AC_TASK_REACTION_GROUP_MAPPING CASCADE;

DROP TABLE EVO_AC_TASK_ACTION_SERIES_MAPPING CASCADE;
DROP SEQUENCE EVO_AC_TASK_ACTION_SERIES_MAPPING_ID_SEQ;

DROP TABLE EVO_NET_TASK_ACTION_SERIES_MAPPING;

DROP TABLE EVO_AC_TASK CASCADE;
DROP SEQUENCE EVO_AC_TASK_ID_SEQ;

DROP TABLE EVO_GA_SETTING CASCADE;
DROP SEQUENCE EVO_GA_SETTING_ID_SEQ;

DROP TABLE EVO_RUN CASCADE;
DROP SEQUENCE EVO_RUN_ID_SEQ;

DROP TABLE EVO_POPULATION CASCADE;
DROP SEQUENCE EVO_POPULATION_ID_SEQ;

DROP TABLE EVO_CHROMOSOME CASCADE;
DROP SEQUENCE EVO_CHROMOSOME_ID_SEQ;

DROP TABLE EVO_AC_SPECIES_ASSIGNMENT_MAPPING CASCADE;

DROP TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING CASCADE;

DROP TABLE EVO_AC_SPECIES_ASSIGNMENT_BOUND CASCADE;
DROP SEQUENCE EVO_AC_SPECIES_ASSIGNMENT_BOUND_ID_SEQ;

DROP TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_BOUND CASCADE;
DROP SEQUENCE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_BOUND_ID_SEQ;

-- DYN
DROP TABLE DYN_STATS CASCADE;
DROP SEQUENCE DYN_STATS_ID_SEQ;

DROP TABLE DYN_STATS_SEQ CASCADE;
DROP SEQUENCE DYN_STATS_SEQ_ID_SEQ;

DROP TABLE DYN_SINGLE_ANALYSIS_SPEC CASCADE;
DROP SEQUENCE DYN_SINGLE_ANALYSIS_SPEC_ID_SEQ;

DROP TABLE DYN_MULTI_ANALYSIS_SPEC CASCADE;
DROP SEQUENCE DYN_MULTI_ANALYSIS_SPEC_ID_SEQ;

DROP TABLE DYN_SINGLE_ANALYSIS_RESULT CASCADE;
DROP SEQUENCE DYN_SINGLE_ANALYSIS_RESULT_ID_SEQ;

DROP TABLE DYN_MULTI_ANALYSIS_RESULT CASCADE;
DROP SEQUENCE DYN_MULTI_ANALYSIS_RESULT_ID_SEQ;

-- TASK
DROP TABLE TASK_EVO_RUN CASCADE;
DROP TABLE TASK CASCADE;
DROP SEQUENCE TASK_ID_SEQ;
