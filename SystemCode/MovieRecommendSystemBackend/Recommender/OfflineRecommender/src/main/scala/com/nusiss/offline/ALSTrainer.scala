package com.nusiss.offline

import breeze.numerics.sqrt
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import com.nusiss.offline.OfflineRecommender.MONGODB_RATING_COLLECTION
/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.offline
 *
 * Author: Chen Pengwen
 */
object ALSTrainer {
  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://moiveAdmin:{thepassword}@nus2.com:37018",
      "mongo.db" -> "movierecommender"
    )

    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("ALSOfflineRecommender")

    // 创建一个SparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._

    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))

    // 加载评分数据
    val ratingRDD = spark.read
      .option("uri", mongoConfig.uri)
      .option("database", mongoConfig.db)
      .option("collection", MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MovieRating]
      .rdd
      .map( rating => Rating( rating.userId, rating.movieId, rating.rating.toDouble ) )    // 转化成rdd，并且去掉时间戳
      .cache()

    // 随机切分数据集，生成训练集和测试集
    val splits = ratingRDD.randomSplit(Array(0.8, 0.2))
    val trainingRDD = splits(0)
    val testRDD = splits(1)

    // 模型参数选择，输出最优参数
    adjustALSParam(trainingRDD, testRDD)

    spark.close()
  }

  def adjustALSParam(trainData: RDD[Rating], testData: RDD[Rating]): Unit ={
    val result = for( rank <- Array(300, 400, 500); lambda <- Array( 0.1 ))
      yield {
        val model = ALS.train(trainData, rank, 5, lambda)
        // 计算当前参数对应模型的rmse，返回Double
        val rmse = getRMSE( model, testData )
        ( rank, lambda, rmse )
      }
    // 控制台打印输出最优参数
    println("best_parameter_: ", result.minBy(_._3))
  }

  def getRMSE(model: MatrixFactorizationModel, data: RDD[Rating]): Double = {
    // 计算预测评分
    val userProducts = data.map(item => (item.user, item.product))
    val predictRating = model.predict(userProducts)

    // 以uid，mid作为外键，inner join实际观测值和预测值
    val observed = data.map( item => ( (item.user, item.product), item.rating ) )
    val predict = predictRating.map( item => ( (item.user, item.product), item.rating ) )
    // 内连接得到(uid, mid),(actual, predict)
    sqrt(
      observed.join(predict).map{
        case ( (uid, mid), (actual, pre) ) =>
          val err = actual - pre
          err * err
      }.mean()
    )
  }
}

