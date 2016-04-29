(defproject rest-repl "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.0.1"]
                 [mvxcvi/puget "1.0.0"]
                 [com.rpl/specter "0.10.0"]
                 [cheshire "5.6.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.cli "0.3.3"]]
  :main rest.main
  :profiles {:uberjar {:aot :all}})
