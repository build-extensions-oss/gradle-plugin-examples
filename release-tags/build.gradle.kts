plugins {
    id("io.github.build-extensions-oss.helm-releases") version "3.1.2"
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

            tags(releaseTags.get())
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
