INSERT INTO AC_SIMULATION_CONFIG
 			(id, ol_version, time_created, name,
 			 ode_solver_type, upper_threshold, lower_threshold, fixed_point_detection_precision, fixed_point_detection_periodicity,
                         time_step, influx_scale
 			)
      VALUES (1, 1, TIMESTAMP '2013-04-17 13:25:13', 'Test AC spec',
              1, 10000, 0.00001, 0.000001, 50,
              0.2, 0.1
             );
