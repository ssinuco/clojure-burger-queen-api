(ns burger-queen-api.middlewares)

(defn wrap-pagination [handler]
  (fn [request]
    (let [{{page :page limit :limit :or {page 1 limit 10}} :params} request]
      (handler (assoc request 
                      :page (Integer/parseInt page) 
                      :limit (Integer/parseInt limit) 
                      :offset (* (Integer/parseInt limit) (dec (Integer/parseInt page))))))))
