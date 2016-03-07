import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by pradyumnad on 07/07/15.
  */
object TwitterStreaming {

  def main(args: Array[String]) {


    val filters = args
var s = ""
    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generate OAuth credentials

    System.setProperty("twitter4j.oauth.consumerKey", "OkeystcYZMES7wC1MUBsYVYAH")
    System.setProperty("twitter4j.oauth.consumerSecret", "rH5ypcyozNu5nJcqitPazO7kAuJLmgdWq4bfNRoPXJN7kPKLaS")
    System.setProperty("twitter4j.oauth.accessToken", "3312312041-h9jSDoP1rdEifOxtdc9QAYmRvzFVZrwCW8PnJ1y")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "UyNvH3VsE15la7odzV5uEEcaXYLS9XpJj7yNpkn4dBpNy")

    //Create a spark configuration with a custom name and master
    // For more master configuration see  https://spark.apache.org/docs/1.2.0/submitting-applications.html#master-urls
    val sparkConf = new SparkConf().setAppName("STweetsApp").setMaster("local[*]")
    //Create a Streaming COntext with 2 second window
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    //Using the streaming context, open a twitter stream (By the way you can also use filters)
    //Stream generates a series of random tweets
    val stream = TwitterUtils.createStream(ssc, None, filters)
    stream.print()
    //Map : Retrieving Hash Tags
    val hashTags = stream.flatMap(status => status.getText.split(" "))

    //Finding the top hash Tags on 30 second window
    val topCounts30 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(30))
      .map{case (topic, count) => (count, topic)}
      .transform(_.sortByKey(false))


    // Print popular hashtags
    topCounts30.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\nPopular topics in last 30 seconds (%s total):".format(rdd.count()))
      topList.foreach{case (count, tag) => s= s+"\n"+ "<%s,%s>".format(tag, count)}
    })

    ssc.start()

    ssc.awaitTermination(50000)
    SocketClient.sendCommandToRobot(s)
  }

}


