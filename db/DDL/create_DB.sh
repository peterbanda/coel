TEMPLATE=template1
DB_ADMIN=postgres
COEL_DBPORT=5432
COEL_DBHOST=localhost

psql -a -d $TEMPLATE -h $COEL_DBHOST -p $COEL_DBPORT -U $DB_ADMIN -f create_DB.sql
