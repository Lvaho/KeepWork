FROM java:8
MAINTAINER 329224911@qq.com
ADD target/seckill-demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
ENV JAVA_OPTS="\
-server \
-Xmx3g \
-Xms3g \
-Xmn2g \
-XX:SurvivorRatio=8 \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m \
-XX:+UseParallelGC \
-XX:ParallelGCThreads=4 \
-XX:+UseParallelOldGC \
-XX:+UseAdaptiveSizePolicy \
-XX:+PrintGCDetails \
-XX:+PrintTenuringDistribution \
-XX:+PrintGCTimeStamps \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/ \
-Xloggc:/gc.log \
-XX:+UseGCLogFileRotation \
-XX:NumberOfGCLogFiles=5 \
-XX:GCLogFileSize=10M"