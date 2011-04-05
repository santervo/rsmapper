(ns rsmapper.core)

(defn- row-map-as [k fields row]
  (let [dependent (select-keys row fields)
        row (apply dissoc row fields)]
    (assoc row k dependent)))

(defn map-as [result-set k fields]
  "Map fields in each row of result-set as nested map with key k."
  (map (partial row-map-as k fields) result-set))

(defn collect-as [result-set k ks]
  "Groups rows from result-set by all fields excluding field ks, and collects all ks fields
  as collection keyd k"
  (let [grouping-f (fn [row] (dissoc row ks))
        combiner-f (fn [[row coll-rows]] (assoc row k (map ks coll-rows)))]
    (map combiner-f (group-by grouping-f result-set))))

(defn map-as-collection [result-set k fields]
  "Maps fields in each row of result-set as nested map and collects them with key k."
  (-> result-set (map-as k fields) (collect-as k k)))

(defn include-as [result-set k other id other-id]
  "Groups rows in other by field id in result-set and other-id in other. Associates collections
  with key k to result-set rows."
  (let [collections (group-by other-id other)]
    (map #(assoc % k (collections (% id))) result-set)))

