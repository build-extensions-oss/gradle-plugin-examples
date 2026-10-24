plugins {
    id("io.github.build-extensions-oss.helm-releases") version "3.2.1-beta2"
}

val releaseTags = providers.gradleProperty("example.release.tags")
    .map { value ->
        value.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()
    }
    .orElse(setOf("application"))

helm {
    releases {
        create("backend") {
            from("example/backend")

            // Since 3.2.1-beta2, tags is a SetProperty<String>. A Provider can be
            // wired into it without resolving the project property eagerly.
            tags.set(releaseTags)
        }
    }

    releaseTargets {
        create("application") {
            selectTags = "application"
        }
        create("database") {
            selectTags = "database"
        }
    }
}
