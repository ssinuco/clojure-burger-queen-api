(ns burger-queen-api.connection
  (:require [next.jdbc :as jdbc]))

(def db
  {:dbtype "sqlite"
   :dbname "db/database.db"})

(def ds (jdbc/get-datasource db))
