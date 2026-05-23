#!/bin/sh

TASK_HOME=/home/user/danzj000/task
#PG_DUMP_FILE=`date +%Y%m%d`.pg_dump
PG_DUMP_FILE=danzj000.pg_dump

cd ${TASK_HOME}
/usr/local/pgsql/bin/pg_dump danzj000 | gzip -c > ${PG_DUMP_FILE}.gz

perl ${TASK_HOME}/tpmr_delete.pl

/usr/local/pgsql/bin/vacuumdb --full danzj000

