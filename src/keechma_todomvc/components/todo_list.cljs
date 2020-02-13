(ns keechma-todomvc.components.todo-list
  "# Todo List component"
  (:require [keechma-todomvc.ui :refer [<comp comp> route> sub>]]))

(defn render
  "## Renders a list of currently visible todos

  `todo` visiblity is controlled by the current `route`.

### Component Deps

- `:todo-item` Each list item is rendered by a `:todo-item` component
  that receives the `todo` and the calculated `is-editing?` value as
  arguments.

### Subscription Deps

- `:todos-by-status` returns `todos` with a `status`
- `:edit-todo` returns the `todo` currently being edited, or nil"
  [ctx]
  (let [route-status (keyword (route> ctx :status))
        is-editing? (fn [id] (= id (:id (sub> ctx :edit-todo))))
        todo-item (fn [{id :id :as todo}]
                    ^{:key id} [comp> ctx :todo-item todo (is-editing? id)])]
    [:ul.todo-list
     (doall (map todo-item (sub> ctx :todos-by-status route-status)))]))

(def component
  (<comp :renderer render
         :component-deps [:todo-item]
         :subscription-deps [:todos-by-status
                             :edit-todo]))
