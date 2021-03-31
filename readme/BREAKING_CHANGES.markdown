# What are the Breaking Changes for Liferay 7.4?

This document presents a chronological list of changes that break existing
functionality, APIs, or contracts with third party Liferay developers or users.
We try our best to minimize these disruptions, but sometimes they are
unavoidable.

Here are some of the types of changes documented in this file:

* Functionality that is removed or replaced
* API incompatibilities: Changes to public Java or JavaScript APIs
* Changes to context variables available to templates
* Changes in CSS classes available to Liferay themes and portlets
* Configuration changes: Changes in configuration files, like
  `portal.properties`, `system.properties`, etc.
* Execution requirements: Java version, J2EE Version, browser versions, etc.
* Deprecations or end of support: For example, warning that a certain
  feature or API will be dropped in an upcoming version.

*This document has been reviewed through commit `ac8f7b9b47639`.*

## Breaking Changes Contribution Guidelines

Each change must have a brief descriptive title and contain the following
information:

* **[Title]** Provide a brief descriptive title. Use past tense and follow
  the capitalization rules from
  <http://en.wikibooks.org/wiki/Basic_Book_Design/Capitalizing_Words_in_Titles>.
* **Date:** Specify the date you submitted the change. Format the date as
  *YYYY-MMM-DD* (e.g., 2014-Feb-25).
* **JIRA Ticket:** Reference the related JIRA ticket (e.g., LPS-12345)
  (Optional).
* **What changed?** Identify the affected component and the type of change that
  was made.
* **Who is affected?** Are end-users affected? Are developers affected? If the
  only affected people are those using a certain feature or API, say so.
* **How should I update my code?** Explain any client code changes required.
* **Why was this change made?** Explain the reason for the change. If
  applicable, justify why the breaking change was made instead of following a
  deprecation process.

Here's the template to use for each breaking change (note how it ends with a
horizontal rule):

