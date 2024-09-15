# Linux Cluster Monitoring Agent
This project aims to keep an eye on a Linux cluster with several nodes. It gathers hardware specs and live resource usage info from every node in the cluster. The collected data ends up in a PostgreSQL database to analyze and report on. System administrators who need to watch and handle resources across multiple servers are the main users of this project. It uses various technologies, including Bash scripts to automate tasks, Docker to containerize, PostgreSQL to store data, and Git to control versions. This project offers a user-friendly answer to watch and manage server resources in a way that's both scalable and effective.

Architecture and Design

![Cluster drawio](https://github.com/user-attachments/assets/42569286-fc31-4e67-a20e-816f468035c6)


The diagram shows a distributed system with three hosts:

- Host 1 runs a PostgreSQL database managed by the `psql_docker` script in a Docker Container.
- Host 2 runs the `host_info` script to gather system information and send it to the PostgreSQL database.
- Host 3 runs the `host_usage` script to monitor and report resource usage to the PostgreSQL database.

Each host interacts with the central PostgreSQL database on Host 1.

Database & Tables

The database is a PostgreSQL instance where all the collected data, including hardware specifications of each host and resource usage metrics like CPU and memory usage over time, is stored. The database primarily consists of two tables: host_info, which stores static hardware information about each host, and host_usage, which stores dynamic resource usage data collected at regular intervals:

The host_info table is designed to store the static hardware specifications of each host (or server) in the cluster. This information is collected only once, typically during the initial setup of each host. Here are the details of the table:

- id: Primary key, uniquely identifies each host. Auto-incremented by PostgreSQL.
- hostname: Fully qualified domain name (FQDN) of the host, unique identifier within the network.
- cpu_number: Total number of CPUs (cores) available on the host.
- cpu_architecture: CPU architecture (e.g., `x86_64`, `arm64`).
- cpu_model: Specific model of the CPU (e.g., `Intel(R) Xeon(R) CPU @ 2.30GHz`).
- cpu_mhz: Clock speed of the CPU in megahertz (MHz).
- l2_cache: Size of the Level 2 (L2) cache in kilobytes (KB).
- total_mem: Total physical memory (RAM) available on the host, measured in kilobytes (KB).
- timestamp:Time when the hardware information was recorded.

The host_usage table keeps track of resource usage data that changes often. It gathers this information every minute to give a precise and up-to-the-moment look at how a server is performing. This regular updating means that those in charge always have the latest details on how much the CPU, memory, and disk are being used. This helps them make quick and smart choices about managing and improving these resources:

- timestamp: Time when the resource usage data was collected.
- host_id : Foreign key linking to the `id` column in the `host_info` table, identifying which host the usage data belongs to.
- memory_free: Amount of free memory (RAM) available on the host at the time of data collection, measured in megabytes (MB).
- cpu_idle: Percentage of time the CPU was idle during the data collection period.
- cpu_kernel: Percentage of CPU time spent in kernel mode (system processes) during the data collection period.
- disk_io: Number of disk I/O operations per second during the data collection period.
- disk_available: Amount of available disk space, measured in megabytes (MB), at the time of data collection.

Scripts descriptions:

Here’s a more concise version of each script description:

`host_info.sh`: Collects and inserts static hardware information (e.g., CPU, memory) of the host into the `host_info` table in the PostgreSQL database. Typically run once during setup.

`psql_docker.sh`: Manages a PostgreSQL database instance using Docker. It allows you to create, start, stop, and manage the PostgreSQL container easily.

`ddl.sql`: Contains SQL commands to create the `host_info` and `host_usage` tables in the PostgreSQL database. It sets up the database schema required for data storage.

`queries.sql`: A collection of SQL queries used to analyze the data stored in the database, helping to extract insights on CPU, memory usage, and server performance.

 Database and Table Initialization
- Steps:
  1. Provision PostgreSQL Instance: Before running the Bash agents, ensure that the PostgreSQL instance is set up. This involves creating and starting a Docker container that hosts the PostgreSQL database.
  2. Create Database Tables: Once the PostgreSQL container is running, initialize the database by running the `ddl.sql` script, which creates the necessary `host_info` and `host_usage` tables.
- Usage:
  ```bash
  # Start the PostgreSQL container
  ./linux_sql/scripts/psql_docker.sh start db_password             

  # Initialize the database schema
  psql -h localhost -U postgres -d host_agent -W -f sql/ddl.sql
  ```

Host Information Initialization (`host_info.sh`)
- Steps:
  1. Collect Hardware Specs: Run the `host_info.sh` script on each host to gather static hardware information like CPU details and memory capacity. This information is then stored in the `host_info` table.
  2. One-Time Setup: This script is executed only once during the initial setup of each host to ensure the database contains accurate hardware specs.
- Usage:
  ```bash
  #Use this command to insert the hardware's specifications into the host_info table
  ./linux_sql/scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
  ```

Host Usage Data Collection (`host_usage.sh`)
- Steps:
  1. Set Up Automated Data Collection: To monitor resource usage over time, configure a cron job to run the `host_usage.sh` script at regular intervals, such as every minute.
  2. Store Usage Data: The script collects data like CPU idle time and free memory and inserts it into the `host_usage` table for ongoing performance monitoring.
- Usage:
  ```bash
  #Use this command to insert the usage's data into the host_usage table
  ./linux_sql/scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
  ```

Crontab Setup
- Steps:
  1. Schedule the `host_usage.sh` Script: Open your crontab configuration to schedule the `host_usage.sh` script. This ensures that resource usage data is automatically collected and stored in the database every minute.
  2. Verify and Save: After adding the cron job, save the configuration to activate the automated data collection.
- Usage:
  ```bash
  # To open the crontab editor use this commmand
  crontab -e
  
  #add this command to the crontab file to automate the host_usage script:
  * * * * * /path/to/scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password > /tmp/host_usage.log 2>&1

  #Wait few second and verify with this command that the crontab job is created:
  crontab-l

  #To verify if the script is running properly run this command:
  cat /tmp/host_usage.log

  #To lso verify if the cron jobs works every minute use this command and every minute a log will appear:
  tail -f /tmp/host_usage.log

  ```

  Improvements:

  -Modularize Code with Functions: Write reusable functions in your Bash scripts to handle repetitive tasks, such as database connection setup or data insertion. This simplifies the scripts by reducing code repetition, making them easier to maintain and update.
  
  -Add Alert Messages for Key Events: Set up alert messages or logs for key events, such as when a script successfully completes a task or if it fails. This keeps track of script execution, ensuring that you are notified of both successes and failures, which aids in monitoring and troubleshooting.

