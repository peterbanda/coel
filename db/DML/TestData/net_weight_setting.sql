INSERT INTO net_weight_setting
            (id, ol_version, name, time_created, created_by, type, index_, parent_id, weights, setting_order, random_distribution_id)
     VALUES (1, 0, 'Test L', NOW(), 1, 1, 1, NULL, NULL, NULL, NULL);

INSERT INTO net_weight_setting
            (id, ol_version, name, time_created, created_by, type, index_, parent_id, weights, setting_order, random_distribution_id)
     VALUES (2, 0, 'Test F', NOW(), 1, 0, 1, 1, '{0.2121,0.73}', 0, NULL);

INSERT INTO net_weight_setting
            (id, ol_version, name, time_created, created_by, type, index_, parent_id, weights, setting_order, random_distribution_id)
     VALUES (3, 0, 'Test T', NOW(), 1, 2, 2, 1, NULL, NULL, 1);


