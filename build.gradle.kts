plugins {
    id("java")
}

group = "com.uce"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.projectlombok:lombok:1.18.38")
    //falta agregarle el driver para conectar a la base de datos xdxdxd
}

tasks.test {
    useJUnitPlatform()
}