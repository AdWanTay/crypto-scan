pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    dependencyResolutionManagement {
        dependencyResolutionManagement {
            versionCatalogs {
                create("libs") {
                    from(files("gradle/libs.toml"))
                }
            }
        }
    }
}

rootProject.name = "Crypto Monitor"
include(":app")
 