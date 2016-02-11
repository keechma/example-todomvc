(ns keechma-todomvc.components.todo-input
  (:require [keechma.ui-component :as ui]
            [reagent.core :as reagent :refer [atom]]
            [keechma-todomvc.util :refer [is-enter? is-esc?]]))

(defn focus-input
  "Focuses the input element."
  [x]
  (let [node (reagent/dom-node x)
        length (count (.-value node))]
    (.focus node)
    (.setSelectionRange node length length)))

(defn update-or-cancel
  "Called on each key down.
  
  - on `enter` key it will update the todo
  - on `esc` key it will remove the edit input field"
  [update cancel e]
  (let [key-code (.-keyCode e)]
    (when (is-enter? key-code) (update))
    (when (is-esc? key-code) (cancel))))

(defn render
  "Renders the input element for todo editing. Input field has the
  following event bindings:

  - on blur - update the todo
  - on change - store the current value in the `todo-title` atom
  - on key down - call `update-or-cancel` function which will update the todo
    or remove the input element"
  [ctx todo-sub todo-title]
  (let [todo @todo-sub
        update #(ui/send-command ctx :update-todo (assoc todo :title @todo-title))
        cancel #(ui/send-command ctx :cancel-edit-todo)
        handle-key-down (partial update-or-cancel update cancel)]
    [:input.edit {:value @todo-title 
                  :on-blur update
                  :on-change #(reset! todo-title (.. % -target -value))
                  :on-key-down handle-key-down}]))

(defn make-renderer
  "Create the component using the [Form-3 way](https://github.com/Day8/re-frame/wiki/Creating-Reagent-Components#form-3-a-class-with-life-cycle-methods).

  We have to do it in this way to be able to add a `:component-did-mount` lifecycle function
  which will focus the input field when the component is mounted."
  [ctx]
  (let [todo-sub (ui/subscription ctx :editing-todo)
        todo-title (atom (:title @todo-sub))]
    (reagent/create-class
     {:reagent-render (partial render ctx todo-sub todo-title) 
      :component-did-mount focus-input})))

(def component {:renderer make-renderer
                :subscription-deps [:editing-todo]})
