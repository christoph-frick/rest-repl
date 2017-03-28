(require '[clojure.spec :as s])
(require '[clojure.spec.test :as stest])

(s/def ::not-empty-string (s/and string? seq))
(s/def ::username ::not-empty-string)
(s/def ::password ::not-empty-string)

(s/def ::create-user-request 
  (s/keys :req-un [::username ::password]))

(s/def ::body map?)
(s/def ::status (s/and int? #(= % 200)))

(s/def ::create-user-response
  (s/keys :req-un [::body ::status]))

(defn create-user [request]
  (POST "http://localhost:5050" (json request)))

(s/fdef create-user
        :args (s/cat :request ::create-user-request)
        :ret ::create-user-response)

(stest/check `create-user)
