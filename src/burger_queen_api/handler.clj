(ns burger-queen-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [clojure.data.json :as json]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as adapter]))
(def db
  {:dbtype "sqlite"
   :dbname "db/database.db"})

(hugsql/def-db-fns "burger_queen_api/sql/users.sql" {:adapter (adapter/hugsql-adapter-next-jdbc)})
(hugsql/def-db-fns "burger_queen_api/sql/products.sql" {:adapter (adapter/hugsql-adapter-next-jdbc)})
(comment 
  (hugsql/def-db-fns "burger_queen_api/sql/orders.sql" {:adapter (adapter/hugsql-adapter-next-jdbc)}))

(def ds (jdbc/get-datasource db))

(defn query-users
  [page limit]  
  (let [offset (* limit (dec page))]    
    (select-users ds {:limit  limit :offset offset})))

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

(defn query-orders
  [page limit]
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


(defn list-users
  [{page :page limit :limit :or {page 1 limit 10}}]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (query-users page limit))})

(defn post-product
  [{product :body}]
  (let []
    {
     :status 201
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id ((keyword "last_insert_rowid()") (first (insert-product ds product)))})}))

(defn get-products
  [{page :page limit :limit offset :offset :or {page 1 limit 10 offset 0}}]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (select-products ds {:limit limit :offset :offset}))})

(defn put-product
  [{product :body {id :id} :params}]
  (let [result (update-product ds (assoc product :id id))]
    {
     :status 200
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id id})}))

(defn del-product
  [{product :body {id :id} :params}]
  (let [result (delete-product ds {:id id})]
    {
     :status 200
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id id})}))


(defn list-orders
  [{page :page limit :limit :or {page 1 limit 10}}]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (query-orders page limit))})

(defn wrap-pagination [handler]
  (fn [request]
    (let [{{page :page limit :limit :or {page 1 limit 10}} :params} request]
      (handler (assoc request 
                      :page (Integer/parseInt page) 
                      :limit (Integer/parseInt limit) 
                      :offset (* limit (dec page)))))))

(defroutes app-routes
  (context "/users" []
           (wrap-pagination 
            (GET "/" [] list-users)))
  (context "/orders" []
           (wrap-json-body
            (POST "/" [] post-product))
           (wrap-pagination 
            (GET "/" [] list-orders)))
  (context "/products" []
           (wrap-json-body 
            (routes 
             (POST "/" [] post-product)
             (PUT "/:id" [id] put-product)
             (DELETE "/:id" [id] del-product)) {:keywords? true})
           (wrap-pagination 
            (GET "/" [] get-products)))
  
  (route/not-found "Not Found"))

(def app (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
