(ns expert-system.eval)

(defn eval-oexp [op a b truth-map]
  (case op
    :and (and (eval-exp a truth-map) (eval-exp b truth-map))
    :or (or (eval-exp a truth-map) (eval-exp b truth-map))))

(defn eval-exp [[type & exp] truth-map]
  (println type exp)
  (if (= :nexp type)
    (not (eval-exp [:sexp exp] truth-map))
    (let [[type & exp] exp]
      (println type exp)
      (case type
       :T ((first exp) truth-map)
       :oexp (apply eval-oexp (conj exp truth-map))
       :pexp (eval-exp exp truth-map)))))

(defn check-inp [[op a b] truth-map]
  (if (= :eq op)
    (and (eval [:inp a b] truth-map)
         (eval [:inp b a] truth-map))
    (some->> a)))
