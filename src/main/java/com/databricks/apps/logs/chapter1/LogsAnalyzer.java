/*
 * originally from:https://databricks.gitbooks.io/databricks-spark-reference-applications/content/index.html
 */
package com.databricks.apps.logs.chapter1;

import com.databricks.apps.logs.ELBAccessLog;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * The LogAnalyzer takes in an AWS / ELB access log file and
 * computes some statistics on them.
 *
 * Example command to run:
 * %  ${YOUR_SPARK_HOME}/bin/spark-submit
 *     --class "com.databricks.apps.logs.chapter1.LogsAnalyzer"
 *     --master local[4]
 *     target/log-analyzer-1.0.jar
 *     ../../data/apache.accesslog
 */
public class LogsAnalyzer {
  private static Function2<Long, Long, Long> SUM_REDUCER = (a, b) -> a + b;

  private static class ValueComparator<K, V>
     implements Comparator<Tuple2<K, V>>, Serializable {
    private Comparator<V> comparator;

    public ValueComparator(Comparator<V> comparator) {
      this.comparator = comparator;
    }

    @Override
    public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
      return comparator.compare(o1._2(), o2._2());
    }
  }

  public static void main(String[] args) {
    // Create a Spark Context.
    SparkConf conf = new SparkConf().setAppName("Log Analyzer");
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Load the text file into Spark.
    if (args.length == 0) {
      System.out.println("Must specify an access logs file.");
      System.exit(-1);
    }
    String logFile = args[0];
    JavaRDD<String> logLines = sc.textFile(logFile);

    // Convert the text log lines to ApacheAccessLog objects and cache them
    //   since multiple transformations and actions will be called on that data.
    JavaRDD<ELBAccessLog> accessLogs = logLines.map(ELBAccessLog::parseFromLogLine).cache();

    // Calculate statistics based on the content size.
    // Note how the contentSizes are cached as well since multiple actions
    //   are called on that RDD.
    
    JavaRDD<Long> contentSizes = accessLogs.map(ELBAccessLog::getSentBytes).cache();
    System.out.println(String.format(" • Sent Bytes : %s, Min: %s, Max: %s",
       contentSizes.reduce(SUM_REDUCER) / contentSizes.count(),
       contentSizes.min(Comparator.naturalOrder()),
       contentSizes.max(Comparator.naturalOrder())));

    // compute requests count
    
    List<Tuple2<String, Long>> requestsCount = 
    		accessLogs.mapToPair(log -> new Tuple2<>(log.getRequest(), 1L)) 
            .reduceByKey(SUM_REDUCER)
            .top(10, new ValueComparator<>(Comparator.<Long>naturalOrder()));
    
    System.out.println(String.format(" • Top requests count: %s", requestsCount));

    List<Tuple2<String, Long>> ipAddressCount = 
    		accessLogs.mapToPair(log -> new Tuple2<>(log.getClientIpAddress(), 1L)) 
            .reduceByKey(SUM_REDUCER)
            .top(10, new ValueComparator<>(Comparator.<Long>naturalOrder()));
    
    System.out.println(String.format(" • Top requests count: %s", ipAddressCount));
    
            
    // Stop the Spark Context before exiting.
    sc.stop();
  }
}
