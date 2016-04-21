package Crawler.Model.Dbo

import Crawler.Model.Database.Dbo.AbstractDbo

/**
  * Key Word Dbo object
  */
object Website extends AbstractDbo {

  /**
    * Insert statement for a single record.
    * @param values
    * @return
    */
  def insert(values: Array[String]): String = {"INSERT IGNORE INTO `website` (`url`) VALUES ('" + escape(values(0)) + "');"}
}
