(ns burger-queen-api.sql.orders
  (:require [clojure.data.json :as json]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as adapter]))

(def orderKeys [:orders/id :orders/client :orders/status :orders/userId :orders/dateEntrym :orders/dateProcessed])

(def productKeys [:orders_products/productId :products/name :products/image :products/price :products/image :orders_products/qty])

(def sqlOrders "SELECT orders.*, products.*, orders_products.qty, orders_products.productId 
FROM( SELECT * FROM orders ORDER BY id ASC LIMIT ? OFFSET ?) AS orders 
INNER JOIN orders_products ON orders.id = orders_products.orderId 
INNER JOIN products ON products.id = orders_products.productId")

(defn order-map
  [row]
  {
   (:orders/id row) 
   (into 
    (select-keys row orderKeys) 
    {:products [(select-keys row productKeys)]})})

(defn select-orders
  [ds page limit]
  (vals 
   (reduce
    (fn [accumulator, row]
      (merge-with 
       #(update-in %1 [:products] conj (first (:products %2))) 
       accumulator
       (order-map row)
       ))
    {}
    (jdbc/plan ds [sqlOrders limit (* limit (dec page))]))))
