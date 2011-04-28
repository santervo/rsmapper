(ns rsmapper.core
  (:require [clojure.set]))

(defn- nest-row [k cols]
  (fn [row]
    (let [nested (select-keys row cols)
          row (apply dissoc row cols)]
      (assoc row k nested))))

(defn- rename-keys-in [k col-map]
  (fn [row]
    (update-in row [k] clojure.set/rename-keys col-map)))

(defn nest [rs k cols]
  "Map columns in each row of result-set as nested map with key k."
  (if (map? cols)
    (map (rename-keys-in k cols) (map (nest-row k (keys cols)) rs))
    (map (nest-row k cols) rs)))

(defn- all-but [k]
  (fn [m] (dissoc m k)))

(defn- map-val [f]
  (fn [e] [(first e) (map f (second e))]))

(defn- assoc-val [k]
  (fn [e] (assoc (first e) k (second e))))

(defn collect [rs coll-key k]
  "Groups rows from result-set by all fields excluding column k, 
  and collects all values of key k as collection of key coll-key." 
  (map (assoc-val coll-key) (map (map-val k) (group-by (all-but k) rs))))

(defn join [rs k other-rs matching-f grouping-f]
  "Joins rs with other-rs by creating a new map for every row r1 in rs
  and row r2 in other-rs where (= (matching-f r1) (grouping-f r2)) so that
  r2 is associated with key k to r1. If no row in other-rs matches a row r in rs,
  then nil is associated with key k to r. Keywords make excellent candidates
  for matching-f and grouping-f."
  (let [collections (group-by grouping-f other-rs)]
    (for [row rs
          other-row (or (get collections (matching-f row) [nil]))]
      (assoc row k other-row))))

