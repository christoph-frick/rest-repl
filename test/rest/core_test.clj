(ns rest.core-test
  (:require [clojure.test :refer :all]
            [rest.core :refer :all]))

(deftest change-dir-test ; gentest?
  (let [abscfg {:base-url "http://localhost:8080"}
        relcfg {:base-url "http://localhost:8080" :current-url "http://localhost:8080/some"}
        tests [; abs
               [abscfg "subdir" "http://localhost:8080/subdir"]
               [abscfg "subdir/subsubdir" "http://localhost:8080/subdir/subsubdir"]
               [abscfg "/subdir" "http://localhost:8080/subdir"]
               [abscfg ".." "http://localhost:8080/"] ; FIXME
               [abscfg "subdir/../subsubdir" "http://localhost:8080/subsubdir"]
               [abscfg "~/subdir" "http://localhost:8080/subdir"]
               [abscfg "http://localhost:5050" "http://localhost:5050"]
               ; rel
               [relcfg "subdir" "http://localhost:8080/some/subdir"]
               [relcfg "subdir/subsubdir" "http://localhost:8080/some/subdir/subsubdir"]
               [relcfg "/subdir" "http://localhost:8080/some/subdir"]
               [relcfg "../../subsubdir" "http://localhost:8080/subsubdir"] ; FIXME
               [relcfg "../subsubdir" "http://localhost:8080/subsubdir"]
               [relcfg "subdir/../subsubdir" "http://localhost:8080/some/subsubdir"]
               [relcfg "~/subdir" "http://localhost:8080/subdir"]
               [relcfg "http://localhost:5050" "http://localhost:5050"]]] 
    (doseq [[config to result] tests]
      (is (= (change-dir config to) result)))))

(deftest debug-test
  (let [config (default-config)
        tests [[[nil false] false]
               [[0 1 "" {} true] true]]]
    (doseq [[values result] tests
            value values]
      (is (= (-> (debug config value) debug) result)))))
