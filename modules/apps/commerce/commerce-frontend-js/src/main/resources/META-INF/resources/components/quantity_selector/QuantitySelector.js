/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {ClayInput, ClaySelectWithOption} from '@clayui/form';
import classnames from 'classnames';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useRef, useState} from 'react';

import {
	UPDATE_AFTER,
	generateQuantityOptions,
	getMinMultipleQuantity,
} from './utils/index';

function QuantitySelector({
	componentId,
	disabled,
	forceDropdown,
	large,
	name,
	onUpdate,
	quantity,
	...optionSettings
}) {
	const initialQuantity = Math.max(
		getMinMultipleQuantity(
			optionSettings.minQuantity,
			optionSettings.multipleQuantity
		),
		quantity
	);

	const [selectedQuantity, setSelectedQuantity] = useState(initialQuantity);

	const isDropdown =
		optionSettings.allowedQuantities?.length > 0 || forceDropdown;

	/**
	 * If source is <input /> and multipleQuantity > 1,
	 * the newly set value will always be floored to the
	 * closest lower multiple value.
	 */
	const onChange = ({target}) => {
		if (target.value) {
			const value = parseInt(target.value, 10);

			setSelectedQuantity(
				value - (value % optionSettings.multipleQuantity)
			);
		}
		else {
			setSelectedQuantity(initialQuantity);
		}
	};

	const keypressDebounce = useRef();

	const willUpdate = useCallback(() => {
		clearTimeout(keypressDebounce.current);

<<<<<<< HEAD
		keypressDebounce.current = setTimeout(
			() => {
				onUpdate(selectedQuantity);
			},
			isDropdown ? 0 : UPDATE_AFTER
		);
	}, [isDropdown, onUpdate, selectedQuantity]);

	// eslint-disable-next-line react-hooks/exhaustive-deps
	useEffect(willUpdate, [selectedQuantity]);

	const commonProps = {
		className: classnames({
			'form-control-lg': large,
			'quantity-selector': true,
		}),
		'data-component-id': componentId,
		disabled,
		name,
		onChange,
		value: selectedQuantity,
	};

	return (
		<>
			{isDropdown ? (
				<ClaySelectWithOption
					options={generateQuantityOptions(optionSettings)}
					{...commonProps}
				/>
=======
	function _decreaseQuantity() {
		if (prevAvailable) {
			updateCurrentQuantity(currentQuantity - props.multipleQuantity);
		}
	}

	function handleInputChange() {
		const {value} = inputRef.current;

		return updateCurrentQuantity(parseInt(value, 10));
	}

	const decreaseQuantity = throttle(_decreaseQuantity, THROTTLE_TIMEOUT),
		increaseQuantity = throttle(_increaseQuantity, THROTTLE_TIMEOUT);

	function handleInputKeyUp(e) {
		switch (e.key) {
			case 'ArrowUp':
				increaseQuantity();
				break;
			case 'ArrowDown':
				decreaseQuantity();
				break;
			case 'Enter':
			default:
				break;
		}
	}

	function handleSelectChange() {
		const {value} = inputRef.current;

		setCurrentQuantity(value);
	}

	let btnSizeClass;
	let formControlSizeClass;

	if (props.size === 'large') {
		btnSizeClass = 'btn-lg';
		formControlSizeClass = 'form-control-lg';
	}

	if (props.size === 'small') {
		btnSizeClass = 'btn-sm';
		formControlSizeClass = 'form-control-sm';
	}

	const content = (
		<div className="quantity-selector">
			{props.allowedQuantities ? (
				<>
					<select
						className={classnames(
							'form-control',
							formControlSizeClass
						)}
						name={props.inputName}
						onChange={handleSelectChange}
						ref={inputRef}
						value={currentQuantity}
					>
						{props.allowedQuantities.map((val) => (
							<option key={val} value={val}>
								{val}
							</option>
						))}
					</select>
				</>
			) : props.style === 'simple' ? (
				<div className="input-group input-group-sm simple">
					{(props.prependedIcon || props.prependedText) && (
						<div className="input-group-item input-group-item-shrink input-group-prepend">
							<span className="input-group-text">
								{props.prependedIcon ? (
									<ClayIcon symbol={props.prependedIcon} />
								) : (
									props.prependedText
								)}
							</span>
						</div>
					)}
					<div
						className={classnames(
							'input-group-item input-group-item-shrink',
							(props.appendedIcon || props.appendedText) &&
								'input-group-prepend'
						)}
					>
						<input
							className={classnames(
								'form-control text-center',
								formControlSizeClass
							)}
							disabled={props.disabled}
							max={props.maxQuantity}
							min={props.minQuantity}
							name={props.inputName}
							onChange={handleInputChange}
							ref={inputRef}
							step={props.multipleQuantity}
							type="number"
							value={currentQuantity}
						/>
					</div>
					{(props.appendedIcon || props.appendedText) && (
						<div className="input-group-append input-group-item input-group-item-shrink">
							<span className="input-group-text">
								{props.appendedIcon ? (
									<ClayIcon symbol={props.appendedIcon} />
								) : (
									props.appendedText
								)}
							</span>
						</div>
					)}
				</div>
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			) : (
				<ClayInput
					max={optionSettings.maxQuantity}
					min={getMinMultipleQuantity(
						optionSettings.minQuantity,
						optionSettings.multipleQuantity
					)}
					step={optionSettings.multipleQuantity}
					type="number"
					{...commonProps}
				/>
			)}
		</>
	);
}

QuantitySelector.defaultProps = {
	allowedQuantities: [],
	disabled: false,
	forceDropdown: false,
	large: false,
	maxQuantity: 99,
	minQuantity: 1,
	multipleQuantity: 1,
	onUpdate: () => {},
	quantity: 1,
};

QuantitySelector.propTypes = {
	allowedQuantities: PropTypes.arrayOf(PropTypes.number),
	componentId: PropTypes.string,
	disabled: PropTypes.bool,
	forceDropdown: PropTypes.bool,
	large: PropTypes.bool,
	maxQuantity: PropTypes.number,
	minQuantity: PropTypes.number,
	multipleQuantity: PropTypes.number,
	name: PropTypes.string,
	onUpdate: PropTypes.func,
	quantity: PropTypes.number,
};

export default QuantitySelector;
