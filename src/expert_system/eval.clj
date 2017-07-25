(ns expert-system.eval)

(defn eval-exp [values exp]
  (if-some [op (:op exp)]
    (->> exp
         :args
         (mapv (partial eval-exp values))
         (apply op))
    (if (keyword? exp)
      (exp values)
      exp)))
