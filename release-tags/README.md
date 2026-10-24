# Backward-compatible release tags

This example shows how to configure `HelmRelease.tags` across the API change in
`gradle-helm-plugin` 3.2.1-beta2. In 3.1.2, `tags` is a `MutableSet<String>`;
in 3.2.1-beta2, it is a `SetProperty<String>`.

The `tags(Iterable<String>)` DSL function is available in both versions. The
build resolves its `Provider<Set<String>>` and passes the result through that
stable function:

```kotlin
tags(releaseTags.get())
```

The example is pinned to 3.1.2 so the oldest supported shape remains covered.
Changing only the plugin version to 3.2.1-beta2 exercises the new
`SetProperty<String>` shape with the same build script.

The default tag is `application`. Resolve the matching release tasks without
contacting Helm or Kubernetes:

```shell
./gradlew helmInstallToApplication --dry-run
```

You can supply another tag through a project property:

```shell
./gradlew helmInstallToDatabase --dry-run \
    --project-prop=example.release.tags=database
```
