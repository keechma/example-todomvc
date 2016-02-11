(ns keechma-todomvc.components.footer
  (:require [keechma.ui-component :as ui]))

(defn items-label [count]
  (if (= count 1) "item" "items"))

(defn render [ctx]
  (fn []
    (let [current-status (get-in @(ui/current-route ctx) [:data :status])
          completed-sub (ui/subscription ctx :todos-by-status [:completed])
          active-sub (ui/subscription ctx :todos-by-status [:active])
          active @active-sub
          active-class #(when (= % current-status) "selected")
          active-count (count active)]
      [:footer.footer
       [:span.todo-count
        [:strong active-count] (str " " (items-label active-count) " left")]
       [:ul.filters
        [:li>a {:href (ui/url ctx {:status "all"})
                :class (active-class "all")} "All"]
        [:li>a {:href (ui/url ctx {:status "active"})
                :class (active-class "active")} "Active"]
        [:li>a {:href (ui/url ctx {:status "completed"})
                :class (active-class "completed")} "Completed"]]
       (when (pos? (count @completed-sub))
         [:button.clear-completed
          {:on-click #(ui/send-command ctx :destroy-completed)}
          "Clear completed"])])))

(def component (ui/constructor {:renderer render
                                :subscription-deps [:todos-by-status]}))
