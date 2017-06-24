# BIOHttpServer
使用BIO实现的httpServer，特点是支持任意长度请求并打印请求内容。  
使用curl命令测试：  
curl -I localhost:8080  
curl -X post -d test localhost:8080  
curl localhost:8080/index.html  
curl -X post -d test -H "Content-Length:9999" localhost:8080 (Content-Length攻击)
