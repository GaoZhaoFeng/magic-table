# magic-table
> 高科定制web表格

## 开发环境
+ 1.IDEA 2020.3
+ 2.MongoDB的安装：[安装教程](https://www.runoob.com/docker/docker-install-mongodb.html)
+ 3.MongoDB学习1：[菜鸟教程Mongo](https://www.runoob.com/mongodb/mongodb-tutorial.html)
+ 4.MongoDB学习2：[尚学堂Mongo](https://www.bilibili.com/video/BV1ga4y1x7te)
+ 5.MongoDB学习3：[java驱动学习](https://mongodb.github.io/mongo-java-driver/4.1/driver/)

## docker安装mongodb
### 1.启动mongo容器
```shell
docker run -itd --name mongo -p 27017:27017 mongo --auth
```
### 2.接着使用以下命令添加用户和设置密码，并且尝试连接。
```shell
docker exec -it mongo mongo admin
```
### 3.创建一个名为 admin，密码为 123456 的用户。
```shell
db.createUser({ user:'admin',pwd:'123456',roles:[ { role:'userAdminAnyDatabase', db: 'admin'},"readWriteAnyDatabase"]});
```
### 4.尝试使用上面创建的用户信息进行连接。
> db.auth('admin', '123456')

## 对应ag-grid后端实现参考
> https://github.com/ag-grid/ag-grid-server-side-oracle-example