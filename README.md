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

## Todo

- Mapping fields by prefix
- Ignoring objects with all fields being nil
- Join separate result sets

## License

Copyright (C) 2011 Santtu Lintervo

Distributed under the Eclipse Public License, the same as Clojure.
