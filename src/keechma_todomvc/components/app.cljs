(ns keechma-todomvc.components.app
  (:require [keechma.ui-component :as ui]))

(defn render [ctx]
  [:section.todoapp
   [(ui/component ctx :header)]
   [(ui/component ctx :todo-list)]])

(def component (ui/constructor {:renderer render
                                :component-deps [:header :todo-list]}))
