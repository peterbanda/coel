OUTPUT_FILE=coel_full_dump_0.8.4.sql

pg_dump -h $COEL_DBHOST -p $COEL_DBPORT -U $COEL_DBUSER $COEL_DB > $OUTPUT_FILE
