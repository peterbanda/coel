TEMPLATE=coel
DB_ADMIN=postgres

sudo -u postgres psql -a -d $TEMPLATE -h $COEL_DBHOST -p $COEL_DBPORT -U $DB_ADMIN -f create_DB_remote.sql
