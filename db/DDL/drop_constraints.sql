---------------
-- FOREIGN KEYS
---------------

-- FUN

ALTER TABLE FUN_FUNCTION DROP CONSTRAINT FK_FUN_FUNCTION_PARENT;
ALTER TABLE FUN_TRANSITION_TABLE DROP CONSTRAINT FK_FUN_TRANSITION_TABLE_FUN;
ALTER TABLE FUN_EXPRESSION DROP CONSTRAINT FK_FUN_EXPRESSION_FUN;

-- NET

ALTER TABLE NET_LAYER_TOPOLOGY_MAPPING DROP CONSTRAINT FK_NET_LAYER_TOPOLOGY_MAPPING_PARENT;
ALTER TABLE NET_LAYER_TOPOLOGY_MAPPING DROP CONSTRAINT FK_NET_LAYER_TOPOLOGY_MAPPING_LAYER;

ALTER TABLE NET_TOPOLOGY DROP CONSTRAINT FK_NET_TOPOLOGY_USER;
ALTER TABLE NET_TEMPLATE_TOPOLOGY DROP CONSTRAINT FK_NET_TEMPLATE_TOPOLOGY_BASE;
ALTER TABLE NET_SPATIAL_TOPOLOGY DROP CONSTRAINT FK_NET_SPATIAL_TOPOLOGY_BASE;
ALTER TABLE NET_SPATIAL_TOPOLOGY DROP CONSTRAINT FK_NET_SPATIAL_TOPOLOGY_NEIGHBORHOOD;

ALTER TABLE NET_SPATIAL_NEIGHBORHOOD DROP CONSTRAINT FK_NET_SPATIAL_NEIGHBORHOOD_USER;
ALTER TABLE NET_SPATIAL_NEIGHBOR DROP CONSTRAINT FK_NET_SPATIAL_NEIGHBOR_PARENT;

ALTER TABLE NET_WEIGHT_SETTING DROP CONSTRAINT FK_NET_WEIGHT_SETTING_PARENT;
ALTER TABLE NET_WEIGHT_SETTING DROP CONSTRAINT FK_NET_WEIGHT_SETTING_USER;
ALTER TABLE NET_WEIGHT_SETTING DROP CONSTRAINT FK_NET_WEIGHT_SETTING_RANDOM_DISTRIBUTION;

ALTER TABLE NET_FUNCTION DROP CONSTRAINT FK_NET_FUNCTION_USER;
ALTER TABLE NET_FUNCTION DROP CONSTRAINT FK_NET_FUNCTION_PARENT;
ALTER TABLE NET_FUNCTION DROP CONSTRAINT FK_NET_FUNCTION_FUNCTION;

ALTER TABLE NET_NETWORK DROP CONSTRAINT FK_NET_NETWORK_TOPOLOGY;
ALTER TABLE NET_NETWORK DROP CONSTRAINT FK_NET_NETWORK_USER;
ALTER TABLE NET_NETWORK DROP CONSTRAINT FK_NET_NETWORK_WEIGHT_SETTING;
ALTER TABLE NET_NETWORK DROP CONSTRAINT FK_NET_NETWORK_FUNCTION;

ALTER TABLE NET_ACTION_SERIES DROP CONSTRAINT FK_NET_ACTION_SERIES_USER;
ALTER TABLE NET_ACTION DROP CONSTRAINT FK_NET_ACTION_SERIES FOREIGN KEY;
ALTER TABLE NET_ACTION DROP CONSTRAINT FK_NET_ACTION_RANDOM_DISTRIBUTION;

ALTER TABLE NET_EVALUATION DROP CONSTRAINT FK_NET_EVALUATION_FUNCTION;
ALTER TABLE NET_EVALUATION DROP CONSTRAINT FK_NET_EVALUATION_USER;
ALTER TABLE NET_EVALUATION_ITEM DROP CONSTRAINT FK_NET_EVALUATION_ITEM_EVALUATION;
ALTER TABLE NET_EVALUATION_ITEM DROP CONSTRAINT FK_NET_EVALUATION_ITEM_VAR;
ALTER TABLE NET_EVALUATION_ITEM DROP CONSTRAINT FK_NET_EVALUATION_ITEM_FUNCTION;
ALTER TABLE NET_EVALUATION_VAR DROP CONSTRAINT FK_NET_EVALUATION_VAR_EVALUATION;

