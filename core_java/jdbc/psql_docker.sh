#!/bin/bash

cmd=$1
db_username=$2
db_password=$3

# Check OS type
os_type=$(uname)

# Function to check Docker status
check_docker_status() {
  if [ "$os_type" == "Linux" ]; then
    # On Linux, use systemctl to check Docker service status
    sudo systemctl status docker > /dev/null 2>&1
    if [ $? -ne 0 ]; then
      echo "Docker is not running. Trying to start Docker..."
      sudo systemctl start docker
      if [ $? -ne 0 ]; then
        echo "Failed to start Docker. Please check Docker installation."
        exit 1
      fi
    fi
  elif [ "$os_type" == "Darwin" ]; then
    # On macOS, use docker info to check if Docker Desktop is running
    if ! docker info > /dev/null 2>&1; then
      echo "Docker is not running. Please start Docker Desktop."
      exit 1
    fi
  else
    echo "Unsupported OS: $os_type"
    exit 1
  fi
}

# Check Docker status based on OS
check_docker_status

# Get container name from the user
read -p "Please enter the container name (default is 'jrvs-psql'): " container_name
container_name=${container_name:-jrvs-psql}  # Default to jrvs-psql if no name is provided

# Check if the container already exists
docker container inspect $container_name > /dev/null 2>&1
container_status=$?

case $cmd in
  create)
    if [ $container_status -eq 0 ]; then
      echo "Container '$container_name' already exists. Please use a different name or manage it with start/stop."
      exit 1
    fi

    if [ $# -ne 3 ]; then
      echo "Create requires username and password."
      exit 1
    fi

    # Ask the user for the port number if creating a new container
    read -p "Please enter a port number for the new container (default is 5432): " port
    port=${port:-5432}

    # Create Docker volume and run the container with the specified name and port
    docker volume create pgdata
    docker run --name $container_name -e POSTGRES_USER=$db_username -e POSTGRES_PASSWORD=$db_password -d -v pgdata:/var/lib/postgresql/data -p $port:5432 postgres:9.6-alpine
    exit $?
    ;;

  start|stop)
    if [ $container_status -ne 0 ]; then
      echo "Container '$container_name' does not exist."
      exit 1
    fi
    # Start or stop the specified container
    docker container $cmd $container_name
    exit $?
    ;;

  *)
    echo "Illegal command"
    echo "Commands: start|stop|create"
    exit 1
    ;;
esac
	
