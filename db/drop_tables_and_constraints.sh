cd DDL

psql -a -d $COEL_DB -h $COEL_DBHOST -p $COEL_DBPORT -U $COEL_DBUSER -f drop_tables_and_constraints.sql
