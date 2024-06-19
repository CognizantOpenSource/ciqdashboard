
# CIQDashboard metric services

[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg?style=flat-square)](CODE_OF_CONDUCT.md)

![JDK version= 11](https://img.shields.io/badge/JDK-11-brightgreen?style=flat-square)
![MongoDB server version= 4.0 or above](https://img.shields.io/badge/MongoDB_Server-4.0_or_above-brightgreen?style=flat-square)

[About](#about) • [Requirements](#requirements)  • [Detailed Features](#detailed-features) • [Properties](#properties) • [Run Locally](#run-locally) • [Contributing](#contributing) • [Related](#related)


## About
This project provides the analytical data of charts for various tools like Jira, ALM, Zephyr, Bots, Jenkins, Service Now, Rally, Git & Xray. Data is fetched from the mongoDB for each tool & metric calculation will be done using metric formula.

## Requirements

- JDK 11
- MongoDB Server 4.0 or above


## Detailed features

- Controller API: /api/metrics/calculate-metrics
  1. This api will calculate the metric value for a chart based on the configuration having formula & formula params that are used inside functions.
  2. This API will receive configuration information like tool name, dashboard name, project name etc
  3. Based on the configuration, it will fetch required metric config having formula’s, formula param’s, functions etc.
  4. Response contains metric calculation output.

- Calculation types:
  Calculation will be done in four ways:
  1. <b>Basic calculation</b> : This calculation is done without considering any trending or grouping.
  2. <b>Trend By</b> : This calculation is done if we want metric data for last 'n' no of weeks/months/years/days.
  3. <b>Group By</b> : This calculation is done based on grouping of a specific field considering distinct data of that field.
  4. <b>Custom functions</b> : This calculation will be done based on function name. Entire logic will be inside the code.

- Authentication will be provided using the project "ciqdashboard-auth"
- UI will get backend logic from the project "ciqdashboard-api"
- ciqdashboard-api will take metric calculation value from this project "ciqdashboard-metric-service" 
- Each project will use the data present in mongoDB. Data will fetch in certain period of time from the collector projects like JIRA, ALM etc



## Properties

In order to run this project, you must provide the following values in application.properties which can be found under [resources](src/main/resources) directory

`MongoDB Server URI`
`MongoDB credents`
`Server port`


## Run Locally

Clone the project

```bash
  git clone https://github.com/CognizantCodeHub/ciqdashboard-metric-services.git
```

Go to the project directory

```bash
  cd ciqdashboard-metric-services
```

Build the project

```bash
  gradle build
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

[CIQDashboard API](https://github.com/CognizantCodeHub/ciqdashboard-api)

[CIQDashboard Auth](https://github.com/CognizantCodeHub/ciqdashboard-auth)

[CIQDashboard Jira Collector](https://github.com/CognizantCodeHub/ciqdashboard-collector-jira)

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