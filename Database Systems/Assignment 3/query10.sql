-- Retrieve the postid and display name of the user who posted it
-- for *all* posts that have linked to at least twenty other posts,
-- ordered by postId.

SELECT Post.Id AS `PostId`, `DisplayName` FROM `User` LEFT OUTER JOIN `Post` ON User.Id = OwnerUserId
LEFT OUTER JOIN `Link` ON Post.Id = Link.PostId GROUP BY Post.Id, `DisplayName` HAVING COUNT(Link.PostId) >= 20;
