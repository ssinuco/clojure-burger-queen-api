(ns burger-queen-api.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body]]
            [burger-queen-api.middlewares :refer :all]
            [burger-queen-api.users :refer :all]
            [burger-queen-api.products :refer :all]
            [burger-queen-api.orders :refer :all]
            ))

(defroutes app-routes
  (context "/users" []
           (wrap-json-body 
            (routes 
             (POST "/" [] post-user)
             (GET "/:id" [id] get-user)
             (PUT "/:id" [id] put-user)
             (DELETE "/:id" [id] del-user)) {:keywords? true})
           (wrap-pagination 
            (GET "/" [] get-users)))

  (context "/orders" []
           (wrap-json-body
            (routes
             (POST "/" [] post-order)
             (PUT "/:id" [id] put-order)
             (DELETE "/:id" [id] del-order)) {:keywords? true})
           (wrap-pagination 
            (GET "/" [] get-orders)))

  (context "/products" []
           (wrap-json-body 
            (routes 
             (POST "/" [] post-product)
             (PUT "/:id" [id] put-product)
             (DELETE "/:id" [id] del-product)) {:keywords? true})

           (wrap-pagination 
            (GET "/" [] get-products)))
  
  (route/not-found "Not Found"))
