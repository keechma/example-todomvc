(ns keechma-todomvc.components.todo-list
  (:require (keechma.ui-component :as ui)))

(defn render [ctx]
  (let [todos-sub (ui/subscription ctx :todos)
        todo-item-component (ui/component ctx :todo-item)
        editing-id-sub (ui/subscription ctx :editing-id)]
    (fn []
      (let [editing-id @editing-id-sub]
        [:ul.todo-list
         (for [todo @todos-sub]
           ^{:key (:id todo)}
           [(ui/component ctx :todo-item) todo (= (:id todo) editing-id)])]))))

(def component (ui/constructor {:subscription-deps [:todos :editing-id]
                                :component-deps [:todo-item]
                                :renderer render}))
