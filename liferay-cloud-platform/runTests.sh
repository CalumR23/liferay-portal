#!/bin/bash
export CLOUD_ENVIRONMENT=coffee
export SANDBOX=
export PROJECT_CLUSTER=us-west1-c1

export BACKUP_IMAGE=
export CI_IMAGE=
export DATABASE_IMAGE=
export LIFERAY_IMAGE=
export SEARCH_IMAGE=
export WEBSERVER_IMAGE=

export DXP1_PROJECT=
export RESTORE1_PROJECT=
export RESTORE2_PROJECT=

main() {
	file="poshi-ext.properties"
	echo "##
## Test Base dir
##
test.base.dir.name=testFunctional
##
## Test Case Name
##
#test.name=FileName#TestName
test.name="$1 > $file
	cat $file
	../gradlew runPoshi
}

main "$1"