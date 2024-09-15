
	psql_host=$1
	psql_port=$2
	db_name=$3
	psql_user=$4
	psql_password=$5


	if [ "$#" -ne 5 ];then
		echo "Illegal number of parameters"
		exit 1

	fi

	export PGPASSWORD=$psql_password

	vmstat_mb=$(vmstat --unit M)
	hostname=$(hostname -f)


	memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -1| xargs)
	cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}'| tail -1| xargs)
	cpu_kernel=$(echo "$vmstat_mb"| awk '{print $14}'| tail -1 | xargs)
	disk_io=$(vmstat -d | awk '{print $10}'|tail -1 | xargs)
	disk_available=$(df -BM / | awk 'NR==2 {print $4}' | sed 's/[^0-9]//g')


	timestamp=$(date '+%Y-%m-%d %H:%M:%S')



	host_id=$(psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -t -c "SELECT id FROM host_info WHERE hostname='$hostname';"| xargs)

	echo "Host ID: $host_id" 
	echo "$hostname"

	insert_stmt="INSERT INTO host_usage (timestamp,memory_free,cpu_idle,cpu_kernel,disk_io,disk_available,host_id) VALUES ('$timestamp','$memory_free','$cpu_idle','$cpu_kernel','$disk_io','$disk_available',$host_id);"


	psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
	
	exit #?




