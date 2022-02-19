-- :name select-orders
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
