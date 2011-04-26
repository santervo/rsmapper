(ns rsmapper.core)

(defn- nest-row [row k cols]
  (let [nested (select-keys row cols)
        row (apply dissoc row cols)]
    (assoc row k nested)))

(defn nest [result-set k cols]
  "Map columns in each row of result-set as nested map with key k."
  (map (partial row-map-as k cols) result-set))

(defn collect [result-set k ks]
  "Groups rows from result-set by all fields excluding field ks, and collects all ks fields
  as collection keyd k"
  (let [grouping-f (fn [row] (dissoc row ks))
        combiner-f (fn [[row coll-rows]] (assoc row k (map ks coll-rows)))]
    (map combiner-f (group-by grouping-f result-set))))

(defn join [rs k other-rs matching-f grouping-f]
  "Joins rs with other-rs by creating a new map for every row r1 in rs
  and row r2 in other-rs where (= (matching-f r1) (grouping-f r2)) so that
  r2 is associated with key k to r1. If no row in other-rs matches a row r in rs,
  then nil is associated with key k to r. Keywords make excellent candidates
  for matching-f and grouping-f."
  (let [collections (group-by grouping-f other-rs)]
    (for [row rs
          other-row (or (collections (matching-f row) [nil]))]
      (assoc row k other-row))))

