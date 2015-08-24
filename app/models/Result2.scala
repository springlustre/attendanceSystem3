package models

/**
 * Created by lustre on 2015/8/10.
 */


import java.io.File

case class Result2(text: String)

// Finds files in the current dir. matching the given search term
object Result2 {

  // Simple list of files in the current directory
  def all = new File(".").listFiles().map(file => file.getName())

  // Simple case-sensitive filter
  def find(term: String) = Result2.all.filter(_.contains(term))
}
