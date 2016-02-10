(ns keechma-todomvc.subscriptions
  (:require [keechma-todomvc.edb :as edb])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn todos [app-db]
  (reaction
   (edb/get-collection @app-db :todos :list)))

(defn editing-id [app-db]
  (reaction
   (get-in @app-db [:kv :editing-id])))

(defn editing-todo [app-db]
  (reaction
   (let [db @app-db
         editing-id (get-in db [:kv :editing-id])]
     (edb/get-item-by-id @app-db :todos editing-id))))

(def subscriptions {:todos todos
                    :editing-id editing-id
                    :editing-todo editing-todo})
