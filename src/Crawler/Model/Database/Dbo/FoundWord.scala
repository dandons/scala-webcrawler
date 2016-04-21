package Crawler.Model.Dbo

import Crawler.Model.Database.Dbo.AbstractDbo

/**
  * Found Word Dbo object
  */
object FoundWord extends AbstractDbo{

  /**
    * Insert statement for a single record.
    * @param values
    * @return
    */
  def insert(values: Array[String]): String = {
    "INSERT IGNORE INTO `found_words` (`website`, `word`) VALUES ('"+ escape(values(0)) +"', '"+ escape(values(1)) +"');"
  }

  /**
    * Select top key words limited by parameter
    * @param limit
    * @return
    */
  def selectFound(limit: Int): String ={
    "SELECT count(`word`) as 'count', `word` FROM `found_words` GROUP BY `word` ORDER BY COUNT(`word`) DESC LIMIT "+limit
  }

  /**
    * Select websites by key word.
    * @param word
    * @return
    */
  def selectByWord(word: String): String ={
    "SELECT `website` FROM `found_words` WHERE `word` = '"+word+"'"
  }
}
