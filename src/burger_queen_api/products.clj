(ns burger-queen-api.products
  (:require [clojure.data.json :as json]
            [next.jdbc :as jdbc]
            [burger-queen-api.connection :refer :all]
            [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as adapter]))

(hugsql/def-db-fns "burger_queen_api/sql/products.sql" {:adapter (adapter/hugsql-adapter-next-jdbc)})


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
   :body (json/write-str (select-products ds {:limit limit :offset offset}))})

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
