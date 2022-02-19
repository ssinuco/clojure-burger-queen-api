-- :name select-users :? :*
-- :doc Get users
SELECT * 
FROM users 
ORDER BY id ASC 
LIMIT :limit 
OFFSET :offset
