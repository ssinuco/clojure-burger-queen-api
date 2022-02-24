(ns burger-queen-api.users
  (:require [clojure.data.json :as json]
            [next.jdbc :as jdbc]
            [burger-queen-api.connection :refer :all]
            [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as adapter]))

(hugsql/def-db-fns "burger_queen_api/sql/users.sql" {:adapter (adapter/hugsql-adapter-next-jdbc)})

(defn get-users
  [{page :page limit :limit offset :offset :or {page 1 limit 10 offset 0}}]
  {
   :status 200
   :headers {"Content-Type" "text/json"}
   :body (json/write-str (select-users ds {:limit limit :offset offset}))})
