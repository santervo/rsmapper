(ns rsmapper.test.core
  (:use [rsmapper.core] :reload)
  (:use [clojure.test]))

(deftest test-nest
  (testing "nesting columns"
    (let [expected [{:name "Santtu" :address {:zipcode "00560" :city "Helsinki"}}]
          result-set [{:name "Santtu" :zipcode "00560" :city "Helsinki"}]
          actual (-> result-set (nest :address [:zipcode :city]))]
      (is (= expected actual))))
  (testing "nesting and renaming columns"
    (let [expected [{:name "Santtu" :address {:zipcode "00560" :city "Helsinki"}}]
          result-set [{:name "Santtu" :address_zipcode "00560" :address_city "Helsinki"}]
          actual (-> result-set (nest :address {:address_zipcode :zipcode :address_city :city}))]
      (is (= expected actual)))))

(deftest test-collect
  (testing "collecting list of values"
    (let [expected [{:name "Joe" :emails ["joe@example.com" "test@example.com"]}]
          result-set [{:name "Joe" :email "joe@example.com"}
                      {:name "Joe" :email "test@example.com"}]
          actual (-> result-set (collect :emails :email))]
      (is (= expected actual))))
  (testing "collecting two lists of values (joined dataset, cartesian product)"
    (let [expected [{:name "Joe" 
                     :emails ["joe@example.com" "test@example.com"]
                     :websites ["joe.example.com" "joesblog.example.com"]}]
          result-set [{:name "Joe" :email "joe@example.com" :website "joe.example.com"}
                      {:name "Joe" :email "joe@example.com" :website "joesblog.example.com"}
                      {:name "Joe" :email "test@example.com" :website "joe.example.com"}
                      {:name "Joe" :email "test@example.com" :website "joesblog.example.com"}]
          actual (-> result-set (collect :emails :email) (collect :websites :website))]
      (is (= expected actual)))))
 
(deftest test-join
  (testing "two posts with one comment each"
    (let [expected [{:id 1 :title "Post 1" :comment {:post_id 1 :title "Comment"}}
                    {:id 2 :title "Post 2" :comment {:post_id 2 :title "Comment"}}]
          post-result-set [{:id 1 :title "Post 1"} {:id 2 :title "Post 2"}]
          comment-result-set [{:post_id 1 :title "Comment"} {:post_id 2 :title "Comment"}]
          actual (-> post-result-set (join :comment comment-result-set :id :post_id))]
      (is (= expected actual))))
  (testing "one post with two comments"
    (let [expected [{:id 1 :title "Post" :comment {:post_id 1 :title "Comment 1"}}
                    {:id 1 :title "Post" :comment {:post_id 1 :title "Comment 2"}}]
          post-result-set [{:id 1 :title "Post"}]
          comment-result-set [{:post_id 1 :title "Comment 1"} 
                              {:post_id 1 :title "Comment 2"}]
          actual (-> post-result-set (join :comment comment-result-set :id :post_id))]
      (is (= expected actual))))
  (testing "one post with no comments"
    (let [expected [{:id 1 :title "Post 1" :comment nil}]
          post-result-set [{:id 1 :title "Post 1"}]
          comment-result-set []
          actual (-> post-result-set (join :comment comment-result-set :id :post_id))]
    (is (= expected actual)))))

(deftest test-chaining-commands
  (let [expected [{:title "Post" 
                   :comments [{:comment_title "Comment 1"} {:comment_title "Comment 2"}]}]
        rs [{:title "Post" :comment_title "Comment 1"}
            {:title "Post" :comment_title "Comment 2"}]
        actual (-> rs (nest :comment [:comment_title]) (collect :comments :comment))]
      (is (= expected actual))))


