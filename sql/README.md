# Introduction

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
###### Question 1: Show all members 

```sql
SELECT *
FROM cd.members
```

###### Questions 2: Lorem ipsum...

```sql
SELECT blah blah 
```

