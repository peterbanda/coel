cd DDL

psql -d $COEL_DB -h $COEL_DBHOST -p $COEL_DBPORT -U $COEL_DBUSER -f create_tables_and_constraints.sql
