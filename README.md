# rsmapper

Library for mapping flat sql result sets to nested data structures.

## Usage

### Mapping single objects

SQL-query:

	SELECT p.name, a.city, a.country FROM person p, address a WHERE p.address_id = address.id

Result set:

	[{:name "Santtu" :city "Helsinki" :country "Finland"}}

Mapping:

	(-> rs (map-as :address [:city :country]))
	
	=> [{:name "Santtu" :address {:city "Helsinki" :country "Finland"}}]

### Todo

- Mapping collections of object
- Mapping collections of values
- Mapping fields by prefix
- Ignoring objects with all fields being nil

## License

Copyright (C) 2011 Santtu Lintervo

Distributed under the Eclipse Public License, the same as Clojure.
