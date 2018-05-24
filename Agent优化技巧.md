### Agent优化技巧

* Micro-optimize allocations: using pooled ByteBuffers, or even better computing them only once.

![](images\1527124496.png)

* Switch to [native epoll transport](http://netty.io/wiki/native-transports.html) (Linux only).

