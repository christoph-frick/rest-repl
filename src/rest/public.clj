(ns rest.public
  (:require [rest.core :as rc]
            [clojure.java.io :as io] 
            [clojure.edn :as edn]
            [clj-http.client :as chttp :refer [json-encode json-decode]]))

(def config
  (atom (rc/default-config)))

(defn cwd
  []
  (rc/cwd @config))

(defmacro ^:private _cd
  [& args]
  `(-> (swap! config rc/cd ~@args)
       rc/cwd))

(defn cd
  ([]
   (_cd)) 
  ([path]
   (_cd path))
  ([old new]
   (_cd old new)))

(defn default
  [& args]
  (swap! config assoc-in (into [:defaults] (drop-last args)) (last args)))

(defn request
  ([method path-or-body]
   (if (string? path-or-body)
     (request method path-or-body {})
     (request method (cwd) path-or-body)))
  ([method path body]
   (let [config @config
         crequest (merge (get config :defaults {})
                         body 
                         {:method method
                          :url (-> (rc/cd config path)
                                   rc/cwd)})] 
     (chttp/request crequest))))

(defn json
  [body]
  {:content-type :json 
   :body (json-encode body)})

(defn load-config
  [filename]
  (reset! config (-> filename slurp edn/read-string)))

(defn save-config
  [filename]
  (spit filename (pr-str @config)))

(defn help
  []
  (println (slurp (io/resource "help.md")))
  (println "Current config:")
  @config)
