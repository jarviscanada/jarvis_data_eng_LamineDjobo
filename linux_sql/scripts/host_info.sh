	#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

export PGPASSWORD=$psql_password   


hostname=$(hostname -f)
lscpu_out=$(lscpu)

cpu_number=$(echo "$lscpu_out" | grep '^CPU(s):' | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | grep 'Architecture' | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | awk -F ': ' '/Model name/ {print $2}' | xargs)
cpu_mhz=$(grep 'cpu MHz' /proc/cpuinfo | head -n 1 | awk '{print $4}')
l2_cache=$(echo "$lscpu_out" | grep 'L2 cache' | awk '{print $3}' | sed 's/K//' | xargs)
total_mem=$(vmstat --unit M | awk 'NR==3 {print $3}' | xargs)


timestamp=$(date '+%Y-%m-%d %H:%M:%S')

insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_MHz, l2_cache, total_mem, timestamp) VALUES ('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '${cpu_MHz:-2300}', '$l2_cache', '$total_mem', '$timestamp');"

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
