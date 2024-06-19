#Develper info

- Fetch collection field names

````
db.getCollection('jenkins_jobs').aggregate(
[
    {
        $project: {data: {"$objectToArray" : "$$ROOT"}}
    },
    {
        $project: {data: "$data.k" }
    },
    {
        $unwind : "$data"
    },
    {
        $group : {"_id":null, keys : {$addToSet : "$data"}}
    }
]
)
````

````
db.getCollection('jenkins_jobs').aggregate(
[
    {
        $project: {fieldType: {"$type" : "$firstBuildUrl"}}
    },
    {
        $match: {fieldType: {"$ne" : "missing"}}
    },
    {
        $group : {"_id":null, keys : {$addToSet : "$fieldType"}}
    },
    {
        $project : {keys : {$arrayElemAt : ["$keys", 0]}}
    }
]
)
````

````
db.orders.aggregate([
   {
     $lookup:
       {
         from: "inventory",
         localField: "item",
         foreignField: "sku",
         as: "inventory_docs"
       }
  }
  ,{$unwind : {path : "$inventory_docs", includeArrayIndex: "arrayIndex"}}
//   ,{$unwind : "$inventory_docs"}
,{$project : {"item":1, "price":1, "quantity":1, "instock" : "$inventory_docs.instock", "sku" : "$inventory_docs.sku", "desc" : "$inventory_docs.description"}}
  //,{$match : {"inventory_docs.sku" : "almonds"}}
])
````

````
db.createView (
   "orderDetails", // name or view
   "orders",   // source collection
   [
     {
     $lookup:
       {
         from: "inventory",
         localField: "item",
         foreignField: "sku",
         as: "inventory_docs"
       }
  }
  ,{$unwind : {path : "$inventory_docs", includeArrayIndex: "arrayIndex"}}
  ,{$project : {"item":1, "price":1, "quantity":1, "instock" : "$inventory_docs.instock", "sku" : "$inventory_docs.sku", "desc" : "$inventory_docs.description"}}
   ]
)
````


````
db.student.aggregate(
[
    {
     $lookup:
       {
         from: "parent",
         localField: "_id",
         foreignField: "sid",
         as: "parents"
       }
	},
	{
     $lookup:
       {
         from: "address",
         localField: "_id",
         foreignField: "sid",
         as: "addresses"
       }
	}
        ,{$unwind : {path : "$parents", "includeArrayIndex" : "parentArrayIndex"}}
        ,{$unwind : {path : "$addresses", "includeArrayIndex" : "addressArrayIndex"}}
        ,{$project : {"name" : "$name", "pName": "$parents.pName", "address":"$addresses.address"}}
]
)
````

- Create view using api

````json
{
  "name": "studentDetails",
  "baseCollection": {
    "name": "student",
    "fields": [
      {
        "name": "name",
        "alias": "name"
      }
    ]
  },
  "lookups": [
    {
      "name": "parent",
      "localField": "_id",
      "foreignField": "sid",
      "alias": "parents",
      "fields": [
        {
          "name": "pName",
          "alias": "pName"
        },{
          "name": "_id",
          "alias": "p_id"
        }
      ]
    },
	{
      "name": "address",
	  "localField": "_id",
      "foreignField": "sid",
      "alias": "addresses",
      "fields": [
        {
          "name": "address",
          "alias": "address"
        }
      ]
    }
  ]
}
````


````
db.student.aggregate(
[
    {
     $lookup:
       {
         from: "parent",
//          localField: "_id",
//          foreignField: "sid",
           let : {stuId : "$_id"},
         pipeline:[
                {
                    $match: {
                        $expr: {
                            $and: [
                            {$eq: ["$sid", "$$stuId"]}
                            ]
                        }
                    }
                    }
           ],
         as: "parents"
       }
	}
       ,{$unwind : {path : "$parents", "includeArrayIndex" : "parentArrayIndex"}}
        ,{$project : {"name" : "$name", "pName": "$parents.pName", "address":"$addresses.address"}}
]
)
````

````json
{
  "name": "test-view",
  "baseCollection": {
    "name": "alm_cycles",
    "fields": [
		{ "name":"endDate","alias": "endDate" },
		{ "name":"releaseId","alias": "releaseId" },
		{ "name":"releaseName","alias": "releaseName" },
		{ "name":"domainName","alias": "domainName" },
		{ "name":"cycleId","alias": "cycleId" },
		{ "name":"projectName","alias": "projectName" },
		{ "name":"cycleName","alias": "cycleName" },
		{ "name":"startDate","alias": "startDate" }
	]
  },
  "lookups": [
    {
      "name": "alm_releases",
      "fields": [
			{ "name":"releaseId",	"alias": "rReleaseId"},
			{ "name":"endDate",		"alias": "rEndDate"},
			{ "name":"releaseName",	"alias": "rReleaseName"},
			{ "name":"domainName",	"alias": "rDomainName"},
			{ "name":"projectName",	"alias": "rProjectName"},
			{ "name":"startDate",	"alias": "rStartDate"}	
		],
      "localForeignFields": [
        {
          "localField": "domainName",
          "foreignField": "domainName"
        },
		{
          "localField": "releaseName",
          "foreignField": "releaseName"
        },
		{
          "localField": "projectName",
          "foreignField": "projectName"
        }
      ],
      "alias": "releases"
    }
  ]
}
````

