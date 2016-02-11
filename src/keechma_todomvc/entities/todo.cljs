(ns keechma-todomvc.entities.todo
  (:require [keechma-todomvc.edb :as edb])
  (:import [goog.ui IdGenerator]))

(def id-generator (IdGenerator.))

(defn id
  "Returns a new ID for the todo."
  []
  (.getNextUniqueId id-generator))

(defn is-active?
  "Is a todo in active state?"
  [todo]
  (:completed todo))

(defn has-title?
  "Checks if the todo title is an empty string."
  [todo]
  (pos? (count (clojure.string/trim (:title todo)))))

(defn create-todo
  "Creates a new todo and adds it to a todos list if
  the todo has a non empty title."
  [app-db title]
  (let [todo {:id (str "todo" (id))
              :completed false
              :title title}]
    (if (has-title? todo)
      (edb/prepend-collection app-db :todos :list [todo])
      app-db)))

(defn edit-todo
  "Saves the id of the todo that is being edited."
  [app-db todo]
  (assoc-in app-db [:kv :editing-id] (:id todo)))

(defn cancel-edit-todo
  "Clears the id of the currently edited todo."
  [app-db]
  (assoc-in app-db [:kv :editing-id] nil))

(defn update-todo
  "Updates the todo with new data if the todo has a non empty title."
  [app-db todo]
  (if (has-title? todo)
    (-> app-db
        (cancel-edit-todo)
        (edb/update-item-by-id :todos (:id todo) todo))
    app-db))

(defn destroy-todo
  "Removes the todo from the EntityDB."
  [app-db todo]
  (edb/remove-item app-db :todos (:id todo)))

(defn toggle-todo
  "Toggles the `:completed` status."
  [app-db todo]
  (update-todo app-db (assoc todo :completed (not (:completed todo)))))

(defn todos-by-status
  "Returns the todos for a status."
  [app-db status]
  (let [todos (edb/get-collection app-db :todos :list)]
    (case status
      :completed (filter is-active? todos)
      :active (filter (complement is-active?) todos)
      todos)))

(defn toggle-all
  "Marks all todos as active or completed based on the `status` argument."
  [app-db status]
  (let [todo-ids (map :id (todos-by-status app-db :all))]
    (reduce #(edb/update-item-by-id %1 :todos %2 {:completed status}) app-db todo-ids)))

(defn destroy-completed
  "Removes all completed todos from the EntityDB."
  [app-db]
  (let [completed-todos (todos-by-status app-db :completed)
        completed-todos-ids (map :id completed-todos)]
    (reduce #(edb/remove-item %1 :todos %2) app-db completed-todos-ids)))
