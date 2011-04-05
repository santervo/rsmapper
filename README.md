# rsmapper

Library for mapping flat sql result sets to nested data structures.

## Usage

All functions take result set as first parameter, so mappings can be chained with -> macro:

	(-> rs (map-as-collection ...) (map-as-collection ...) ...)

Function map-as maps some fields of each row in result set as nested map:

	(-> [{:name "Santtu" :city "Helsinki" :country "Finland"}} 
	    (map-as :address [:city :country]))
	
	=> [{:name "Santtu" :address {:city "Helsinki" :country "Finland"}}]

Function map-as-collection maps fields as collection of maps. Function groups rows by other fields values.

	(-> [{:post "Post1" :comment "Comment1"} {:post "Post1" :comment "Comment2"}]
	    (map-as-collection :comments [:comment]))
	
	=> [{:post "Post1" :comments [{:comment "Comment1"} {:comment "Comment2"}]}]

Function collect-as maps a collection of values:

	(-> [{:post "Post1" :tag "Tag1"} {:post "Post1" :tag "Tag2"}]
	    (collect-as :tags :tag))

	=> [{:post "Post1" :tags ["Tag1" "Tag2"]}]

Function include-as maps other result-set as nested collections of result-set rows:

	(-> [{:id 1 :title "Post 1"}]
	    (include-as :comments [{:c_id 1 :title 1 "Comment 1"} {:c_id 1 :title "Comment 2"}] :id :c_id))

	=> [{:id 1 :title "Post 1" :comments [{:c_id 1 :title 1 "Comment 1"} {:c_id 1 :title "Comment 2"}]}]

## Todo

- Mapping fields by prefix
- Ignoring objects with all fields being nil

## License

Copyright (C) 2011 Santtu Lintervo

Distributed under the Eclipse Public License, the same as Clojure.