ALTER TABLE NET_PERFORMANCE DROP CONSTRAINT FK_NET_PERFORMANCE_USER;
ALTER TABLE NET_PERFORMANCE DROP CONSTRAINT FK_NET_PERFORMANCE_ACTION_SERIES;
ALTER TABLE NET_PERFORMANCE DROP CONSTRAINT FK_NET_PERFORMANCE_NETWORK;
ALTER TABLE NET_PERFORMANCE DROP CONSTRAINT FK_NET_PERFORMANCE_EVALUATION;
ALTER TABLE NET_PERFORMANCE DROP CONSTRAINT FK_NET_PERFORMANCE_STATS;
ALTER TABLE NET_PERFORMANCE DROP CONSTRAINT FK_NET_PERFORMANCE_STATS_SEQ;

ALTER TABLE NET_DERRIDA DROP CONSTRAINT FK_NET_DERRIDA_USER;
ALTER TABLE NET_DERRIDA DROP CONSTRAINT FK_NET_DERRIDA_NETWORK;
ALTER TABLE NET_DERRIDA DROP CONSTRAINT FK_NET_DERRIDA_STATS_SEQ;

ALTER TABLE NET_DAMAGE_SPREADING DROP CONSTRAINT FK_NET_DAMAGE_SPREADING_USER;
ALTER TABLE NET_DAMAGE_SPREADING DROP CONSTRAINT FK_NET_DAMAGE_SPREADING_NETWORK;
ALTER TABLE NET_DAMAGE_SPREADING DROP CONSTRAINT FK_NET_DAMAGE_SPREADING_STATS_SEQ;

-- AC

ALTER TABLE AC_ARTIFICIAL_CHEMISTRY DROP CONSTRAINT FK_AC_ARTIFICIAL_CHEMISTRY_SKIN_COMPARTMENT;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY DROP CONSTRAINT FK_AC_ARTIFICIAL_CHEMISTRY_USER;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY DROP CONSTRAINT FK_AC_ARTIFICIAL_CHEMISTRY_SPEC;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY DROP CONSTRAINT FK_AC_ARTIFICIAL_CHEMISTRY_CONFIG;

ALTER TABLE AC_SPECIES DROP CONSTRAINT FK_AC_SPECIES_SET;

ALTER TABLE AC_SPECIES_SET DROP CONSTRAINT FK_AC_SPECIES_SET_USER;
ALTER TABLE AC_SPECIES_SET DROP CONSTRAINT FK_AC_SPECIES_SET_PARENT;

ALTER TABLE AC_PARAMETER DROP CONSTRAINT FK_AC_PARAMETER_SET;
ALTER TABLE AC_PARAMETER DROP CONSTRAINT FK_AC_PARAMETER_FUN;

ALTER TABLE AC_PARAMETER_SET DROP CONSTRAINT FK_AC_PARAMETER_SET_USER;

ALTER TABLE AC_REACTION DROP CONSTRAINT FK_AC_REACTION_SET;
ALTER TABLE AC_REACTION DROP CONSTRAINT FK_AC_REACTION_FORWARD_RATE_FUN;
ALTER TABLE AC_REACTION DROP CONSTRAINT FK_AC_REACTION_REVERSE_RATE_FUN;
ALTER TABLE AC_REACTION DROP CONSTRAINT FK_AC_REACTION_GROUP;

ALTER TABLE AC_REACTION_GROUP DROP CONSTRAINT FK_AC_REACTION_GROUP_SET;

ALTER TABLE AC_SPECIES_REACTION_ASSOCIATION DROP CONSTRAINT FK_AC_SPECIES_REACTION_ASSOC_SPECIES;
ALTER TABLE AC_SPECIES_REACTION_ASSOCIATION DROP CONSTRAINT FK_AC_SPECIES_REACTION_ASSOC_REACTION;

ALTER TABLE AC_REACTION_SET DROP CONSTRAINT FK_AC_REACTION_SET_SPECIES_SET;
ALTER TABLE AC_REACTION_SET DROP CONSTRAINT FK_AC_REACTION_SET_USER;

ALTER TABLE AC_COMPARTMENT DROP CONSTRAINT FK_AC_COMPARTMENT_REACTION_SET;
ALTER TABLE AC_COMPARTMENT DROP CONSTRAINT FK_AC_COMPARTMENT_USER;

ALTER TABLE AC_SUB_COMPARTMENT_ASSOCIATION DROP CONSTRAINT FK_AC_SUB_COMPARTMENT_ASSOCIATION_PARENT;
ALTER TABLE AC_SUB_COMPARTMENT_ASSOCIATION DROP CONSTRAINT FK_AC_SUB_COMPARTMENT_ASSOCIATION_SUB;

ALTER TABLE AC_COMPARTMENT_CHANNEL DROP CONSTRAINT FK_AC_COMPARTMENT_CHANNEL_COMPARTMENT;
ALTER TABLE AC_COMPARTMENT_CHANNEL DROP CONSTRAINT FK_AC_COMPARTMENT_CHANNEL_SOURCE;
ALTER TABLE AC_COMPARTMENT_CHANNEL DROP CONSTRAINT FK_AC_COMPARTMENT_CHANNEL_TARGET;

