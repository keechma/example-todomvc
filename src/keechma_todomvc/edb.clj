(ns keechma-todomvc.edb
  "# Entity Database Wrappers

  Wraps EntityDB API functions to adapt them for the case where the
  entity-db is the value at a key in an enclosing map")

(defmacro def-accessors
  "Defines convenience functions that wrap `entity-db` functions to
  allow the `app-db` to be passed in and updated rather than just the
  `entity-db` itself. Takes advantage of the consistent convention
  used to name `entity-db` functions to distinguish `getters` from
  `setters`. Uses an appropriate wrapper for each: `getters` return
  the value, `setters` return an updated app-db."
  [edb-key dbal & fn-names]
  `(do
     ~@(for [fn-name fn-names
             :let [fn-key (keyword fn-name)]]
         (if (.startsWith (name fn-name) "get-")
           `(defn ~fn-name [db# & args#]
              (apply (~fn-key ~dbal) (~edb-key db#) args#))
           `(defn ~fn-name [db# & args#]
              (assoc db# ~edb-key
                     (apply (~fn-key ~dbal) (~edb-key db#) args#)))))))
