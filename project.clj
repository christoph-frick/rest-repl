(defproject rest-repl "0.2.0"
  :dependencies [[org.clojure/clojure "1.9.0-alpha15"]
                 [org.clojure/test.check "0.9.0"]
                 [clj-http "3.4.1"]
                 [mvxcvi/puget "1.0.1"]
                 [com.rpl/specter "1.0.0"]
                 [cheshire "5.7.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main rest.main
  :profiles {:uberjar {:aot :all}})
