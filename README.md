# rsmapper

Library for mapping flat sql result sets to nested data structures.

## Usage

All functions take result set as first parameter, so mappings can be chained with -> macro:

	(-> rs (nest ...) (collect ...) ...)

Map columns as nested object:

	(-> [{:name "Santtu" :city "Helsinki" :country "Finland"}} 
	    (nest :address [:city :country]))
	
	=> [{:name "Santtu" :address {:city "Helsinki" :country "Finland"}}]

Rename mapped columns:

	(-> [{:name "Santtu" :address_city "Helsinki" :address_country "Finland"}} 
	    (nest :address {:address_city :city :address_country :country}))
	
	=> [{:name "Santtu" :address {:city "Helsinki" :country "Finland"}}]

Map values as collection:

	(-> [{:post "Post1" :tag "Tag1"} {:post "Post1" :tag "Tag2"}]
	    (collect :tags :tag))

	=> [{:post "Post1" :tags ["Tag1" "Tag2"]}]

Join two result sets:

	(-> [{:id 1 :title "Post 1"}]
	    (join :comment [{:c_id 1 :title 1 "Comment 1"} {:c_id 1 :title "Comment 2"}] :id :c_id))

	=> [{:id 1 :title "Post 1" :comment {:c_id 1 :title "Comment 1"}}
	    {:id 1 :title "Post 1" :comment {:c_id 1 :title "Comment 2"}}]

Chain mappings:

	(-> [{:post "Post1" :comment_body "Comment1"} {:post "Post1" :comment_body "Comment2"}]
	    (nest :comment {:comment_body :body})
	    (collect :comments :comment))
	
	=> [{:post "Post1" :comments [{:body "Comment1"} {:body "Comment2"}]}]

## License

Copyright (C) 2011 Santtu Lintervo

Distributed under the Eclipse Public License, the same as Clojure.
