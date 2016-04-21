package Crawler.Model

import java.net.URL
import org.jsoup.Jsoup
import scala.collection.mutable.ArrayBuffer

/**
  * Page loader class that retries the links and can scrape the meta information.
  */
object PageReader {

  /***
    * Retrieve the Urls by using Jsoup.
    * @param url The url you want look through.
    * @return
    */
  def getUrls(url: String): ArrayBuffer[String] = {
    // The array where the urls are being placed in.
    var content = ArrayBuffer[String]()

    // Try retrieving the page
    try {
      val contentLinks =          // Container for the new available links
        Jsoup
          .connect(url)           // Connect to the URL
          .timeout(30000)         // Time out to receive less 503 errors
          .get()
          .getElementsByTag("a")  // Get the <a> element
      val contentIterator = contentLinks.iterator()
      while (contentIterator.hasNext) {
        val filteredUrl = filterUrl(              // Add the original URL in front of the new URL if the path is relative
          url,
          contentIterator.next().attr("href")
        )

        // Add the urls to the array
        if (filteredUrl.length > 0) {
          content += filteredUrl
        }

      }
    }catch{ // Catch http 503 or looking up a Mime type
      case e: org.jsoup.UnsupportedMimeTypeException => content
      case e: org.jsoup.HttpStatusException => content
    }
    content
  }

  /**
    * Retrieves the meta information from a page.
    *
    * @param url The url you want look through.
    * @return
    */
  def getDescriptionMeta(url: String): String = {
    var content = ""          // Meta information variable

    try{
      val doc =               // Container for the new available links
        Jsoup
          .connect(url)       // Connect to the URL
          .timeout(30000)     // Time out to receive less 503 errors
          .get()              // Get the page

      // Here is where the magic happens:
      // Get element by the tag meta and select the meta tags with the attribute name=description
      val metaElements = doc.getElementsByTag("meta").select("[name=description]")
      if (metaElements.size() > 0) { // If there is more then 0 elements continue
        // Select the first element. There should be only one in a web page.
        // Retrieve the content attribute value
        content = metaElements.first().attr("content")
      }
    }catch{ // Catch http 503 or looking up a Mime type
      case e: org.jsoup.UnsupportedMimeTypeException => content
      case e: org.jsoup.HttpStatusException => content
    }

    // return if available the description
    content
  }

  /**
    * Add the base to relative paths
    *
    * @param baseUrl The base url
    * @param url The new url
    * @return
    */
  def filterUrl(baseUrl: String, url: String): String ={
    var newUrl = ""
    if(url != "/"){
      if(url.startsWith("/")){
        val base: URL  = new URL(baseUrl)
        newUrl = new URL(base, url).toString
      }
    }
    newUrl
  }

  /**
    * Split a sentence into words
    * @param content The content that needs to be split
    * @return
    */
  def splitDescription(content: String): Array[String] = content.split(" +")

}
