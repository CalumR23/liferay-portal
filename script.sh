#!/bin/bash
until mysqladmin ping --host=mysql-container --silent
do
	echo "Waiting for MySQL 8.4 to be ready"
	sleep 10
done