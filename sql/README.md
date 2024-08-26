# Introduction

This project is designed to help you learn and practice SQL by setting up and interacting with a PostgreSQL database. It involves loading a pre-defined database schema and data, and then testing your SQL skills by answering about 30 different queries related to the data. The database is organized into several tables within a schema named cd, which manages information about club facilities, members, and bookings.

# Quick Start

Before you begin, make sure you have the following installed:

Docker (for running PostgreSQL in a container)
PostgreSQL client (psql)
Git (for cloning the repository)

Step 1: Clone the Repository
Start by cloning the project repository from GitHub, which contains the psql.sh script and the clubdata.sql file.

```bash
git clone https://github.com/jarviscanada/jarvis_data_eng_LamineDjobo.git
cd jarvis_data_eng_LamineDjobo
```

Step 2: Start or Create the PostgreSQL Database
Use the psql.sh script to either create a new PostgreSQL instance or start an existing one.

To create a new PostgreSQL container:

```bash
./psql.sh create <container_name> <db_username> <db_password>
```

Replace <container_name>, <db_username>, and <db_password> with your desired container name, database username, and password.

To start an existing PostgreSQL container:

```bash
./psql.sh start <container_name>
```
Replace <container_name> with the name of your existing container.

Step 3: Connect to the Database
1.Access the PostgreSQL Database:
```bash
docker exec -it pgsql-container psql -U postgres -d postgres
```
2.Verify the loaded data:
After connecting to the database, you can verify the data by listing the tables:
```sql
\dt cd.*
SELECT * FROM cd.members
```
This will list all tables in the cd schema and the second one will list all the members from the table cd.members. You can now test locally and try the queries below.


# SQL Queries

###### Table Setup (DDL)
The order in which tables are created is important when foreign keys (FK) are involved. To ensure all foreign key references are valid, follow this order:

Create the referenced tables (parents) first:

Tables that are referenced by foreign keys in other tables should be created first. This ensures that references are valid when foreign keys are defined.
Then create the tables containing foreign keys (children):

Tables containing foreign keys should be created after the referenced tables.

```sql-- 
1. Create the facilities table first CREATETABLE cd.facilities (
    facid INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    membercost NUMERIC NOT NULL,
    guestcost NUMERIC NOT NULL,
    initialoutlay NUMERIC NOT NULL,
    monthlymaintenance NUMERIC NOT NULL
);

-- 2. Create the members table CREATE TABLE cd.members (
    memid INTEGER PRIMARY KEY NOT NULL,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    address VARCHAR(300) NOT NULL,
    zipcode INTEGER NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    joindate TIMESTAMP NOT NULL,
    recommendedby INTEGER,
    CONSTRAINT fk_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid)
);

-- 3. Create the bookings table that references facilities and members CREATE TABLE cd.bookings (
    bookid INTEGER PRIMARY KEY NOT NULL,
    zslots INTEGER NOT NULL,
    starttime TIMESTAMP NOT NULL,
    memid INTEGER NOT NULL,
    facid INTEGER NOT NULL,
    CONSTRAINT fk_memid FOREIGN KEY (memid) REFERENCES cd.members(memid),
    CONSTRAINT fk_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid)
);
```
###### Question 1: Insert a new facility into the facilities table

```sql
INSERT INTO facilities (
  name, membercost, guestcost, initialoutlay, 
  monthlymaintenance
) 
VALUES 
  ('Spa', 20, 30, 100000, 800);
```

###### Question 2: Update the initial outlay for a specific facility

```sql
UPDATE cd.facilities 
SET initialoutlay = 10000 
WHERE facid = 1;
```

###### Question 3: Increase the cost of the second tennis court by 10% based on the first tennis court

```sql
UPDATE facilities 
SET membercost = membercost * 1.10, 
    guestcost = guestcost * 1.10 
WHERE name = 'Tennis Court 2' 
AND membercost = (
    SELECT membercost 
    FROM facilities 
    WHERE name = 'Tennis Court 1'
) 
AND guestcost = (
    SELECT guestcost 
    FROM facilities 
    WHERE name = 'Tennis Court 1'
);
```

###### Question 4: Delete all records from the bookings table

```sql
DELETE FROM cd.bookings;
```

###### Question 5: Delete a specific member by ID

```sql
DELETE FROM cd.members 
WHERE memid = 37;
```

###### Question 6: List facilities with a member cost less than 1/50th of the monthly maintenance

```sql
SELECT facid, name, membercost, monthlymaintenance 
FROM facilities 
WHERE membercost > 0 
AND membercost < (monthlymaintenance / 50);
```

###### Question 7: Show all facilities that contain 'Tennis' in the name

```sql
SELECT * 
FROM cd.facilities 
WHERE name LIKE '%Tennis%';
```

###### Question 8: Show details of specific facilities based on their IDs

```sql
SELECT * 
FROM cd.facilities 
WHERE facid IN (1, 5);
```

###### Question 9: List members who joined after September 1st, 2012

```sql
SELECT memid, surname, firstname, joindate 
FROM cd.members 
WHERE joindate >= '2012-09-01';
```

