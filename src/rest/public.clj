(ns rest.public
  (:require [rest.core :as rc]
            [clojure.java.io :as io] 
            [clojure.edn :as edn]
            [clj-http.client :as chttp :refer [json-encode json-decode]]
            [clojure.data.xml :as xml]))

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
  ([]
   @config)
  ([& args] 
   (swap! config #(apply rc/set-default % args))))

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

(defn GET 
  ([path-or-body] (request :get path-or-body))
  ([path body] (request :get path body)))

(defn POST 
  ([path-or-body] (request :post path-or-body))
  ([path body] (request :post path body)))

(defn json
  [body]
  {:content-type :json 
   :body (json-encode body)})

(defn xml
  [body]
  {:content-type :xml 
   :body (-> body xml/sexp-as-element xml/emit-str)})

(defn help
  []
  (println (slurp (io/resource "help.md")))
  (println "Current config:")
  @config)
