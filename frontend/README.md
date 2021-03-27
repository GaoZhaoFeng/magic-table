# 在线表格系统前端
> 专注于高定制化在线表格管理~基于SpringBoot2.4 + Vue3 + Ant Design Vue2 + Ag-Grid + MongoDB
## 开发环境
> npm务必不要用7的版本，否则npm install的时候会直接报错
+ node:10.15.0
+ npm:6.14.11

## 相关命令
+ 安装依赖：`npm install`
+ 启动项目：`npm run serve`
+ 源码编译：`npm run build`

## Todo
+ 1.实现后端分页(分页样式自己完善好)
+ 2.实现后端过滤(用户点击filter时从后端distinct出来，支持不同的过滤器类型，用户)和排序(sort，点击箭头时去后端筛选)
  > 支持用户保存表级别的筛选条件
+ 3.实现自定义表(包括表的信息和字段的信息，系统表名以`system_`开头，用户表自定义表以`user_`开头)
+ 4.实现单元格可编辑，并针对不同的字段类型弹出不同的编辑框，字段自定义支持如下类型：
  > ag-grid支持用户自己扩容字段编辑类型
  + 默认值
  + 下拉多选
  + 下拉单选
  + 富文本(用summernote)
  + 超链接(用户自定义超链接的拼接规则和显示样式)
  + 支持单元格根据不同值渲染不同的单元格背景颜色(键值对，键为单元格内容，值为对应的单元格背景色)
+ 实现表格工具栏的各个按钮的功能
+ 实现自定义报表的功能(支持根据筛选条件获取表的数据，并配置ECharts必要的字段来生成图标)
+ 实现插件功能(用户自己下载基础vue功能，基于本系统提供的对表格增删改查的API可以扩展各种插件，基于nginx的动态路由实现)
+ 实现全局搜索框和自定义显示那些列的配置按钮

## 筛选条件举例
+ `>` 大于 - `$gt`
+ `<` 小于 - `$lt`
+ `>=` 大于等于 - `$gte`
+ `<=` 小于等于 - `$lte`
+ `=`  等于 `key:value`
+ `!=` 不等于 `$ne`  
+ 逻辑与：`$and`
+ 逻辑或：`$or`
+ 正则：`$regex`
+ 排序：-1为降序；1为升序

```json
{
  "filter": {
    "age": {
      "$gt": 40,
      "$lt": 70
    },
    "$or": [
      {
        "driver": {
          "$regex": "金"
        }
      },
      {
        "driver": {
          "$regex": "鲍"
        }
      }
    ]
  },
  "sorter": {
    "driver": -1,
    "plate": 1
  }
}
```

## ag-grid使用技巧
+ 多行排序：shift+点击多个标题栏可以实现多条件排序
+ 自定义过滤器：https://www.ag-grid.com/vue-grid/component-filter/
+ 自定义单元格编辑器：https://www.ag-grid.com/vue-grid/component-cell-editor/

## 典型的filter
> ag-grid典型的过滤器，支持多条件。还没对接后端。
```json
{
    "fuel": {
        "filterType": "number",
        "operator": "AND",
        "condition1": {
            "filterType": "number",
            "type": "greaterThan",
            "filter": 500
        },
        "condition2": {
            "filterType": "number",
            "type": "lessThan",
            "filter": 1000
        }
    },
    "date": {
        "dateFrom": "2021-03-10 00:00:00",
        "dateTo": "2021-03-26 00:00:00",
        "type": "inRange",
        "filterType": "date"
    },
    "driver": {
        "filterType": "text",
        "type": "contains",
        "filter": "子"
    }
}
```
