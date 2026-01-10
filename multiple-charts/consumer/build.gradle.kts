plugins {
    id("io.github.build-extensions-oss.helm-releases")
}

helm {
    releases {
        create("test") {
            from(chart(project = ":producer", chart = "myChart"))
            // defaults target "default" -> helmInstallTestToDefault
        }
    }
}