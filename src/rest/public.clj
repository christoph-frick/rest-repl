(ns rest.public
  (:require [rest.core :as rc]
            [clj-http.client :as chttp :refer [json-encode json-decode]]))

(def config
  (atom (rc/default-config)))

(defn cwd
  []
  (rc/cwd @config))

(defn cd
  [path]
  (-> (swap! config rc/cd path)
      rc/cwd))

(defn debug
  ([] (rc/debug @config))
  ([on?] (swap! config rc/debug on?)))

(defn request
  ([method body]
   (request method (rc/cwd @config) body))
  ([method path body]
   (let [config @config
         crequest (merge {:debug (rc/debug config)}
                         (get config :defaults {})
                         body 
                         {:method method
                          :url (-> (rc/cd config path)
                                   rc/cwd)})] 
     (chttp/request crequest))))

(defn as-json
  [body]
  {:content-type :json 
   :as :json 
   :body (json-encode body)})
