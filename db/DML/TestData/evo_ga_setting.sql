INSERT INTO EVO_GA_SETTING
            (id, ol_version, create_time, created_by, name, elite_number, population_size,
			cond_mutation, cond_crossover, crossover_prob, crossover_type, mutation_prob, mutation_per_bit_prob,
			mutation_type, bit_mutation_type, selection_type, fitness_renorm_type, generation_limit, max_value_flag
            )
     VALUES (1, 1, TIMESTAMP '2011-11-09 01:22:13', 1, 'Elite GA Default', 20, 100,
             false, false, 0.9, 0, 0.016, NULL,
             0, 0, 1, NULL, 100, true
            );
