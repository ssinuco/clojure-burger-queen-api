(ns burger-queen-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.data.json :as json]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))
(def db
  {:dbtype "sqlite"
   :dbname "db/database.db"})

(def ds (jdbc/get-datasource db))

(defn query-users
  []
  (jdbc/execute! ds ["select * from users"]))

(defn query-products
  []
  (jdbc/execute! ds ["select * from products"]))

(def orderKeys [:orders/id :orders/client :orders/status :orders/userId :orders/dateEntrym :orders/dateProcessed])

(def productKeys [:orders_products/productId :products/name :products/image :products/price :products/image :orders_products/qty])

(def sqlOrders "SELECT orders.*, products.*, orders_products.qty, orders_products.productId FROM orders INNER JOIN orders_products ON orders.id = orders_products.orderId INNER JOIN products ON products.id = orders_products.productId")

(defn query-orders
  []
  (vals 
   (reduce
    (fn [obj, row]
      (merge-with 
       #(update-in %1 [:products] conj (first (:products %2))) 
       obj 
       {
        (:orders/id row) 
        (into 
         (select-keys row orderKeys) 
         {:products [(select-keys row productKeys)]})}))
    {}
    (jdbc/plan ds [sqlOrders]))))


(defn list-users
  [req]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (query-users))})

(defn list-products
  [req]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (query-products))})

(defn list-orders
  [req]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (query-orders))})

(defroutes app-routes
  (GET "/users" [] list-users)
  (GET "/orders" [] list-orders)
  (GET "/products" [] list-products)
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
