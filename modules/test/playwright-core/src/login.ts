/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Cookie, Page, expect} from '@playwright/test';
import {promises as dns} from 'dns';

import {clearAuthToken, getHeader, readAuthToken} from './authToken';
import {liferayConfig} from './liferayConfig';

export type LoginScreenName =
	| 'demo.company.admin'
	| 'demo.organization.owner'
	| 'demo.unprivileged'
	| 'test'
	| 'user';

export const userData = {
	'demo.company.admin': {
		name: 'Demo',
		password: 'demo',
		surname: 'Company Admin',
	},
	'demo.organization.owner': {
		name: 'Demo',
		password: 'demo',
		surname: 'Organization Owner',
	},
	'demo.unprivileged': {
		name: 'Demo',
		password: 'demo',
		surname: 'Unprivileged',
	},
	'test': {
		name: 'Test',
		password: liferayConfig.environment.password,
		surname: 'Test',
	},
	'user': {
		password: liferayConfig.environment.password,
	},
};

interface LoginOptions {
	domain?: string;
	loginUrl?: string;
	page: Page;
	password?: string;
	rememberMe?: boolean;
	screenName: LoginScreenName | string;
}

export async function performLogin(
	page: Page,
	screenName: LoginScreenName | string,
	baseUrl = '/',
	domain = '@liferay.com',
	rememberMe = true
): Promise<Cookie[]> {
	const {name, password, surname} = userData[screenName];

	await page.goto(baseUrl);

	await page.getByRole('button', {name: 'Sign In'}).click();

	const emailAddressInput = page.getByLabel('Email Address');

	await expect(emailAddressInput).toBeVisible();

	await emailAddressInput.fill(`${screenName}${domain}`);

	await expect(emailAddressInput).toHaveValue(`${screenName}${domain}`);

	await page.getByLabel('Password').fill(password);
	await page.getByLabel('Remember Me').setChecked(rememberMe);

	await page
		.locator('form.sign-in-form')
		.getByRole('button', {name: 'Sign In'})
		.click();

	await expect(page.getByLabel(`${name} ${surname}`)).toBeVisible({
		timeout: 30 * 1000,
	});

	await readAuthToken(page);

	return await page.context().cookies();
}

async function collectLoginDiagnostics({
	loginUrl,
	page,
	params,
}: {
	loginUrl: string;
	page: Page;
	params: URLSearchParams;
}) {
	const hostname = new URL(loginUrl).hostname;

	const results: string[] = [];

	try {
		const addresses = await dns.lookup(hostname, {all: true});

		results.push(`dns ${hostname}: ${JSON.stringify(addresses)}`);
	}
	catch (dnsError) {
		results.push(`dns ${hostname} threw: ${dnsError.message}`);
	}

	let startedAt = Date.now();

	try {
		const response = await page.request.get(loginUrl, {
			timeout: 10 * 1000,
		});

		results.push(
			`request GET ${loginUrl}: ${response.status()} in ${Date.now() - startedAt}ms`
		);
	}
	catch (getError) {
		results.push(
			`request GET ${loginUrl} threw after ${Date.now() - startedAt}ms: ${getError.message.split('\n')[0]}`
		);
	}

	startedAt = Date.now();

	try {
		const headers = await getHeader(
			page,
			'application/x-www-form-urlencoded'
		);

		const browserFetch = page.evaluate(
			async ({body, headers, url}) => {
				const abortController = new AbortController();

				const abortTimer = setTimeout(
					() => abortController.abort(),
					10 * 1000
				);

				const fetchStartedAt = Date.now();

				try {
					const response = await fetch(url, {
						body,
						credentials: 'include',
						headers,
						method: 'POST',
						signal: abortController.signal,
					});

					return `${response.status} in ${Date.now() - fetchStartedAt}ms`;
				}
				catch (fetchError) {
					return `threw ${fetchError.name} after ${Date.now() - fetchStartedAt}ms`;
				}
				finally {
					clearTimeout(abortTimer);
				}
			},
			{
				body: params.toString(),
				headers,
				url: `${loginUrl}/c/portal/login`,
			}
		);

		const evaluateTimeout = new Promise<string>((resolve) =>
			setTimeout(
				() => resolve('page.evaluate did not return within 15000ms'),
				15 * 1000
			)
		);

		results.push(
			`browser fetch POST: ${await Promise.race([browserFetch, evaluateTimeout])}`
		);
	}
	catch (evaluateError) {
		results.push(
			`browser fetch POST threw after ${Date.now() - startedAt}ms: ${evaluateError.message.split('\n')[0]}`
		);
	}

	return results.join(' | ');
}

export async function performLoginViaApi({
	domain = '@liferay.com',
	loginUrl = liferayConfig.environment.baseUrl,
	page,
	password = undefined,
	rememberMe = true,
	screenName,
}: LoginOptions) {
	const resolvedPassword =
		password ?? userData[screenName || 'test'].password;

	const params = new URLSearchParams({
		login: `${screenName}${domain}`,
		password: resolvedPassword,
		rememberMe: String(rememberMe),
	});

	let lastAttempt = '(no attempt made)';

	try {
		await page.goto(loginUrl);

		clearAuthToken(page);

		const url = `${loginUrl}/c/portal/login`;

		await expect
			.poll(async () => {
				const attemptStartedAt = Date.now();

				try {
					lastAttempt = `reading the auth token on ${page.url()}`;

					const headers = await getHeader(
						page,
						'application/x-www-form-urlencoded'
					);

					lastAttempt = `posting to ${url} from ${page.url()}`;

					const response = await page.request.post(url, {
						data: params.toString(),
						headers,
						timeout: 10 * 1000,
					});

					lastAttempt = `reading the body of ${response.status()} ${response.url()}`;

					const body = await response.text();

					lastAttempt = `${response.status()} ${response.url()} ${body.slice(0, 200)}`;

					return response.status();
				}
				catch (postError) {
					lastAttempt = `threw after ${Date.now() - attemptStartedAt}ms: ${postError.message}`;

					throw postError;
				}
			})
			.toBe(200);

		await page.goto(loginUrl);

		await readAuthToken(page);
	}
	catch (error) {
		const diagnostics = await collectLoginDiagnostics({
			loginUrl,
			page,
			params,
		});

		error.message = `Login via API failed\n\nLast attempt: ${lastAttempt}\n\nDiagnostics: ${diagnostics}\n\n${error.message}`;

		throw error;
	}

	return await page.context().cookies();
}

export async function performLogout(page: Page) {
	await page.goto('/c/portal/logout');

	await page.waitForURL((url) => !url.pathname.endsWith('/c/portal/logout'));

	clearAuthToken(page);
}

export async function performUserSwitch(
	page: Page,
	screenName: LoginScreenName | string
) {
	await performLogout(page);

	await performLogin(page, screenName);
}

export async function performUserSwitchViaApi(
	page: Page,
	screenName: LoginScreenName | string
) {
	await page.waitForURL((url) => !url.pathname.endsWith('/c/portal/logout'));

	await performLoginViaApi({page, screenName});
}
