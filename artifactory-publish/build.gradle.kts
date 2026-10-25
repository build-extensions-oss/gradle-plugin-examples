plugins {
    id("io.github.build-extensions-oss.helm") version "3.3.0"
    id("io.github.build-extensions-oss.helm-publish") version "3.3.0"
}

version = "0.0.1"

helm {
    charts {
        create("main") {
            sourceDir.set(file("src/main/helm"))
            // this is default option - however let's have it explicit
            publish = true
        }
    }
    publishing {
        repositories {
            artifactory {
                url = uri("http://localhost:8080/artifactory/folder1/folder2")

                credentials {
                    username = "testUserName"
                    password = "testPassword"
                }
            }
        }
    }
}