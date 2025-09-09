#!/bin/bash

source $(dirname ${BASH_SOURCE[0]})/../../../../env/common.sh

cd ${_PORTAL_PROJECT_DIR}

ant -f build-test.xml rebuild-database -Ddatabases.size=${1}

prepare_additional_bundles ${1} "true"