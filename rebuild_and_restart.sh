cd bookmarks-manager
mvn clean install
cd ..
cd uri-shortner
mvn clean install
cd ..
cd tagging-service
mvn clean install
cd ..
docker-compose build
docker-compose up
