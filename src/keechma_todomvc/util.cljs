(ns keechma-todomvc.util)

(defn is-enter? [key-code]
  (= key-code 13))

(defn is-esc? [key-code]
  (= key-code 27))
