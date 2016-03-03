import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import java.io._

/**
  * Created by Sowmya on 02-Mar-16.
  */
object SparkWordCount {

  def main(args: Array[String]) {
    val filters = args

    System.setProperty("hadoop.home.dir","C:\\winutils");

    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generate OAuth credentials

    System.setProperty("twitter4j.oauth.consumerKey", "UamFKOWeTXeekMhrPMx1nZQl7")
    System.setProperty("twitter4j.oauth.consumerSecret", "FH8J6Oo5xUb5w15eV0h7IWlxQeodHrNEJs8IZBIydUQ3iyjXne")
    System.setProperty("twitter4j.oauth.accessToken", "880485062-w6apxV2oOtjemE2LM6V48NUcWvHIqdi2NWVNSJuj")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "z8qazdkNJTiU9chJJbiB1RNBtTRgyrtDxkyi5qggWGx1F")

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    //Using the streaming context, open a twitter stream (By the way you can also use filters)
    //Stream generates a series of random tweets
    val stream = TwitterUtils.createStream(ssc, None, filters)
    var s = "\n"+"Most popular Tweets Word count list" + "\n"
    val hashTags = stream.flatMap(status => status.getText.split(" "))
    val wc=hashTags.map(word=>(word,1)).cache()
    val output=wc.reduceByKey(_+_)

    //printing the top hash Tags on 30 second window
    output.foreachRDD(rdd => {
      val topList = rdd.take(10)
      //topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
      topList.foreach{case (count, tag) => s+= "\n" +"<"+ tag +","+ count+">"}
    })


    ssc.start()

    ssc.awaitTerminationOrTimeout(10000)
    SocketClient.sendCommandToRobot(s)
  }


}
