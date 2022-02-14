(defproject burger-queen-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [org.clojure/data.json "2.4.0"]
                 [com.github.seancorfield/next.jdbc "1.2.772"]
                 [org.xerial/sqlite-jdbc "3.36.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler burger-queen-api.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
