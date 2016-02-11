(ns keechma-todomvc.subscriptions
  (:require [keechma-todomvc.edb :as edb]
            [keechma-todomvc.entities.todo :as todo])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn todos
  "Based on the current route, returns the list of todos."
  [app-db]
  (reaction
   (let [db @app-db
         current-status (keyword (get-in db [:route :data :status]))]
     (todo/todos-by-status @app-db current-status))))

(defn editing-id
  "Returns the id of the todo that is being edited."
  [app-db]
  (reaction
   (get-in @app-db [:kv :editing-id])))

(defn editing-todo
  "Returns the todo that is being edited."
  [app-db]
  (reaction
   (let [db @app-db
         editing-id (get-in db [:kv :editing-id])]
     (edb/get-item-by-id @app-db :todos editing-id))))

(defn todos-by-status
  "Returns the list of todos for passed status."
  [app-db status]
  (reaction
   (todo/todos-by-status @app-db status)))

(def subscriptions {:todos todos
                    :editing-id editing-id
                    :editing-todo editing-todo
                    :todos-by-status todos-by-status})
