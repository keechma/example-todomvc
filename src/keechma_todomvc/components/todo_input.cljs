(ns keechma-todomvc.components.todo-input
  (:require [keechma.ui-component :as ui]
            [reagent.core :as reagent :refer [atom]]
            [keechma-todomvc.util :refer [is-enter? is-esc?]]))

(defn focus-input [x]
  (let [node (reagent/dom-node x)
        length (count (.-value node))]
    (.focus node)
    (.setSelectionRange node length length)))

(defn update-or-cancel [update cancel e]
  (let [key-code (.-keyCode e)]
    (when (is-enter? key-code) (update))
    (when (is-esc? key-code) (cancel))))

(defn render [ctx todo-sub todo-title]
  (let [todo @todo-sub
        update #(ui/send-command ctx :update-todo (assoc todo :title @todo-title))
        cancel #(ui/send-command ctx :cancel-edit-todo)
        handle-key-down (partial update-or-cancel update cancel)]
    [:input.edit {:value @todo-title 
                  :on-blur update
                  :on-change #(reset! todo-title (.. % -target -value))
                  :on-key-down handle-key-down}]))

(defn make-renderer [ctx]
  (let [todo-sub (ui/subscription ctx :editing-todo)
        todo-title (atom (:title @todo-sub))]
    (reagent/create-class
     {:reagent-render (partial render ctx todo-sub todo-title) 
      :component-did-mount focus-input})))

(def component {:renderer make-renderer
                :subscription-deps [:editing-todo]})
