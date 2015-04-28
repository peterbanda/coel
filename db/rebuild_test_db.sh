#!/bin/bash

clear

echo "CONNECTION INFO:"
echo $COEL_DBUSER
echo $COEL_DB
echo $COEL_DBHOST
echo $COEL_DBPORT
echo ""

echo "DROPING TABLES AND CONSTRAINTS..."
echo ""

sh drop_tables_and_constraints.sh

echo ""
echo "CREATING TABLES AND CONSTRAINTS..."
echo ""

sh create_tables_and_constraints.sh

echo "" 
echo "CREATING TESTDATA..."
echo ""

sh recreate_testdata.sh

echo ""
echo "BYE!"
