(ns keechma-todomvc.subscriptions
  (:require [keechma-todomvc.edb :as edb]
            [keechma-todomvc.entities.todo :as todo])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn todos [app-db]
  (reaction
   (let [db @app-db
         current-status (keyword (get-in db [:route :data :status]))]
     (todo/todos-by-status @app-db current-status))))

(defn editing-id [app-db]
  (reaction
   (get-in @app-db [:kv :editing-id])))

(defn editing-todo [app-db]
  (reaction
   (let [db @app-db
         editing-id (get-in db [:kv :editing-id])]
     (edb/get-item-by-id @app-db :todos editing-id))))

(defn todos-by-status [app-db status]
  (reaction
   (todo/todos-by-status @app-db status)))

(def subscriptions {:todos todos
                    :editing-id editing-id
                    :editing-todo editing-todo
                    :todos-by-status todos-by-status})
