db.accounts.drop();
db.permissions.drop();
db.roles.drop();
db.users.drop();
db.project.drop();

db.users.insert(
{
	"_id" : ObjectId("5d3ee668a544183c5ce3cf6f"),
	"createdDate" : ISODate("2019-07-29T12:28:24.001Z"),
	"account" : DBRef("accounts", ObjectId("5d3ee667a544183c5ce3cf6e")),
	"active" : true,
	"modifiedDate" : ISODate("2019-07-29T12:28:24.001Z"),
	"org" : "cognizant",
	"email" : "idashboard@cognizant.com",
	"lastName" : "cognizant",
	"firstName" : "idashboard",
	"password" : "$2a$10$1K2DLZkriw4i9NvfyHrUdulnX1apBMiZsDN1L3S0y7p65AXt2du82",
	"username" : "idashboard"
}
);

db.accounts.insert(
{
	"_id" : ObjectId("5d3ee667a544183c5ce3cf6e"),
	"userId" : "idashboard@cognizant.com",
	"roles" : [
		DBRef("roles", "Admin")
	],
	"projectIds" : [
	]
}
);

db.permissions.insert(
  [
    {
      "_id" : "permission.admin",
      "name" : "Admin Permission"
    },

    {
      "_id" : "user.account.view",
      "name" : "View Account"
    },
    {
      "_id" : "user.account.update",
      "name" : "View Accounts"
    },

    {
      "_id" : "permission.view",
      "name" : "View Permissions"
    },

    {
      "_id" : "role.view",
      "name" : "View Roles"
    },
    {
      "_id" : "role.create",
      "name" : "View Roles"
    },
    {
      "_id" : "role.update",
      "name" : "View Roles"
    },
    {
      "_id" : "role.delete",
      "name" : "View Roles"
    },

    {
      "_id" : "user.view",
      "name" : "View Users"
    },
    {
      "_id" : "user.create",
      "name" : "View Users"
    },
    {
      "_id" : "user.update",
      "name" : "View Users"
    },
    {
      "_id" : "user.delete",
      "name" : "View Users"
    },
	
	{
      "_id" : "team.create",
      "name" : "Create Team"
    },
    {
      "_id" : "team.update",
      "name" : "Update Team"
    },
    {
      "_id" : "team.delete",
      "name" : "Delete Team"
    },
    {
      "_id" : "team.view",
      "name" : "View Team"
    },
	
	{
      "_id" : "idashboard.create",
      "name" : "Create Dashboard"
    },
    {
      "_id" : "idashboard.update",
      "name" : "Update Dashboard"
    },
    {
      "_id" : "idashboard.delete",
      "name" : "Delete Dashboard"
    },
    {
      "_id" : "idashboard.view",
      "name" : "View Dashboard"
    },

    {
      "_id" : "idashboard.project.create",
      "name" : "Create Dashboard Project"
    },
    {
      "_id" : "idashboard.project.update",
      "name" : "Update Dashboard Project"
    },
    {
      "_id" : "idashboard.project.delete",
      "name" : "Delete Dashboard Project"
    },
    {
      "_id" : "idashboard.project.view",
      "name" : "View Dashboard Project"
    },

    {
      "_id" : "idashboard.external.data.update",
      "name" : "Upload external data"
    },

    {
      "_id" : "idashboard.datasource.create",
      "name" : "Create Dashboard Datasource"
    },
    {
      "_id" : "idashboard.datasource.update",
      "name" : "Update Dashboard Datasource"
    },
    {
      "_id" : "idashboard.datasource.delete",
      "name" : "Delete Dashboard Datasource"
    },
    {
      "_id" : "idashboard.datasource.view",
      "name" : "View Dashboard Datasource"
    },

    {
      "_id" : "idashboard.chart.create",
      "name" : "Create Dashboard Chart"
    },
    {
      "_id" : "idashboard.chart.update",
      "name" : "Update Dashboard Chart"
    },
    {
      "_id" : "idashboard.chart.delete",
      "name" : "Delete Dashboard Chart"
    },
    {
      "_id" : "idashboard.chart.view",
      "name" : "View Dashboard Chart"
    },

    {
      "_id" : "idashboard.page.create",
      "name" : "Create Dashboard Page"
    },
    {
      "_id" : "idashboard.page.update",
      "name" : "Update Dashboard Page"
    },
    {
      "_id" : "idashboard.page.delete",
      "name" : "Delete Dashboard Page"
    },
    {
      "_id" : "idashboard.page.view",
      "name" : "View Dashboard Page"
    }

  ]
);

