# Resilience Patterns for Distributed systems

* [Overview](#overview)
* [Tools / Frameworks used](#tools--frameworks-used)
* [Application installation](#osx)
  * [OSX](#osx)
  * [Ubuntu 18.04](#linux-ubutnu-1804)
  * [Windows](#windows)
* [Load test setup](#load-test-setup)
* [Scenario execution](#execution)
  * [Bulkheads](#bulkheads)
  * [CircuitBreaker](#circuit-breakers)
  * [Graceful Degradation](#graceful-degradation--failover-passing-control-to-failover-function-for-dummy-tags)
  * [RateLimiting](#rate-limiting)
  * [Backpressure](#backpressure)


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

## Application installation

### OSX

* Install Java with brew `brew install openjdk@11`
* Install Docker Desktop from [here](https://hub.docker.com/editions/community/docker-ce-desktop-mac)
* Run docker desktop
* Install maven with brew `brew install maven`
* Clone this repo `git clone https://github.com/priyaaank/patterns-distributed-systems.git` 
* Setup branches
```
git branch --track bulkheads origin/bulkheads
git branch --track bulkheads-fix origin/bulkheads-fix
git branch --track transientfailure origin/transientfailure
git branch --track transientfailure-fix origin/transientfailure-fix
git branch --track circuitbreaker origin/circuitbreaker
git branch --track circuitbreaker-fix origin/circuitbreaker-fix
git branch --track gracefuldegradation origin/gracefuldegradation
git branch --track batchtostream origin/batchtostream
git branch --track batchtostream-fix origin/batchtostream-fix
git branch --track backpressure origin/backpressure
git branch --track backpressure-fix origin/backpressure-fix
```
* Execute `./rebuild_and_restart.sh`
* Execute following `curl` command in terminal to validate text, title, etc for url are bring returned
```
curl -X GET "http://localhost:8080/bookmark/enrich?url=https://github.com&fieldsRequested=title,text,longUrl,shortenedUrl,tags"
```
  
### Linux (Ubutnu 18.04)

* `cd ~`
* `git clone https://github.com/priyaaank/patterns-distributed-systems.git`
* `apt-get install openjdk-11-jdk`
* `apt-get install maven`
* Follow [instructions](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-18-04) to install `docker`
* Follow [instructions](https://linuxhostsupport.com/blog/how-to-install-and-configure-docker-compose-on-ubuntu-20-04/) to install `docker-compose` 
* `cd ~/patterns-distributed-systems`
* Setup branches
```
git branch --track bulkheads origin/bulkheads
git branch --track bulkheads-fix origin/bulkheads-fix
git branch --track transientfailure origin/transientfailure
git branch --track transientfailure-fix origin/transientfailure-fix
git branch --track circuitbreaker origin/circuitbreaker
git branch --track circuitbreaker-fix origin/circuitbreaker-fix
git branch --track gracefuldegradation origin/gracefuldegradation
git branch --track batchtostream origin/batchtostream
git branch --track batchtostream-fix origin/batchtostream-fix
git branch --track backpressure origin/backpressure
git branch --track backpressure-fix origin/backpressure-fix
```
* Execute `./rebuild_and_restart.sh`
* Execute following `curl` command in terminal to validate text, title, etc for url are bring returned
```
curl -X GET "http://localhost:8080/bookmark/enrich?url=https://github.com&fieldsRequested=title,text,longUrl,shortenedUrl,tags"
```

### Windows 

* Help needed to populate the instructions for installation

## Load test setup

* Execute `mkdir -p load-testing/mount/`
* cd into the directory `cd load-testing/mount/`
* Download JMeter from [here](https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.4.3.tgz)
* Extract files in the folder `load-testing/mount/`
* Execute on command line `./apache-jmeter-5.4.3/bin/jmeter`
* Download plugins manager for jmeter from [here](https://jmeter-plugins.org/get/)
* Move the downloaded jar to `load-testing/mount/apache-jmeter-5.4.3/lib/ext/`
* Download influxdb2 metrics reporter jar from [here](https://github.com/mderevyankoaqa/jmeter-influxdb2-listener-plugin/releases/download/v1.5/jmeter-plugin-influxdb2-listener-1.5-all.jar)
* Move the downloaded jar to `load-testing/mount/apache-jmeter-5.4.3/lib/ext/`
* Restart Jmeter
* Open script `load-testing/scripts/Bulkheads.jmx`
* If it prompts to install new plugins, click ok and install

## Execution

Following is a short guide on executing load tests for specific scenarions and making observations

### Bulkheads

#### Without Bulkheads (Common HTTP thread pool contention)

* `git checkout bulkheads`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/Bulkheads.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

#### With Bulkheads fix (Individual HTTP thread pools)

[Code Fix](https://github.com/priyaaank/patterns-distributed-systems/compare/bulkheads...bulkheads-fix)

* `git checkout bulkheads-fix`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/Bulkheads.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

### Circuit Breakers

#### Without Timeouts (Using RestTemplate without timeout config)

* `git checkout transientfailure`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/TransientFailure.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

#### With Timeouts fix (Using RestTemplate with timeout config)

* `git checkout transientfailure`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/TransientFailure.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

#### With Retries (Retry all transient errors)

[Code Fix](https://github.com/priyaaank/patterns-distributed-systems/compare/transientfailure...transientfailure-fix)

* `git checkout transientfailure-fix`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/TransientFailure.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

#### Without Circuit breaker with Systematic failures (Retry causing load on service)

* `git checkout circuitbreaker`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/CircuitBreaker.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

#### With Circuit breaker for Systematic failures (Circuit breaker preventing load aggravation)

[Code Fix](https://github.com/priyaaank/patterns-distributed-systems/compare/circuitbreaker...circuitbreaker-fix)

* `git checkout circuitbreaker-fix`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/CircuitBreaker.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

### Graceful degradation / Failover (Passing control to failover function for dummy tags)

* `git checkout gracefuldegradation`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/CircuitBreaker.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

### Rate Limiting

#### Without Rate Limiting fix (All messages enqueued by the job locally)

* `git checkout batchtostream`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/BatchToOLTP.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

#### With Rate Limiting fix (Job queues one record at a time using HTTP API)

[Code Fix](https://github.com/priyaaank/patterns-distributed-systems/compare/batchtostream...batchtostream-fix)

* `git checkout batchtostream-fix`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/BatchToOLTP.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

### Backpressure

#### Without Backpressure (Unbounded queue into RabbitMQ Queue)

* `git checkout backpressure`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/Backpressure.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`

#### With Rate Limiting fix (Job queues one record at a time using HTTP API)

[Code Fix](https://github.com/priyaaank/patterns-distributed-systems/compare/backpressure...backpressure-fix)

* `git checkout backpressure-fix`
* `./rebuild_and_restart.sh`
* Open script `~/patterns-distributed-systems/load-testing/scripts/Backpressure.jmx` and execute in JMeter
* View `Transactions per Second` and `Summary view`
