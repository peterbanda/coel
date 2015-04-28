INPUT_FILE=coel_data_dump_0.8.1.sql

psql -d $COEL_DB -h $COEL_DBHOST -p $COEL_DBPORT -U $COEL_DBUSER -f $INPUT_FILE
