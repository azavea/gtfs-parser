#GTFS Parser#

This project is concerned with injesting GTFS files and representing them as an object model.

The current concern is to handle full schedule spec, the abiliy for services to become available and unavailble depending on a date, and frequency spec, for those trips that specified as repeated trips.

The consumable output of the parser is a set of Trips that link to the trip records that generated them and have specific date-time, rather than an offset, for arrival and departure times.
