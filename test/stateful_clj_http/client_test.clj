(ns stateful-clj-http.client-test
  (:require [clojure.test :refer :all]
            [stateful-clj-http.client :refer :all]))

(deftest default-config-tests
  (is (string? (:base-url default-config)))
  (is (map? (:request default-config))))

(deftest set-default-tests
  (is (= {} (:request (set-default default-config {}))))
  (is (= true
         (get-in (set-default default-config :debug true)
                 [:request :debug])))
  (is (= "something"
         (get-in (set-default default-config :headers "X-Auth-Token" "something")
                 [:request :headers "X-Auth-Token"]))))

(def abscfg {:base-url "http://localhost:8080"})

(def relcfg {:base-url "http://localhost:8080" :current-url "http://localhost:8080/some"})

(deftest cd-no-param-tests
  (are [cfg] (= (:current-url (cd cfg)) (:base-url cfg))
    abscfg
    relcfg))

(deftest cd-change-dir-tests
  (are [config to result] (= (:current-url (cd config to)) result)
    ; abs
    abscfg "subdir" "http://localhost:8080/subdir"
    abscfg "subdir/subsubdir" "http://localhost:8080/subdir/subsubdir"
    abscfg "/subdir" "http://localhost:8080/subdir"
    abscfg ".." "http://localhost:8080/"
    abscfg "//////////" "http://localhost:8080/"
    abscfg "subdir/../subsubdir" "http://localhost:8080/subsubdir"
    abscfg "subdir/../x1/../x2" "http://localhost:8080/x2"
    abscfg "subdir/../../x1/../../x2" "http://localhost:8080/x2"
    abscfg "~/subdir" "http://localhost:8080/subdir"
    abscfg "http://localhost:5050" "http://localhost:5050"
    ; rel
    relcfg "subdir" "http://localhost:8080/some/subdir"
    relcfg "subdir/subsubdir" "http://localhost:8080/some/subdir/subsubdir"
    relcfg "/subdir" "http://localhost:8080/some/subdir"
    relcfg "../../subsubdir" "http://localhost:8080/subsubdir"
    relcfg "../subsubdir" "http://localhost:8080/subsubdir"
    relcfg "subdir/../subsubdir" "http://localhost:8080/some/subsubdir"
    relcfg "~/subdir" "http://localhost:8080/subdir"
    relcfg "http://localhost:5050" "http://localhost:5050"))

(deftest cd-absolute-url-changes-base-tests
  (let [abs-url "http://example.com"
        {:keys [current-url base-url]} (cd abscfg abs-url)]
    (is (= base-url current-url abs-url))))

(deftest cd-replace-tests
  (are [cfg args result] (= (:current-url (apply cd cfg args)) result)
    abscfg ["not-in-there" "also-not-in-there"] "http://localhost:8080"
    abscfg ["not-in-there" "also-not-in-there"] "http://localhost:8080"
    abscfg ["8080" "5050"] "http://localhost:5050"
    abscfg [#"\d+" "5050"] "http://localhost:5050"
    relcfg ["some" "other"] "http://localhost:8080/other"))

(deftest pwd-tests
  (is (= (pwd {}) nil))
  (is (= (pwd abscfg) (:base-url abscfg)))
  (is (= (pwd relcfg) (:current-url relcfg))))

(deftest home-tests
  (is (= (home {}) nil))
  (is (= (home abscfg) (:base-url abscfg)))
  (is (= (home relcfg) (:base-url relcfg))))

(deftest old-pwd-tests
  (is (= (old-pwd {}) nil))
  (is (= (old-pwd abscfg) (:current-url abscfg)))
  (let [cd-ed (cd abscfg "/some/url")
        back (cd cd-ed "-")]
    (is (= (pwd cd-ed) (old-pwd back)))
    (is (= (pwd back) (old-pwd cd-ed)))))
