-- Retrieve the name and count of the Badge awarded
-- the second-most number of times.

SELECT `Name`, COUNT(`Name`) AS `Frequency` FROM `Badge` GROUP BY `Name` ORDER BY `Frequency` DESC LIMIT 1,1;
