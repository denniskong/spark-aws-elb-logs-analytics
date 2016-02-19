ORIGIN: https://databricks.gitbooks.io/databricks-spark-reference-applications/content/index.html

```bash
mvn package
```



```bash
spark-submit  --class "com.databricks.apps.logs.chapter1.LogsAnalyzer"  --master local[4]  target/log-analyzer-1.0.jar ./data/access_log
```
