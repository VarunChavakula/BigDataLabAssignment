package edu.umkc.fv

import edu.umkc.fv.NLPUtils._
import edu.umkc.fv.Utils._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Sowmya on 02-Mar-16.
  */
object FeatureVector1 {

   def main(args: Array[String]) {
     val filters = args
     // Set the system properties so that Twitter4j library used by twitter stream
     // can use them to generate OAuth credentials

     System.setProperty("twitter4j.oauth.consumerKey", "UamFKOWeTXeekMhrPMx1nZQl7")
     System.setProperty("twitter4j.oauth.consumerSecret", "FH8J6Oo5xUb5w15eV0h7IWlxQeodHrNEJs8IZBIydUQ3iyjXne")
     System.setProperty("twitter4j.oauth.accessToken", "880485062-w6apxV2oOtjemE2LM6V48NUcWvHIqdi2NWVNSJuj")
     System.setProperty("twitter4j.oauth.accessTokenSecret", "z8qazdkNJTiU9chJJbiB1RNBtTRgyrtDxkyi5qggWGx1F")
     System.setProperty("hadoop.home.dir", "C:\\winutils")
     val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark-Machine_Learning-Text-1").set("spark.driver.memory", "3g").set("spark.executor.memory", "3g")
     val ssc = new StreamingContext(sparkConf, Seconds(2))
    val stream = TwitterUtils.createStream(ssc, None, filters)
    // stream.saveAsTextFiles("data/testing/output.txt")
    /* val health = stream.flatMap(status => status.getText.split(" ").filter(_.contains("health")))
     health.saveAsTextFiles("data/training/Tweets_Training_data_health.txt")
     val android1 = stream.flatMap(status => status.getText.split(" ").filter(_.contains("android")))
     android1.saveAsTextFiles("data/training/Tweets_Training_data_android.txt")
     val jobs1 = stream.flatMap(status => status.getText.split(" ").filter(_.contains("jobs")))
     jobs1.saveAsTextFiles("data/training/Tweets_Training_data_jobs.txt")*/
     //ssc.start()
     //ssc.awaitTermination(1000000)
     val sc = ssc.sparkContext
     val stopWords = sc.broadcast(loadStopWords("/stopwords.txt")).value
     val labelToNumeric = createLabelMap("data/training/")
     var model: NaiveBayesModel = null
     // Training the data
     val training = sc.wholeTextFiles("data/training/*")
       .map(rawText => createLabeledDocument(rawText, labelToNumeric, stopWords))
     val X_train = tfidfTransformer(training)
     X_train.foreach(vv => println(vv))

     model = NaiveBayes.train(X_train, lambda = 1.0)

     val lines=sc.wholeTextFiles("data/testing/*")
     val data = lines.map(line => {

         val test = createLabeledDocumentTest(line._2, labelToNumeric, stopWords)
         println(test.body)
         test


     })

          val X_test = tfidfTransformerTest(sc, data)

            val predictionAndLabel = model.predict(X_test)
            println("PREDICTION")
            predictionAndLabel.foreach(x => {
              labelToNumeric.foreach { y => if (y._2 == x) {
                println(y._1)
              }
              }
            })





   }


 }
