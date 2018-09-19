### Optimize Skills

* Micro-optimize allocations: using pooled ByteBuffers, or even better computing them only once.

  ![](https://github.com/shuaijunlan/dubbo-mesh-agent/blob/master/images/1527124496.png?raw=true)

* Switch to [native epoll transport](http://netty.io/wiki/native-transports.html) (Linux only).

### Features

- [ ] Remote Calls
- [ ] Message Serialization
- [ ] Load Balance
- [ ] Registry Center
- [ ] Cluster Fault Tolerance
- [ ] Routing
- [ ] Distributed Link Tracking