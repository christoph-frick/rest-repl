(ns rest.main
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as s]
            [rest.public :refer [cd]]
            [rest.repl :as repl])
  (:import [java.io File])
  (:gen-class))

(def ^:const cli-options
  [["-i" "--init script.clj" "Run the given file before the first prompt"
    :validate [#(.exists (File. %)) "file must exist"]]
   ["-e" "--eval string" "Evaluate the expression (after --init if both given)"] 
   ["-h" "--help"]])

(defn usage
  [summary]
  (str "REST-REPL\n\nrest-repl [options...] [url]\n\n" summary))

(defn error-message
  [usage errors]
  (str "ERRORS:\n\n" (s/join "\n" errors) "\n\n" usage))

(defn exit
  [exit-code message]
  (println message)
  (System/exit exit-code))

(defn -main [& args]
  (let [opts (parse-opts args cli-options)
        {:keys [options arguments errors summary]} opts]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-message (usage summary) errors))
      (seq arguments) (cd (last arguments)))
    (repl/run (:init options) (:eval options))))
