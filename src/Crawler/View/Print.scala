package Crawler.View

import Crawler.Controller.PageCrawler
import Crawler.Model.Database.Connector
import Crawler.Model.Dbo.FoundWord

/**
  * Simple print object that displays the queries and starts the crawling
  */
object Print {

  /**
    * Start the crawling process
    * @param urls Starting URL's
    */
  def startCrawling(urls: Array[String]): Unit ={
    println("Concurrent run")
    val start = System.nanoTime                     // Log start time
    println()
    println("Websites crawled: "+PageCrawler.crawlConcurrently(urls) )  // Crawl and get the number of links
    val end = System.nanoTime                       // Log end time
    println("Method took " + (end - start) / 1000000000.0 + " seconds.") // Display end time
  }

  /**
    * Print a main URL that is running
    * @param url
    */
  def running(url: String): Unit ={
    println("Running: " + url)
  }

  /**
    * Search for top words with a limit.
    * @param limit
    */
  def getTopWords(limit: Int): Unit ={
    val connector: Connector = new Connector()                    // Connecting to the MySQL database
    val query = connector.select(FoundWord.selectFound(limit))    // Retrieve the SELECT statement from the DBO and execute the select
    println("Count \t\t Key")
    println("--------------")
    while (query.next) {  // While their is a value in the result:
      val count = query.getString("count")                    // Word count value
      val word = query.getString("word")                      // Word value
      val tabs = if(count.length < 3){"\t\t\t"}else{"\t\t"}   // Formatting purpose
      println("%s %s %s".format(count, tabs, word))           // Pasting them all together
    }
    println()
  }

  /**
    * Get the URL's by keyword
    * @param word
    */
  def getUrlsByWord(word: String): Unit ={
    val connector: Connector = new Connector()                    // Connecting to the MySQL database
    val query = connector.select(FoundWord.selectByWord(word))    // Retrieve the SELECT statement from the DBO and execute the select
    var count = 0
    println(word)
    println("--------------")
    while (query.next) {                                          // While their is a value in the result:
      count+=1                                                    // Word increment value
      val website = query.getString("website")                    // Website value
      println("#%s \t\t %s".format(count, website))               // Pasting them all together
    }
    println()
  }
}
