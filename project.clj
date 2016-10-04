(defproject rest-repl "0.2.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.3.0"]
                 [mvxcvi/puget "1.0.1"]
                 [com.rpl/specter "0.13.0"]
                 [cheshire "5.6.3"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main rest.main
  :profiles {:uberjar {:aot :all}})
