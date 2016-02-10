(ns keechma-todomvc.components.header
  (:require [keechma.ui-component :as ui]
            [reagent.core :refer [atom]]
            [keechma-todomvc.util :refer [is-enter?]]))

(defn handle-key-down [ctx e new-todo]
  (when (is-enter? (.-keyCode e))
    (do
      (ui/send-command ctx :create-todo @new-todo)
      (reset! new-todo ""))))

(defn render [ctx]
  (let [new-todo (atom "")]
    (fn []
      [:header.header
       [:h1 "todos"]
       [:input.new-todo
        {:placeholder "What needs to be done?"
         :value @new-todo
         :autofocus true
         :on-key-down #(handle-key-down ctx % new-todo)
         :on-change #(reset! new-todo (.. % -target -value))}]])))

(def component (ui/constructor {:renderer render}))
