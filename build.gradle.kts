plugins {
    id("java")
    id("application")
}
group = "com.uce"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javafxVersion = "21.0.1"

dependencies {
    // HikariCP y Lombok como ya lo tienes
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.projectlombok:lombok:1.18.38")

    // JavaFX para Linux (classifier = "linux")
    listOf("base", "controls", "graphics", "fxml").forEach { module ->
        implementation("org.openjfx:javafx-$module:$javafxVersion:linux")
    }

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // Cambia esto al nombre real de tu clase principal
    mainClass.set("com.uce.Main")
}



