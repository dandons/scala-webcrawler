package Crawler.Model.Database.Dbo

import scala.collection.mutable

/**
  * An trait for basic guide lines of a Dba
  */
trait AbstractDbo {

  /**
    * Used for independent SQL insertion
    */
  var queue: mutable.Queue[Array[String]] = new mutable.Queue[Array[String]]()

  /**
    * Insert statement for a single record.
    * @param values
    * @return
    */
  def insert(values: Array[String]): String

  /**
    * Escape function to remove chars that breaks the string
    * @param raw
    * @return
    */
  def escape(raw: String): String = {
    raw.replaceAll("\'", "\\\\'").replaceAll("\"", "\\\\\"").replaceAll(",", "")
  }
}
