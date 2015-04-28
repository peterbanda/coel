INSERT INTO DYN_SINGLE_ANALYSIS_RESULT
            (id, time_created, initial_state, spatial_correlations_id, time_correlations_id, neighbor_time_correlations,
             spatial_stationary_points_per_time_id, time_stationary_points_per_time_id, spatial_cumulative_diff_per_time_id, time_cumulative_diff_per_time_id,
             spatial_nonlinearity_errors_id, time_nonlinearity_errors_id, final_fixed_points_detected, mean_fixed_points_detected,
             unbound_values_detected, final_lyapunov_exponents, derrida_results_id, multi_run_result_id)
     VALUES (1, TIMESTAMP '2013-04-04 22:15:13', '{1.5, 1.1, 1.2, 0.4}', 1, 2, '{0.9, 0.21, -0.45, -0.24}', 
     		 1, 1, 2, 2,
     		 2, 1, '{true, false, false, false}', '{0.1, 0.1, 0.5, 0.8, 0.8}',
     		 '{true, false, false, false}', '{0.1, 0.002, -0.03, -3.8}', 1, 1);

INSERT INTO DYN_SINGLE_ANALYSIS_RESULT
            (id, time_created, initial_state, spatial_correlations_id, time_correlations_id, neighbor_time_correlations,
             spatial_stationary_points_per_time_id, time_stationary_points_per_time_id, spatial_cumulative_diff_per_time_id, time_cumulative_diff_per_time_id,
             spatial_nonlinearity_errors_id, time_nonlinearity_errors_id, final_fixed_points_detected, mean_fixed_points_detected,
             unbound_values_detected, final_lyapunov_exponents, derrida_results_id, multi_run_result_id)
     VALUES (2, TIMESTAMP '2013-04-04 22:15:15', '{1.1, 1.8, 1.8, 0.9}', 2, 1, '{0.8, 0.41, 0.51, -0.84}', 
     		 2, 1, 1, 2,
     		 1, 2, '{false, false, false, false}', '{0.1, 0.2, 0.2, 0.2, 0.3}',
     		 '{true, false, true, false}', '{0.9, -0.2, -8.47, -2.1}', 2, 1);
