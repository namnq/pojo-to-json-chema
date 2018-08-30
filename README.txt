  sử dụng annotation @ToJsonSchema mark ở đầu class muốn gen json schema
  param có thể sử dụng: @ToJsonSchema(path="/tmp/data/") default là folder /tmp/ascend/
  sử dụng code JSONSchema.genJsonSchema(); (import com.ascendmoney.ami.scanner.JSONSchema;) để gen jsonSchema
  
  <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
  <dependency>
	    <groupId>com.github.namnq</groupId>
	    <artifactId>pojo-to-json-chema</artifactId>
	    <version>master-SNAPSHOT</version>
	</dependency>
<dependency>
			<groupId>com.fasterxml.jackson.module</groupId>
			<artifactId>jackson-module-jsonSchema</artifactId>
			<version>2.9.6</version>
		</dependency>
  