ALTER TABLE AC_COMPARTMENT_CHANNEL_GROUP DROP CONSTRAINT FK_AC_COMPARTMENT_CHANNEL_GROUP_COMPARTMENT;
ALTER TABLE AC_COMPARTMENT_CHANNEL_GROUP_MAPPING DROP CONSTRAINT FK_AC_COMPARTMENT_CHANNEL_GROUP_MAPPING_GROUP;
ALTER TABLE AC_COMPARTMENT_CHANNEL_GROUP_MAPPING DROP CONSTRAINT FK_AC_COMPARTMENT_CHANNEL_GROUP_MAPPING_CHANNEL;

ALTER TABLE AC_SPECIES_ACTION DROP CONSTRAINT FK_AC_SPECIES_ACTION_ACTION;
ALTER TABLE AC_SPECIES_ACTION DROP CONSTRAINT FK_AC_SPECIES_ACTION_SPECIES;
ALTER TABLE AC_SPECIES_ACTION DROP CONSTRAINT FK_AC_SPECIES_ACTION_FUN;

ALTER TABLE AC_ACTION DROP CONSTRAINT FK_AC_ACTION_SERIES;

ALTER TABLE AC_ACTION_SERIES DROP CONSTRAINT FK_AC_ACTION_SERIES_SPECIES_SET;
ALTER TABLE AC_ACTION_SERIES DROP CONSTRAINT FK_AC_ACTION_SERIES_USER;
ALTER TABLE AC_ACTION_SERIES DROP CONSTRAINT FK_AC_ACTION_SERIES_PARENT;

ALTER TABLE AC_ACTION_SERIES_IMMUTABLE_SPECIES_MAPPING DROP CONSTRAINT FK_ACTION_SERIES_IMMUTABLE_SPECIES_MAPPING_ACTION_SERIES;
ALTER TABLE AC_ACTION_SERIES_IMMUTABLE_SPECIES_MAPPING DROP CONSTRAINT FK_ACTION_SERIES_IMMUTABLE_SPECIES_MAPPING_SPECIES;

ALTER TABLE AC_ACTION_VAR DROP CONSTRAINT FK_AC_ACTION_VAR_SERIES;

ALTER TABLE AC_CACHE_WRITE DROP CONSTRAINT FK_AC_CACHE_WRITE_ACTION;
ALTER TABLE AC_CACHE_WRITE DROP CONSTRAINT FK_AC_CACHE_WRITE_VAR;
ALTER TABLE AC_CACHE_WRITE DROP CONSTRAINT FK_AC_CACHE_WRITE_FUN;

ALTER TABLE AC_EVALUATED_SPECIES_ACTION DROP CONSTRAINT FK_AC_EVALUATED_SPECIES_ACTION_SPECIES_ACTION;
ALTER TABLE AC_EVALUATED_SPECIES_ACTION DROP CONSTRAINT FK_AC_EVALUATED_SPECIES_ACTION_EVAL_ACTION;
ALTER TABLE AC_EVALUATED_ACTION DROP CONSTRAINT FK_AC_EVALUATED_ACTION_ACTION;
ALTER TABLE AC_EVALUATED_ACTION DROP CONSTRAINT FK_AC_EVALUATED_ACTION_EVAL_SERIES;
ALTER TABLE AC_EVALUATED_ACTION_SERIES DROP CONSTRAINT FK_AC_EVALUATED_ACTION_SERIES_SERIES;
ALTER TABLE AC_EVALUATED_ACTION_SERIES DROP CONSTRAINT FK_AC_EVALUATED_ACTION_SERIES_USER;

ALTER TABLE AC_RUN DROP CONSTRAINT FK_AC_RUN_ACTION_SERIES;
ALTER TABLE AC_RUN DROP CONSTRAINT FK_AC_RUN_AC;
ALTER TABLE AC_RUN DROP CONSTRAINT FK_AC_RUN_USER;
ALTER TABLE AC_RUN DROP CONSTRAINT FK_AC_RUN_EVAL_ACTION_SERIES;

ALTER TABLE AC_SPECIES_HISTORY DROP CONSTRAINT FK_AC_SPECIES_HISTORY_RUN;
ALTER TABLE AC_SPECIES_HISTORY DROP CONSTRAINT FK_AC_SPECIES_HISTORY_SPECIES;

ALTER TABLE AC_TRANSLATION_ITEM DROP CONSTRAINT FK_AC_TRANSLATION_ITEM_TRANSLATION;
ALTER TABLE AC_TRANSLATION_ITEM DROP CONSTRAINT FK_AC_TRANSLATION_ITEM_FUN;
ALTER TABLE AC_TRANSLATION_ITEM DROP CONSTRAINT FK_AC_TRANSLATION_ITEM_VAR;