````json
{
  "name": "test-view",
  "baseCollection": {
    "name": "inventoryTest",
    "fields": [
		{ "name":"item","alias": "item" },
		{ "name":"qty","alias": "qty" }
	]
  },
  "lookups": [
    {
      "name": "tags",
      "fields": [
			{ "name":"tag",	"alias": "tag"},
			{ "name":"tagName",		"alias": "tagName"}	
		],
      "localForeignFields": [
        {
          "localField": "tags",
		  "opType": "in",
          "foreignField": "tag"
        }
      ],
      "alias": "tags"
    }
  ]
}
````

````
db.inventoryTest.aggregate(
[
    {
     $lookup:
       {
         from: "tags",
           let : {tags : "$tags"},
         pipeline:[
                {
                    $match: {
                        $expr: {
                            $and: [
                            {$in: ["$tag", "$$tags"]}
                            ]
                        }
                    }
                    }
           ],
         as: "tags"
       }
	}
       ,{$unwind : {path : "$tags", "includeArrayIndex" : "parentArrayIndex"}}
        ,{$project : {"qty" : "$qty", "tagName": "$tags.tagName", "tag":"$tags.tag"}}
]
)
````

- Create Collection 

````

[
	{"_id":100, "name":"ram", "dep":"IT", "sal":10000},
	{"_id":101, "name":"anji", "dep":"ADMIN", "sal":20000},
	{"_id":102, "name":"rama", "dep":"QA", "sal":30000},
	{"_id":103, "name":"abhi", "dep":"HR", "sal":40000},
	{"_id":104, "name":"ramanji", "dep":"IT", "sal":50000},
	{"_id":105, "name":"abhay", "dep":"IT", "sal":60000}
]


[
	{"_id":1100, "name":"CTSram", "dep":"IT", "sal":10000},
	{"_id":1101, "name":"CTSanji", "dep":"ADMIN", "sal":20000},
	{"_id":1102, "name":"CTSrama", "dep":"QA", "sal":30000},
	{"_id":1103, "name":"CTSabhi", "dep":"HR", "sal":40000},
	{"_id":1104, "name":"CTSramanji", "dep":"IT", "sal":50000},
	{"_id":1105, "name":"CTSabhay", "dep":"IT", "sal":60000}
]
````

- Group aggregation

````json
{
   "id":"5f22a38b4482fa7470c184f3",
   "name":"New Chart",
   "description":"new mock chart",
   "filters":[
      {
         "name":"projectName-in",
         "configs":[
            {
               "field":"projectName",
               "op":"in",
               "value":[
                  "ciqdashboard-api"
               ]
            }
         ],
         "active":null
      }
   ],
   "groupBy":[
     
   ],
   "projection":[
      "message",
      "committerName",
      "committedDate"
   ],
   "type":"table",
   "maxRecords":5,
   "aggregate": {
    "name": "SumAggregate",
    "groups": [
      {
        "name": "Group1",
		"operator" : "add",
        "aggregates": [
          {
            "field": "projectId",
            "type": "SUM",
            "filters": null,
			"operator" : "add",
            "value": 0
          },
		  {
            "field": "projectId",
            "type": "MAX",
            "filters": null,
			"operator" : "add",
            "value": 0
          },
		  {
            "field": "projectId",
            "type": "MIN",
            "filters": null,
			"operator" : "sub",
            "value": 0
          }
        ]
      },
	  {
        "name": "Group2",
		"operator" : "mul",
        "aggregates": [
          {
            "field": "projectId",
            "type": "SUM",
            "filters": null,
			"operator" : "add",
            "value": 0
          },
		  {
            "field": "projectId",
            "type": "MAX",
            "filters": null,
			"operator" : "add",
            "value": 0
          },
		  {
            "field": "projectId",
            "type": "MIN",
            "filters": null,
			"operator" : "sub",
            "value": 0
          }
        ]
      }
    ]
  },
   "sourceGroup":null,
   "itemGroup":null,
   "source":"gitlab-commits",
   "options":{
      "xLabel":"",
      "yLabel":""
   }
}
````

````json
"aggregate": {
    "name": "SumAggregate",
    "groups": [
      {
        "name": "Group1",
		"operator" : "add",
        "aggregates": [
          {
            "field": "projectId",
            "type": "SUM",
            "filters": null,
			"operator" : "add",
            "value": 0
          },
		  {
            "field": "projectId",
            "type": "MAX",
            "filters": null,
			"operator" : "add",
            "value": 0
          },
		  {
            "field": "projectName",
            "type": "DISTINCT_COUNT",
            "filters": null,
			"operator" : "add",
            "value": 0
          }
        ]
      }
    ]
  }
