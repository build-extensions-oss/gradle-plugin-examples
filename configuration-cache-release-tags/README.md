# Provider-backed release tags

This example exercises the `HelmRelease.tags` API introduced in
`gradle-helm-plugin` 3.2.1-beta2. Release tags are supplied by a Gradle
`Provider<Set<String>>`, so the value remains lazy and can participate in the
configuration cache.

The default tag is `application`. Resolve the matching release tasks without
contacting Helm or Kubernetes:

```shell
./gradlew helmInstallToApplication --dry-run --configuration-cache
./gradlew helmInstallToApplication --dry-run --configuration-cache
```

The first invocation stores the configuration cache entry and the second one
reuses it. You can supply another tag through a project property:

```shell
./gradlew helmInstallToDatabase --dry-run --configuration-cache \
    --project-prop=example.release.tags=database
```