ALTER TABLE AC_RANGE_TRANSLATION DROP CONSTRAINT FK_AC_RANGE_TRANSLATION_SERIES;

ALTER TABLE AC_TRANSLATION_SERIES DROP CONSTRAINT FK_AC_TRANSLATION_SERIES_SPECIES_SET;
ALTER TABLE AC_TRANSLATION_SERIES DROP CONSTRAINT FK_AC_TRANSLATION_SERIES_USER;

ALTER TABLE AC_TRANSLATED_RUN DROP CONSTRAINT FK_AC_TRANSLATED_RUN_TRANSLATION_SERIES;
ALTER TABLE AC_TRANSLATED_RUN DROP CONSTRAINT FK_AC_TRANSLATED_RUN_RUN;
ALTER TABLE AC_TRANSLATED_RUN DROP CONSTRAINT FK_AC_TRANSLATED_RUN_USER;

ALTER TABLE AC_TRANSLATION_ITEM_HISTORY DROP CONSTRAINT FK_AC_TRANSLATION_ITEM_HISTORY_TRANSLATED_RUN;
ALTER TABLE AC_TRANSLATION_ITEM_HISTORY DROP CONSTRAINT FK_AC_TRANSLATION_ITEM_HISTORY_VAR;

ALTER TABLE AC_EVALUATION DROP CONSTRAINT FK_AC_EVALUATION_USER;
ALTER TABLE AC_EVALUATION DROP CONSTRAINT FK_AC_EVALUATION_TRANS_SERIES;
ALTER TABLE AC_EVALUATION DROP CONSTRAINT FK_AC_EVALUATION_FUN;

ALTER TABLE AC_EVALUATED_RUN DROP CONSTRAINT FK_AC_EVALUATED_RUN_EVALUATION;
ALTER TABLE AC_EVALUATED_RUN DROP CONSTRAINT FK_AC_EVALUATED_RUN_TRANSLATED_RUN;
ALTER TABLE AC_EVALUATED_RUN DROP CONSTRAINT FK_AC_EVALUATED_RUN_USER;

ALTER TABLE AC_TRANSLATION_VAR DROP CONSTRAINT FK_AC_TRANSLATION_VAR_SERIES;

ALTER TABLE AC_EVALUATED_PERFORMANCE DROP CONSTRAINT AC_EVALUATED_PERFORMANCE_USER;
ALTER TABLE AC_EVALUATED_PERFORMANCE DROP CONSTRAINT AC_EVALUATED_PERFORMANCE_EVALUATION;
ALTER TABLE AC_EVALUATED_PERFORMANCE DROP CONSTRAINT AC_EVALUATED_PERFORMANCE_COMPARTMENT;
ALTER TABLE AC_EVALUATED_PERFORMANCE DROP CONSTRAINT AC_EVALUATED_PERFORMANCE_SIM_CONFIG;
ALTER TABLE AC_EVALUATED_PERFORMANCE DROP CONSTRAINT AC_EVALUATED_PERFORMANCE_ACTION_SERIES;

ALTER TABLE AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING DROP CONSTRAINT FK_AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING_PERFORMANCE;
ALTER TABLE AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING DROP CONSTRAINT FK_AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING_RATE_CONST;


ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC DROP CONSTRAINT FK_ARTIFICIAL_CHEMISTRY_SPEC_RATE_CONSTANT_DIST;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC DROP CONSTRAINT FK_ARTIFICIAL_CHEMISTRY_SPEC_INFLUX_RATE_DIST;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC DROP CONSTRAINT FK_ARTIFICIAL_CHEMISTRY_SPEC_OUTFLUX_RATE_DIST;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC DROP CONSTRAINT FK_ARTIFICIAL_CHEMISTRY_SPEC_PARTIAL_BINDING_DIST;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC DROP CONSTRAINT FK_ARTIFICIAL_CHEMISTRY_SPEC_OUTFLUX_NON_REACTIVE_RATE_DIST;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC DROP CONSTRAINT AC_ARTIFICIAL_CHEMISTRY_SPEC_USER FOREIGN KEY;

ALTER TABLE AC_RATE_CONSTANT_TYPE_BOUND DROP CONSTRAINT AC_RATE_CONSTANT_TYPE_BOUND_USER;

ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC_BOUND DROP CONSTRAINT AC_ARTIFICIAL_CHEMISTRY_SPEC_BOUND_USER;

ALTER TABLE AC_SIMULATION_CONFIG DROP CONSTRAINT AC_SIMULATION_CONFIG_USER;


ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_SPEC_BOUND;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_SIM_CONFIG;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_ANALYSIS_SPEC;