###### Question 10: Combine surnames from the members table with names from the facilities table

```sql
SELECT surname 
FROM cd.members 
UNION 
SELECT name 
FROM cd.facilities;
```

###### Question 11: List the start times of bookings for a specific member ('David Farrell')

```sql
SELECT b.starttime 
FROM bookings b 
JOIN members m ON b.memberid = m.memberid 
WHERE m.firstname = 'David' 
AND m.surname = 'Farrell';
```

###### Question 12: List start times for bookings of tennis courts on a specific date

```sql
SELECT b.starttime AS start, 
       f.name AS name 
FROM cd.facilities f 
INNER JOIN cd.bookings b ON f.facid = b.facid 
WHERE f.name IN ('Tennis Court 2', 'Tennis Court 1') 
AND b.starttime::date = '2012-09-21' 
ORDER BY b.starttime;
```

###### Question 13: List members and their recommenders (if any)

```sql
SELECT mems.firstname AS memfname, 
       mems.surname AS memsname, 
       recs.firstname AS recfname, 
       recs.surname AS recsname 
FROM cd.members mems 
LEFT JOIN cd.members recs ON recs.memid = mems.recommendedby 
ORDER BY memsname, memfname;
```

###### Question 14: List members who have recommended another member

```sql
SELECT DISTINCT recs.firstname AS firstname, 
                recs.surname AS surname 
FROM cd.members recs 
JOIN cd.members mems ON recs.memid = mems.recommendedby 
ORDER BY recs.surname, recs.firstname;
```

###### Question 15: List members with their recommenders in a single string

```sql
SELECT DISTINCT mems.firstname || ' ' || mems.surname AS member, 
                (SELECT recs.firstname || ' ' || recs.surname 
                 FROM cd.members recs 
                 WHERE recs.memid = mems.recommendedby) AS recommender 
FROM cd.members mems 
ORDER BY member;
```

###### Question 16: Count the number of recommendations made by each member

```sql
SELECT recommendedby, 
       COUNT(*) 
FROM cd.members 
WHERE recommendedby IS NOT NULL 
GROUP BY recommendedby 
ORDER BY recommendedby;
```

###### Question 17: List the total number of slots booked per facility

```sql
SELECT facid, 
       SUM(slots) AS "Total Slots" 
FROM cd.bookings 
GROUP BY facid 
ORDER BY facid;
```

###### Question 18: List the total number of slots booked per facility in September 2012

```sql
SELECT facid, 
       SUM(slots) AS "Total Slots" 
FROM cd.bookings 
WHERE starttime >= '2012-09-01' 
AND starttime < '2012-10-01' 
GROUP BY facid 
ORDER BY SUM(slots);
```

###### Question 19: List the total number of slots booked per facility per month in 2012

```sql
SELECT facid, 
       EXTRACT(MONTH FROM starttime) AS month, 
       SUM(slots) AS "Total Slots" 
FROM cd.bookings 
WHERE EXTRACT(YEAR FROM starttime) = 2012 
GROUP BY facid, month 
ORDER BY facid, month;
```

###### Question 20: Count the distinct number of members who made bookings

```sql
SELECT COUNT(DISTINCT memid) 
FROM cd.bookings;
```

###### Question 21: List each member's first booking after September 1st, 2012

```sql
SELECT mems.surname, 
       mems.firstname, 
       mems.memid, 
       MIN(bks.starttime) AS starttime 
FROM cd.bookings bks 
INNER JOIN cd.members mems ON mems.memid = bks.memid 
WHERE starttime >= '2012-09-01' 
GROUP BY mems.surname, mems.firstname, mems.memid 
ORDER BY mems.memid;
```

###### Question 22: List the total number of members

```sql
SELECT COUNT(*) OVER(), 
       firstname, 
       surname 
FROM cd.members 
ORDER BY joindate;
```

###### Question 23: Assign row numbers to members based on their join date

```sql
SELECT ROW_NUMBER() OVER(ORDER BY joindate) AS row_num, 
       firstname, 
       surname 
FROM cd.members 
ORDER BY joindate;
```

###### Question 24: Identify the facility with the highest number of bookings

```sql
SELECT facid, 
       total 
FROM (SELECT facid, 
             SUM(slots) AS total, 
             RANK() OVER (ORDER BY SUM(slots) DESC) AS rank 
      FROM cd.bookings 
      GROUP BY facid) AS ranked 
WHERE rank = 1;
```

###### Question 25: Format member names as "surname, firstname"

```sql
SELECT surname || ', ' || firstname AS name 
FROM cd.members;
```

###### Question 26: List members with telephone numbers containing specific characters

```sql
SELECT memid, 
       telephone 
FROM cd.members 
WHERE telephone ~ '[()]';
```

###### Question 27: Count members grouped by the first letter of their surname

```sql
SELECT SUBSTR(mems.surname, 1, 1) AS letter, 
       COUNT(*) AS count 
FROM cd.members mems 
GROUP BY letter 
ORDER BY letter;
```

