INPUT_FILE=coel_full_dump_0.8.4.sql

psql -d $COEL_DB -h $COEL_DBHOST -p $COEL_DBPORT -U $COEL_DBUSER -f $INPUT_FILE
