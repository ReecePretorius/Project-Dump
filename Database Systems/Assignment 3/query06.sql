-- Retrieve the display name of all users who have
-- posted at least one post, ordered ascending.

SELECT DISTINCT `DisplayName` FROM `User` INNER JOIN `Post` ON User.Id = Post.OwnerUserId ORDER BY `DisplayName` ASC;
