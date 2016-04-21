package Crawler.Controller

import Crawler.Model.Database.{Connector, Filler}
import Crawler.Model.Dbo.FoundWord
import Crawler.Model.PageReader
import Crawler.View.Print

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  *
  */
object PageCrawler {

  /**
    * Recursive crawl through a website and if possible crawl through the children.
    *
    * @param url
    * @param nesting
    * @return
    */
  def crawl(url: String, nesting: Int): Int ={
    var crawledThroughWebsites = 1                // Count this crawl
    val connector: Connector = new Connector()    // New connector
    val filler: Filler = new Filler(connector)    // New filler
    fetchDescriptionWords(url, filler)            // Fetch words and website and enqueue them
    saveData(connector, filler)                   // Dequeue and save words and website

    // If nesting is set; continue
    if(nesting > 0) {
      // Get child URL's
      for ((newUrl, i) <- PageReader.getUrls(url).view.zipWithIndex) {
        // Crawl through the site -- don't forget decreasing the nesting!
        crawledThroughWebsites +=crawl(newUrl, nesting-1)
      }
    }

    // Give the data back
    crawledThroughWebsites
  }

  /**
    * Save the found words
    *
    * @param connector
    * @param filler
    */
  def saveData(connector: Connector, filler: Filler): Unit ={
      connector.openConnection()
      filler.insert(FoundWord)
      connector.closeConnection()
  }

  /**
    * Fetch and enqueue the found words
    *
    * @param url
    * @param filler
    */
  def fetchDescriptionWords(url: String, filler: Filler): Unit ={
    val words = PageReader.splitDescription(
      PageReader.getDescriptionMeta(url)
    )
    filler.enqueue(words, url)
  }

  /**
    * Retrieves page size multithreaded
    *
    * @param urls request the links in a link
    */
  def crawlConcurrently(urls: Array[String]): Int = {
    var numberedCrawled = 0                           // Count numbered crawled
    val nesting = 1                                   // Give nesting amount
    var concurrent = ArrayBuffer[Future[Int]]()       // Container for the Future's
    for ((url, i) <- urls.view.zipWithIndex) {        // Loop through the URL's
      concurrent += {                                 // Add Future to array
        Future {
          Print.running(url)                          // Print URL that is going to be crawled
          crawl(url, nesting)                         // Crawl the website
        }
      }
    }

    // For each job wait until it's done.
    for(job <- concurrent){
      numberedCrawled += Await.result(job, scala.math.pow(30, nesting) minutes)
    }

    // Return the results
    numberedCrawled
  }
}
