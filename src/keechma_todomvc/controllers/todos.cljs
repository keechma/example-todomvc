(ns keechma-todomvc.controllers.todos
  (:require [keechma.controller :as controller :refer [dispatcher]]
            [cljs.core.async :refer [<!]]
            [keechma-todomvc.edb :as edb]
            [keechma-todomvc.entities.todo :as todo])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn updater! [modifier-fn]
  (fn [app-db-atom args]
    (reset! app-db-atom (modifier-fn @app-db-atom args))))

(defrecord Controller []
  controller/IController
  (params [_ _] true)
  (start [_ params app-db]
    (edb/insert-collection app-db :todos :list []))
  (handler [_ app-db-atom in-chan _]
    (dispatcher app-db-atom in-chan
                {:toggle-todo (updater! todo/toggle-todo)
                 :create-todo (updater! todo/create-todo)
                 :update-todo (updater! todo/update-todo)
                 :destroy-todo (updater! todo/destroy-todo)
                 :edit-todo (updater! todo/edit-todo)
                 :cancel-edit-todo (updater! todo/cancel-edit-todo)
                 :destroy-completed (updater! todo/destroy-completed)
                 :toggle-all (updater! todo/toggle-all)})))
