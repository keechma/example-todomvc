(ns keechma-todomvc.entities.todo
  "# Todo Entities"
  (:require [clojure.string :as str]
            [keechma-todomvc.edb :as edb])
  (:import [goog.ui IdGenerator]))

;; ## Helpers

(def id-generator (IdGenerator.))

(defn id
  "Returns a unique ID for a new todo."
  []
  (.getNextUniqueId id-generator))

(defn valid-title?
  "Returns `true` if `title` is a valid `todo` title."
  [title]
  (not (str/blank? title)))

;; ## Subscription Support (Read)

(defn todos-by-status
  "Returns all `todos` with a `status`."
  [app-db status]
  (let [todos (edb/get-collection app-db :todos :list)]
    (case status
      :completed (filter :completed todos)
      :active (filter (complement :completed) todos)
      :all todos)))

(defn edit-todo
  "Returns the `todo` currently being edited."
  [app-db]
  (->> (get-in app-db [:kv :editing-id])
       (edb/get-item-by-id app-db :todos)))

;; ## Action Support (Write)

;; ### Edit Management

(defn start-edit
  "Sets state: this `todo` is currently being edited."
  [app-db {:keys [id] :as todo}]
  (assoc-in app-db [:kv :editing-id] id))

(defn cancel-edit
  "Clears state: no `todo` is currently being edited."
  [app-db]
  (assoc-in app-db [:kv :editing-id] nil))

(defn confirm-edit
  "Updates `todo` if `new-todo` has a valid `title`."
  [app-db {:keys [id title] :as new-todo}]
  (assert id)
  (if (valid-title? title)
    (-> app-db
        (cancel-edit)
        (edb/insert-item :todos new-todo))
    app-db))

;; ### Create, Update, Delete Todos

(defn create-todos
  "Creates an empty `todos` list."
  [app-db]
  (edb/insert-collection app-db :todos :list []))

(defn create-todo
  "If `title` is valid, creates a new `todo` and inserts it at the
  beginning of the `todo` list."
  [app-db title]
  (if (valid-title? title)
    (let [todo {:id (str "todo" (id))
                :completed false
                :title title}]
      (edb/prepend-collection app-db :todos :list [todo]))
    app-db))

(defn update-todo
  "Merges new data into a `todo`. `patch-todo` must have an `:id`
  value that is the `id` of the `todo` to be updated."
  [app-db {:keys [id] :as patch-todo}]
  (assert id)
  (edb/insert-item app-db :todos patch-todo))

(defn toggle-todo
  "Toggles the `:completed` status of a saved `todo`."
  [app-db todo]
  (update-todo app-db (update todo :completed not)))

(defn toggle-all
  "Marks all `todos` as completed or not based on `completed?`."
  [app-db completed?]
  (reduce (fn [acc todo]
            (update-todo acc (assoc todo :completed completed?)))
          app-db
          (todos-by-status app-db :all)))

(defn delete-todo
  "Removes a `todo` from the EntityDB."
  [app-db {:keys [id] :as todo}]
  (edb/remove-item app-db :todos id))

(defn delete-completed
  "Removes all completed `todos` from the EntityDB."
  [app-db]
  (reduce (fn [acc {:keys [id]}]
            (edb/remove-item acc :todos id))
          app-db
          (todos-by-status app-db :completed)))

;; ## Actions for keechma.controllers/dispatcher

(defn actions
  "Returns a mapping from `todo` topic `command` keywords to `action`
  functions. The mapping is suitable for use by
  `keechma.controller/dispatcher`.

  Adapts the calling convention from the one used by
  `keechma.controllers/dispatcher`:

  `(action-fn app-db-atom args-vec)`

  to one that is more natural for ClojureScript:

  `(handler app-db-atom arg1 arg2 arg3 ...)`.

  Combined with `<cmd` in `ui.cljs`, this allows natural-looking
  arguments in calls to `<cmd` to be handled by a handler with a
  natural looking argument vector in its definition. This insulates
  app developers from the restriction against varags in protocol
  functions that results in `keechma.ui-component/send-command`
  supporting only exactly 2 or 3 args, the last one an optional
  explicit vector of additional args.

  The adapter also provides the side-effect of updating the `app-db`
  with the result of each call, allowing the handlers to be written
  and tested as pure functions that return the new `app-db` value."
  []
  (let [cmd-handlers {:start-edit start-edit
                      :cancel-edit cancel-edit
                      :confirm-edit confirm-edit
                      :create-todo create-todo
                      :delete-todo delete-todo
                      :toggle-todo toggle-todo
                      :toggle-all toggle-all
                      :delete-completed delete-completed}
        action-adapter (fn [f]
                         (fn [app-db args]
                           (apply swap! app-db f args)))]
    (reduce-kv (fn [acc k f]
                 (assoc acc k (action-adapter f)))
               {}
               cmd-handlers)))
