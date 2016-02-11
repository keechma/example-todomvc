(ns keechma-todomvc.components.app
  (:require [keechma.ui-component :as ui]))


(defn render [ctx]
  (fn []
    (let [todos-sub (ui/subscription ctx :todos-by-status [:all])
          has-todos? (pos? (count @todos-sub))]
      [:section.todoapp
       [:header.header
        [:h1 "todos"]
        [(ui/component ctx :new-todo)]]
       (when has-todos? 
         [:section.main
          [(ui/component ctx :toggle-todos)]
          [(ui/component ctx :todo-list)]])
       (when has-todos? [(ui/component ctx :footer)])])))

(def component
  (ui/constructor
   {:renderer render
    :component-deps [:new-todo :todo-list :footer :toggle-todos]
    :subscription-deps [:todos-by-status]}))
