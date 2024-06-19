
# CIQDashboard API

[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg?style=flat-square)](CODE_OF_CONDUCT.md)

![JDK version= 11](https://img.shields.io/badge/JDK-11-brightgreen?style=flat-square)
![MongoDB server version= 4.0 or above](https://img.shields.io/badge/MongoDB_Server-4.0_or_above-brightgreen?style=flat-square)

[About](#about) • [Requirements](#requirements) • [Properties](#properties) • [Run Locally](#run-locally) • [Contributing](#contributing) • [Related](#related)

## About
This Project is mainly to create the charts.
A key benefit of charts is that they allow you to keep track of trends. This is critical in any business, and it helps you see where things might be growing or faltering.  
Charts are designed to assist you with monitoring and identifying trends. This allows you to keep an eye on areas of growth or concern. By doing so, you can make quick, yet informed decisions for your business.
You’ll also notice that charts and graphs help you store and organize large data quantities. This can be a huge help for companies that are unable to keep up with all their figures. By creating a chart or graph with the statistics, you’ll get a better understanding of how things are going.
CIQD dashboard project contains several types of charts there are:
<ol>
  <li>AREA charts</li>
  <li>BAR charts</li>
  <li>COMBO charts</li>
  <li>Fusion charts</li>
  <li>Gauge charts</li>
  <li>Line charts</li>
  <li>Pie charts and other type charts.</li>
</ol>
CIQD dashboard Java project is acts like glue between the user interface and database. java project contains business logics and provides the data flow in certain format.

## Requirements

- JDK 11
- MongoDB Server 4.0 or above

## Properties

Important configuration like server port, mongoDB url & also authentication properties present inside “application.properties” file which can be found under [resources](src/main/resources) directory

## Run Locally

Clone the project

```bash
  git clone https://github.com/CognizantCodeHub/ciqdashboard-api.git
```

Go to the project directory

```bash
  cd ciqdashboard-api
```

Build the project

```bash
  gradlew build
```

Start the application

```bash
  gradle bootrun
```

## Contributing

Contributions are always welcome!

See [CONTRIBUTING.md](CONTRIBUTING.md) for ways to get started.

Please adhere to this project's [code of conduct](CODE_OF_CONDUCT.md).

## Related

Here are some related projects

[CIQDashboard UI](https://github.com/CognizantCodeHub/ciqdashboard-ui)

[CIQDashboard Metric Services](https://github.com/CognizantCodeHub/ciqdashboard-metric-services)

[CIQDashboard Auth](https://github.com/CognizantCodeHub/ciqdashboard-auth)

[CIQDashboard JIRA Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-jira)

[CIQDashboard ALM Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-alm)

[CIQDashboard Jira(cloud) Collector](https://github.com/CognizantCodeHub/IPR000025_ciqdashboard-collector-jira-cloud)

[CIQDashboard Jira+Zephyr(cloud) Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-jira-zephyr-cloud)

[CIQDashboard Jira+Zephyr(On Premise) Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-jira-zephyr)

[CIQDashboard Rally Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-rally)

[CIQDashboard Jenkins Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-jenkins)

[CIQDashboard GitHub Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-github)

[CIQDashboard ServiceNow Collector](https://github.com/CognizantCodeHub/IPR000025_ciqdashboard-collector-servicenow)

## License

[To edit](https://choosealicense.com/licenses/)