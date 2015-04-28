INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (1, 1, 0, NULL, 1, '{14.2, 2.6, 1}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (2, 1, NULL, 1, NULL, '{2, 3, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (3, 1, NULL, 1, NULL, '{0, 101, 15}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (111, 1, NULL, NULL, NULL, '{0, 3, 2}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (112, 1, NULL, NULL, NULL, '{0, 0, 1}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (201, 1, NULL, NULL, NULL, '{1, 0, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (202, 1, NULL, NULL, NULL, '{2, 0, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (203, 1, NULL, NULL, NULL, '{1, 0, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (204, 1, NULL, NULL, NULL, '{0, 1, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (205, 1, NULL, NULL, NULL, '{0, 1, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (206, 1, NULL, NULL, NULL, '{0, 0, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (207, 1, NULL, NULL, NULL, '{1, 0, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (208, 1, NULL, NULL, NULL, '{0, 0, 2}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (209, 1, NULL, NULL, NULL, '{0, 0, 2}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (210, 1, NULL, NULL, NULL, '{0, 0, 0}'
            );

            
            
-- \lambda \rightarrow React

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (301, 1, NULL, NULL, NULL, '{0, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (302, 1, NULL, NULL, NULL, '{1, 0}'
            );

-- Prod + 2 React \rightarrow 3React

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (303, 1, NULL, NULL, NULL, '{2, 1}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (304, 1, NULL, NULL, NULL, '{3, 0}'
            );

-- React \rightarrow Prod

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (305, 1, NULL, NULL, NULL, '{1, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (306, 1, NULL, NULL, NULL, '{0, 1}'
            );

-- React \rightarrow \lambda

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (307, 1, NULL, NULL, NULL, '{1, 0}'
            );

INSERT INTO AC_MULTISET
            (id, ol_version, step, parent_multiset_id, run_id, stochiometric_vector
            )
     VALUES (308, 1, NULL, NULL, NULL, '{0, 0}'
            );         