(ns expert-system.util
  (:require [instaparse.core :as insta]
            [expert-system.eval :refer [eval-exp]]
            [expert-system.parse :refer [transform make-fn]]
            [clojure.math.numeric-tower :refer [expt]]))

(def test-parser
  (insta/parser (clojure.java.io/resource "expert-system.parser")))

(defn parse-fact [s]
  (transform
   (insta/parse test-parser s :start :fact)))

(defn parse-sexp [s]
  (transform
   (insta/parse test-parser s :start :sexp)))

(defn parse-oexp [s]
  (transform
   (insta/parse test-parser s :start :oexp)))

(defn get-truth-values [exp]
  (->> exp
       (tree-seq map? :args)
       (filter keyword?)
       (into #{})))

(defn check-fact [exp]
  (println exp)
  (if (some? (:op exp))
    (check-fact {:fact :imp :args [exp true]})
    (case (:fact exp)
      :eq (and
           (check-fact {:fact :imp :args (:args exp)})
           (check-fact {:fact :imp :args (reverse (:args exp))}))
      :imp (let [truth-values (get-truth-values exp)
                 total (expt 2 (count truth-values))
                 [a b] (:args exp)]
             (loop [x 0]
               (if (= total x) true
                   (and
                    (eval-exp
                     (zipmap truth-values
                             (->> x
                                  (iterate #(bit-shift-right % 1))
                                  (map (comp zero? (partial bit-and 1)))))
                     {:op (make-fn or)
                      :args [{:op (make-fn not)
                              :args [a]} b]})
                    (recur (inc x)))))))))
