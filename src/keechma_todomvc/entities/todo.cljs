(ns keechma-todomvc.entities.todo
  (:require [keechma-todomvc.edb :as edb])
  (:import [goog.ui IdGenerator]))

(def id-generator (IdGenerator.))

(defn id []
  (.getNextUniqueId id-generator))

(defn is-active? [todo]
  (:completed todo))

(defn has-title? [todo]
  (pos? (count (clojure.string/trim (:title todo)))))

(defn create-todo [app-db title]
  (let [todo {:id (str "todo" (id))
              :completed false
              :title title}]
    (if (has-title? todo)
      (edb/prepend-collection app-db :todos :list [todo])
      app-db)))

(defn edit-todo [app-db todo]
  (assoc-in app-db [:kv :editing-id] (:id todo)))

(defn cancel-edit-todo [app-db]
  (assoc-in app-db [:kv :editing-id] nil))

(defn update-todo [app-db todo]
  (if (has-title? todo)
    (-> app-db
        (cancel-edit-todo)
        (edb/update-item-by-id :todos (:id todo) todo))
    app-db))

(defn destroy-todo [app-db todo]
  (edb/remove-item app-db :todos (:id todo)))

(defn toggle-todo [app-db todo]
  (update-todo app-db (assoc todo :completed (not (:completed todo)))))

(defn todos-by-status [app-db status]
  (let [todos (edb/get-collection app-db :todos :list)]
    (case status
      :completed (filter is-active? todos)
      :active (filter (complement is-active?) todos)
      todos)))

(defn toggle-all [app-db status]
  (let [todo-ids (map :id (todos-by-status app-db :all))]
    (reduce #(edb/update-item-by-id %1 :todos %2 {:completed status}) app-db todo-ids)))

(defn destroy-completed [app-db]
  (let [completed-todos (todos-by-status app-db :completed)
        completed-todos-ids (map :id completed-todos)]
    (reduce #(edb/remove-item %1 :todos %2) app-db completed-todos-ids)))
