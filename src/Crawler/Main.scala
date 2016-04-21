package Crawler

import Crawler.View.Print

/**
  * Query for information or crawl to fill the database
  */
object Main {
  /**
    * Start of the application
    * @param args
    */
  def main(args: Array[String]): Unit = {
    //Print.getTopWords(10)
    //Print.getUrlsByWord("Tweets")
    Print.startCrawling(urls)
  }

  /**
    * List of URLs to be searched
    */
  val urls = Array(
    "http://www.amazon.com/",
    "http://www.twitter.com/",
    "http://www.google.com/",
    "http://www.cnn.com/"
  )
}