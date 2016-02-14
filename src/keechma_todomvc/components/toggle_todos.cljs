(ns keechma-todomvc.components.toggle-todos
  (:require [keechma.ui-component :as ui]))

(defn render
  "Renders the checkbox component which toggles the status of all components."
  [ctx]
  (fn []
    (let [active-sub (ui/subscription ctx :todos-by-status [:active])]
      [:input.toggle-all
       {:type "checkbox"
        :on-change #(ui/send-command ctx :toggle-all (.. % -target -checked))
        :checked (= 0 (count @active-sub))}])))

(def component (ui/constructor {:renderer render
                                :subscription-deps [:todos-by-status]}))
