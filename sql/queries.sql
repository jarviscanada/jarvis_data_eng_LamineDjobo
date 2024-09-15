INSERT INTO cd.facilities 
VALUES 
  (9, 'Spa', 20, 30, 100000, 800);

INSERT INTO facilities (
  name, membercost, guestcost, initialoutlay, 
  monthlymaintenance
) 
VALUES 
  ('Spa', 20, 30, 100000, 800);

update 
  cd.facilities 
set 
  initialoutlay = 10000 
where 
  facid = 1;

UPDATE 
  facilities 
SET 
  membercost = membercost * 1.10, 
  guestcost = guestcost * 1.10 
WHERE 
  name = 'Tennis Court 2' 
  AND membercost = (
    SELECT 
      membercost 
    FROM 
      facilities 
    WHERE 
      name = 'Tennis Court 1'
  ) 
  AND guestcost = (
    SELECT 
      guestcost 
    FROM 
      facilities 
    WHERE 
      name = 'Tennis Court 1'
  );

DELETE FROM 
cd.bookings;




DELETE FROM 
  cd.members 
where 
  memid = 37;

SELECT 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
FROM 
  facilities 
WHERE 
  membercost > 0 
  AND membercost < (monthlymaintenance / 50);

select 
  * 
from 
  cd.facilities 
where 
  name like '%Tennis%';

select 
  * 
from 
  cd.facilities 
where 
  facid in (1, 5);

select 
  memid, 
  surname, 
  firstname, 
  joindate 
from 
  cd.members 
where 
  joindate >= '2012-09-01';





select 
  surname 
from 
  cd.members 
union 
select 
  name 
from 
  cd.facilities;

SELECT 
  b.starttime 
FROM 
  bookings b 
  JOIN members m ON b.memberid = m.memberid 
WHERE 
  m.firstname = 'David' 
  AND m.surname = 'Farrell';

SELECT 
  b.starttime AS start, 
  f.name AS name 
FROM 
  cd.facilities f 
  INNER JOIN cd.bookings b ON f.facid = b.facid 
WHERE 
  f.name IN (
    'Tennis Court 2', 'Tennis Court 1'
  ) 
  AND b.starttime :: date = '2012-09-21' 
ORDER BY 
  b.starttime;

SELECT 
  mems.firstname AS memfname, 
  mems.surname AS memsname, 
  recs.firstname AS recfname, 
  recs.surname AS recsname 
FROM 
  cd.members mems 
  LEFT JOIN cd.members recs ON recs.memid = mems.recommendedby 
ORDER BY 
  memsname, 
  memfname;

SELECT 
  DISTINCT recs.firstname AS firstname, 
  recs.surname AS surname 
FROM 
  cd.members recs 
  JOIN cd.members mems ON recs.memid = mems.recommendedby 
ORDER BY 
  recs.surname, 
  recs.firstname;

select 
  distinct mems.firstname || ' ' || mems.surname as member, 
  (
    select 
      recs.firstname || ' ' || recs.surname as recommender 
    from 
      cd.members recs 
    where 
      recs.memid = mems.recommendedby
  ) 
from 
  cd.members mems 
order by 
  member;

select 
  recommendedby, 
  count(*) 
from 
  cd.members 
where 
  recommendedby is not null 
group by 
  recommendedby 
order by 
  recommendedby;


select 
  facid, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
group by
facid 
order by 
  facid;

select 
  facid, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
where 
  starttime >= '2012-09-01' 
  and starttime < '2012-10-01' 
group by 
  facid 
order by 
  sum(slots);

select 
  facid, 
  extract(
    month 
    from 
      starttime
  ) as month, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
where 
  extract(
    year 
    from 
      starttime
  ) = 2012 
group by 
  facid, 
  month 
order by 
  facid, 
  month;

select 
  count(distinct memid) 
from 
  cd.bookings;

select 
  mems.surname, 
  mems.firstname, 
  mems.memid, 
  min(bks.starttime) as starttime 
from 
  cd.bookings bks 
  inner join cd.members mems on mems.memid = bks.memid 
where 
  starttime >= '2012-09-01' 
group by 
  mems.surname, 
  mems.firstname, 
  mems.memid 
order by 
  mems.memid;

select 
  count(*) over(), 
  firstname, 
  surname 
from 
  cd.members 
order by 
  joindate;

select 
  row_number() over(
    order by 
      joindate
  ), 
  firstname, 
  surname 
from 
  cd.members 
order by 
  joindate;

select 
  facid, 
  total 
from 
  (
    select 
      facid, 
      sum(slots) total, 
      rank() over (
        order by 
          sum(slots) desc
      ) rank 
    from 
      cd.bookings 
    group by 
      facid
  ) as ranked 
where 
  rank = 1;

select 
  surname || ', ' || firstname as name 
from 
  cd.members;

select 
  memid, 
  telephone 
from 
  cd.members 
where 
  telephone ~ '[()]';

select 
  substr (mems.surname, 1, 1) as letter, 
  count(*) as count 
from 
  cd.members mems 
group by 
  letter 
order by 
  letter;

