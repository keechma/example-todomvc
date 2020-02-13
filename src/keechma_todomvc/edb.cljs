(ns keechma-todomvc.edb
  "# Entity Database

  Defines a function for each key in the the `database abstraction layer`
  (`dbal`) map provided by `entitydb.core`. Each function adapts the
  `dbal` function to accept and update `app-db` instead of just the
  `entity-db` it contains.

## Note

  Uses an explicit list of function names rather than `(keys dbal)`
  because macro expansion happens in separate compilation phase before
  the resulting ClojureScript is compiled. At macro expansion time,
  the keys are not yet available."
  (:require [entitydb.core :as edb])
  (:require-macros [keechma-todomvc.edb :refer [def-accessors]]))

(def dbal (edb/make-dbal {:todos {:id :id}}))

(def-accessors :entity-db dbal
  append-collection
  append-related-collection
  get-collection
  get-collection-meta
  get-item-by-id
  get-item-meta
  get-named-item
  get-named-item-meta
  insert-collection
  insert-item
  insert-meta
  insert-named-item
  insert-related-collection
  prepend-collection
  prepend-related-collection
  remove-collection
  remove-item
  remove-meta
  remove-named-item
  remove-related-collection
  vacuum)
