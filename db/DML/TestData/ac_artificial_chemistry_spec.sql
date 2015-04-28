INSERT INTO AC_ARTIFICIAL_CHEMISTRY_SPEC
			(id, ol_version, time_created, name, species_num, reaction_num,
			 rate_constant_distribution_id, species_forbidden_redundancy, reactants_per_reaction_number, products_per_reaction_number,
			 catalysts_per_reaction_number, inhibitors_per_reaction_number, outflux_all
			)
     VALUES (1, 1, TIMESTAMP '2013-04-08 13:25:13', 'Test AC spec', 10, 10,
             1, 1, 1, 1,
             0, 0, false
            );