db.roles.insert(
[
  {
    "_id": "Admin",
    "permissions": [
      {
        "_id" : "permission.admin",
        "name" : "Admin Permission"
      }
    ]
  },
  {
    "_id": "ProjectLead",
    "permissions": [
      {
        "_id": "idashboard.create",
        "name": "Create Dashboard"
      },
      {
        "_id": "idashboard.update",
        "name": "Update Dashboard"
      },
      {
        "_id": "idashboard.delete",
        "name": "Delete Dashboard"
      },
      {
        "_id": "idashboard.view",
        "name": "View Dashboard"
      },
      {
        "_id": "idashboard.project.create",
        "name": "Create Dashboard Project"
      },
      {
        "_id": "idashboard.project.update",
        "name": "Update Dashboard Project"
      },
      {
        "_id": "idashboard.project.delete",
        "name": "Delete Dashboard Project"
      },
      {
        "_id": "idashboard.project.view",
        "name": "View Dashboard Project"
      },
      {
        "_id": "idashboard.external.data.update",
        "name": "Upload external data"
      },
      {
        "_id": "idashboard.datasource.create",
        "name": "Create Dashboard Datasource"
      },
      {
        "_id": "idashboard.datasource.update",
        "name": "Update Dashboard Datasource"
      },
      {
        "_id": "idashboard.datasource.delete",
        "name": "Delete Dashboard Datasource"
      },
      {
        "_id": "idashboard.datasource.view",
        "name": "View Dashboard Datasource"
      },
      {
        "_id": "idashboard.chart.create",
        "name": "Create Dashboard Chart"
      },
      {
        "_id": "idashboard.chart.update",
        "name": "Update Dashboard Chart"
      },
      {
        "_id": "idashboard.chart.delete",
        "name": "Delete Dashboard Chart"
      },
      {
        "_id": "idashboard.chart.view",
        "name": "View Dashboard Chart"
      },
      {
        "_id": "idashboard.page.create",
        "name": "Create Dashboard Page"
      },
      {
        "_id": "idashboard.page.update",
        "name": "Update Dashboard Page"
      },
      {
        "_id": "idashboard.page.delete",
        "name": "Delete Dashboard Page"
      },
      {
        "_id": "idashboard.page.view",
        "name": "View Dashboard Page"
      },
      {
        "_id": "team.create",
        "name": "Create Team",
        "_class": "com.cognizant.leap.users.beans.Permission"
      },
      {
        "_id": "team.update",
        "name": "Update Team"
      },
      {
        "_id": "team.delete",
        "name": "Delete Team"
      },
      {
        "_id": "team.view",
        "name": "View Team"
      }
    ]
  },
  {
    "_id": "TeamMember",
    "permissions": [
      {
        "_id": "idashboard.create",
        "name": "Create Dashboard"
      },
      {
        "_id": "idashboard.update",
        "name": "Update Dashboard"
      },
      {
        "_id": "idashboard.view",
        "name": "View Dashboard"
      },
      {
        "_id": "idashboard.project.update",
        "name": "Update Dashboard Project"
      },
      {
        "_id": "idashboard.project.view",
        "name": "View Dashboard Project"
      },
      {
        "_id": "idashboard.external.data.update",
        "name": "Upload external data"
      },
      {
        "_id": "idashboard.datasource.create",
        "name": "Create Dashboard Datasource"
      },
      {
        "_id": "idashboard.datasource.update",
        "name": "Update Dashboard Datasource"
      },
      {
        "_id": "idashboard.datasource.view",
        "name": "View Dashboard Datasource"
      },
      {
        "_id": "idashboard.chart.create",
        "name": "Create Dashboard Chart"
      },
      {
        "_id": "idashboard.chart.update",
        "name": "Update Dashboard Chart"
      },
      {
        "_id": "idashboard.chart.delete",
        "name": "Delete Dashboard Chart"
      },
      {
        "_id": "idashboard.chart.view",
        "name": "View Dashboard Chart"
      },
      {
        "_id": "idashboard.page.create",
        "name": "Create Dashboard Page"
      },
      {
        "_id": "idashboard.page.update",
        "name": "Update Dashboard Page"
      },
      {
        "_id": "idashboard.page.view",
        "name": "View Dashboard Page"
      },
      {
        "_id": "team.view",
        "name": "View Team"
      }
    ]
  },
  {
    "_id": "Viewer",
    "permissions": [
      {
        "_id": "idashboard.view",
        "name": "View Dashboard"
      },
      {
        "_id": "idashboard.project.view",
        "name": "View Dashboard Project"
      },
      {
        "_id": "idashboard.datasource.view",
        "name": "View Dashboard Datasource"
      },
      {
        "_id": "idashboard.chart.view",
        "name": "View Dashboard Chart"
      },
      {
        "_id": "idashboard.page.view",
        "name": "View Dashboard Page"
      },
      {
        "_id": "team.view",
        "name": "View Team"
      }
    ]
  }
]
);




