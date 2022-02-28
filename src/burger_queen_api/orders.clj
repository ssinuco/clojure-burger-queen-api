(ns burger-queen-api.orders
  (:require [clojure.data.json :as json]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [burger-queen-api.connection :refer :all]
            [burger-queen-api.sql.orders :refer :all]
            [burger-queen-api.utils :refer :all]
            [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as adapter]))

(hugsql/def-db-fns "burger_queen_api/sql/orders.sql" {:adapter (adapter/hugsql-adapter-next-jdbc)})

(defn create-order
  [order]
  (with-open [conn (jdbc/get-connection ds)]
    (jdbc/execute! conn ["PRAGMA foreign_keys = ON"])
    (jdbc/with-transaction [tx conn]
      (let [orderId (exec-returning-id insert-order tx order)]
        (println (sql/query tx ["PRAGMA foreign_keys"]))
        (doseq [orderProduct (:products order)]
          (insert-order-product tx (assoc orderProduct :orderId orderId)))
        orderId))))

(defn destroy-order
  [id]
  (with-open [conn (jdbc/get-connection ds)]
    (jdbc/execute! conn ["PRAGMA foreign_keys = ON"])
    (jdbc/with-transaction [tx conn]
      (delete-order-products tx {:orderId id})
      (delete-order tx {:id id}))
    id))

(defn post-order
  [{order :body}]
  (let []
    {
     :status 201
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id (create-order order)})}))


(defn get-orders
  [{page :page limit :limit :or {page 1 limit 10}}]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (select-orders ds page limit))})

(defn put-order
  [{order :body {id :id} :params}]
  (let [result (update-order ds (assoc order :id id))]
    {
     :status 200
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id id})}))

(defn del-order
  [{{id :id} :params}]
  (let [result (delete-order ds {:id id})]
    {
     :status 200
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id (destroy-order id)})}))

(defn get-order
  [req]
  req)
