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
}

rootProject.name = "Music"
include(":app")
include(":player_screen")
include(":domain")
include(":components")
include(":utils")
include(":scopes")
include(":home_screen")
include(":search_screen")
include(":notifications")
include(":registration_flow")
include(":registration_flow")
