INSERT INTO AC_EVALUATED_PERFORMANCE
			(id, ol_version, time_created, created_by,
			 action_series_id, ac_id, evaluation_id, random_rate_generation_num, repetitions, averaged_correct_rates, length
			)
	VALUES (-1, 1, TIMESTAMP '2012-11-11 11:25:13', 1,
			1, 1, 1, NULL, 10000, '{0.991, 0.992, 0.9992}', 3
			);

INSERT INTO AC_EVALUATED_PERFORMANCE
			(id, ol_version, time_created, created_by,
			 action_series_id, ac_id, evaluation_id, random_rate_generation_num, repetitions, averaged_correct_rates, length
			)
	VALUES (-2, 1, TIMESTAMP '2012-09-12 08:15:55', 1,
			1, 1, 1, 10000, 10, '{0.891, 0.892, 0.8892}', 3
			);
