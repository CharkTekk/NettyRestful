# NettyRestful


```java
new RestBuilder().setAddress("127.0.0.1", 4545).setRoute((url, channel) -> {
      if(url.contains("test"))
            return "TEST";
      return "";
}).build().connect();

````
