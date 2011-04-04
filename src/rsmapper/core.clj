(ns rsmapper.core)

(defn- row-map-as [k fields row]
  (let [dependent (select-keys row fields)
        row (apply dissoc row fields)]
    (assoc row k dependent)))

(defn map-as [result-set k fields]
  (map (partial row-map-as k fields) result-set))

(defn collect-as [result-set k ks]
  (let [grouping-f (fn [row] (dissoc row ks))
        combiner-f (fn [[row coll-rows]] (assoc row k (map ks coll-rows)))]
    (map combiner-f (group-by grouping-f result-set))))

(defn map-as-collection [result-set k fields]
  (-> result-set (map-as k fields) (collect-as k k)))
