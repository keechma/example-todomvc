(ns keechma-todomvc.components.new-todo
  (:require [keechma.ui-component :as ui]
            [reagent.core :refer [atom]]
            [keechma-todomvc.util :refer [is-enter?]]))

(defn handle-key-down [ctx e new-todo]
  (when (is-enter? (.-keyCode e))
    (do
      (ui/send-command ctx :create-todo @new-todo)
      (reset! new-todo ""))))

(defn render
  "Renders the input field for the new todo. Stores the todo
  value inside the local atom, and when the user presses enter
  sends the command to creat todo.

  Todo is created by the `todos` controller."
  [ctx]
  (let [new-todo (atom "")]
    (fn [] 
      [:input.new-todo
       {:placeholder "What needs to be done?"
        :value @new-todo
        :autofocus true
        :on-key-down #(handle-key-down ctx % new-todo)
        :on-change #(reset! new-todo (.. % -target -value))}])))

(def component (ui/constructor {:renderer render}))