-- UM

ALTER TABLE UM_USER_ROLE_MAPPING DROP CONSTRAINT FK_UM_USER_ROLE_MAPPING_USER;
ALTER TABLE UM_USER_ROLE_MAPPING DROP CONSTRAINT FK_UM_USER_ROLE_MAPPING_ROLE;

-- EVO

ALTER TABLE EVO_GA_SETTING DROP CONSTRAINT FK_EVO_GA_SETTING_USER;

ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_GA_SET;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_GA_AC;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_GA_EVALUATION;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_USER;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_NETWORK;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT FK_EVO_AC_TASK_NETWORK_EVALUATION;

ALTER TABLE EVO_AC_TASK_ACTION_SERIES_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_ACTION_SERIES_MAPPING_TASK;
ALTER TABLE EVO_AC_TASK_ACTION_SERIES_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_ACTION_SERIES_MAPPING_SERIES;
ALTER TABLE EVO_NET_TASK_ACTION_SERIES_MAPPING DROP CONSTRAINT FK_EVO_NET_TASK_ACTION_SERIES_MAPPING_TASK;
ALTER TABLE EVO_NET_TASK_ACTION_SERIES_MAPPING DROP CONSTRAINT FK_EVO_NET_TASK_ACTION_SERIES_MAPPING_SERIES;

ALTER TABLE EVO_RUN DROP CONSTRAINT FK_EVO_RUN_TASK;
ALTER TABLE EVO_RUN DROP CONSTRAINT FK_EVO_RUN_INIT_CHROM;
ALTER TABLE EVO_RUN DROP CONSTRAINT EVO_RUN_USER FOREIGN KEY;

ALTER TABLE EVO_POPULATION DROP CONSTRAINT FK_EVO_POPULATION_RUN;
ALTER TABLE EVO_POPULATION DROP CONSTRAINT FK_EVO_POPULATION_BEST_CHROMOSOME;
ALTER TABLE EVO_CHROMOSOME DROP CONSTRAINT FK_EVO_CHROMOSOME_POPULATION;

ALTER TABLE EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING_EVO_TASK;
ALTER TABLE EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING_RATE_CONST;

ALTER TABLE EVO_AC_TASK_REACTION_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_REACTION_MAPPING_EVO_TASK;
ALTER TABLE EVO_AC_TASK_REACTION_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_REACTION_MAPPING_REACTION;
ALTER TABLE EVO_AC_TASK_REACTION_GROUP_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_REACTION_GROUP_MAPPING_EVO_TASK;
ALTER TABLE EVO_AC_TASK_REACTION_GROUP_MAPPING DROP CONSTRAINT FK_EVO_AC_TASK_REACTION_GROUP_MAPPING_REACTION_GROUP;

ALTER TABLE EVO_AC_SPECIES_ASSIGNMENT_BOUND DROP CONSTRAINT FK_EVO_AC_SPECIES_ASSIGNMENT_BOUND_EVO_TASK;
ALTER TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_BOUND DROP CONSTRAINT FK_EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_BOUND_EVO_TASK;
ALTER TABLE EVO_AC_SPECIES_ASSIGNMENT_MAPPING DROP CONSTRAINT FK_EVO_AC_SPECIES_ASSIGNMENT_MAPPING_BOUND;
ALTER TABLE EVO_AC_SPECIES_ASSIGNMENT_MAPPING DROP CONSTRAINT FK_EVO_AC_SPECIES_ASSIGNMENT_MAPPING_ASSIGNMENT;
ALTER TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING DROP CONSTRAINT FK_EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING_BOUND;
ALTER TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING DROP CONSTRAINT FK_EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING_ASSIGNMENT;

-- DYN

ALTER TABLE DYN_STATS DROP CONSTRAINT FK_DYN_STATS_SEQ;

ALTER TABLE DYN_SINGLE_ANALYSIS_SPEC DROP CONSTRAINT DYN_SINGLE_ANALYSIS_SPEC_USER;
ALTER TABLE DYN_MULTI_ANALYSIS_SPEC DROP CONSTRAINT FK_DYN_MULTI_ANALYSIS_SPEC_SINGLE_SPEC;
ALTER TABLE DYN_MULTI_ANALYSIS_SPEC DROP CONSTRAINT FK_DYN_MULTI_ANALYSIS_SPEC_INIT_DIST;
ALTER TABLE DYN_MULTI_ANALYSIS_SPEC DROP CONSTRAINT DYN_MULTI_ANALYSIS_SPEC_USER;

ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_SPAT_COR;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_TIME_COR;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_SPAT_STAT_POINT;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_TIME_STAT_POINT;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_SPAT_CUM_DIFF;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_TIME_CUM_DIFF;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_SPAT_NONLINE_ERRORS;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_TIME_NONLINE_ERRORS;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_DERRIDA;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_SINGLE_ANALYSIS_RESULT_MULTI;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT DYN_SINGLE_ANALYSIS_RESULT_USER;

