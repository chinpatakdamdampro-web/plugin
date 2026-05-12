plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.aliucord.plugin) apply true
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    // Only apply if this is actually a plugin (has src/main)
    if (project.file("src/main").exists()) {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.android")
        apply(plugin = "com.aliucord.plugin")
    }
}
