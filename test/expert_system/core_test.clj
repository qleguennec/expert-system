(ns expert-system.core-test
  (:require [clojure.test :refer :all]
            [expert-system.util :refer :all]))

(defn test-fact [s]
  (testing s (is (check-fact (parse-fact s)))))

(deftest absurd
  (test-fact "!!A <=> A"))

(deftest associativity
  (test-fact "A + (B + C) <=> (A + B) + C"))