ALTER TABLE DYN_MULTI_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_MULTI_ANALYSIS_RESULT_SPEC;
ALTER TABLE DYN_MULTI_ANALYSIS_RESULT DROP CONSTRAINT FK_DYN_MULTI_ANALYSIS_RESULT_AC;
ALTER TABLE DYN_MULTI_ANALYSIS_RESULT DROP CONSTRAINT DYN_MULTI_ANALYSIS_RESULT_USER;


-- TASK

ALTER TABLE TASK_EVO_RUN DROP CONSTRAINT FK_TASK_EVO_RUN_SUPER;
ALTER TABLE TASK_EVO_RUN DROP CONSTRAINT FK_TASK_EVO_RUN_EVO_TASK;
ALTER TABLE TASK DROP CONSTRAINT TASK_USER FOREIGN KEY;

--------------
-- UNIQUE KEYS
--------------

-- NET



-- AC

ALTER TABLE AC_SPECIES DROP CONSTRAINT UK_AC_SPECIES;
ALTER TABLE AC_SPECIES DROP CONSTRAINT UK_AC_SPECIES_2;

ALTER TABLE AC_PARAMETER DROP CONSTRAINT UK_AC_PARAMETER;
ALTER TABLE AC_PARAMETER DROP CONSTRAINT UK_AC_PARAMETER_2;

ALTER TABLE AC_REACTION DROP CONSTRAINT UK_AC_REACTION;

--ALTER TABLE AC_SPECIES_REACTION_ASSOCIATION DROP CONSTRAINT UK_AC_SPECIES_REACTION_ASSOCIATION;

ALTER TABLE AC_SPECIES_ACTION DROP CONSTRAINT UK_AC_SPECIES_ACTION;
ALTER TABLE AC_ACTION DROP CONSTRAINT UK_AC_ACTION;

ALTER TABLE AC_EVALUATED_SPECIES_ACTION DROP CONSTRAINT UK_AC_EVALUATED_SPECIES_ACTION;
ALTER TABLE AC_SPECIES_HISTORY DROP CONSTRAINT UK_AC_SPECIES_HISTORY;

ALTER TABLE AC_TRANSLATION_ITEM DROP CONSTRAINT UK_AC_TRANSLATION_ITEM;

ALTER TABLE AC_TRANSLATION_ITEM_HISTORY DROP CONSTRAINT UK_AC_TRANSLATION_ITEM_HISTORY;

ALTER TABLE AC_EVALUATION_ITEM DROP CONSTRAINT UK_AC_EVALUATION_ITEM;

ALTER TABLE AC_TRANSLATION_VAR DROP CONSTRAINT UK_AC_TRANSLATION_VAR;
ALTER TABLE AC_TRANSLATION_VAR DROP CONSTRAINT UK_AC_TRANSLATION_VAR_2;

ALTER TABLE AC_ACTION_VAR DROP CONSTRAINT UK_AC_ACTION_VAR;
ALTER TABLE AC_ACTION_VAR DROP CONSTRAINT UK_AC_ACTION_VAR_2;

-- UM

ALTER TABLE UM_USER DROP CONSTRAINT UK_UM_USER_1;
ALTER TABLE UM_USER DROP CONSTRAINT UK_UM_USER_2;
ALTER TABLE UM_ROLE DROP CONSTRAINT UK_UM_ROLE_1;
ALTER TABLE UM_USER_ROLE_MAPPING DROP CONSTRAINT UK_UM_USER_ROLE_MAPPING;

-- EVO

----------------
-- PRIMARY KEYS
----------------

-- FUN

ALTER TABLE FUN_FUNCTION DROP CONSTRAINT PK_FUN_FUNCTION;
ALTER TABLE FUN_TRANSITION_TABLE DROP CONSTRAINT PK_FUN_TRANSITION_TABLE;
ALTER TABLE FUN_EXPRESSION DROP CONSTRAINT PK_FUN_EXPRESSION;
ALTER TABLE FUN_RANDOM_DISTRIBUTION DROP CONSTRAINT PK_FUN_RANDOM_DISTRIBUTION;

-- NET

