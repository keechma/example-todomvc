(ns keechma-todomvc.components.todo-item
  (:require [keechma.ui-component :as ui]))

(defn classes
  "Helper function that returns the `li` element clasess based on `is-editing?` and
  `completed?` arguments."
  [is-editing? completed?]
  (clojure.string/join " " (remove nil? [(when is-editing? "editing")
                                         (when completed? "completed")])))

(defn render
  "Renders on todo item. If this item is currently being edited, renders the
  edit input element."
  [ctx todo is-editing?]
  [:li {:class (classes is-editing? (:completed todo))}
   [:div.view {:on-double-click #(ui/send-command ctx :edit-todo todo)}
    [:input.toggle {:type "checkbox"
                    :checked (:completed todo)
                    :on-change #(ui/send-command ctx :toggle-todo todo)}]
    [:label (:title todo)]
    [:button.destroy {:on-click #(ui/send-command ctx :destroy-todo todo)}]]
   (when is-editing?
     [(ui/component ctx :todo-input)])])

(def component (ui/constructor {:renderer render
                                :component-deps [:todo-input]}))
