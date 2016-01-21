(ns rest.main
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as s]
            [clj-http.client :refer [parse-edn]]
            [rest.public :refer [config]]
            [rest.repl :as repl])
  (:import [java.io File])
  (:gen-class))

(def ^:const cli-options
  [["-c" "--config-file config.edn" "Config file to use for @config"
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
      (:config-file options) (reset! config (-> options :config-file slurp parse-edn))
      (seq arguments) (swap! config assoc :base-url (last arguments)))
    (repl/run)))
