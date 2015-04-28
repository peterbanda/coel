cd DML/TestData

psql -d $COEL_DB -h $COEL_DBHOST -p $COEL_DBPORT -U $COEL_DBUSER -f recreate_testdata.sql
