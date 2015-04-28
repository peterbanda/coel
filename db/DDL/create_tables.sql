-- FUNCTION

CREATE TABLE FUN_FUNCTION (	
	id int8 NOT NULL,
	arity int4,
	fun_type int4 NOT NULL,
	input_clazz_name varchar(40),
	output_clazz_name varchar(40),
	parent_id int8
);

CREATE TABLE FUN_TRANSITION_TABLE (
	id int8 NOT NULL,
	outputs varchar[] NOT NULL
);

CREATE TABLE FUN_EXPRESSION (
	id int8 NOT NULL,
	content varchar(256) NOT NULL
);

CREATE TABLE FUN_RANDOM_DISTRIBUTION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	"from" float8,
	"to" float8,
	location float8,
	shape float8,
	type int2,
	value_type_name varchar(100),
	probabilities float8[],
    	values_as_string varchar[]
);

-- NETWORK

CREATE TABLE NET_TOPOLOGY (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(60) NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8,
        type int4 NOT NULL
);


CREATE TABLE NET_LAYER_TOPOLOGY_MAPPING (
	parent_id int8 NOT NULL,
	layer_id int8 NOT NULL,
	pos int4 NOT NULL
);

CREATE TABLE NET_TEMPLATE_TOPOLOGY (
	topology_id int8 NOT NULL,
	nodes_num int4,
	layers_num int4,
	nodes_per_layer int4,
	generate_bias bool NOT NULL,
	all_edges bool NOT NULL,
	inner_layer_all_edges bool NOT NULL,
	in_edges_num int4,
	inner_layer_in_edges_num int4 DEFAULT NULL,
	allow_self_edges bool NOT NULL,
	allow_multi_edges bool NOT NULL
);

CREATE TABLE NET_SPATIAL_TOPOLOGY (
	topology_id int8 NOT NULL,
    	sizes int4[] NOT NULL,
	torus_flag bool NOT NULL,
	metrics_type int2,
	radius int4,
	its_own_neighor bool NOT NULL,
	neighborhood_id int8
);

CREATE TABLE NET_SPATIAL_NEIGHBORHOOD (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(60) NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8
);

CREATE TABLE NET_SPATIAL_NEIGHBOR (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
        index_ int4 NOT NULL,
	coordinate_diffs int4[] NOT NULL,
        parent_id int8 NOT NULL
);

CREATE TABLE NET_WEIGHT_SETTING (	
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(50) NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8,
        type int4 NOT NULL,
	index_ int4 NOT NULL,
	parent_id int8,
	weights float8[],
        setting_order int2,
	random_distribution_id int8
);

CREATE TABLE NET_FUNCTION (	
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(50) NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8,
	index_ int4,
	parent_id int8,
	function_id int8,
	states_weights_integrator_type int2,
	multi_component_updater_type int2 NOT NULL
);

CREATE TABLE NET_NETWORK (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(60) NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8,
	topology_id int8 NOT NULL,
        function_id int8 NOT NULL,
	weight_setting_id int8
);

CREATE TABLE NET_ACTION_SERIES (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(200) NOT NULL,
	repetitions int4,
	periodicity int4,
	repeat_from int4,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL
);

CREATE TABLE NET_ACTION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	start_time int4 NOT NULL,
	time_length float8 NOT NULL,
	alternation_type int2 NOT NULL,
	action_series_id int8 NOT NULL,
	state_distribution_id int8
);

CREATE TABLE NET_EVALUATION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(100) NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	evaluation_fun_id int8 NOT NULL,
	var_sequence_num int4 NOT NULL
);

CREATE TABLE NET_EVALUATION_ITEM ( 
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	evaluation_id int8 NOT NULL,
	evaluation_var_id int8 NOT NULL,
	eval_fun_id int8 NOT NULL
);

CREATE TABLE NET_EVALUATION_VAR (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	variable_index int4 NOT NULL,
	label varchar(30) NOT NULL,
	evaluation_id int8 NOT NULL
);

