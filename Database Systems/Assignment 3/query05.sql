-- Retrieve by name and frequency and ordered by increasing name
-- those unique Badges that have been awarded at least five times,
-- but never before 2014 and never after 2019.

SELECT `Name`, COUNT(`Name`) AS `Frequency` FROM `Badge`
WHERE DATE(Date) BETWEEN '2013-12-31' AND '2019-12-31'
AND `Name` NOT IN (SELECT DISTINCT `Name` FROM `Badge` WHERE DATE(Date) < '2014-01-01')
AND `Name` NOT IN (SELECT DISTINCT `Name` FROM `Badge` WHERE DATE(Date) >= '2020-01-01')
GROUP BY `Name` HAVING COUNT(*) >= 5
ORDER BY `Name` ASC;
