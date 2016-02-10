(ns keechma-todomvc.entities.todo
  (:require [keechma-todomvc.edb :as edb])
  (:import [goog.ui IdGenerator]))

(def id-generator (IdGenerator.))

(defn id []
  (.getNextUniqueId id-generator))

(defn create-todo [app-db title]
  (let [todo {:id (str "todo" (id))
              :completed false
              :title title}] 
    (edb/prepend-collection app-db :todos :list [todo])))

(defn edit-todo [app-db todo]
  (assoc-in app-db [:kv :editing-id] (:id todo)))

(defn cancel-edit-todo [app-db]
  (assoc-in app-db [:kv :editing-id] nil))

(defn update-todo [app-db todo]
  (-> app-db
      (cancel-edit-todo)
      (edb/update-item-by-id :todos (:id todo) todo)))

(defn toggle-todo [app-db todo]
  (update-todo app-db (assoc todo :completed (not (:completed todo)))))
