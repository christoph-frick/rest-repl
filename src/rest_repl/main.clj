(ns rest-repl.main
  (:require [wrepl.main]
            [wrepl.config])
  (:gen-class))

(defn -main
  [& args]
  (binding [wrepl.config/*default-config-filename* ".rest-repl.edn"]
    (apply wrepl.main/-main args)))
