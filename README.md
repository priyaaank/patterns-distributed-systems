# Resilience Patterns for Distributed systems

## Overview

This repository is a compilation of working code scripts that complete the [O'Reilly session on Patterns for Distributed Systems](https://learning.oreilly.com/live-events/design-patterns-for-distributed-systems/0636920061982/0636920061981/) The code here will be used during the session to reason and discuss various patterns and their benefits and applications. 

The code here can remain an independent and open means of learning about the patterns. This guide outlines how different scenarios be executed and impact of pattern is visualized. The guide covers 5 specific patterns and their execution in the repository. 

## Tools / Frameworks used

* Java 11
* Maven
* Springboot 
* JMeter as load generation tool
* Influx db as timeseries store for load metrics
* Grafana as a visualization tool
* RabbitMQ as the distributed message queue
* Docker

Only basic Java knowledge is required to execute the patterns. All other components can be treated as a blackbox. 

## Installation

### OSX
* Install Java with brew `brew install openjdk@11`
* Install Docker Desktop from [here](https://hub.docker.com/editions/community/docker-ce-desktop-mac)
* Run docker desktop
* Install maven with brew `brew install maven`
* Clone this repo `git clone https://github.com/priyaaank/patterns-distributed-systems.git` 
* Execute `./rebuild_and_restart.sh`
* Execute following `curl` command in terminal 
  `curl -X GET "http://localhost:8080/bookmark/enrich?url=https://github.com&fieldsRequested=title,text,longUrl,shortenedUrl,tags"`

If you get a response to the last `curl` command, then your local setup is complete and now load testing setup remains

* Execute `mkdir -p load-testing/mount/`
* cd into the directory `cd load-testing/mount/`
* Download JMeter from [here](https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.4.3.tgz)
* Extract files in the folder `load-testing/mount/`

### Linux

* Help needed to populate the instructions for installation


### Windows 

* Help needed to populate the instructions for installation


## Execution


### Bulkheads

### Circuit Breakers

#### Timeout / Response SLA

#### Retry

#### Circuit breaker

### Graceful degradation / Failover

### Batch to stream

### Backpressure


