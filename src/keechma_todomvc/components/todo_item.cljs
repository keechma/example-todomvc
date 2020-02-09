(ns keechma-todomvc.components.todo-item
  "# Todo Item component"
  (:require [keechma-todomvc.ui :refer [<cmd <comp comp>]]))

(defn render
  "## Renders a todo list item

  The item's content includes:

- a `checkbox` for toggling `:completed`
- a `label` displaying the `:title`
- a `button` to delete the item

If the item is currently being edited, overlays a `todo-edit` component.

### Component Deps

- `:todo-edit` presents ui to edit a `todo`'s title

### Arguments

  This render function has arguments in addition to `ctx`. Values for
  those are provided at the site where this component is included
  within another, in this case `:todo-list`. If this were a `form-2`
  render function, the returned function would have only `todo` and
  `is-editing?` as arguments."
  [ctx todo is-editing?]
  [:li {:class [(when is-editing? :editing)
                (when (:completed todo) :completed)]}
   [:div.view {:on-double-click #(<cmd ctx :start-edit todo)}
    [:input.toggle {:type :checkbox
                    :checked (:completed todo)
                    :on-change #(<cmd ctx :toggle-todo todo)}]
    [:label (:title todo)]
    [:button.destroy {:on-click #(<cmd ctx :delete-todo todo)}]]
   (when is-editing?
     [comp> ctx :todo-edit])])

(def component
  (<comp :renderer render
         :component-deps [:todo-edit]))
