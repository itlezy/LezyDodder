<pre>
        ________      _________________
___  __ \___________  /_____  /____________
__  / / /  __ \  __  /_  __  /_  _ \_  ___/
_  /_/ // /_/ / /_/ / / /_/ / /  __/  /
/_____/ \____/\__,_/  \__,_/  \___//_/     A distributed DHT web crawler that supports cluster deployment.
</pre>
-------

Simplified version to run on local file-system and translated to English. At an high-level, this software has a service which scrapes the DHT and save unique info-hashes to Redis and stream them to Kafka. Another service then downloads the metadata, torrents get stored, etc..
The portion of interest for now is the DHT scraper "dodder-dht-server", which can run stand-alone by removing Redis and Kafka. This version just need to be executed and wait for info-hashes to be written out.

Original work of https://github.com/xwlcn/Dodder

# quick start
#### environment dependent
- Zookeeper-3.7.0 ([http://zookeeper.apache.org/](http://zookeeper.apache.org/))
- Kafka-2.13-2.8.0 ([http://kafka.apache.org/](http://kafka.apache.org/))
- Redis-2.6 ([https://redis.io/](https://redis.io/))
- MongoDB-4.4.5 ([https://www.mongodb.com/](https://www.mongodb.com/))
- Elasticsearch-7.12.0 ([https://www.elastic.co/](https://www.elastic.co/))
- elasticsearch-analysis-ik-7.12.0 ([https://github.com/medcl/elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik))

announce_peer messages:

![announce_peer](https://github.com/xwlcn/img/raw/master/announce_peer.gif)

Stand-alone operating environment:
* CPU:	Intel Xeon E3-1230 v3 - 3.3 GHz - 4 core(s)
* RAM:	32GB - DDR3
* Hard Drive(s):	2x 1TB (HDD SATA)
* Bandwidth:	Unmetered @ 1Gbps

* 2021-06-11
  - Optimized search, related recommendation query speed
  - Solve the memory leak problem of dodder-torrent-download-service
...

#### Overall structure
![架构图](https://github.com/xwlcn/Dodder/raw/master/20190305.jpg)

Note: `dht-server`, `download-service`, `store-service` in the project can be deployed in clusters.
`dht-server` is responsible for crawling the info_hash in the DHT network, and then writes it to the Kafka message queue, `download-service`
Responsible for reading the info_hash information to the specified ip to download the metadata of the torrent file (when deploying the cluster, pay attention to setting the number of partitions of the kafka topic,
Number of partitions >= number of service deployments). The downloaded metadata parses the file information and encapsulates it into a Torrent object and writes it to Kafka
`torrentMessages` topic, `store-service` is responsible for reading torrent storage into Elasticsearch.

Deduplication: The first time Redis deduplicates, MongDB and Elasticsearch use upsert to insert data to prevent repeated insertion.

#### deploy
After all the previous environments are set up, clone the entire project locally. If it is a cluster deployment, please modify some ip address parameters in each service module.
I have a limited number of servers here, and I only use one server for stand-alone deployment. If there is a problem with the cluster deployment, please submit an issue.

### Notice
**dht-server needs public IP to crawl to info_hash**
