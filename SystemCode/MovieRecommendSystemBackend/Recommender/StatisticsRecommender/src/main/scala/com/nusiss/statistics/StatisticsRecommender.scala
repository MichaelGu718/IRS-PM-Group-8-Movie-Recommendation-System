package com.nusiss.statistics

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.statistics
 *
 * Created by tangyi on 2022-10-21 14:42
 */
case class Movie(movieId: Int, title: String, plotSummary: String, runtime: String, releaseYear: String,
                 languages: String, genres: String, actors: String, directors: String)

case class Rating(userId: Int, movieId: Int, rating: String, timestamp: String)

case class MongoConfig(uri:String, db:String)

// define a base recommendation object
case class Recommendation(movieId: Int, rating: String)

// define movie genre top10 recommendation object
case class GenresRecommendation( genres: String, recs: Seq[Recommendation] )

object StatisticsRecommender {

  // 定义表名
  val MONGODB_MOVIE_COLLECTION = "movie"
  val MONGODB_RATING_COLLECTION = "rating"

  //统计的表的名称
  val RATE_MORE_MOVIES = "rate_more_movies"
  val RATE_MORE_RECENTLY_MOVIES = "rate_more_recently_movies"
  val AVERAGE_MOVIES = "average_movies"
  val GENRES_TOP_MOVIES = "genres_top_movies"

  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://movieAdmin:{thepassword}@nus2.com:37018",
      "mongo.db" -> "movierecommender"
    )

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("StatisticsRecommeder")

    // 创建一个SparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._

    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))

    // 从mongodb加载数据
    val ratingDF = spark.read
      .option("uri", mongoConfig.uri)
      .option("database", mongoConfig.db)
      .option("collection", MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Rating]
      .toDF()

    val movieDF = spark.read
      .option("uri", mongoConfig.uri)
      .option("database", mongoConfig.db)
      .option("collection", MONGODB_MOVIE_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Movie]
      .toDF()

    // 创建名为ratings的临时表
    ratingDF.createOrReplaceTempView("ratings")

    // different statistics recommend result
    // 1. 历史热门统计，历史评分数据最多，mid，count
    val rateMoreMoviesDF = spark.sql("select movieId, count(movieId) as count from ratings group by movieId")
    // 把结果写入对应的mongodb表中
    storeDFInMongoDB( rateMoreMoviesDF, RATE_MORE_MOVIES )

    // 2. 优质电影统计，统计电影的平均评分，mid，avg
    val averageMoviesDF = spark.sql("select movieId, avg(rating) as avg from ratings group by movieId")
    storeDFInMongoDB(averageMoviesDF, AVERAGE_MOVIES)

    // 3. 各类别电影Top统计
    // 定义所有类别
    val genres = List("Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy","Foreign","History","Horror","Music","Mystery"
      ,"Romance","Science","Tv","Thriller","War","Western")

    // 把平均评分加入movie表里，加一列，inner join
    val movieWithScore = movieDF.join(averageMoviesDF, "movieId")

    // 为做笛卡尔积，把genres转成rdd
    val genresRDD = spark.sparkContext.makeRDD(genres)

    // 计算类别top10，首先对类别和电影做笛卡尔积
    val genresTopMoviesDF = genresRDD.cartesian(movieWithScore.rdd)
      .filter{
        // 条件过滤，找出movie的字段genres值(Action|Adventure|Sci-Fi)包含当前类别genre(Action)的那些
        case (genre, movieRow) => movieRow.getAs[Seq[String]]("genres").mkString("|").toLowerCase.contains( genre.toLowerCase )
      }
      .map{
        case (genre, movieRow) => ( genre, ( movieRow.getAs[Int]("movieId"), movieRow.getAs[Double]("avg") ) )
      }
      .groupByKey()
      .map{
        case (genre, items) => GenresRecommendation( genre, items.toList.sortWith(_._2>_._2).take(10).map( item=> Recommendation(item._1, item._2.toString)) )
      }
      .toDF()

    storeDFInMongoDB(genresTopMoviesDF, GENRES_TOP_MOVIES)

    spark.stop()
  }

  def storeDFInMongoDB(df: DataFrame, collection_name: String)(implicit mongoConfig: MongoConfig): Unit ={
    df.write
      .option("uri", mongoConfig.uri)
      .option("collection", collection_name)
      .option("database", mongoConfig.db)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()
  }

}
