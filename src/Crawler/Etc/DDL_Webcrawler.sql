/**
 * Create Database webcrawler, drop if exists
 */
DROP DATABASE IF EXISTS `webcrawler`;
CREATE DATABASE `webcrawler`;

USE `webcrawler`;

/**
 * Create table website
 */
CREATE TABLE `website` (
  `url` VARCHAR(767) PRIMARY KEY
);

/**
 * Create table website
 */
CREATE TABLE `key_word` (
  `word` VARCHAR(255) PRIMARY KEY
);

/**
 * Create table website
 */
CREATE TABLE `found_words` (
  `website` VARCHAR(767) ,
  `word`    VARCHAR(255) NOT NULL,
  PRIMARY KEY(`website`, `word`),
  CONSTRAINT fk_found_words_website FOREIGN KEY (`website`) REFERENCES website(`url`),
  CONSTRAINT fk_found_words_word FOREIGN KEY (`word`) REFERENCES key_word(`word`)
);