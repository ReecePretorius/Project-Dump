-- Retrieve the post id of all posts before July 2010 that
-- have never been linked to, ordered descending.

SELECT Post.Id AS `Id` FROM `Post` LEFT OUTER JOIN `Link` ON Post.Id = Link.PostId WHERE Post.CreationDate < '2010-07-01' AND Link.PostId IS NULL ORDER BY `Id` DESC;
