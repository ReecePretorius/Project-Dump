-- Retrieve the five posts with the most votes.

SELECT `PostId` FROM `Vote` INNER JOIN `Post` ON PostId = Post.Id GROUP BY `PostId` ORDER BY COUNT(*) DESC LIMIT 5;
