plugins {
    id("io.github.build-extensions-oss.helm")
}

version = "0.1.0"

helm {
    charts {
        create("myChart") {
            chartName.set("my-chart")
            chartVersion.set(project.version.toString())
            sourceDir = file("src/main/helm")
        }
    }
}