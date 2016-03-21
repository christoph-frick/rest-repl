(ns rest.repl
  (:require [puget.printer :as pp]
            [clojure.main :refer [repl repl-read]]
            [clojure.java.io :as io] 
            [rest.public :refer [cwd]]))

(defn init'
  []
  (println (slurp (io/resource "intro.txt")))
  (in-ns 'user)
  (require '[rest.public :refer :all]) 
  (require '[com.rpl.specter :refer :all]) 
  (require '[clojure.string :as str]) 
  (require '[cheshire.core :as json]) 
  (require '[clojure.data.xml :as xml]))

(defn print'
  [x]
  (pp/cprint x) 
  (when *flush-on-newline*
    (flush)))

(defn- prompt'
  []
  (printf "%s => " (cwd)))

(defn run
  [init-script]
  (pp/with-options
    {:color-scheme {:delimiter [:red]
                    :string nil
                    :character nil
                    :keyword [:yellow]
                    :symbol [:magenta]
                    :function-symbol [:bold :magenta]
                    :class-delimiter [:magenta]
                    :class-name [:bold :magenta]}}
    (repl 
     :init #(do
              (init') 
              (when init-script 
                (load-file init-script)))
     :prompt prompt'
     :print print')))
