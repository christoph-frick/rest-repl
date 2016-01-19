(ns rest.repl
  (:require [puget.printer :refer [cprint]]
            [clojure.main :refer [repl repl-read]]
            [clojure.java.io :as io] 
            [rest.public :refer [cwd]]))

(defn init'
  []
  (println (slurp (io/resource "intro.txt")))
  (in-ns 'user)
  (require '[rest.public :refer :all]) 
  (require '[com.rpl.specter :refer :all]))

(defn print'
  [x]
  (cprint x) 
  (when *flush-on-newline*
    (flush)))

(defn- prompt'
  []
  (printf "%s => " (cwd)))

(defn run
  []
  (repl 
   :init init'
   :prompt prompt'
   :print print'))
