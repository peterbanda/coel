INSERT INTO EVO_AC_TASK
            (id, ol_version, create_time, created_by, name, task_type,
	     as_repetitions, ga_setting_id, ac_id, ac_evaluation_id, run_steps, last_eval_steps_to_count
            )
     VALUES (1, 1, TIMESTAMP '2011-11-09 01:22:13', 1, 'Test AC Task', 0,
             1, 1, 1, 1, 600100, 20
            );

INSERT INTO EVO_AC_TASK
            (id, ol_version, create_time, created_by, name, task_type,
	     as_repetitions, ga_setting_id, network_id, run_steps
            )
     VALUES (2, 1, NOW(), 1, 'Test NET Task', 4,
             1, 1, 1, 600100
            );

INSERT INTO EVO_AC_TASK
            (id, ol_version, create_time, created_by, name, task_type,
	     as_repetitions, ga_setting_id, ac_id, ac_evaluation_id, run_steps, last_eval_steps_to_count
            )
     VALUES (2, 1, NOW(), 1, 'Test AC Interaction Series Task', 5,
             1, 1, 1, 1, 600100, 20
            );
