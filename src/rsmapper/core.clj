(ns rsmapper.core)

(defn- row-map-as [k fields row]
  (let [dependent (select-keys row fields)
        row (apply dissoc row fields)]
    (assoc row k dependent)))

(defn map-as [result-set k fields]
  (map (partial row-map-as k fields) result-set))
