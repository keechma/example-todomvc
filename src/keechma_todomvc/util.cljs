(ns keechma-todomvc.util
  "# Generally useful functions")

(defn is-enter? [key-code]
  (= key-code 13))

(defn is-esc? [key-code]
  (= key-code 27))
