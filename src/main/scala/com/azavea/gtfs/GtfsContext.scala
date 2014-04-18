package com.azavea.gtfs



/**
 * Provides access to persistent and mutable GTFS data, imbues GTFS classes with extension methods to get related records
 */
trait GtfsContext {
  /**
   * README:
   * What if .. there is no traits
   *
   * What if DAO just HAS something that imports implicits that will bind events to it
   * That way File dao can follow the same code structure, but with different impilcits
   *
   * It will make code will compile and link with different types, find the implicits
   *  Neo ... there IS no interface.
   */

  //What about updates?
  /**
   * Option 1: expose update/insert functions through the context
   *  - User has to know when to call which. Which is a little shitty because context is hiding the fact that there is a DB under all this
   *
   * Option 2: expose persist methods that will do select,insert/update
   *  - Even in transaction this will fail sometimes with parallel writes. This case seems unlikely in current application but it's "bad form"
   *
   * Option 3: expose only insert/update/delete functions
   *  - Look you're either reading or you writing, updating, presumably you're working off the data I gave you so if there is an error its an exception
   *
   *
   ** Option 3 seems most reasonable for a persistence layer.
   */

  //What is the right level for GtfsContext
  /**
   * So some kind of context needs to exist that hooks provides lookup functionality.
   *
   * Why not just nest all the classes?
   *  - This is very wasteful. Route can have hundreds, into thousands, of trips.
   *    Fetching the full tree is not warranted in EVERY case and very expensive to default to
   *
   *  - The relation between routes and trips is a lot more fluid. While trip IS it's stops, route is more of a label
   *
   * Problem: So my slick DAO already implements a lot of the methods I'll need for context get/insert/delete.
   *  How do I build context?
   *
   *  Option 1: Just do a full wrap.
   *  - Duplicating all those methods seems like a code smell through. Especially since they will not be very composable
   *
   *  Option 2: Find some way to to make them implement some context interface.
   *    - this is going to be super tricky because DAO relies on implicit session.
   *    - removing the session is certainly a code smell, complicated combinations/loops will thrash the session pool
   *    + Does not seem to be possible.
   *    + Also it may end up a good cleaner to keep context and DAO separated
   *
   *
   */
}
