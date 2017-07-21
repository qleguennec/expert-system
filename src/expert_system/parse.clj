(ns expert-system.parse
  (:require [instaparse.core :as insta]))

(defn parse [file]
  (insta/parse
   (insta/parser (clojure.java.io/resource "expert-system.parser"))
   (slurp (clojure.java.io/resource file))))

(defn neg [[t a]]
  (if (= :nexp t)
    [:sexp a]
    [:nexp a]))

(defn gen-or [a b]
  [:oexp :or a b])

(defn gen-and [a b]
  [:oexp :and a b])

(defn transform [tree]
  (->> tree
       (insta/transform
        {:oexp (fn [a [_ [op]] b]
                 (case op
                   :inp (gen-or (neg a) b)
                   :xor (gen-and (gen-or a b)
                                 (gen-and (neg a) (neg b)))
                   :eq (gen-and (gen-or (neg a) b)
                                (gen-or a (neg b)))
                   [:oexp op a b]))})
       (insta/transform
        {:nexp (fn [[t a]]
                 (case t
                   :sexp [:nexp a]
                   :nexp [:sexp a]
                   [:nexp [t a]]))})))

(defn parse-transform [file]
  (transform (parse file)))
