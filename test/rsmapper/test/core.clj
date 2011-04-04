(ns rsmapper.test.core
  (:use [rsmapper.core] :reload)
  (:use [clojure.test]))


(deftest test-map-as
  (let [expected [{:name "Santtu" :address {:zipcode "00560" :city "Helsinki"}}]
        result-set [{:name "Santtu" :zipcode "00560" :city "Helsinki"}]
        actual (-> result-set (map-as :address [:zipcode :city]))]
    (is (= expected actual))))

(deftest test-map-as-collection
  (testing "mapping collection"
    (let [expected [{:title "Post 1" :comments [{:comment_title "Comment 1"} {:comment_title "Comment 2"}]}
                    {:title "Post 2" :comments [{:comment_title "Comment"}]}]
          result-set [{:title "Post 1" :comment_title "Comment 1"}
                      {:title "Post 1" :comment_title "Comment 2"}
                      {:title "Post 2" :comment_title "Comment"}]
          actual (-> result-set (map-as-collection :comments [:comment_title]))]
      (is (= expected actual))))
  (testing "mapping two collections"
    (let [expected [{:title "Post 1" 
                     :comments [{:comment_title "Comment 1"} {:comment_title "Comment 2"}]
                     :tags [{:tag_name "Tag1"} {:tag_name "Tag2"}]}]
          result-set [{:title "Post 1" :comment_title "Comment 1" :tag_name "Tag1"}
                      {:title "Post 1" :comment_title "Comment 2" :tag_name "Tag1"}
                      {:title "Post 1" :comment_title "Comment 1" :tag_name "Tag2"}
                      {:title "Post 1" :comment_title "Comment 2" :tag_name "Tag2"}]
          actual (-> result-set 
                   (map-as-collection :comments [:comment_title])
                   (map-as-collection :tags [:tag_name]))]
      (is (= expected actual)))))

