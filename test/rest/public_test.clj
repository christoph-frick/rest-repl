(ns rest.public-test
  (:require [clojure.test :refer :all]
            [rest.public :refer :all]))

(deftest json-tests
  (is (map? (json {})))
  (is (= :json (:content-type (json {}))))
  (is (= "{\"a\":1}" (:body (json {:a 1})))))

(deftest xml-tests
  (is (map? (xml [:root])))
  (is (= :xml (:content-type (xml [:root]))))
  (is (= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root attr=\"attr\">content</root>" (:body (xml [:root {:attr "attr"} "content"])))))