CREATE TABLE NET_PERFORMANCE (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	repetitions int4 NOT NULL,
	run_time int4 NOT NULL,
	action_series_id int8 NOT NULL,
	network_id int8 NOT NULL,
	evaluation_id int8 NOT NULL,
	result_stats_id int8,
	size_from int4,
	size_to int4,
	result_stats_seq_id int8
);

CREATE TABLE NET_DERRIDA (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	repetitions int4 NOT NULL,
	run_time int4 NOT NULL,
	network_id int8 NOT NULL,
	result_stats_seq_id int8 NOT NULL
);

-- ARTIFICIAL CHEMISTRY

CREATE TABLE AC_ARTIFICIAL_CHEMISTRY (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(100) NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8,
	skin_compartment_id int8 NOT NULL,
	generated_by_spec_id int8,
	simulation_config_id int8 NOT NULL
);

CREATE TABLE AC_SPECIES (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	variable_index int4 NOT NULL,
	label varchar(30) NOT NULL,
	sort_order int4 NOT NULL,
	structure varchar(30),
	species_set_id int8 NOT NULL
);

CREATE TABLE AC_SPECIES_SET (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(50) NOT NULL,
	sequence_num int4 NOT NULL,
	parent_set int8,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_PARAMETER (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	variable_index int4 NOT NULL,
	label varchar(30) NOT NULL,
	sort_order int4 NOT NULL,
	evol_function_id int8 NOT NULL,
	parameter_set_id int8 NOT NULL
);

CREATE TABLE AC_PARAMETER_SET (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(50) NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_REACTION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	label varchar(30) NOT NULL,
	priority int4,
	sort_order int4 NOT NULL,
	reaction_set_id int8 NOT NULL,
	reaction_group_id int8, 
	forward_rate_function_id int8,
	forward_rate_constants float8[],
	reverse_rate_function_id int8,
	reverse_rate_constants float8[],
	col_catalysis_type int2,
	col_inhibition_type int2,	
	enabled bool NOT NULL
);

CREATE TABLE AC_SPECIES_REACTION_ASSOCIATION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	species_id int8 NOT NULL,
	stoichiometric_factor float8,
	reaction_id int8 NOT NULL,
	assoc_type int2 NOT NULL    -- 0 - reactant, 1 - product, 2 - catalyst, 3 - inhibitor
);

CREATE TABLE AC_REACTION_SET (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	label varchar(100) NOT NULL,
	species_set_id int8 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_REACTION_GROUP (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	label varchar(30) NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8 NOT NULL,
	reaction_set_id int8 NOT NULL
);

CREATE TABLE AC_COMPARTMENT (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	label varchar(100) NOT NULL,
	reaction_set_id int8 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_SUB_COMPARTMENT_ASSOCIATION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	parent_id int8 NOT NULL,
	sub_id int8 NOT NULL,
	pos int4 NOT NULL
);

CREATE TABLE AC_COMPARTMENT_CHANNEL (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	compartment_id int8 NOT NULL,
	source_species_id int8 NOT NULL,
	target_species_id int8 NOT NULL,
	direction int2 NOT NULL,
	permeability float8 NOT NULL
);

CREATE TABLE AC_COMPARTMENT_CHANNEL_GROUP (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	compartment_id int8 NOT NULL
);

CREATE TABLE AC_COMPARTMENT_CHANNEL_GROUP_MAPPING (
	group_id int8 NOT NULL,
	channel_id int8 NOT NULL
);

CREATE TABLE AC_SPECIES_ACTION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	action_id int8 NOT NULL,
	species_id int8 NOT NULL,
	setting_fun_id int8 NOT NULL
);

CREATE TABLE AC_ACTION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	start_time int4 NOT NULL,
	time_length float8 NOT NULL,
	alternation_type int2 NOT NULL,
	action_series_id int8 NOT NULL
);

