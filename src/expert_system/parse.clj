(ns expert-system.parse
  (:require [instaparse.core :as insta]))

(defmacro make-fn [m]
  `(fn ~m [& args#]
     (eval `(~'~m ~@args#))))

(defn parse [file]
  (insta/parse
   (insta/parser (clojure.java.io/resource "expert-system.parser"))
   (slurp (clojure.java.io/resource file))))

(defn imp [a b]
  (or (not a) b))

(defn eq [a b]
  (= a b))

(defn transform [tree]
  (->> tree
       (insta/transform
        {:nexp (fn [a]
                 {:op (make-fn not) :args [a]})
         :pexp (fn [a]
                 {:op (make-fn identity) :args [a]})
         :oexp (fn [a [op] b]
                 {:op (case op
                        :or (make-fn or)
                        :and (make-fn and)
                        :imp (make-fn imp)
                        :eq (make-fn eq))
                  :args [a b]})
         :fact (fn [a [op] b]
                 {:fact op :args [a b]})
         :T (fn [a] (keyword a))})))

(defn parse-transform [file]
  (transform (parse file)))
