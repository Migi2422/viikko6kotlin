import java.io.FileInputStream
import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    val properties = Properties()
    properties.load(FileInputStream(localPropertiesFile))
    properties.forEach { key, value ->
        rootProject.extra.set(key.toString(), value.toString())
    }
}
