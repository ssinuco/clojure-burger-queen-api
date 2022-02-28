(ns burger-queen-api.users
  (:require [clojure.data.json :as json]
            [next.jdbc :as jdbc]
            [burger-queen-api.connection :refer :all]
            [burger-queen-api.utils :refer :all]
            [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as adapter]))

(hugsql/def-db-fns "burger_queen_api/sql/users.sql" {:adapter (adapter/hugsql-adapter-next-jdbc)})

(defn post-user
  [{user :body}]
  (let []
    {
     :status 201
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id (exec-returning-id insert-user ds user)})}))

(defn get-users
  [{page :page limit :limit offset :offset :or {page 1 limit 10 offset 0}}]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (select-users ds {:limit limit :offset offset}))})

(defn get-user
  [{{id :id} :params}]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (first (select-user ds {:id id})))})

(defn put-user
  [{user :body {id :id} :params}]
  (let [result (update-user ds (assoc user :id id))]
    {
     :status 200
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id id})}))

(defn del-user
  [{user :body {id :id} :params}]
  (let [result (delete-user ds {:id id})]
    {
     :status 200
     :headers {"Content-Type" "text/json"}
     :body (json/write-str {:id id})}))
