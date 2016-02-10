(ns keechma-todomvc.edb
  (:require [keechma.edb :as edb]))

(def dbal (edb/make-dbal {:todos {:id :id}}))

(defn wrap-entity-db-get [dbal-fn]
  (fn [db & rest]
    (let [entity-db (:entity-db db)]
      (apply dbal-fn (concat [entity-db] rest)))))

(defn wrap-entity-db-mutate [dbal-fn]
  (fn [db & rest]
    (let [entity-db (:entity-db db)
          resulting-entity-db (apply dbal-fn (concat [entity-db] rest))]
      (assoc db :entity-db resulting-entity-db))))

(def insert-item (wrap-entity-db-mutate (:insert-item dbal)))
(def insert-named-item (wrap-entity-db-mutate (:insert-named-item dbal)))
(def insert-collection (wrap-entity-db-mutate (:insert-collection dbal)))
(def insert-meta (wrap-entity-db-mutate (:insert-meta dbal)))
(def append-collection (wrap-entity-db-mutate (:append-collection dbal)))
(def prepend-collection (wrap-entity-db-mutate (:prepend-collection dbal)))
(def remove-item (wrap-entity-db-mutate (:remove-item dbal)))
(def remove-named-item (wrap-entity-db-mutate (:remove-named-item dbal)))
(def remove-collection (wrap-entity-db-mutate (:remove-collection dbal)))
(def remove-meta (wrap-entity-db-mutate (:remove-meta dbal)))
(def get-item-by-id (wrap-entity-db-get (:get-item-by-id dbal)))
(def get-named-item (wrap-entity-db-get (:get-named-item dbal)))
(def get-collection (wrap-entity-db-get (:get-collection dbal)))
(def get-item-meta (wrap-entity-db-get (:get-item-meta dbal)))
(def get-named-item-meta (wrap-entity-db-get (:get-named-item-meta dbal)))
(def get-collection-meta (wrap-entity-db-get (:get-collection-meta dbal)))
(def vacuum (wrap-entity-db-mutate (:vacuum dbal)))

(defn update-item-by-id [db entity-kw id data]
  (let [item (get-item-by-id db entity-kw id)]
    (insert-item db entity-kw (merge item data))))

(defn collection-empty? [collection]
  (let [collection-meta (meta collection)]
    (and (= (:state collection-meta) :completed)
         (= (count collection) 0))))