ALTER TABLE NET_TOPOLOGY DROP CONSTRAINT PK_NET_TOPOLOGY;
ALTER TABLE NET_TEMPLATE_TOPOLOGY DROP CONSTRAINT PK_NET_TEMPLATE_TOPOLOGY;
ALTER TABLE NET_SPATIAL_TOPOLOGY DROP CONSTRAINT PK_NET_SPATIAL_TOPOLOGY;
ALTER TABLE NET_SPATIAL_NEIGHBORHOOD DROP CONSTRAINT PK_NET_SPATIAL_NEIGHBORHOOD;
ALTER TABLE NET_SPATIAL_NEIGHBOR DROP CONSTRAINT PK_NET_SPATIAL_NEIGHBOR;
ALTER TABLE NET_WEIGHT_SETTING DROP CONSTRAINT PK_NET_WEIGHT_SETTING;
ALTER TABLE NET_FUNCTION DROP CONSTRAINT PK_NET_FUNCTION;
ALTER TABLE NET_NETWORK DROP CONSTRAINT PK_NET_NETWORK;
ALTER TABLE NET_ACTION_SERIES DROP CONSTRAINT PK_NET_ACTION_SERIES;
ALTER TABLE NET_ACTION DROP CONSTRAINT PK_NET_ACTION;
ALTER TABLE NET_EVALUATION DROP CONSTRAINT PK_NET_EVALUATION;
ALTER TABLE NET_PERFORMANCE DROP CONSTRAINT PK_NET_PERFORMANCE;
ALTER TABLE NET_DERRIDA DROP CONSTRAINT PK_NET_DERRIDA;
ALTER TABLE NET_DAMAGE_SPREADING DROP CONSTRAINT PK_NET_DAMAGE_SPREADING;

-- AC 

ALTER TABLE AC_ARTIFICIAL_CHEMISTRY DROP CONSTRAINT PK_AC_ARTIFICIAL_CHEMISTRY;
ALTER TABLE AC_SPECIES DROP CONSTRAINT PK_AC_SPECIES;

ALTER TABLE AC_SPECIES_SET DROP CONSTRAINT PK_AC_SPECIES_SET;
ALTER TABLE AC_PARAMETER DROP CONSTRAINT PK_AC_PARAMETER;

ALTER TABLE AC_PARAMETER_SET DROP CONSTRAINT PK_AC_PARAMETER_SET;
ALTER TABLE AC_REACTION DROP CONSTRAINT PK_AC_REACTION;
ALTER TABLE AC_SPECIES_REACTION_ASSOCIATION DROP CONSTRAINT PK_AC_SPECIES_REACTION_ASSOCIATION;
ALTER TABLE AC_REACTION_SET DROP CONSTRAINT PK_AC_REACTION_SET;
ALTER TABLE AC_REACTION_GROUP DROP CONSTRAINT PK_AC_REACTION_GROUP;
ALTER TABLE AC_COMPARTMENT DROP CONSTRAINT PK_AC_COMPARTMENT;
ALTER TABLE AC_SUB_COMPARTMENT_ASSOCIATION DROP CONSTRAINT PK_AC_SUB_COMPARTMENT_ASSOCIATION;
ALTER TABLE AC_COMPARTMENT_CHANNEL DROP CONSTRAINT PK_AC_COMPARTMENT_CHANNEL;
ALTER TABLE AC_COMPARTMENT_CHANNEL_GROUP DROP CONSTRAINT PK_AC_COMPARTMENT_CHANNEL_GROUP;

ALTER TABLE AC_SPECIES_ACTION DROP CONSTRAINT PK_AC_SPECIES_ACTION;
ALTER TABLE AC_ACTION DROP CONSTRAINT PK_AC_ACTION;
ALTER TABLE AC_ACTION_SERIES DROP CONSTRAINT PK_AC_ACTION_SERIES;
ALTER TABLE AC_RUN DROP CONSTRAINT PK_AC_RUN;
ALTER TABLE AC_SPECIES_HISTORY DROP CONSTRAINT PK_AC_SPECIES_HISTORY;

ALTER TABLE AC_ACTION_VAR DROP CONSTRAINT PK_AC_ACTION_VAR;
ALTER TABLE AC_CACHE_WRITE DROP CONSTRAINT PK_AC_CACHE_WRITE;;

ALTER TABLE AC_EVALUATED_SPECIES_ACTION DROP CONSTRAINT PK_AC_EVALUATED_SPECIES_ACTION;
ALTER TABLE AC_EVALUATED_ACTION DROP CONSTRAINT PK_AC_EVALUATED_ACTION;
ALTER TABLE AC_EVALUATED_ACTION_SERIES DROP CONSTRAINT PK_AC_EVALUATED_ACTION_SERIES;

