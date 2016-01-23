(ns rest.core-test
  (:require [clojure.test :refer :all]
            [rest.core :refer :all]))

(def abscfg {:base-url "http://localhost:8080"})

(def relcfg {:base-url "http://localhost:8080" :current-url "http://localhost:8080/some"})

(def cd-tests [; abs
              [abscfg "subdir" "http://localhost:8080/subdir"]
              [abscfg "subdir/subsubdir" "http://localhost:8080/subdir/subsubdir"]
              [abscfg "/subdir" "http://localhost:8080/subdir"]
              [abscfg ".." "http://localhost:8080/"] ; FIXME
              [abscfg "subdir/../subsubdir" "http://localhost:8080/subsubdir"]
              [abscfg "subdir/../x1/../x2" "http://localhost:8080/x2"]
              [abscfg "subdir/../../x1/../../x2" "http://localhost:8080/x2"]
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
              [relcfg "http://localhost:5050" "http://localhost:5050"]])

(deftest chane-dir-tests ; gentest?
  (doseq [[config to result] cd-tests]
    (is (= (change-dir config to) result))))

(deftest cd-no-param-tests
  (doseq [cfg [abscfg relcfg]]
    (is (= (:current-url (cd cfg)) (:base-url cfg)))))

(deftest cd-change-dir-tests
  (doseq [[config to result] cd-tests]
    (is (= (:current-url (cd config to)) result))))

