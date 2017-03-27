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
({:failure #<clojure.lang.ExceptionInfo@62cba181 clojure.lang.ExceptionInfo: Specification-based check failed {:clojure.spec/problems ({:path [:ret :status], :pred (= % 200), :val 500, :via [:user/status], :in [:status]} {:path [:ret :body], :pred map?, :val "Assertion failed: \n\nassert u.password.size() < 16\n       | |        |      |\n       | |        16     false\n       | 0000000000000000...", :via [:user/body], :in [:body]}), :clojure.spec.test/args ({:username "00", :password "0000000000000000"}), :clojure.spec.test/val {:request-time 2, :repeatable? false, :protocol-version {:name "HTTP", :major 1, :minor 1}, :streaming? true, :chunked? true, :reason-phrase "Internal Server Error", :headers {"content-type" "text/plain", "connection" "close", "transfer-encoding" "chunked"}, :orig-content-encoding nil, :status 500, :length -1, :content-type :text/plain, :content-type-params {}, :body "Assertion failed: \n\nassert u.password.size() < 16\n       | |        |      |\n       | |        16     false\n       | 0000000000000000...", :trace-redirects ["http://localhost:5050"]}, :clojure.spec/failure :check-failed}>,
  :spec #<clojure.spec$fspec_impl$reify__14248@1b482cbf>,
  :sym user/create-user,
  :clojure.spec.test.check/ret {:fail [({:password "i2XLIJuP8d6Jk753b",
                                         :username "Z8rNRw2S0fa2K50mD"})],
                                :failing-size 17,
                                :num-tests 18,
                                :result #<clojure.lang.ExceptionInfo@661fe025 clojure.lang.ExceptionInfo: Specification-based check failed {:clojure.spec/problems ({:path [:ret :status], :pred (= % 200), :val 500, :via [:user/status], :in [:status]} {:path [:ret :body], :pred map?, :val "Assertion failed: \n\nassert u.password.size() < 16\n       | |        |      |\n       | |        17     false\n       | i2XLIJuP8d6Jk753b...", :via [:user/body], :in [:body]}), :clojure.spec.test/args ({:username "Z8rNRw2S0fa2K50mD", :password "i2XLIJuP8d6Jk753b"}), :clojure.spec.test/val {:request-time 4, :repeatable? false, :protocol-version {:name "HTTP", :major 1, :minor 1}, :streaming? true, :chunked? true, :reason-phrase "Internal Server Error", :headers {"content-type" "text/plain", "connection" "close", "transfer-encoding" "chunked"}, :orig-content-encoding nil, :status 500, :length -1, :content-type :text/plain, :content-type-params {}, :body "Assertion failed: \n\nassert u.password.size() < 16\n       | |        |      |\n       | |        17     false\n       | i2XLIJuP8d6Jk753b...", :trace-redirects ["http://localhost:5050"]}, :clojure.spec/failure :check-failed}>,
                                :seed 1490650149403,
                                :shrunk {:depth 58,
                                         :result #<clojure.lang.ExceptionInfo@62cba181 clojure.lang.ExceptionInfo: Specification-based check failed {:clojure.spec/problems ({:path [:ret :status], :pred (= % 200), :val 500, :via [:user/status], :in [:status]} {:path [:ret :body], :pred map?, :val "Assertion failed: \n\nassert u.password.size() < 16\n       | |        |      |\n       | |        16     false\n       | 0000000000000000...", :via [:user/body], :in [:body]}), :clojure.spec.test/args ({:username "00", :password "0000000000000000"}), :clojure.spec.test/val {:request-time 2, :repeatable? false, :protocol-version {:name "HTTP", :major 1, :minor 1}, :streaming? true, :chunked? true, :reason-phrase "Internal Server Error", :headers {"content-type" "text/plain", "connection" "close", "transfer-encoding" "chunked"}, :orig-content-encoding nil, :status 500, :length -1, :content-type :text/plain, :content-type-params {}, :body "Assertion failed: \n\nassert u.password.size() < 16\n       | |        |      |\n       | |        16     false\n       | 0000000000000000...", :trace-redirects ["http://localhost:5050"]}, :clojure.spec/failure :check-failed}>,
                                         :smallest [({:password "0000000000000000",
                                                      :username "00"})],
                                         :total-nodes-visited 666}}})
```

(The exception bodies are shortened for readability; the "666" is no fake!)

Note the actual failing case, that got the error going and the smallest test
case `test.check` could come up with (does not really help in this case, but
shows what is going on)