ALTER TABLE AC_TRANSLATION_ITEM DROP CONSTRAINT PK_AC_TRANSLATION_ITEM;
ALTER TABLE AC_RANGE_TRANSLATION DROP CONSTRAINT PK_AC_RANGE_TRANSLATION;
ALTER TABLE AC_TRANSLATION_SERIES DROP CONSTRAINT PK_AC_TRANSLATION_SERIES;
ALTER TABLE AC_TRANSLATED_RUN DROP CONSTRAINT PK_AC_TRANSLATED_RUN;
ALTER TABLE AC_TRANSLATION_ITEM_HISTORY DROP CONSTRAINT PK_AC_TRANSLATION_ITEM_HISTORY;
ALTER TABLE AC_TRANSLATION_VAR DROP CONSTRAINT PK_AC_TRANSLATION_VAR;

ALTER TABLE AC_EVALUATED_RUN DROP CONSTRAINT PK_AC_EVALUATED_RUN;
ALTER TABLE AC_EVALUATION DROP CONSTRAINT PK_AC_EVALUATION;

ALTER TABLE AC_RATE_CONSTANT_TYPE_BOUND DROP CONSTRAINT PK_AC_RATE_CONSTANT_TYPE_BOUND;
ALTER TABLE AC_EVALUATED_PERFORMANCE DROP CONSTRAINT PK_AC_EVALUATED_PERFORMANCE;
ALTER TABLE AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING DROP CONSTRAINT PK_AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC DROP CONSTRAINT PK_AC_ARTIFICIAL_CHEMISTRY_SPEC;

ALTER TABLE AC_SIMULATION_CONFIG DROP CONSTRAINT PK_AC_SIMULATION_CONFIG;
ALTER TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC_BOUND DROP CONSTRAINT PK_AC_ARTIFICIAL_CHEMISTRY_SPEC_BOUND;

-- UM

ALTER TABLE UM_USER DROP CONSTRAINT PK_UM_USER;
ALTER TABLE UM_ROLE DROP CONSTRAINT PK_UM_ROLE;
ALTER TABLE UM_USER_ROLE_MAPPING DROP CONSTRAINT PK_UM_USER_ROLE_MAPPING;

-- EVO

ALTER TABLE EVO_GA_SETTING DROP CONSTRAINT PK_EVO_GA_SETTING;
ALTER TABLE EVO_AC_TASK DROP CONSTRAINT PK_EVO_AC_TASK;
ALTER TABLE EVO_AC_TASK_ACTION_SERIES_MAPPING DROP CONSTRAINT PK_EVO_AC_TASK_ACTION_SERIES_MAPPING;

ALTER TABLE EVO_RUN DROP CONSTRAINT PK_EVO_RUN;
ALTER TABLE EVO_POPULATION DROP CONSTRAINT PK_EVO_POPULATION;
ALTER TABLE EVO_CHROMOSOME DROP CONSTRAINT PK_EVO_CHROMOSOME;

ALTER TABLE EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING DROP CONSTRAINT PK_EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING;
ALTER TABLE EVO_AC_TASK_REACTION_MAPPING DROP CONSTRAINT PK_EVO_AC_TASK_REACTION_MAPPING;
ALTER TABLE EVO_AC_TASK_REACTION_GROUP_MAPPING DROP CONSTRAINT PK_EVO_AC_TASK_REACTION_GROUP_MAPPING;

ALTER TABLE EVO_AC_SPECIES_ASSIGNMENT_BOUND DROP CONSTRAINT PK_EVO_AC_SPECIES_ASSIGNMENT_BOUND;
ALTER TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_BOUND DROP CONSTRAINT PK_EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_BOUND;
ALTER TABLE EVO_AC_SPECIES_ASSIGNMENT_MAPPING DROP CONSTRAINT PK_EVO_AC_SPECIES_ASSIGNMENT_MAPPING;
ALTER TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING DROP CONSTRAINT PK_EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING;

-- DYN

ALTER TABLE DYN_STATS DROP CONSTRAINT PK_DYN_STATS;
ALTER TABLE DYN_STATS_SEQ DROP CONSTRAINT PK_DYN_STATS_SEQ; 
ALTER TABLE DYN_SINGLE_ANALYSIS_SPEC DROP CONSTRAINT PK_DYN_SINGLE_ANALYSIS_SPEC;
ALTER TABLE DYN_MULTI_ANALYSIS_SPEC DROP CONSTRAINT PK_DYN_MULTI_ANALYSIS_SPEC;
ALTER TABLE DYN_SINGLE_ANALYSIS_RESULT DROP CONSTRAINT PK_DYN_SINGLE_ANALYSIS_RESULT;
ALTER TABLE DYN_MULTI_ANALYSIS_RESULT DROP CONSTRAINT PK_DYN_MULTI_ANALYSIS_RESULT;

-- TASK

ALTER TABLE TASK DROP CONSTRAINT PK_TASK;
ALTER TABLE TASK_EVO_RUN DROP CONSTRAINT PK_EVO_RUN_DEF;
