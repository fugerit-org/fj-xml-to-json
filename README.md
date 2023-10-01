# fj-xml-to-json

Module to convert xml to json and viceversa

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](https://github.com/fugerit-org/fj-xml-to-json/blob/master/CHANGELOG.md) 
[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-xml-to-json.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-xml-to-json)
[![license](https://img.shields.io/badge/License-Apache%20License%202.0-teal.svg)](https://opensource.org/licenses/Apache-2.0)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-xml-to-json&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-xml-to-json)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-xml-to-json&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-xml-to-json)

[![Java runtime version](https://img.shields.io/badge/run%20on-java%208+-%23113366.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java11.html)
[![Java build version](https://img.shields.io/badge/build%20on-java%2011+-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java11.html)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9.0+-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://universe.fugerit.org/src/docs/versions/maven3_9.html)

## Quickstart

Sample code : 

```
		try ( Reader reader = StreamHelper.resolveReader( path ) ) {
			Document doc = DOMIO.loadDOMDoc( reader );
			XmlToJsonHandler handler = new XmlToJsonHandler();
			JsonNode node = handler.convert( doc.getDocumentElement() );
			FileIO.writeString( node.toPrettyString() , outputFile );
		}
```

For instance a xml like this : 

```
<config default-list="list1">
	<test-list id="list1">
		<test-entry id="entry1">Entry 1</test-entry>
		<test-entry id="entry2">Entry 2</test-entry>
	</test-list>
</config>
```

Will be converted to : 

```
{
  "default-list" : "list1",
  "_t" : "config",
  "_e" : [ {
    "id" : "list1",
    "_t" : "test-list",
    "_e" : [ {
      "id" : "entry1",
      "_t" : "test-entry",
      "_v" : "Entry 1"
    }, {
      "id" : "entry2",
      "_t" : "test-entry",
      "_v" : "Entry 2"
    } ]
  } ]
}
```

See [conversion conventions](src/main/docs/xml_conversion.md) below for more info.

## Documentation

- xml to json conversion conventions (see [xml conversion](src/main/docs/xml_conversion.md))