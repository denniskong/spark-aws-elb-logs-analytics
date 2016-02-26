# [spark-aws-elb-logs-analytics](https://github.com/tillawy/spark-aws-elb-logs-analytics)

based on: [Databricks Spark Reference Applications ](https://databricks.gitbooks.io/databricks-spark-reference-applications/content/index.html)


##### to build

#### to package the application
```bash
mvn package
```


##### to run for the supplied sample

```bash
spark-submit  --class "com.databricks.apps.logs.chapter1.LogsAnalyzer"  --master local[4]  target/log-analyzer-1.0.jar ./data/elb_logs
```



