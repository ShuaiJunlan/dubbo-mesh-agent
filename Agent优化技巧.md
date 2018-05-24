### Agent优化技巧

* Micro-optimize allocations: using pooled ByteBuffers, or even better computing them only once.
* Switch to [native epoll transport](http://netty.io/wiki/native-transports.html) (Linux only).