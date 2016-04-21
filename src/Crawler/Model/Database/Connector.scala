package Crawler.Model.Database

import java.sql
import java.sql.{ResultSet, Connection, DriverManager}

/**
  * A connector class for connection to the MySQL database and executing SQL commando's.
  * @param username Username of your Database
  * @param password Password of your Database
  * @param url      URL for connection to the databse: e.g. jdbc:mysql://localhost:3306/webcrawler
  * @param driver   Database driver - advise to keep the default value: com.mysql.jdbc.Driver
  */
class Connector(
                 username: String = "root",
                 password: String = "",
                 url: String = "jdbc:mysql://localhost:3306/webcrawler",
                 driver: String = "com.mysql.jdbc.Driver"
               ) extends App {

  var connection: Connection = _
  var statement: sql.Statement = _

  /**
    * Add a query to the batch. (Primarily insert statements)
    * @param query
    */
  def query(query: String): Unit = {
    statement.addBatch(query)
  }

  /**
    * Select query
    * @param query
    * @return ResultSet
    */
  def select(query: String): ResultSet = {
    openConnection()
    newStatement()
    statement.execute(query)
    statement.getResultSet
  }

  /**
    * Opens a connection to the database. Mandatory to perform a SQL statement.
    */
  def openConnection() {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
  }

  /**
    * Closed a connection to the database.
    */
  def closeConnection() {
    connection.close()
  }

  /**
    * Create a new batch statement.
    */
  def newStatement(): Unit = {
    statement = connection.createStatement()
  }

  /**
    * Execute the batch statement.
    */
  def executeStatement(): Unit = {
    statement.executeBatch()
  }
}
