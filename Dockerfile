FROM java:8
MAINTAINER 329224911@qq.com
ADD target/seckill-demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]