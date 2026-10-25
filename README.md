This is helper repository for https://github.com/build-extensions-oss/gradle-helm-plugin.

It has three purposes:
1. To keep list of examples of Gradle plugin usage.
2. To test how plugin features are visible from side user.
3. To test new plugin releases (if they break something or not).

The repository has workflow to test inputs on Windows/Linux/MacOS.

## Examples

* `artifactory-publish` publishes a chart to an Artifactory repository.
* `multiple-charts` builds a chart in one project and consumes it as a release
  from another project.
* `release-tags` supplies release tags through a Gradle
  provider using a DSL function shared by plugin versions 3.1.2 and
  3.2.1-beta3.