```
### Title
- **Date:**
- **JIRA Ticket:**

#### What changed?

#### Who is affected?

#### How should I update my code?

#### Why was this change made?

---------------------------------------

```
**80 Columns Rule:** Text should not exceed 80 columns. Keeping text within 80
columns makes it easier to see the changes made between different versions of
the document. Titles, links, and tables are exempt from this rule. Code samples
must follow the column rules specified in Liferay's
[Development Style](http://www.liferay.com/community/wiki/-/wiki/Main/Liferay+development+style).

The remaining content of this document consists of the breaking changes listed
in ascending chronological order.

## Breaking Changes List

### The tag liferay-ui:flash is no longer available
- **Date:** 2020-Oct-13
- **JIRA Ticket:** [LPS-121732](https://issues.liferay.com/browse/LPS-121732)

#### What changed?

The tag `liferay-ui:flash` has been deleted and is no longer available.

#### Who is affected?

This affects any development that uses the `liferay-ui:flash` tag to embed
Adobe Flash movies in a page.

#### How should I update my code?

If you still need to embed Adobe Flash content in a page, you would need to
write your own code using one of the standard mechanisms such as `SWFObject`.

#### Why was this change made?

This change was made to align with [Adobe dropping support for Flash](https://www.adobe.com/products/flashplayer/end-of-life.html)
in December 31, 2020 and browsers removing Flash support in upcoming versions.

---------------------------------------

### The /portal/flash path is no longer available
- **Date:** 2020-Oct-13
- **JIRA Ticket:** [LPS-121733](https://issues.liferay.com/browse/LPS-121733)

#### What changed?

The public path `/portal/flash` that could be used to play an Adobe Flash movie
passing the movie URL as a parameter has been removed.

Additionally, the property and accessors have been removed from `ThemeDisplay`
and are no longer accesible.

#### Who is affected?

This affects people that were using the path `/c/portal/flash` directly to show
pages with Adobe Flash content.

#### How should I update my code?

A direct code update is not possible. One possible solution would be to create
a custom page simulating to simulate the old behaviour and read the different
movie parameters from the URL and then instantiate it using the common means
for Adobe Flash reproduction.

#### Why was this change made?

This change was made to align with [Adobe dropping support for Flash](https://www.adobe.com/products/flashplayer/end-of-life.html)
in December 31, 2020 and browsers removing Flash support in upcoming versions.

---------------------------------------

### The AUI module `swfobject` is no longer available
- **Date:** 2020-Oct-13
- **JIRA Ticket:** [LPS-121736](https://issues.liferay.com/browse/LPS-121736)

#### What changed?

The AUI module `swfobject` that provided a way to load the library SWFObject
commonly used to embed Adobe Flash content has been removed.

#### Who is affected?

This affects people that were requiring the AUI `swfobject` module as a way to
make the library available globally.

#### How should I update my code?

If you still need to embed Adobe Flash content, you can inject the SWFObject
library directly in your application using any of the available mechanisms.

#### Why was this change made?

This change was made to align with [Adobe dropping support for Flash](https://www.adobe.com/products/flashplayer/end-of-life.html)
in December 31, 2020 and browsers removing Flash support in upcoming versions.

---------------------------------------

### Refactor Clamd integration to use Clamd remote service and remove portal
properties configuration for AntivirusScanner selection and hook support for
AntivirusScanner registration in favor of AntivirusScanner OSGi integration.

- **Date:** 2020-Oct-21
- **JIRA Ticket:** [LPS-122280](https://issues.liferay.com/browse/LPS-122280)

#### What changed?

The portal impl version of Clamd integration has been pulled out as an OSGi
service to use Clamd remote service.
The portal properties configuration for AntivirusScanner implementation
selection and hook support for AntivirusScanner implementation registration has
been removed in favor of the AntivirusScanner OSGi integration.

#### Who is affected?

This affects people that were using the portal impl version of Clamd integration
and people that were providing their own AntivirusScanner implementation by hook.

#### How should I update my code?

If you were using the portal impl version of Clamd integration, you need to go
to Control Panel -> System Settings -> Security -> category.antivirus to
configure the new Clamd remote service.

<<<<<<< HEAD
If you were providing your own AntivirusScanner implementation by hook, you need
to update your implementation as an OSGi service with a service ranking higher
than Clamd remote service AntivirusScanner implementation which is default to 0.

#### Why was this change made?

This change was made to better support container environment and unify the api
to do OSGi integration.
=======
### Replaced OSGi configuration Property autoUpgrade
- **Date:** 2020-Jan-03
- **JIRA Ticket:** [LPS-102842](https://issues.liferay.com/browse/LPS-102842)

#### What changed?

The OSGi property `autoUpgrade` defined in `com.liferay.portal.upgrade.internal.configuration.ReleaseManagerConfiguration.config` was replaced with the portal property `upgrade.database.auto.run`.

Unlike the old property, which only controlled the upgrade processes in modules, the new one also affects the Core upgrade processes. The default value is `false`, so upgrade processes won't run on startup or module deployment. You can execute module upgrade processes anytime via the Gogo Shell console or via Database Upgrade Tool when the server is down.

This property is set to `true` in the `portal-developer.properties`

#### Who is affected?

This change affects any environment where you're expecting to run upgrades automatically on server startup or on module deployment. Setting `upgrade.database.auto.run` to `true` is not recommended in production environments. If you must, however, upgrade on server startup, first back up your Liferay database and File Store (Document Library).

If you set `upgrade.database.auto.run` to `false` (default value) but database upgrade is required, Liferay prints information about the required upgrade and halts startup. Database upgrade is typically required by major/minor Liferay releases and may be required by early CE Portal GA releases and certain Service Packs (in exceptional cases)--Fix Packs never require database upgrade. On startup, Liferay prints information about any pending micro changes. You can always use the Gogo Shell console and release notes to check such changes and then decide whether to execute them.

#### How should I update my code?

This change doesn't affect your code.

#### Why was this change made?

This change was made to unify the auto-upgrade feature between the Core and modules. The default value was also changed to avoid executing new upgrade processes on startup in production environments.
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

---------------------------------------

### The AssetEntries_AssetCategories table and its corresponding code have been removed from the portal
- **Date:** 2020-Oct-16
- **JIRA Ticket:** [LPS-89065](https://issues.liferay.com/browse/LPS-89065)

#### What changed?

AssetEntries_AssetCategories and its corresponding code have been removed from
the portal. In 7.2, this mapping table and the corresponding interface were
replaced by the table AssetEntryAssetCategoryRel and the service
AssetEntryAssetCategoryRelLocalService.

#### Who is affected?

This affects any content or code that relies on calling the old interfaces for
the AssetEntries_AssetCategories relationship, through the
AssetEntryLocalService and AssetCategoryLocalService.

#### How should I update my code?

Use the new methods in AssetEntryAssetCategoryRelLocalService to retrieve the
same data as before. The method signatures haven't changed; they have just been
relocated to a different service.

#### Why was this change made?

This change was made due to changes resulting from [LPS-76488](https://issues.liferay.com/browse/LPS-76488),
which let developers control the order of a list of assets for a given category.
The breaking changes regarding the service replacement were notified on
2019-Sep-11, this would be the final step to removing the table.

---------------------------------------

### The way we register display pages for entities has changed
- **Date:** 2020-Oct-27
- **JIRA Ticket:** [LPS-122275](https://issues.liferay.com/browse/LPS-122275)

#### What changed?

The way default display pages are handled has changed. From Liferay Portal 7.1
through Liferay Portal 7.3 the entities that had a default display page were
persisted in the database while those that don't have display pages associated
to them were ommited. This behaviour has been switched, so that the default
display pages are not persisted and those entities that don't have a display
page associated to them are tracked.

#### Who is affected?

Everyone with custom entities for which display pages can be created

#### How should I update my code?

If you have custom entities with display pages, we have created a base upgrade
process (`BaseUpgradeAssetDisplayPageEntries`) that receives a table, primary
key column name and a className, that will handle the swap logic.

#### Why was this change made?

This change was made to make the logic for display pages more consistent with
the overall concept of display pages.

---------------------------------------

### Previously unused and deprecated JSP tags are no longer available
- **Date:** 2020-Nov-24
- **JIRA Ticket:** [LPS-112476](https://issues.liferay.com/browse/LPS-112476)

#### What changed?

A series of deprecated and unused JSP tags have been removed and are no longer
available. This list includes:

- clay:table
- liferay-ui:alert
- liferay-ui:input-scheduler
- liferay-ui:organization-search-container-results
- liferay-ui:organization-search-form
- liferay-ui:ratings
- liferay-ui:search-speed
- liferay-ui:table-iterator
- liferay-ui:toggle-area
- liferay-ui:toggle
- liferay-ui:user-search-container-results
- liferay-ui:user-search-

#### Who is affected?

Everyone still using one of the removed tags

#### How should I update my code?

Use the new tags for those where replacements were previously avaialable. In
many cases, there's no direct replacement for these tags, so if you still need
to use them, you could make a copy of the old implementation and serve it
directly from your project.

#### Why was this change made?

This change was made to remove legacy code that was previously signaled for
removal in an attempt to clarify the default JSP component offering and focus
on providing a smaller but higher quality set of compoentns.

---------------------------------------
### The CSS class .container-fluid-1280 has been replaced with .container-fluid.container-fluid-max-xl
- **Date:** 2020-Nov-24
- **JIRA Ticket:** [LPS-123894](https://issues.liferay.com/browse/LPS-123894)

#### What changed?

<<<<<<< HEAD
The CSS class `.container-fluid-1280` has been replaced with `.container-fluid.container-fluid-max-xl` and the compatibility layer that had its style has been removed from Portal.

#### Who is affected?

All the container elements that had the CSS class `.container-fluid-1280`

#### How should I update my code?

The first recommendation is to use the updated CSS classes from Clay `.container-fluid.container-fluid-max-xl` instead of `.container-fluid-1280`. The second one is to use ClayLayout [Components](https://clayui.com/docs/components/layout.html) & [Taglibs](https://clayui.com/docs/get-started/using-clay-in-jsps.html#clay-sidebar)

#### Why was this change made?

This change was made to remove deprecated legacy code from Portal and improve the code consistency and performance
=======
The `addAction` methods with signatures `String, Class, GroupedModel, String, UriInfo` and `String, Class, Long, String, String, Long, UriInfo` were removed.

#### Who is affected?

This affects anyone using the removed `addAction` methods or anyone that has dependencies like `compileOnly group: "com.liferay", name: "com.liferay.portal.vulcan.api", version: "[1.0.0, 2.0.0)"`.

#### How should I update my code?

Use `addAction` methods with the signature `String, Class, GroupedModel, String, Object, UriInfo` or `String, Class, Long, String, String, Object, Long, UriInfo`.

#### Why was this change made?

These methods were removed as part of a cleanup refactor.
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

---------------------------------------

### Runtime minification of CSS and JS resources is now disabled by default
- **Date:** 2020-Nov-27
- **JIRA Ticket:** [LPS-123550](https://issues.liferay.com/browse/LPS-123550)

#### What changed?

The `minifier.enable` setting in `portal.properties` now defaults to
`false`. Instead of performing run-time minification of CSS and JS
resources, we prepare pre-minified resources at build-time. There should
be no user-visible changes in page styles or logic.

#### Who is affected?

Anybody who relies on specific implementation details of the run-time minifier
(usually the Google Closure Compiler).

#### How should I update my code?

If you wish to maintain the run-time minification behavior, you can set
`minifier.enable` back to `true` in `portal.properties`.

#### Why was this change made?

By moving minification of frontend resources from run-time to build-time
we reduce server load and gain access to the latest minification
technologies available within the frontend ecosystem.

---------------------------------------

### SoyPortlet is no longer available
- **Date:** 2020-Dec-9
- **JIRA Ticket:** [LPS-122955](https://issues.liferay.com/browse/LPS-122955)

#### What changed?

The class `SoyPortlet` used to implement Portlet whose views are backed by
Closure Templates (Soy) has been removed and is no longer available.

#### Who is affected?

Anyone using `SoyPortlet` as a base for their portlet developments.

#### How should I update my code?

We heavily recommend re-writing your Soy portlets using either a well
established architecture such as `MVCPortlet` using JSPs or a particular frontend
framework of your choice.

As a temporary measure, you could alternatively copy all the necessary removed
classes into you own. However, support for Soy templates is likely to be removed
in this version as well so doing this might require a lot of work.

#### Why was this change made?

This is done as a way to simplify our frontend technical offering and better
focus on proven technologies with high demand in the market.

A further exploration and analysis of the different frontend options available
can be found in [The State of Frontend Infrastructure](https://liferay.dev/blogs/-/blogs/the-state-of-frontend-infrastructure) including a rationale on why we're moving
away from Soy:

> Liferay has invested several years into Soy believing it was the holy grail.
> We believed the ability to compile Closure templates would provide us the
> performance of JSP with the reusable components of other JavaScript
> frameworks. While it came close to achieving some of those goals, we never
> hit the performance we wanted and more importantly, it always felt like we
> were the only people using this technology.

---------------------------------------

<<<<<<< HEAD
### Server-side Closure Templates (Soy) Support has been removed
- **Date:** 2020-Dec-14
- **JIRA Ticket:** [LPS-122956](https://issues.liferay.com/browse/LPS-122956)

#### What changed?

The following modules and the classes they exported to allow Soy rendering
server-side have been removed:
- `portal-template-soy-api`
- `portal-template-soy-impl`
- `portal-template-soy-context-contributor`
=======
### The ContentField value Property Name Was Changed to contentFieldValue
- **Date:** 2020-Mar-18
- **JIRA Ticket:** [LPS-106886](https://issues.liferay.com/browse/LPS-106886)

#### What changed?

In Headless Delivery API, the property name `value` inside the ContentField schema was changed to `contentFieldValue`.

#### Who is affected?

This affects REST clients depending in the ContentField `value` property name.

#### How should I update my code?

Change the property name to `contentFieldValue` in the REST client.

#### Why was this change made?

This change restores consistency with all value property names in the Headless APIs, called `{schemaName}+Value`.

---------------------------------------

### Removed liferay-editor-image-uploader Plugin
- **Date:** 2020-Mar-27
- **JIRA Ticket:** [LPS-110734](https://issues.liferay.com/browse/LPS-110734)

### What changed?

`liferay-editor-image-uploader` AUI plugin was removed. Its code was merged into `addimages` CKEditor plugin, used by Alloy Editor and CKEditor.

### Who is affected

This affects custom solutions that use the plugin directly.

### How should I update my code?

There's no direct replacement for the `liferay-editor-image-uploader` plugin. If you have a component that relies on it, you can co-locate a copy of the old implementation and use it locally within your module.

#### Why was this change made?

This change enables image drag and drop handling in CKEditor and provides a common image uploader for both Alloy Editor and CKEditor.

---------------------------------------

### TinyMCE Editor Is No Longer Bundled by Default
- **Date:** 2020-Mar-27
- **JIRA Ticket:** [LPS-110733](https://issues.liferay.com/browse/LPS-110733)

### What changed?

As of 7.3, CKEditor is the default and only supported WYSIWYG editor.

### Who is affected

This affects anyone who uses TinyMCE.
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

To simplify the migration, the following modules remain available in a deprecated
deprecated fashion providing only client-side initialization of previous Soy
components:
- `portal-template-soy-renderer-api`
- `portal-template-soy-renderer-impl`

#### Who is affected?

Anyone directly using removed classes like `SoyContext`, `SoyHTMLData`... or
declaring `TemplateContextContributor` using `LANG_TYPE_SOY` as the value for
the `lang.type` attribute.

Developers using our Soy `ComponentRenderer` to initialize Soy components.

#### How should I update my code?

There is no replacement for the removed Soy support. If you fall under the first
scenario, we recommend switching to a different supported template language and
rewrite your templates and components.

If you're using `ComponentRenderer`, the only difference should be that your
components no longer produce markup server-side. If this is important to you, a
temporary workaround has been added. You can manually generate a version of the
markup you want to render server-side and pass it as a `__placeholder__` property
in your `context` parameter. Keep in mind that `ComponentRenderer` is deprecated
and will go away in the future, so we kindly recommend that you rewrite your
component using a different technology.

#### Why was this change made?

This is done as a way to simplify our frontend technical offering and better
focus on proven technologies with high demand in the market.

A further exploration and analysis of the different frontend options available
can be found in [The State of Frontend Infrastructure](https://liferay.dev/blogs/-/blogs/the-state-of-frontend-infrastructure) including a rationale on why we're moving
away from Soy:

> Liferay has invested several years into Soy believing it was the holy grail.
> We believed the ability to compile Closure templates would provide us the
> performance of JSP with the reusable components of other JavaScript
> frameworks. While it came close to achieving some of those goals, we never
> hit the performance we wanted and more importantly, it always felt like we
> were the only people using this technology.

---------------------------------------

### The spi.id property in log4j xml definition file has been removed
- **Date:** 2021-Jan-19
- **JIRA Ticket:** [LPS-125998](https://issues.liferay.com/browse/LPS-125998)

#### What changed?

The `spi.id` property in log4j xml definition file has been removed.

#### Who is affected?

Anyone is using `@spi.id@` in its custom log4j xml definition file.

#### How should I update my code?

Remove `@spi.id@` from log4j xml definition file.

#### Why was this change made?

The support of SPI has been removed by LPS-110758.

---------------------------------------

### Deprecated attributes have been removed from the frontend-taglib-clay tags
- **Date:** 2021-Jan-26
- **JIRA Ticket:** [LPS-125256](https://issues.liferay.com/browse/LPS-125256)

#### What changed?

The deprecated attributes have been removed from the `frontend-taglib-clay`
taglib.

#### Who is affected?

Anyone using deprecated attributes for `<clay:*>` tags.

#### Why was this change made?

The `frontend-taglib-clay` module is now using components from
[`Clay v3`](https://github.com/liferay/clay), which doesn't support the
previous attributes.

---------------------------------------

### Handling HTML boolean attributes in tags
- **Date:** 2021-Feb-18
- **JIRA Ticket:** [LPS-127832](https://issues.liferay.com/browse/LPS-127832)

#### What changed?

Boolean HTML attributes will only be rendered if passed a value of `true`.
The value for such attributes will be their canonical name.

Previously, a value such as `false` for a `disabled` attribute
would be rendered into the DOM as `disabled="false"`; now, it is simply
omitted. Likewise, a `true` value for a `disabled` attribute was
formerly rendered into the DOM as `disabled="true"`; now it is rendered
as `disabled="disabled"`.

#### Who is affected?

Anyone passing the following boolean attributes to tag libraries:

"allowfullscreen", "allowpaymentrequest", "async", "autofocus", "autoplay",
"checked", "controls", "default", "disabled", "formnovalidate", "hidden",
"ismap", "itemscope", "loop", "multiple", "muted", "nomodule", "novalidate",
"open", "playsinline", "readonly", "required", "reversed", "selected",
and "truespeed".

#### How should I update my code?

Ensure that you pass `true` when you want a boolean attribute to be
present in the DOM. If you have any CSS selectors targeting a `true`
value (e.g., `[disabled="true"]`) update them to instead target presence
of the attribute (e.g., `[disabled]`) or its canonical name (e.g.,
`[disabled="disabled"]`).

#### Why was this change made?

This change is being made for better compliance with [the HTML Standard](https://html.spec.whatwg.org/#boolean-attribute),
which says that "The presence of a boolean attribute on an element represents
the true value, and the absence of the attribute represents the false value. If
the attribute is present, its value must either be the empty string or a value
that is an ASCII case-insensitive match for the attribute's canonical name."

---------------------------------------

### Remove CSS Compatibility Layer
- **Date:** 2021-Jan-2
- **JIRA Ticket:** [LPS-123359](https://issues.liferay.com/browse/LPS-123359)

#### What changed?

The support for Boostrap 3 markup has been deleted and is no longer available.

#### Who is affected?

This affects any development that uses the old Boostrap 3 markup and was not correctly migrated to Boostrap 4 markup.

#### How should I update my code?

If you are using Clay markup you can update it by following the last [Clay components](https://clayui.com/docs/components/index.html) version.
If your markup is based on Boostrap 3, you can update it with new Boostrap 4 markup following [migrating guidelines](https://getbootstrap.com/docs/4.4/migration/).

#### Why was this change made?

<<<<<<< HEAD
We included a "small" configurable CSS compatibility layer to simplify the migration from Liferay 7.0 to 7.1. Now it has been removed in order to fix conflicts with new styles and improve general CSS weight.
=======
This method was removed as part of a clean up refactor.

---------------------------------------

### Moving Lexicon icons path
- **Date:** 2020-Aug-17
- **JIRA Ticket:** [LPS-115812](https://issues.liferay.com/browse/LPS-115812)

### What changed?

The path for the Lexicon icons has been changed from `themeDisplay.getPathThemeImages() + "/lexicon/icons.svg` to `themeDisplay.getPathThemeImages() + "/clay/icons.svg`

### Who is affected

This affects custom solutions that use the Lexicon icons path directly. The Gradle task for building the icons on the `lexicon` path will be removed.

### How should I update my code?

Update the path to reference `clay` instead of `lexicon`

#### Why was this change made?

This change was made to unify references to the icon sprite map.

---------------------------------------

### Replaced portal properties: view.count.enabled and buffered.increment.enabled
- **Date:** 2020-Oct-01
- **JIRA Ticket:** [LPS-120626](https://issues.liferay.com/browse/LPS-120626) and [LPS-121145](https://issues.liferay.com/browse/LPS-121145)

#### What changed?

Enabling and disabling view counts globally and specifically for entities has been removed from portal properties and is now configured as system settings. View counts can be configured in the UI at *System Settings* &rarr; *Infrastructure* &rarr; *View Count* or using a configuration file named `com.liferay.view.count.configuration.ViewCountConfiguration.config`.

Here are the portal property changes:

The `buffered.increment.enabled` portal property has been removed. Enabling and disabling view counts globally is now done using the `enabled` property on the View Count page.

Disabling view count behavior for a specific entity is no longer done in portal properties, for example, by setting `view.count.enabled[SomeEntity]=false` in 7.3 or `buffered.increment.enabled[SomeEntity]=false` in 7.2, but is now done by adding the entity class name to the `Disabled Class Name` value list on the View Count page.

#### Who is affected?

This affects anyone who has the portal property setting `view.count.enabled=false` or `buffered.increment.enabled=false`.

This affects anyone who has disabled view counts for some entity (e.g., `SomeEntity`) using portal property settings `view.count.enabled[SomeEntity]=false` in early 7.3 versions or `buffered.increment.enabled[SomeEntity]=false` in 7.2 portal.

#### How should I update my code?

Remove `view.count.enabled` or `buffered.increment.enabled` portal properties and entity-specific properties such as `view.count.enabled[SomeEntity]=false` or `buffered.increment.enabled[SomeEntity]=false`.

Configure view count behavior in System Settings or using a configuration file:

In *System Settings* &rarr; *Infrastructure* &rarr; *View Count*, set `enabled` to `false` to disable view counts globally, or set `enabled` to `true` to enable view counts globally and disable view counts for specific entities by adding the entity class names to the `Disabled Class Name` value list.

To use a configuration file, configure view counts in System Settings, save the settings, and export them to a `com.liferay.view.count.configuration.ViewCountConfiguration.config` file. Then deploy the configuration by placing the file in your `[Liferay Home]/osgi/configs` folder.

#### Why was this change made?

This change was made to facilitate managing view count behavior.

---------------------------------------
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
