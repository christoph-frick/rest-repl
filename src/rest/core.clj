(ns rest.core
  (:require [clojure.string :as str])
  (:import [java.net URI]))

(defn default-config
  []
  {:base-url "http://localhost:8080"
   :defaults {:as :auto
              :coerce :always
              :throw-exceptions false
              :debug false}})

(defn set-default
  [config & args]
  (assoc-in config (into [:defaults] (drop-last args)) (last args)))

; TODO: this does not deal with more .. than there is path
(defn normalize-url
  [url]
  (-> (URI/create url)
      (.normalize)
      (.toString)
      ; `..` in normalize remain on first level
      (str/replace #"\.\./?" "")))

(defn home
  [config]
  (get config :base-url)) 

(defn cwd
  [config]
  (get config :current-url (:base-url config)))

(defn absolute-url?
  [url]
  (some? (re-find #"://" url)))

(defn change-dir ; very naive
  [config to]
  {:pre [(string? to)]}
  (normalize-url (cond
                   (absolute-url? to) to
                   (str/starts-with? to "~") (str (home config) "/" (subs to 1))
                   :else (str (cwd config) "/" to))))

(defn cd
  ([config]
   (cd config (home config)))
  ([config path]
   (if (absolute-url? path)
     (assoc config
            :current-url path
            :base-url path)
     (let [next-path (change-dir config path)]
       (assoc config
              :current-url next-path))))
  ([config old new]
   (assoc config :current-url (str/replace (cwd config) old new))))