CREATE TABLE AC_ACTION_SERIES (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(200) NOT NULL,
	species_set_id int8 NOT NULL,
	parent_id int8,
	repetitions int4,
	periodicity int4,
	repeat_from int4,
	var_sequence_num int4 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_ACTION_VAR (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	variable_index int4 NOT NULL,
	label varchar(30) NOT NULL,
	action_series_id int8 NOT NULL
);

CREATE TABLE AC_ACTION_SERIES_IMMUTABLE_SPECIES_MAPPING (
	action_series_id int8 NOT NULL,
	species_id int8 NOT NULL
);

CREATE TABLE AC_CACHE_WRITE (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	action_id int8 NOT NULL,
	variable_id int8 NOT NULL,
	setting_fun_id int8 NOT NULL
);

CREATE TABLE AC_EVALUATED_SPECIES_ACTION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	value float8 NOT NULL,
	species_action_id int8 NOT NULL,
	evaluated_action_id int8 NOT NULL
);

CREATE TABLE AC_EVALUATED_ACTION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	apply_time int4 NOT NULL,
	action_id int8 NOT NULL,
	evaluated_action_series_id int8 NOT NULL
);

CREATE TABLE AC_EVALUATED_ACTION_SERIES (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	action_series_id int8 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_RUN (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	steps int4 NOT NULL,
	run_time float8 NOT NULL,
	action_series_id int8 NOT NULL,
	ac_id int8 NOT NULL,
	evaluated_action_series_id int8,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_SPECIES_HISTORY (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	configuration_vector float8[] NOT NULL,
	species_id int8 NOT NULL,
	run_id int8 NOT NULL
);

CREATE TABLE AC_TRANSLATION_ITEM ( 
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	translation_id int8 NOT NULL,
	translation_var_id int8 NOT NULL,
	translation_fun_id int8 NOT NULL
);

-- TODO - rename to AC_TRANSLATION
CREATE TABLE AC_RANGE_TRANSLATION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	from_time int4 NOT NULL,
	to_time int4,
	translation_series_id int8 NOT NULL
);

-- TODO - merge with AC_ACTION_SERIES
CREATE TABLE AC_TRANSLATION_SERIES (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(50) NOT NULL,
	species_set_id int8 NOT NULL,
	repetitions int4,
	periodicity int4,
	repeat_from int4,
	var_sequence_num int4 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_TRANSLATED_RUN (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	steps int4 NOT NULL,
	translation_series_id int8 NOT NULL,
	run_id int8 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8
);

CREATE TABLE AC_TRANSLATION_ITEM_HISTORY (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,	
	translation_var_id int8 NOT NULL,
	sequence float8[] NOT NULL,
	translated_run_id int8 NOT NULL
);

CREATE TABLE AC_TRANSLATION_VAR (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	variable_index int4 NOT NULL,
	label varchar(30) NOT NULL,
	translation_series_id int8 NOT NULL
);

CREATE TABLE AC_EVALUATION (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(50) NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8 NOT NULL,
	translation_series_id int8 NOT NULL,
	periodic_translations_number int4 NOT NULL,
	evaluation_fun_id int8 NOT NULL
);

CREATE TABLE AC_EVALUATED_RUN (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	steps int4 NOT NULL,	
	evaluation_id int8 NOT NULL,
	evaluation_states int2[] NOT NULL,
	translated_run_id int8 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8 NOT NULL
);

CREATE TABLE AC_EVALUATED_PERFORMANCE (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	repetitions int4 NOT NULL,
	random_rate_generation_num int4,
	perturbation_num int4,           
	perturbation_strength float8,
	action_series_id int8 NOT NULL,
	compartment_id int8 NOT NULL,        -- ADDED
	simulation_config_id int8 NOT NULL,  -- ADDED
	evaluation_id int8 NOT NULL,
	averaged_correct_rates float8[] NOT NULL
	length int4 NOT NULL
);

CREATE TABLE AC_PERFORMANCE_RATE_CONSTANT_TYPE_BOUND_MAPPING (
	random_rate_performance_id int8 NOT NULL,
	rate_constant_type_bound_id int8 NOT NULL
);

CREATE TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	name varchar(100) NOT NULL,
	rate_constant_distribution_id int8 NOT NULL,
	species_forbidden_redundancy int2 NOT NULL,
	influx_ratio float8,                           
	influx_rate_constant_distribution_id int8, 
	outflux_ratio float8,                          
	outflux_all bool NOT NULL,                          
	outflux_rate_constant_distribution_id int8,
	outflux_non_reactive_rate_distribution_id int8,
	constant_species_ratio float8,
	species_num int4,
	reaction_num int4,
	reactants_per_reaction_number int4,        
	products_per_reaction_number int4,         
	catalysts_per_reaction_number int4,        
	inhibitors_per_reaction_number int4,
	single_strands_num int4,
	upper_to_lower_strand_ratio float8,
	complementary_strands_ratio float8,
	upper_strand_partial_binding_distribution_id int8,
	mirror_complementarity bool,
	use_global_order bool
);

CREATE TABLE AC_SIMULATION_CONFIG (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	name varchar(100) NOT NULL,
	ode_solver_type int2 NOT NULL,
	upper_threshold float8,
	lower_threshold float8,
	fixed_point_detection_precision float8,
	fixed_point_detection_periodicity float8,
	tolerance float8,
	time_step float8 NOT NULL,
	influx_scale float8
);

CREATE TABLE AC_RATE_CONSTANT_TYPE_BOUND (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	created_by int8 NOT NULL,
	rate_constant_type int2 NOT NULL,
	lower_bound float8 NOT NULL,
	upper_bound float8 NOT NULL
);

CREATE TABLE AC_ARTIFICIAL_CHEMISTRY_SPEC_BOUND (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	name varchar(100) NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	influx_ratio_lower_bound float8 NOT NULL,
	influx_ratio_upper_bound float8 NOT NULL,
	outflux_ratio_lower_bound float8 NOT NULL,
	outflux_ratio_upper_bound float8 NOT NULL,
	constant_species_ratio_lower_bound float8 NOT NULL,
	constant_species_ratio_upper_bound float8 NOT NULL,
	rate_constant_distribution_shape_lower_bound float8 NOT NULL,
	rate_constant_distribution_shape_upper_bound float8 NOT NULL,
	rate_constant_distribution_location_lower_bound float8 NOT NULL,
	rate_constant_distribution_location_upper_bound float8 NOT NULL,
	influx_rate_constant_distribution_shape_lower_bound float8 NOT NULL,
	influx_rate_constant_distribution_shape_upper_bound float8 NOT NULL,
	influx_rate_constant_distribution_location_lower_bound float8 NOT NULL,
	influx_rate_constant_distribution_location_upper_bound float8 NOT NULL,
	outflux_rate_constant_distribution_shape_lower_bound float8 NOT NULL,
	outflux_rate_constant_distribution_shape_upper_bound float8 NOT NULL,
	outflux_rate_constant_distribution_location_lower_bound float8 NOT NULL,
	outflux_rate_constant_distribution_location_upper_bound float8 NOT NULL,
	outflux_non_reactive_rate_distribution_shape_lower_bound float8,
	outflux_non_reactive_rate_distribution_shape_upper_bound float8,
	outflux_non_reactive_rate_distribution_location_lower_bound float8,
	outflux_non_reactive_rate_distribution_location_upper_bound float8,
	species_num_lower_bound int4,
	species_num_upper_bound int4,
	reaction_num_lower_bound int4,
	reaction_num_upper_bound int4,
	reactants_per_reaction_number_lower_bound int4,
	reactants_per_reaction_number_upper_bound int4,
	products_per_reaction_number_lower_bound int4,
	products_per_reaction_number_upper_bound int4,
	catalysts_per_reaction_number_lower_bound int4,
	catalysts_per_reaction_number_upper_bound int4,
	inhibitors_per_reaction_number_lower_bound int4,
	inhibitors_per_reaction_number_upper_bound int4,
	single_strands_num_lower_bound int4,
	single_strands_num_upper_bound int4,
	upper_to_lower_strand_ratio_lower_bound float8,
	upper_to_lower_strand_ratio_upper_bound float8,
	complementary_strands_ratio_lower_bound float8,
	complementary_strands_ratio_upper_bound float8,
	upper_strand_partial_binding_distribution_shape_lower_bound float8,
	upper_strand_partial_binding_distribution_shape_upper_bound float8,
	upper_strand_partial_binding_distribution_location_lower_bound float8,
	upper_strand_partial_binding_distribution_location_upper_bound float8,
	use_global_order bool
);

-- USER MANAGEMENT

CREATE TABLE UM_USER (	
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	username varchar(20) NOT NULL,
	password varchar(100) NOT NULL,
	password_hint varchar(50),
	firstName varchar(20) NOT NULL,
	lastName varchar(30) NOT NULL,
        affiliation VARCHAR(80) NOT NULL,
        intended_use VARCHAR(1000) NOT NULL,
    email varchar(40) NOT NULL,
    phone_number varchar(25),
    website varchar(50),
    address varchar(150),
    enabled bool NOT NULL,
    account_expired bool NOT NULL,
    account_locked bool NOT NULL,
    credentials_expired bool NOT NULL,
    about_me varchar(500),
    create_time timestamp NOT NULL,
    change_time timestamp
);

CREATE TABLE UM_ROLE (
    id int8 NOT NULL,
    ol_version int8 NOT NULL,
    name varchar(20) NOT NULL,
    description varchar(100)
);

CREATE TABLE UM_USER_ROLE_MAPPING (
	id int8 NOT NULL,
	user_id int8 NOT NULL,
	role_id int8 NOT NULL
);

-- EVO

CREATE TABLE EVO_GA_SETTING (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8 NOT NULL,
	name varchar(200) NOT NULL,
	elite_number int4,
	population_size int4 NOT NULL,
	cond_mutation bool NOT NULL,
	cond_crossover bool NOT NULL,
	crossover_prob float8 NOT NULL,
	crossover_type int2 NOT NULL,
	mutation_prob float8 NOT NULL,
	mutation_per_bit_prob float8,
	mutation_type int2 NOT NULL,
	bit_mutation_type int2 NOT NULL,
	pertrub_mutation_strength float8, 
	selection_type int2 NOT NULL,
	fitness_renorm_type int2,
	generation_limit int4 NOT NULL,
	max_value_flag bool NOT NULL
);

-- TODO - rename to EVO_TASK
CREATE TABLE EVO_AC_TASK (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	create_time timestamp NOT NULL,
	created_by int8 NOT NULL,
	name varchar(200) NOT NULL,
	task_type int2 NOT NULL,
	ga_setting_id int8 NOT NULL,
	as_repetitions int4,
	ac_id int8,
	ac_evaluation_id int8,
	run_steps int4,
	last_eval_steps_to_count int4,
	spec_id int8,
	spec_bound_id int8,
	sim_config_id int8,
	multi_run_analysis_spec_id int8,
	network_id int8,
	network_evaluation_id int8,
	fixed_point_detection_periodicity float8
);

-- ADDED
CREATE TABLE EVO_AC_SPECIES_ASSIGNMENT_BOUND (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	evo_task_id int8 NOT NULL,
	lower_bound float8 NOT NULL,
	upper_bound float8 NOT NULL
);

-- ADDED
CREATE TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_BOUND (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	evo_task_id int8 NOT NULL,
	lower_bound float8 NOT NULL,
	upper_bound float8 NOT NULL
);

CREATE TABLE EVO_AC_TASK_RATE_CONSTANT_TYPE_BOUND_MAPPING (
    evo_ac_task_id int8 NOT NULL,
    rate_constant_type_bound_id int8 NOT NULL
);

-- ADDED
CREATE TABLE EVO_AC_SPECIES_ASSIGNMENT_MAPPING (
    evo_ac_species_bound_id int8 NOT NULL,
    species_assignment_id int8 NOT NULL    
);

-- ADDED
CREATE TABLE EVO_AC_INTERACTION_VARIABLE_ASSIGNMENT_MAPPING (
    evo_ac_variable_bound_id int8 NOT NULL,
    variable_assignment_id int8 NOT NULL    
);

CREATE TABLE EVO_AC_TASK_ACTION_SERIES_MAPPING (
    id int8 NOT NULL,
    evo_ac_task_id int8 NOT NULL,
    action_series_id int8 NOT NULL
);

CREATE TABLE EVO_NET_TASK_ACTION_SERIES_MAPPING (
    evo_task_id int8 NOT NULL,
    action_series_id int8 NOT NULL
);

CREATE TABLE EVO_AC_TASK_REACTION_MAPPING (
    evo_ac_task_id int8 NOT NULL,
    reaction_id int8 NOT NULL
);

CREATE TABLE EVO_AC_TASK_REACTION_GROUP_MAPPING (
    evo_ac_task_id int8 NOT NULL,
    reaction_group_id int8 NOT NULL
);

CREATE TABLE EVO_RUN (
	id int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	evo_task_id int8 NOT NULL,
	init_chromosome_id int8
);

CREATE TABLE EVO_POPULATION (
	id int8 NOT NULL,
	time_created timestamp NOT NULL,
	generation integer NOT NULL,
	min_score float8,
	mean_score float8,
	max_score float8,
	min_fitness float8,
	mean_fitness float8,
	max_fitness float8,
	best_chromosome_id int8,
	evo_run_id int8 NOT NULL
);

CREATE TABLE EVO_CHROMOSOME (
	id int8 NOT NULL,
	score float8 NOT NULL,
	fitness float8,
	population_id int8,
	array_code varchar[] NOT NULL,
	type_name varchar(100) NOT NULL
);

-- DYN

CREATE TABLE DYN_STATS (
	id int8 NOT NULL,
	index float8 NOT NULL,
	mean float8 NOT NULL,
	standard_deviation float8 NOT NULL,
	min float8 NOT NULL,
	max float8 NOT NULL,
	seq_id int8
);

CREATE TABLE DYN_STATS_SEQ (
	id int8 NOT NULL,
	time_created timestamp NOT NULL
);

CREATE TABLE DYN_SINGLE_ANALYSIS_SPEC (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	name varchar(200) NOT NULL,
	time_step_length float8 NOT NULL,
	iterations int4 NOT NULL,
	lyapunov_perturbation_strength float8 NOT NULL,
	derrida_perturbation_strength float8 NOT NULL,
	derrida_time_length float8 NOT NULL,
	time_step_to_filter int4 NOT NULL,
	fixed_point_detection_precision float8 NOT NULL,
	derrida_resolution float8 NOT NULL
);

CREATE TABLE DYN_MULTI_ANALYSIS_SPEC (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	name varchar(200) NOT NULL,
	single_run_spec_id int8 NOT NULL,
	initial_state_distribution_id int8 NOT NULL,
	run_num int4 NOT NULL
);

CREATE TABLE DYN_SINGLE_ANALYSIS_RESULT (
	id int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	initial_state float8[] NOT NULL,
	spatial_correlations_id int8 NOT NULL,
	time_correlations_id int8 NOT NULL,
 	neighbor_time_correlations float8[] NOT NULL,
    spatial_stationary_points_per_time_id int8 NOT NULL,
    time_stationary_points_per_time_id int8 NOT NULL,
    spatial_cumulative_diff_per_time_id int8 NOT NULL,
    time_cumulative_diff_per_time_id int8 NOT NULL,
    spatial_nonlinearity_errors_id int8 NOT NULL,
    time_nonlinearity_errors_id int8 NOT NULL,
    final_fixed_points_detected bool[] NOT NULL,
    mean_fixed_points_detected float8[] NOT NULL,
    unbound_values_detected bool[] NOT NULL,
    final_lyapunov_exponents float8[] NOT NULL,
    derrida_results_id int8 NOT NULL,
    multi_run_result_id int8
);

CREATE TABLE DYN_MULTI_ANALYSIS_RESULT (
	id int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	spec_id int8 NOT NULL,
	ac_id int8
);

-- TASK DEFs

CREATE TABLE TASK (
	id int8 NOT NULL,
	ol_version int8 NOT NULL,
	time_created timestamp NOT NULL,
	created_by int8 NOT NULL,
	repeat bool,
	jobs_in_sequence_num int4,
	max_jobs_in_parallel_num int4
);

CREATE TABLE TASK_EVO_RUN (
	task_id int8 NOT NULL,
	evo_task_id int8 NOT NULL,
	population_content_store_option int2 NOT NULL,
	population_selection int2 NOT NULL,
	auto_save bool NOT NULL
);
