(ns rsmapper.core)

(defn- row-map-as [k fields row]
  (let [dependent (select-keys row fields)
        row (apply dissoc row fields)]
    (assoc row k dependent)))

(defn map-as [result-set k fields]
  (map (partial row-map-as k fields) result-set))

(defn map-as-collection [result-set k fields]
  (let [result-set (map-as result-set k fields)
        grouping-f (fn [row] (dissoc row k))
        combiner-f (fn [[row coll-rows]] (assoc row k (map k coll-rows)))]
    (map combiner-f (group-by grouping-f result-set))))
