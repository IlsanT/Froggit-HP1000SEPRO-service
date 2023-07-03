# Froggit-HP1000SEPRO-service
A service written in Kotlin with Spring Boot to store incoming data from the Froggit HP1000 SE PRO weather station and convert to other standards. There is also a very simple UI to download the stored data. 

This can be used when you don't wish to use sites as wunderground or ecowitt to store weather station data.

## Features
* REST endpoint to accept requests from the weather station
* REST endpoint to retrieve and download the weather data files
* Data gets converted to use Celsius scale instead of Fahrenheit, miles are converted to kilometers to measure wind speed, inches of rain to millimeters and inches of mercury are converted to hectopascals (hPa)
* Files generated in raw-format, which contains the unaltered data received from the weather station
* Files generated in json-format, which are converted to other standards
* Basic UI to view all files
* Comes with a Docker image, see section Docker for more info

## Usage

### Running on an IDE

Maven, Java 17 and Kotlin are required to run it through the IDE.

### Building the service

In order to build it Docker is also required.

Running a maven install should create a docker image with everything required available.

### Running with Docker

Example to run with Docker
```console
foo@bar:~$ docker run -d -p 8080:8080 --name weather-station-service ilsant/froggit-hp1000sepro-service:latest
```

### ENV variable

You can opt to override this environment variable: 
* $FILELOCATIONURL 

It basically determines where files will be stored. This can be useful to change if you wish to store the files somewhere outside the default location (docker volume as example)

---
**IMPORTANT**

The weather station does send a unique key (based on the station) with each request, however the service does not check for this. It is not suggested to run this service in an open and untrusted network

---

## Docker

It's also possible to pull the docker images from docker hub. The latest tag and other version can be found here:

https://hub.docker.com/r/ilsant/froggit-hp1000sepro-service/tags


## Support

This has been tested only with the Froggit HP1000 SE Pro weather station, with different firmware versions.

## Roadmap

* Adding (more) unit tests
* Rename file extensions to distinguish more easy what kind of file it is (e.g. json instead of log)
* Add more conversion units and make this configurable
* Add Github actions to build the docker image
* Create a nicer looking UI
* Potentially: Deleting weather data files after N days to keep the storage reasonably small

## Contributing

If you wish to contribute feel free to get in contact with me, or by creating a pull request with proposed changes. I will very likely not be really active on this project but might work on some roadmap items when I have some free time.
