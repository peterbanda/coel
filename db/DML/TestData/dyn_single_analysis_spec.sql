INSERT INTO DYN_SINGLE_ANALYSIS_SPEC
            (id, ol_version, time_created, name, time_step_length, iterations, lyapunov_perturbation_strength, derrida_perturbation_strength, derrida_time_length,
			 time_step_to_filter, fixed_point_detection_precision, derrida_resolution)
     VALUES (1, 1, TIMESTAMP '2013-04-04 22:15:13', 'Single Spec For Test1', 1, 1500, 0.00001, 1, 1,
             20, 0.000001, 0.05);

INSERT INTO DYN_SINGLE_ANALYSIS_SPEC
            (id, ol_version, time_created, name, time_step_length, iterations, lyapunov_perturbation_strength, derrida_perturbation_strength, derrida_time_length,
			 time_step_to_filter, fixed_point_detection_precision, derrida_resolution)
     VALUES (2, 1, TIMESTAMP '2013-04-04 22:13:11', 'Single Spec For Test2', 1, 2000, 0.00001, 1, 2,
             20, 0.00005, 0.02);