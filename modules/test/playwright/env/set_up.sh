#!/bin/bash

CURRENT_DIR_NAME=$(dirname ${BASH_SOURCE[0]})

source ${CURRENT_DIR_NAME}/common.sh

function main {
	echo "in main set up"
	export PORTAL_URL=http://"$(hostname)":8080

	playwright_project_dir=$(get_playwright_project_dir)

	if [[ -f ${playwright_project_dir}/env/set_up.sh ]]
	then
		echo "in condition set up"
		/bin/bash ${playwright_project_dir}/env/set_up.sh
	else
		echo "in default condition set up"
		default_set_up
	fi
}

main "${@}"