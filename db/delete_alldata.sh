cd DML

psql -d $COEL_DB -h $COEL_DBHOST -p $COEL_DBPORT -U $COEL_DBUSER -f delete_alldata.sql
