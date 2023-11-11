-- Retrieve the display names of every user
-- who has received the Badge that has been
-- awarded the most times, excluding those badges
-- that have been awarded over ten thousand times.
-- Order the result in descending order.

SELECT `DisplayName` FROM `User` LEFT OUTER JOIN `Badge` ON User.Id = UserId 
WHERE Badge.Name = (SELECT `Name` FROM `Badge` GROUP BY `Name` ORDER BY COUNT(`Name`) DESC LIMIT 6,1)
GROUP BY `DisplayName` ORDER BY `DisplayName` DESC;
