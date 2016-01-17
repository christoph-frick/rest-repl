(ns rest.main
  (:require [clojure.tools.cli :refer [parse-opts]]
            [rest.repl :as repl])
  (:gen-class))

(defn -main [& args]
  (repl/run))
