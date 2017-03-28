Interactive testing with spec
=============================

Start the our fake [ratpack user service](faux-user-service.groovy) on
http://localhost:5050 with:

```sh
groovy faux-user-service.groovy
```

The service contains an artificial error.  The length of the password may not
exceed 16 chars, because we use a legacy DB with a `varchar(16)`.

Start the REST-REPL and load [the test](test-user-service.clj) and watch it
trace down the minimal error case:

```clojure
http://localhost:8080 => (load-file "test-user-service.clj")
({:failure #<clojure.lang.ExceptionInfo@54a2d96e clojure.lang.ExceptionInfo: Specification-based check failed {:clojure.spec/problems ({:path [:ret :status], :pred (= % 200), :val 500, :via [:user/status], :in [:status]} {:path [:ret :body], :pred map?, :val "Assertion failed: \n\nassert u.password.size() < 17\n       | |        |      |\n       | |        17     false\n       | 00000000000000000\n       ...", :via [:user/body], :in [:body]}), :clojure.spec.test/args ({:username "0", :password "00000000000000000"}), :clojure.spec.test/val {:request-time 2, :repeatable? false, :protocol-version {:name "HTTP", :major 1, :minor 1}, :streaming? true, :chunked? true, :reason-phrase "Internal Server Error", :headers {"content-type" "text/plain", "connection" "close", "transfer-encoding" "chunked"}, :orig-content-encoding nil, :status 500, :length -1, :content-type :text/plain, :content-type-params {}, :body "Assertion failed: \n\nassert u.password.size() < 17\n       | |        |      |\n       | |        17     false\n       | 00000000000000000\n       ...", :trace-redirects ["http://localhost:5050"]}, :clojure.spec/failure :check-failed}>,
  :spec #<clojure.spec$fspec_impl$reify__14248@66a53104>,
  :sym user/create-user,
  :clojure.spec.test.check/ret {:fail [({:password "EnFjJh0hmmwfZ5csa",
                                         :username "DW85483va98"})],
                                :failing-size 20,
                                :num-tests 21,
                                :result #<clojure.lang.ExceptionInfo@6d229b1c clojure.lang.ExceptionInfo: Specification-based check failed {:clojure.spec/problems ({:path [:ret :status], :pred (= % 200), :val 500, :via [:user/status], :in [:status]} {:path [:ret :body], :pred map?, :val "Assertion failed: \n\nassert u.password.size() < 17\n       | |        |      |\n       | |        17     false\n       | EnFjJh0hmmwfZ5csa\n       ...", :via [:user/body], :in [:body]}), :clojure.spec.test/args ({:username "DW85483va98", :password "EnFjJh0hmmwfZ5csa"}), :clojure.spec.test/val {:request-time 2, :repeatable? false, :protocol-version {:name "HTTP", :major 1, :minor 1}, :streaming? true, :chunked? true, :reason-phrase "Internal Server Error", :headers {"content-type" "text/plain", "connection" "close", "transfer-encoding" "chunked"}, :orig-content-encoding nil, :status 500, :length -1, :content-type :text/plain, :content-type-params {}, :body "Assertion failed: \n\nassert u.password.size() < 17\n       | |        |      |\n       | |        17     false\n       | EnFjJh0hmmwfZ5csa\n       ...", :trace-redirects ["http://localhost:5050"]}, :clojure.spec/failure :check-failed}>,
                                :seed 1490694554607,
                                :shrunk {:depth 56,
                                         :result #<clojure.lang.ExceptionInfo@54a2d96e clojure.lang.ExceptionInfo: Specification-based check failed {:clojure.spec/problems ({:path [:ret :status], :pred (= % 200), :val 500, :via [:user/status], :in [:status]} {:path [:ret :body], :pred map?, :val "Assertion failed: \n\nassert u.password.size() < 17\n       | |        |      |\n       | |        17     false\n       | 00000000000000000\n       ...", :via [:user/body], :in [:body]}), :clojure.spec.test/args ({:username "0", :password "00000000000000000"}), :clojure.spec.test/val {:request-time 2, :repeatable? false, :protocol-version {:name "HTTP", :major 1, :minor 1}, :streaming? true, :chunked? true, :reason-phrase "Internal Server Error", :headers {"content-type" "text/plain", "connection" "close", "transfer-encoding" "chunked"}, :orig-content-encoding nil, :status 500, :length -1, :content-type :text/plain, :content-type-params {}, :body "Assertion failed: \n\nassert u.password.size() < 17\n       | |        |      |\n       | |        17     false\n       | 00000000000000000\n       ...", :trace-redirects ["http://localhost:5050"]}, :clojure.spec/failure :check-failed}>,
                                         :smallest [({:password "00000000000000000",
                                                      :username "0"})],
                                         :total-nodes-visited 821}}})
```

(The exception bodies are shortened for readability)

Note the actual failing case, that got the error going and the smallest test
case `test.check` could come up with (does not really help in this case, but
shows what is going on)
