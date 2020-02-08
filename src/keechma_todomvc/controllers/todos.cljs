(ns keechma-todomvc.controllers.todos
  "# Todo Controller

- returns a truthy value from `params` indicating this Controller
  should run throughout the app's lifetime (independent of `route`)
- creates an empty collection of `todos` on `start`
- uses `controller/dispatcher` to handle commands by relaying the
  corresponding action to the `todos` `entity` functions."
  (:require [keechma.controller :as controller]
            [keechma-todomvc.entities.todo :as entity]))

(defrecord Controller [])

(defmethod controller/params Controller
  [_ route-params]
  :always-running)

(defmethod controller/start Controller
  [_ params app-db]
  (entity/create-todos app-db))

(defmethod controller/handler Controller
  [_ app-db-atom in-chan _]
  (controller/dispatcher app-db-atom in-chan (entity/actions)))
