window.onload = function () {
	var statusChangesRowHeader = getElementByXpath('//th[contains(.,"Test Suite")]');

	triggerEvent(statusChangesRowHeader, 'click');
}

addReportName();

if ((typeof categoryTableData !== 'undefined') && categoryTableData) {
	let categoryTableDataElement = createTable(categoryTableData, 'test-suite-data-table');

	addUtilizationRows(categoryTableDataElement);

	updateHeaderNames(categoryTableDataElement);

	window.onload = function () {
		triggerEvent(getElementByXpath('//th[contains(.,"Category")]'), 'click');

		createBarChartFromTable('Daily Server Duration by Test Suite', 'server-duration-canvas', 'Total Server Duration', categoryTableDataElement);
	}
}

if ((typeof tableData !== 'undefined') && tableData) {
	let tableElement = createTable(tableData, 'test-suite-data-table');

	addTotalColumn(tableElement);

	Sortable.init();
}