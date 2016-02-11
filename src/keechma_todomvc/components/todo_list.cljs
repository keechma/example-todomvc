(ns keechma-todomvc.components.todo-list
  (:require (keechma.ui-component :as ui)))

(defn render
  "Renders the list of todos. This component gets the list of todos
  from the `:todos` subscription and the current editing id from the
  `:editing-id` subscription.

  Each todo item is rendered by the `:todo-item` component which receives
  the todo entity and `is-editing?` (based on the todo entity id and the
  current editing id)"
  [ctx] 
  (fn []
    (let [todos-sub (ui/subscription ctx :todos)
          todo-item-component (ui/component ctx :todo-item)
          editing-id-sub (ui/subscription ctx :editing-id)
          editing-id @editing-id-sub]
      [:ul.todo-list
       (for [todo @todos-sub]
         ^{:key (:id todo)}
         [(ui/component ctx :todo-item) todo (= (:id todo) editing-id)])])))

(def component (ui/constructor {:subscription-deps [:todos :editing-id]
                                :component-deps [:todo-item]
                                :renderer render}))
