# YAML support

It is possible to use this module to convert xml to json.

## Quick start

1. Add dataformat-yaml dependency

```
		<dependency>
		    <groupId>com.fasterxml.jackson.dataformat</groupId>
		    <artifactId>jackson-dataformat-yaml</artifactId>
		</dependency>
```

Note : by default this dependency is not included to avoid not necessary imports.

2. Creates an instance of XmlToJsonHandler using a YAMLMapper : 

`XmlToJsonHandler handler = new XmlToJsonHandler( new YAMLMapper() );`

For instance : 

```
		try ( Reader reader = StreamHelper.resolveReader( path ) ) {
			XmlToJsonHandler handler = new XmlToJsonHandler( new YAMLMapper() );
			JsonNode node = handler.convertToJsonNode(reader);
			handler.getMapper().writerWithDefaultPrettyPrinter().writeValue( outputFile , node );
		}
```