(ns keechma-todomvc.ui
  "# UI Convenience functions

  Adapted from and inspired by `keechma.toolbox.ui`."
  (:require [keechma.ui-component :as ui]))

;; ## Component Constructor

(defn <comp
  "Constructs a Keechma `component`. Args can be a `map` or inline
  `keys` and `values`."
  ([kvs]
   (ui/constructor kvs))
  ([k v & {:as kvs}]
   (<comp (assoc kvs k v))))

;; ## Context Functions

;; ### Export

(defn <cmd
  "Sends a `command` via the `command channel` for `ctx`. Gathers up any
  `args` beyond the `command` into an explicit `vector` for
  transport."
  [ctx command & args]
  (ui/send-command ctx command args))

(defn <url
  "Builds a URL from `ctx` and params. Params can be specified by a
  `map` or inline `keys` and `values`."
  ([ctx kvs]
   (ui/url ctx kvs))
  ([ctx k v & {:as kvs}]
   (<url ctx (assoc kvs k v))))

;; ### Import

(defn comp>
  "Used to embed a `component` within another. Returns a `hiccup` vector
  requesting a call to the `component` specified by `key` on each
  render. Additional args are gathered into an explicit `vector` for
  `ui/component`."
  [ctx key & args]
  (apply vector (ui/component ctx key) args))

(defn sub>
  "Reads and derefs a `ctx` subscription."
  [ctx subscription & args]
  @(ui/subscription ctx subscription args))

(defn route>
  "Reads current route `data` from `ctx`. `args` are an inline `key
  path` within the route map's `:data` value."
  [ctx & args]
  (get-in (:data @(ui/current-route ctx)) args))
