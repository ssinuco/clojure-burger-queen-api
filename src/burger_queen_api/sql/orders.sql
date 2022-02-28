-- :name select-orders-hugsql
-- :doc Get orders
-- :command plan
SELECT orders.*, products.*, orders_products.qty, orders_products.productId 
FROM ( 
      SELECT * 
      FROM orders 
      ORDER BY id ASC 
      LIMIT :limit OFFSET :offset
      ) AS orders 
INNER JOIN orders_products ON orders.id = orders_products.orderId 
INNER JOIN products ON products.id = orders_products.productId

-- :name insert-order
-- :doc Insert a new order
-- :command :insert
INSERT INTO orders (userId, client, status, dateEntry) 
VALUES (:userId, :client, "pending", CURRENT_TIMESTAMP)

-- :name insert-order-product
-- :doc Insert a new product for a order
-- :command :insert
INSERT INTO orders_products (orderId, productId, qty)
VALUES (:orderId, :productId, :qty)

-- :name update-order
-- :doc Update a order
UPDATE orders
SET status = :status
WHERE id = :id

-- :name delete-order
-- :doc Delete a order
DELETE FROM orders WHERE id = :id

-- :name delete-order-products
-- :doc Delete products from a order
DELETE FROM orders_products WHERE orderId = :orderId
