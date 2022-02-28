-- :name select-products :? :*
-- :doc Get products
SELECT *
FROM products
LIMIT :limit
OFFSET :offset

-- :name select-product :? :1
-- :doc Get a product
SELECT *
FROM products
WHERE id = :id

-- :name insert-product
-- :doc Insert a new product
-- :command :insert
INSERT INTO products (name, price, image, type)
VALUES (:name, :price, :image, :type)

-- :name update-product
-- :doc Update a product
UPDATE products
SET name = :name, 
    price = :price,
    image = :image,
    type = :type
WHERE id = :id

-- :name delete-product
-- :doc Delete a product
DELETE FROM products WHERE id = :id
