(ns keechma-todomvc.components
  (:require [keechma-todomvc.components.app :as app]
            [keechma-todomvc.components.footer :as footer]
            [keechma-todomvc.components.new-todo :as new-todo]
            [keechma-todomvc.components.todo-item :as todo-item]
            [keechma-todomvc.components.todo-list :as todo-list]
            [keechma-todomvc.components.todo-input :as todo-input]
            [keechma-todomvc.components.toggle-todos :as toggle-todos]))

(def system {:main app/component
             :new-todo (assoc new-todo/component :topic :todos)
             :footer (assoc footer/component :topic :todos)
             :todo-item (assoc todo-item/component :topic :todos)
             :todo-list todo-list/component
             :todo-input (assoc todo-input/component :topic :todos)
             :toggle-todos (assoc toggle-todos/component :topic :todos)})
