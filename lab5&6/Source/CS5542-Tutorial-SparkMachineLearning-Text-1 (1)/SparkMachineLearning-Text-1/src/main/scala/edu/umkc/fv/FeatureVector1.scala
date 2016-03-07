package edu.umkc.fv

import edu.umkc.fv.NLPUtils._
import edu.umkc.fv.Utils._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Mayanka on 14-Jul-15.
  */

object FeatureVector1 {

   def main(args: Array[String]) {
     val filters = args
     System.setProperty("hadoop.home.dir", "C:\\winutils")
     System.setProperty("twitter4j.oauth.consumerKey", "OkeystcYZMES7wC1MUBsYVYAH")
     System.setProperty("twitter4j.oauth.consumerSecret", "rH5ypcyozNu5nJcqitPazO7kAuJLmgdWq4bfNRoPXJN7kPKLaS")
     System.setProperty("twitter4j.oauth.accessToken", "3312312041-h9jSDoP1rdEifOxtdc9QAYmRvzFVZrwCW8PnJ1y")
     System.setProperty("twitter4j.oauth.accessTokenSecret", "UyNvH3VsE15la7odzV5uEEcaXYLS9XpJj7yNpkn4dBpNy")
     val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark-Machine_Learning-Text-1").set("spark.driver.memory", "3g").set("spark.executor.memory", "3g")
     val ssc = new StreamingContext(sparkConf, Seconds(2))
     val sc = ssc.sparkContext

     val stream = TwitterUtils.createStream(ssc, None, filters)
   /*   val health = stream.flatMap(status => status.getText.split(" ")).filter(_.contains("health"))
    health.saveAsTextFiles("data/training/health/op_health.txt")
     val android = stream.flatMap(status => status.getText.split(" ")).filter(_.contains("android"))
     android.saveAsTextFiles("data/training/android/op_android.txt")
     val jobs = stream.flatMap(status => status.getText.split(" ")).filter(_.contains("jobs"))
     jobs.saveAsTextFiles("data/training/jobs/op_jobs.txt")*/

     stream.saveAsTextFiles("data/testing/x.txt")
     ssc.start()

     ssc.awaitTermination(1000)
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