````
- Date filter

````json
 "filters":[
      {
         "name":"projectName-in",
         "configs":[
            {
               "field":"createdDate",
               "op":"gte",
               "value":"2020-06-20T04:59:53.102Z"
            }
         ],
         "active":null
      }
   ]
````

````
db.source_gitlab_commits.aggregate([
{ $addFields: {stringDate: { $dateToString: { format: "%Y-%m-%d", date: "$committedDate" } } } },
{ $match: {"stringDate":{$eq:"2020-07-08"}}},
{ $project:{"stringDate":0}}
])
````

````json
"filters":[
      {
         "name":"projectName-in",
         "configs":[
            {
               "field":"committedDate",
               "op":"gt",
               "value":"2020-07-29T00:00:00.000Z"
            }
         ],
         "active":null
      }
   ]
````

- Between 

````json
"filters":[
      {
         "name":"projectName-in",
         "configs":[
            {
               "field":"createdDate",
               "op":"between",
               "value":"2020-07-31T05:00:02.726Z",
               "maxValue":"2020-07-31T05:00:04.644Z"
            }
         ],
         "active":null
      }
   ]
````

- Combo Graph

````json
{
  "id": "5f22a38b4482fa7470c184f3",
  "name": "New Chart",
  "description": "new mock chart",
  "filters": [
    {
      "name": "projectName-in",
      "configs": [
        {
          "field": "projectName",
          "op": "in",
          "value": [
            "ciqdashboard-api"
          ],
          "maxValue": null
        }
      ],
      "active": null
    },
    {
      "name": "projectName-in",
      "configs": [
        {
          "field": "projectName",
          "op": "in",
          "value": [
            "ciqdashboard-api"
          ],
          "maxValue": null
        }
      ],
      "active": null
    },
    {
      "name": "projectName-in",
      "configs": [
        {
          "field": "projectName",
          "op": "in",
          "value": [
            "ciqdashboard-api"
          ],
          "maxValue": null
        }
      ],
      "active": null
    }
  ],
  "groupBy": null,
  "projection": null,
  "type": "combo",
  "aggregate": null,
  "comboGroupBy": {
    "query1": {
      "filters": [
        {
          "name": "projectName-in",
          "configs": [
            {
              "field": "projectName",
              "op": "in",
              "value": [
                "ciqdashboard-api"
              ],
              "maxValue": null
            }
          ],
          "active": null
        }
      ],
      "groupBy": [
        "projectName",
        "branchName",
        "projectId"
      ],
      "type": "pie-chart"
    },
    "query2": {
      "filters": [
        {
          "name": "projectName-in",
          "configs": [
            {
              "field": "projectName",
              "op": "in",
              "value": [
                "ciqdashboard-api"
              ],
              "maxValue": null
            }
          ],
          "active": null
        }
      ],
      "groupBy": [
        "projectName",
        "branchName",
        "projectId"
      ],
      "type": "card-chart"
    }
  },
  "sourceGroup": null,
  "itemGroup": null,
  "source": "gitlab-commits",
  "options": {
    "xLabel": "",
    "yLabel": ""
  },
  "data": [
    {
      "name": "query1",
      "value": null,
      "children": [
        {
          "name": "ciqdashboard-api",
          "value": 5,
          "children": [
            {
              "name": "master",
              "value": 5,
              "children": [
                {
                  "name": "51",
                  "value": 5,
                  "children": null,
                  "series": null
                }
              ],
              "series": null
            }
          ],
          "series": null
        }
      ],
      "series": null
    },
    {
      "name": "query2",
      "value": null,
      "children": [
        {
          "name": "ciqdashboard-api",
          "value": 5,
          "children": [
            {
              "name": "master",
              "value": 5,
              "children": [
                {
                  "name": "51",
                  "value": 5,
                  "children": null,
                  "series": null
                }
              ],
              "series": null
            }
          ],
          "series": null
        }
      ],
      "series": null
    }
  ]
}
````

- LogicalOperators

````json
{
   "id":"5f22a38b4482fa7470c184f3",
   "name":"New Chart",
   "description":"new mock chart",
   "filters":[
      {
         "name":"projectName-in",
		 "logicalOperator": "OR",
         "configs":[
            {
               "field":"projectName",
               "op":"in",
               "value":[
                  "ciqdashboard-api"
               ]
            }
         ],
         "active":null
      },
	  {
         "name":"projectId",
		 "logicalOperator": "OR",
         "configs":[
            {
               "field":"projectId",
               "op":"eq",
               "value": 50
            }
         ],
         "active":null
      }
   ],
   "groupBy":[
		"projectName",
		"branchName",
		"projectId"
   ],
   "projection":null,
   "type":"pie-chart",
   "maxRecords":5,
   "aggregate": null,
   "sourceGroup":null,
   "itemGroup":null,
   "source":"gitlab-commits",
   "options":{
      "xLabel":"",
      "yLabel":""
   }
}
````