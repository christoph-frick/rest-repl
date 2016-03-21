(ns rest.main
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as s]
            [rest.public :refer [config load-config]]
            [rest.repl :as repl])
  (:import [java.io File])
  (:gen-class))

(def ^:const cli-options
  [["-c" "--config-file config.edn" "Config file to use for @config"
    :validate [#(.exists (File. %)) "file must exist"]]
   ["-i" "--init script.clj" "Run the given file"
    :validate [#(.exists (File. %)) "file must exist"]]
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
      (:config-file options) (-> options :config-file load-config)
      (seq arguments) (swap! config assoc :base-url (last arguments)))
    (repl/run (:init options))))
