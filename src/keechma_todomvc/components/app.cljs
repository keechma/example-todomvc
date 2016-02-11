(ns keechma-todomvc.components.app
  (:require [keechma.ui-component :as ui]))


(defn render
  "Main app component. Renders all the other components.

  Depends on the `:todos-by-status` subscription which returns
  the list of todos for a status. This is used to check if there
  are any todos in the EntityDB.

  This component depends on `:new-todo`, `:todo-list`, `:footer`
  and `:toggle-todos` components. Each of these components has
  it's own context passed in."
  [ctx]
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
