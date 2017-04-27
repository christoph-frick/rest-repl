(defproject rest-repl "0.2.0"
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/test.check "0.9.0"]
                 [clj-http "3.5.0"]
                 [mvxcvi/puget "1.0.1"]
                 [com.rpl/specter "1.0.1"]
                 [cheshire "5.7.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main rest.main
  :profiles {:uberjar {:aot :all}})
