package filehandling

import scala.io.Source

object ReadFile {
     def main(args: Array[String]) {
   	 	val filename = "myfile.txt"
		try {
		  for (line <- Source.fromFile(filename).getLines()) {
		    println(line)
		  }
		} catch {
		  case ex: Exception => println("An exception happened.")
		}
     }
}
