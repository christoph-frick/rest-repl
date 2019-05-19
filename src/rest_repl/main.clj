(ns rest-repl.main
  (:require [wrepl.main]
            [wrepl.config])
  (:gen-class))

(defn -main
  [& args]
  (binding [wrepl.config/*default-base-name* "rest-repl"]
    (apply wrepl.main/-main args)))
