(ns expert-system.core-test
  (:require [clojure.test :refer :all]
            [instapare.core :as insta]
            [expert-system.parse :refer [transform]]
            [expert-system.core :refer :all]))

(def test-parser
  (insta/parser (clojure.java.io/resource "expert-system.parser")))

(defn parse-fact [s]
  (transform
   (insta/parse test-parser s :start :fact)))

(defn parse-oexp [s]
    (transform
     (insta/parse test-parser s :start :oexp)))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
