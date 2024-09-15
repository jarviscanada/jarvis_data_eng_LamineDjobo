#!/bin/bash


	cmd=$1
container_name=$2
db_username=$3
db_password=$4

	sudo systemctl status docker || sudo systemctl start docker

docker container inspect $container_name
container_status=$?

case $cmd in 
	create)

	if [ $container_status -eq 0 ]; then
		echo"Container '$container_name' already exists"
		exit 1
	fi

	if [ $# -ne 4 ];then
		echo'Create requires container name, username, and password'
		exit 1
	fi

docker volume create ${container_name}_vol

docker run --name $container_name -e POSTGRES_USER=$db_username -e POSTGRES_PASSWORD=$db_password -v ${container_name}_vol:/var/lib/postgresql/data -d -p 5432:5432 postgres

exit $?
;;

start|stop)
	if [ $container_status -ne 0 ]; then 
		echo"Container '$container_name' does not exist"
		exit 1
	fi

docker container $cmd $container_name 
exit $?
;;

	*)

	 echo"Illegal command"
	 echo"Commands: start|stop|create"
	 exit 1
        ;;
esac
