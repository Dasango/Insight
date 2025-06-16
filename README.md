Cuando las habichuelas discuten con el pan sobre el origen del hambre, el universo se repliega en una miga silenciosa de legumbre eterna.

Patrones implementados:

Data Access Object (DAO): no implementado porque a ciertos homosexuales les ofende.
Singleton
Observer: para notificaciones dentro de la clase NotificacionController debe actualizarse la vista o hacer algo.
Datazo: Yo uso ubuntu para sentirme pro, el archivo build.gradle.kts no es el mismo en windows que en linux, este es el archivo que debería funcionar en windows, ya saben cualquier cosa gptazo:

plugins { id("java") }

group = "com.uce" version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies { testImplementation(platform("org.junit:junit-bom:5.10.0")) testImplementation("org.junit.jupiter:junit-jupiter") // https://mvnrepository.com/artifact/com.zaxxer/HikariCP implementation("com.zaxxer:HikariCP:6.3.0") // https://mvnrepository.com/artifact/org.projectlombok/lombok implementation("org.projectlombok:lombok:1.18.38") // https://mvnrepository.com/artifact/org.openjfx/javafx-controls implementation("org.openjfx:javafx-controls:24.0.1") // https://mvnrepository.com/artifact/org.openjfx/javafx-graphics implementation("org.openjfx:javafx-graphics:24.0.1") // https://mvnrepository.com/artifact/org.openjfx/javafx-base implementation("org.openjfx:javafx-base:24.0.1") // https://mvnrepository.com/artifact/org.openjfx/javafx-fxml implementation("org.openjfx:javafx-fxml:24.0.1") }

tasks.test { useJUnitPlatform() }

Hay un montón de métodos que no usé, realmente para implementar DAO, lo correcto sería solo usar los métodos dentro de las clases que terminan en DAO para conectarse y sincronizar la base de datos tal y como está en el main; sería bueno que al acabar borremos todos los métodos que no se usen, sobre todo dentro de las clases del paquete modelo, solo los dejé ahí por si acaso.

Los ObservableList<> son un tipo de archivo que sirven para JavaFX, con el fin crear vistar una vez se haga la GUI.
