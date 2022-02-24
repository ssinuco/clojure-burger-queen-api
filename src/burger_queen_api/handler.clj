(ns burger-queen-api.handler
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [burger-queen-api.routes :refer :all]
            ))

(def app (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
