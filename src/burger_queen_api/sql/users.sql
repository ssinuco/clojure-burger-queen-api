-- :name select-users :? :*
-- :doc Get users
SELECT * 
FROM users 
ORDER BY id ASC 
LIMIT :limit 
OFFSET :offset

-- :name select-user :? :1
-- :doc Get a user
SELECT * 
FROM users
WHERE id = :id

-- :name insert-user
-- :doc Insert a new user
-- :command :insert
INSERT INTO users (email, admin)
VALUES (:email, :admin)

-- :name update-user
-- :doc Update a user
UPDATE users
SET email = :email, admin = :admin
WHERE id = :id

-- :name delete-user
-- :doc Delete a user
DELETE FROM users WHERE id = :id
