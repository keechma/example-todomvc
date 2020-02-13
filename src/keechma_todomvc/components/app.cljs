(ns keechma-todomvc.components.app
  "# Main app component"
  (:require [keechma-todomvc.ui :refer [<comp comp> sub>]]))

(defn render
  "## Renders the top level UI

  Some elements are rendered inline, others are implemented as
  `components`. Each `component` will have its own `context` provided.

### Component Deps

- `:new-todo` top field where new `todos` are entered
- `:toggle-todos` checkbox to the left of `:new-todo`
- `:todo-list` main body list of `todos`
- `:footer` active count, filtering, clearing

### Subscription Deps

- `:has-todos?` returns true if there are any todos in the EntityDB."
  [ctx]
  [:<>
   [:section.todoapp
    [:header.header
     [:h1 "todos"]
     [comp> ctx :new-todo]]
    (when (sub> ctx :has-todos?)
      [:<>
       [:section.main
        [comp> ctx :toggle-todos]
        [comp> ctx :todo-list]]
       [comp> ctx :footer]])]
   [:footer.info
    [:p "Double-click to edit a todo"]
    [:p
     [:a {:href "https://keechma.com"} "Keechma"] " "
     [:a {:href "http://todomvc.com"} "TodoMVC"]]]])

(def component
  (<comp :renderer render
         :component-deps [:new-todo
                          :toggle-todos
                          :todo-list
                          :footer]
         :subscription-deps [:has-todos?]))
