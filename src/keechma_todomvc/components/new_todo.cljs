(ns keechma-todomvc.components.new-todo
  "# New Todo component"
  (:require [keechma-todomvc.ui :refer [<cmd <comp]]
            [keechma-todomvc.util :refer [is-enter?]]
            [reagent.core :as reagent]))

(defn form-2-render
  "## Renders an input field for a new todo

  Stores the pending new `title` inside a local `atom`. When the user
  presses `enter`, sends a `:create-todo` command to the `todos`
  controller to create the new `todo`.

### Form 2 Render

  A `form 2` render function sets up local state and returns a
  rendering function from its first call. That `inner` rendering
  function will be called each time `reagent` needs to render the
  component. The arguments for the `inner` function are the same as
  for the `form 2` render function except that the `ctx` argument is
  not present. The local state can be used by the code in the `inner`
  render function. It will persist until the component is unmounted."
  [ctx]
  (let [new-title (reagent/atom "")]
    (fn []
      [:input.new-todo
       {:placeholder "What needs to be done?"
        :value @new-title
        :auto-focus true
        :on-change #(reset! new-title (.. % -target -value))
        :on-key-down #(when (is-enter? (.-keyCode %))
                        (<cmd ctx :create-todo @new-title)
                        (reset! new-title ""))}])))

(def component
  (<comp :renderer form-2-render))
