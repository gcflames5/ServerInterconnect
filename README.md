ServerInterconnect
==================
ServerInterconnect is a lightweight java library to handle various types of networking structures. Don't worry about the packet I/O and worry more about the more important details of your project!

[JavaDoc](http://docs.njay.net/serverinterconnect/)<br>
[Wiki](https://github.com/gcflames5/ServerInterconnect/wiki)


**How to include this library as a maven repository:**

The following XML is for you to add to your pom.xml, it is needed for Maven to find the dependency.

```xml
<repositories>
    ...
    <!-- repository for NJay dependencies -->
    <repository>
        <id>njay-repo</id>
        <url>http://repo.njay.net/content/groups/public</url>
    </repository>
    ...
</repositories>
```
```xml
<dependencies>
    ...
   <dependency>
        <groupId>net.njay</groupId>
        <artifactId>ServerInterconnect</artifactId>
        <version>x.x.x</version> <!-- Replace with desired version -->
    </dependency>
    ...
</dependencies>
```
If you're using an IDE, you may need to reimport your Maven dependencies in order for your IDE to detect the changes in your pom.xml. 

**You must call this method before any packet IO can begin:**

```java
ServerInterconnect.registerCorePackets();
```
