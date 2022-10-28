docker_username=""
image_name="main-server"
container_name="main-server"
port=8080

echo "Automation docker build and run"

echo "=> Stop and Remove previous container"
docker stop ${container_name}
docker rm ${container_name}

echo "=> Build container"
docker build -t main-server:$1 .

#echo "=> Remove none container"
#docker rmi '$(docker images -f "dangling=true" -q)'

echo "=> Run container"
docker run --name main-server -p 8080:8080 -d main-server:$1

