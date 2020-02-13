(ns keechma-todomvc.app
  "# Keechma TodoMVC App"
  (:require [keechma-todomvc.components :as components]
            [keechma-todomvc.controllers.todos :as todos]
            [keechma-todomvc.subscriptions :as subscriptions]
            [keechma.app-state :as app-state]))

(defonce running-app (atom nil))

;; ## App Definition

(def app-definition
  "Pulls the app together."
  {:routes [[":status" {:status "all"}]]
   :controllers {:todos (todos/->Controller)}
   :components components/system
   :subscriptions subscriptions/subscriptions
   :html-element (.getElementById js/document "app")})

;; ## App Lifecycle

(defn start!
  "Starts the app."
  []
  (reset! running-app (app-state/start! app-definition)))

(defn restart!
  "Restarts the app. `project.clj` contains a config setting for
  `figwheel` requesting a call to this function whenever the code is
  reloaded."
  []
  (if-let [current @running-app]
    (app-state/stop! current start!)
    (start!)))

(defn dev-setup
  "Enables console output for dev builds."
  []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

;; ## Main

(defn ^:export main
  "Called from `index.html` to start the app. Exported because
  `index.html` refers to it by name even in non-dev builds."
  []
  (dev-setup)
  (start!))
