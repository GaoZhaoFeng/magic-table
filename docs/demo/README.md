# ag-Grid Server-Side Oracle Example

A reference implementation showing how to perform server-side operations using Oracle with ag-Grid.

![](https://github.com/ag-grid/ag-grid-docs/blob/latest/src/oracle-server-side-operations/oracle-enterprise.png "")

For full details see: http://ag-grid.com/oracle-server-side-operations/

## Usage

- Clone the project
- run `mvn clean install`
- start with `mvn spring-boot:run`
- open browser at `localhost:9090`

## 本地安装oracle的jdbc驱动
先下载官方的ojdbc驱动：https://www.oracle.com/database/technologies/jdbc-upc-downloads.html

然后执行如下名下安装jar包
```shell
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.2 -Dpackaging=jar -Dfile=./ojdbc7.jar
```

## Docker安装oracle11
> 镜像：https://hub.docker.com/r/rohitbasu77/oracle11g

```yml
docker run -d --name oracle11g  -p 1521:1521 -p 8080:8080 rohitbasu77/oracle11g:latest
```

在描述页中可以看到一些关于连接的基本信息。所以直接使用就好。

```yaml
hostname: localhost or docker machine ip
port: 1521
sid: xe
username: system
password: oracle
# Password for SYS & SYSTEM is oracle
# Password for fareuser, searchuser, bookinguser, checkinuser is rohit123
```
SQLcl
连接oracle的方式有很多，据说官方的SQL developer做的也挺好的，这里我们使用官方的一款命令行 SQLcl