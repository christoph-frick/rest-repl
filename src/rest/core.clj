(ns rest.core
  (:import [java.net URI]))

(defn default-config
  []
  {:base-url "http://localhost:8080"
   :defaults {:debug false}})

; TODO: this does not deal with more .. than there is path
(defn normalize-url
  [url]
  (-> (URI/create url)
      (.normalize)
      (.toString)))

(defn home
  [config]
  (get config :base-url)) 

(defn cwd
  [config]
  (get config :current-url (:base-url config)))

(defn change-dir ; very naive
  [config to]
  {:pre [(string? to)]}
  (-> (cond
        (re-find #"://" to) to
        (.startsWith to "~") (str (home config) "/" (subs to 1))
        :else (str (cwd config) "/" to))
      normalize-url))

(defn cd
  ([config]
   (cd config (home config)))
  ([config path]
   (let [next-path (change-dir config path)] 
     (assoc config :current-url next-path))))
