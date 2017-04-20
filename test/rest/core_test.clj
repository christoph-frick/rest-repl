(ns rest.core-test
  (:require [clojure.test :refer :all]
            [rest.core :refer :all]))

(deftest default-config-tests
  (is (string? (:base-url (default-config)))) 
  (is (map? (:defaults (default-config)))))

(deftest set-default-tests
  (is (= {} (:defaults (set-default (default-config) {}))))
  (is (= true
         (get-in (set-default (default-config) :debug true)
                 [:defaults :debug])))
  (is (= "something" 
         (get-in (set-default (default-config) :headers "X-Auth-Token" "something")  
                 [:defaults :headers "X-Auth-Token"]))))

(def abscfg {:base-url "http://localhost:8080"})

(def relcfg {:base-url "http://localhost:8080" :current-url "http://localhost:8080/some"})

(def cd-tests [; abs
               [abscfg "subdir" "http://localhost:8080/subdir"]
               [abscfg "subdir/subsubdir" "http://localhost:8080/subdir/subsubdir"]
               [abscfg "/subdir" "http://localhost:8080/subdir"]
               [abscfg ".." "http://localhost:8080/"]
               [abscfg "//////////" "http://localhost:8080/"]
               [abscfg "subdir/../subsubdir" "http://localhost:8080/subsubdir"]
               [abscfg "subdir/../x1/../x2" "http://localhost:8080/x2"]
               [abscfg "subdir/../../x1/../../x2" "http://localhost:8080/x2"]
               [abscfg "~/subdir" "http://localhost:8080/subdir"]
               [abscfg "http://localhost:5050" "http://localhost:5050"]
              ; rel
               [relcfg "subdir" "http://localhost:8080/some/subdir"]
               [relcfg "subdir/subsubdir" "http://localhost:8080/some/subdir/subsubdir"]
               [relcfg "/subdir" "http://localhost:8080/some/subdir"]
               [relcfg "../../subsubdir" "http://localhost:8080/subsubdir"]
               [relcfg "../subsubdir" "http://localhost:8080/subsubdir"]
               [relcfg "subdir/../subsubdir" "http://localhost:8080/some/subsubdir"]
               [relcfg "~/subdir" "http://localhost:8080/subdir"]
               [relcfg "http://localhost:5050" "http://localhost:5050"]])

(deftest change-dir-tests ; gentest?
  (doseq [[config to result] cd-tests]
    (is (= (change-dir config to) result))))

(deftest cd-no-param-tests
  (doseq [cfg [abscfg relcfg]]
    (is (= (:current-url (cd cfg)) (:base-url cfg)))))

(deftest cd-change-dir-tests
  (doseq [[config to result] cd-tests]
    (is (= (:current-url (cd config to)) result))))

(deftest cd-absolute-url-changes-base
  (let [abs-url "http://example.com"
        {:keys [current-url base-url]} (cd abscfg abs-url)]
    (is (and (= current-url abs-url)
             (= base-url abs-url)))))

(deftest cd-replace-tests
  (is (= (:current-url (cd abscfg "not-in-there" "also-not-in-there")) "http://localhost:8080"))  
  (is (= (:current-url (cd abscfg "8080" "5050")) "http://localhost:5050"))  
  (is (= (:current-url (cd abscfg #"\d+" "5050")) "http://localhost:5050"))  
  (is (= (:current-url (cd relcfg "some" "other")) "http://localhost:8080/other")))

(deftest cwd-tests
  (is (= (cwd {}) nil))
  (is (= (cwd abscfg) (:base-url abscfg)))
  (is (= (cwd relcfg) (:current-url relcfg))))
