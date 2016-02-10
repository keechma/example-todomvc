(ns keechma-todomvc.components
  (:require [keechma-todomvc.components.app :as app]
            [keechma-todomvc.components.footer :as footer]
            [keechma-todomvc.components.header :as header]
            [keechma-todomvc.components.todo-item :as todo-item]
            [keechma-todomvc.components.todo-list :as todo-list]
            [keechma-todomvc.components.todo-input :as todo-input]))

(def system {:main app/component
             :header (assoc header/component :topic :todos)
             :todo-item (assoc todo-item/component :topic :todos)
             :todo-list todo-list/component
             :todo-input (assoc todo-input/component :topic :todos)})
