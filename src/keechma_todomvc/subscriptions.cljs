(ns keechma-todomvc.subscriptions
  "# Todo Subscriptions

  Subscription functions return `reagent reactions` that cause
  subscribed UI `components` to re-render when the value managed by
  the `reaction` changes."
  (:require [keechma-todomvc.entities.todo :as todo]
            [reagent.ratom :refer-macros [reaction]]))

;; ## Todos List

(defn todos-by-status
  "Returns a `reaction` on the list of `todos` with a `status`."
  [app-db status]
  (reaction
   (todo/todos-by-status @app-db status)))

(defn count-todos-by-status
  "Derived `reaction` that returns the `count` of `todos` with a
  `status`. Fires only when the `count` changes, independent of the
  details of the list contents."
  [app-db status]
  (reaction
   (count @(todos-by-status app-db status))))

(defn has-todos-by-status?
  "Derived `reaction` that returns `true` if any `todos` with a `status`
  exist. Fires only on a transition either way between `none` and
  `some`, independent of the details of the list contents."
  [app-db status]
  (reaction
   (some? (seq @(todos-by-status app-db status)))))

(defn has-todos?
  "Returns a `reaction` that indicates if there are currently any `todos`
  at all."
  [app-db]
  (has-todos-by-status? app-db :all))

;; ## Todo Editing

(defn edit-todo
  "Returns a `reaction` on the `todo` that is being edited."
  [app-db]
  (reaction
   (todo/edit-todo @app-db)))

(def subscriptions {:todos-by-status todos-by-status
                    :count-todos-by-status count-todos-by-status
                    :has-todos-by-status? has-todos-by-status?
                    :has-todos? has-todos?
                    :edit-todo edit-todo})
