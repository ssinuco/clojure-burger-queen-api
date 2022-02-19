(defproject burger-queen-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.1"]
                 [org.clojure/data.json "2.4.0"]
                 [com.github.seancorfield/next.jdbc "1.2.772"]
                 [org.xerial/sqlite-jdbc "3.36.0"]
                 [com.layerware/hugsql-core "0.5.1"]
                 [com.layerware/hugsql-adapter-next-jdbc "0.5.1"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler burger-queen-api.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
