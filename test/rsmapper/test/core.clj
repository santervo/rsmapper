(ns rsmapper.test.core
  (:use [rsmapper.core] :reload)
  (:use [clojure.test]))

(deftest test-map-as
  (let [expected [{:name "Santtu" :address {:zipcode "00560" :city "Helsinki"}}]
        result-set [{:name "Santtu" :zipcode "00560" :city "Helsinki"}]
        actual (-> result-set (map-as :address [:zipcode :city]))]
    (is (= expected actual))))
 
