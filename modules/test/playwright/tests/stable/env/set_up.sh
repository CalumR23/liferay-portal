CURRENT_DIR_NAME=$(dirname ${BASH_SOURCE[0]})

echo CURRENT_DIR_NAME=${CURRENT_DIR_NAME}

source ${CURRENT_DIR_NAME}/../../../env/common.sh

function main {
update_portal_ext_properties

prepare_additional_bundles 1

start_additional_bundles 1

	start_default_app_server

	deploy_parent_project_osgi_modules

	deploy_project_osgi_modules

	deploy_parent_project_deploy_folder

	deploy_project_deploy_folder

	deploy_parent_project_osgi_configs

	deploy_project_osgi_configs

	deploy_parent_project_client_extensions

	deploy_project_client_extensions
}
main "${@}"