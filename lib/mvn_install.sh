# installing MediaChest library to local repository
mvn install:install-file -Dfile=./MediaChest.jar -DgroupId=mediachest -DartifactId=mediachest -Dversion=2.20 -Dpackaging=jar

# installing mediautil library to local repository
mvn install:install-file -Dfile=./mediautil.jar -DgroupId=mediautil -DartifactId=mediautil -Dversion=2.20 -Dpackaging=jar
