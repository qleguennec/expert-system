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
        {:oexp (fn [a [op] b] [:oexp op a b])
         :fact (fn [a [op] b] [:fact op a b])
         :T (fn [a] [:T (keyword a)])})
       (insta/transform
        {:nexp (fn [[t a]]
                 (case t
                   :sexp [:nexp a]
                   :nexp [:sexp a]
                   [:nexp [t a]]))})))

(defn parse-transform [file]
  (transform (parse file)))
