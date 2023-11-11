-- Retrieve by name and frequency the twenty unique Badges that have been awarded the most often after 2019.

SELECT `Name`, COUNT(`Name`) AS `Frequency` FROM `Badge` WHERE `Date` >= '2020-01-01' GROUP BY `Name` ORDER BY `Frequency` DESC LIMIT 20;
