(ns keechma-todomvc.components.footer
  "# Footer component"
  (:require [keechma-todomvc.ui :refer [<cmd <comp route> sub> <url]]))

(defn render
  "## Renders the Footer

- the active `todo` count
- buttons to filter the `todo` list by status
- the `clear-completed` button if there are any completed `todos`

### Route Data

  Reads the `route status` from the current route `data` and uses it
  to highlight the correct filter button as selected.

### Subscription Deps

  Each `todo` has a `status` of either `:completed` or `:active`.

- `:count-todos-by-status` returns the count of `todos` with a `status`
- `:has-todos-by-status?` returns `true` if there are any `todos` with
  a `status`."
  [ctx]
  (let [route-status (keyword (route> ctx :status))
        filter-item (fn [status label]
                      [:li>a
                       {:href (<url ctx :status (name status))
                        :class (when (= status route-status) "selected")}
                       label])
        active-count (sub> ctx :count-todos-by-status :active)
        count-label (str " item" ({1 ""} active-count "s") " left")]
    [:footer.footer
     [:span.todo-count
      [:strong active-count] count-label]
     [:ul.filters
      (filter-item :all "All")
      (filter-item :active "Active")
      (filter-item :completed "Completed")]
     (when (sub> ctx :has-todos-by-status? :completed)
       [:button.clear-completed
        {:on-click #(<cmd ctx :delete-completed)}
        "Clear completed"])]))

(def component
  (<comp :renderer render
         :subscription-deps [:count-todos-by-status
                             :has-todos-by-status?]))
