(ns burger-queen-api.utils)

(defn exec-returning-id
  [f & params]
  ((keyword "last_insert_rowid()") (first (apply f params))))
