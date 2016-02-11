(ns keechma-todomvc.core
  (:require [keechma.app-state :as app-state]
            [keechma-todomvc.controllers.todos :as todos]
            [keechma-todomvc.components :as components]
            [keechma-todomvc.subscriptions :as subscriptions]))

(enable-console-print!)

(def app-definition
  "Defines the application."
  {:routes [[":status" {:status "all"}]]
   :controllers {:todos (todos/->Controller)}
   :components components/system
   :subscriptions subscriptions/subscriptions
   :html-element (.getElementById js/document "app")})
 
(defonce running-app (clojure.core/atom))

(defn start-app!
  "Helper function that starts the application."
  []
  (reset! running-app (app-state/start! app-definition)))

(defn restart-app!
  "Helper function that restarts the application whenever the
  code is hot reloaded."
  []
  (let [current @running-app]
    (if current
      (app-state/stop! current start-app!)
      (start-app!))))

(restart-app!)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
