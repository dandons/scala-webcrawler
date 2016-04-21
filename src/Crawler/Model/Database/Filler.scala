package Crawler.Model.Database

import Crawler.Model.Database.Dbo.AbstractDbo
import Crawler.Model.Dbo.{Website, FoundWord, KeyWord}
import scala.util.control.Breaks

/**
  * A filler class to add the found words into the queue and dequeue it and putting the found words in the database.
  * @param connector
  */
class Filler(connector: Connector) {
  val limit: Int = 100

  /**
    * Insert a record in the database
    * @param dbo
    */
  def insert(dbo: AbstractDbo): Unit = {
    // Create a new statement for the batch
    connector.newStatement()

    // Start new loop, optional break if there is not item in the queue.
    val loop = new Breaks
    loop.breakable {
      for (a <- 1 to limit) {                         // Start inserting to maximum of the limit. (limit = batch size)
        if (dbo.queue.nonEmpty) {
          val record = dbo.queue.dequeue()            // dequeue item
          if (record != null) {                       // If there is a record; continue
            connector.query(Website.insert(record))   // insert website
            connector.query(KeyWord.insert(record))   // insert key word
            connector.query(FoundWord.insert(record)) // insert found word
          } else {
            loop.break()
          }
        }
      }
      connector.executeStatement()                    // execute the batch
    }
  }

  /**
    * Add a record to the queue
    * @param words
    * @param url
    */
  def enqueue(words: Array[String], url: String): Unit = {
    for (keyWord <- words) {
      if (keyWord.length > 3) {                       // filter basic words todo: the filter can be way better.
        FoundWord.queue.enqueue(Array(url, keyWord))  // enqueue website and word.
      }
    }
  }


}
