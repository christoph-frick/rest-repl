(ns stateful-clj-http.client
  (:require [clj-http.client :as http-client]
            [clojure.string :as str])
  (:import (java.net URI)))

; TODO: this does not proplery deal with more .. than there is path
(defn- normalize-url
  "Normalize the URL to it's most direct and absolute form.  E.g. resolve `..`."
  [url]
  (-> (URI/create url)
      (.normalize)
      (.toString)
      ; `..` in normalize remain on first level
      (str/replace #"\.\./?" "")))

(defn- absolute-url?
  "Predicate, if the URL is considered absolute"
  [url]
  (boolean (some? (re-find #"://" url))))

(def ^:const default-config
  "Starting config"
  {:base-url "http://localhost:8080"
   :request {:as :auto
             :coerce :always
             :throw-exceptions false
             :debug false}})

(defn set-default
  "Convenience method to set a request default in the config"
  [config & args]
  (assoc-in config (into [:request] (drop-last args)) (last args)))

(defn home
  "The starting, or base URL, of the config"
  [config]
  (get config :base-url))

(defn pwd
  "The current url in the config"
  [config]
  (get config :current-url (:base-url config)))

(defn old-pwd
  "The previous url in the config"
  [config]
  (get config :previous-url (:current-url config)))

(defn- change-dir ; very naive
  "Helper to create the next URL from the config and cd-ish instruction."
  [config to]
  {:pre [(string? to)]}
  (normalize-url (cond
                   (absolute-url? to) to
                   (str/starts-with? to "~") (str (home config) "/" (subs to 1))
                   :else (str (pwd config) "/" to))))

(defn- remember-old-pwd
  "Helper to change the current url and remember the previous one"
  [config new-pwd]
  (let [old-pwd (pwd config)]
    (assoc config
           :current-url new-pwd
           :previous-url old-pwd)))

(defn cd
  "Change directory in the config.

   - with no param given, jump back to what is considered home
   - given a path, change relative or absolute URL
   - with two params replace any occurence of the first argument in the pwd with the second"
  ([config]
   (cd config (home config)))
  ([config path]
   (cond
     ; absolute url changes base too and sets current
     (absolute-url? path)
     (remember-old-pwd
      (assoc config :base-url path)
      path)
     ; a `-` changes back to the previous url
     (= path "-")
     (remember-old-pwd
      config
      (old-pwd config))
     ; "regular" case
     :else
     (remember-old-pwd
      config
      (change-dir config path))))
  ([config old new]
   (remember-old-pwd config (str/replace (pwd config) old new))))

(defn request
  "Same as clj-http.client/request, but using the passed in config for URL and body transformation"
  ([config method]
   (request config method (pwd config) {}))
  ([config method path]
   (request config method path {}))
  ([config method path request]
   (let [default-request (get config :request {})
         crequest (merge
                   default-request
                   request
                   {:method method
                    :url (-> config (cd path) (pwd))})]
     (http-client/request crequest))))
