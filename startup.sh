#stop already started containers
jetty=$(docker ps -q --filter="ancestor=dev11_podgorny/jetty")
mysql=$(docker ps -q --filter="ancestor=dev11_podgorny/mysql")

if [ ! -z "$jetty" ]; then
  docker stop $jetty
fi

if [ ! -z "$mysql" ]; then
  docker stop $mysql
fi
#create images
docker build -f ./src/main/resources/docker/jetty.dockerfile -t dev11_podgorny/jetty ./src/main/resources/docker/
docker build -f ./src/main/resources/docker/mysql.dockerfile -t dev11_podgorny/mysql ./src/main/resources/docker/
#run images
echo "Starting MySQL"
docker run --name p_mysql -d --rm -p 3306:3306 dev11_podgorny/mysql
echo "Starting JETTY"
docker run --name p_jetty -u jetty --rm -p 8888:8080 -p 8883:8443 --link $(docker ps -q --filter="ancestor=dev11_podgorny/mysql"):mysql dev11_podgorny/jetty
