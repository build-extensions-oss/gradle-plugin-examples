plugins {
    id("io.github.build-extensions-oss.helm-releases") version "3.2.0"
}

helm {
    releases {
        create("backend") {
            from("example/backend")

            tags(
                setOf("application")
            )
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
