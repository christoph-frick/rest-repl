(ns rest.public
  (:require [clj-http.client :as http-client]
            [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [relative-clj-http.client :as client]))

(def config
  (atom client/default-config))

(defn pwd
  []
  (client/pwd @config))

(defn old-pwd
  []
  (client/old-pwd @config))

(defmacro ^:private _cd
  [& args]
  `(-> (swap! config client/cd ~@args)
       (client/pwd)))

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
   (swap! config #(apply client/set-default % args))))

(defn request
  ([method]              (client/request @config method))
  ([method path]         (client/request @config method path))
  ([method path request] (client/request @config method path request)))

; TODO: make them a macro

(defn GET
  ([]          (request :get))
  ([path]      (request :get path))
  ([path body] (request :get path body)))

(defn POST
  ([]          (request :post))
  ([path]      (request :post path))
  ([path body] (request :post path body)))

(defn PUT
  ([]          (request :put))
  ([path]      (request :put path))
  ([path body] (request :put path body)))

(defn DELETE
  ([]          (request :delete))
  ([path]      (request :delete path))
  ([path body] (request :delete path body)))

(defn json
  [body]
  {:content-type :json 
   :body  (http-client/json-encode body)})

(defn xml
  [body]
  {:content-type :xml 
   :body  (-> body xml/sexp-as-element xml/emit-str)})

(defn help
  []
  (println (slurp (io/resource "help.md")))
  (println "Current config:")
  @config)
