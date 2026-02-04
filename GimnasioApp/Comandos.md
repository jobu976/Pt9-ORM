
mvn clean compile dependency:copy-dependencies -DskipTests -q


java -cp "target\classes;target\dependency\*" com.gimnasio.app.GimnasioApp
