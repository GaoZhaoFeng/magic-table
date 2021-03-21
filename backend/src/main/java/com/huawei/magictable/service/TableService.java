/***********************************************************
 * @Description : 用户自定义表格相关的接口
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/22 00:00
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.service;

import com.alibaba.fastjson.JSONObject;

public interface TableService {
    /**
     * 根据查询条件得到分页查询结果
     *
     * @param table    表名
     * @param pageNum  第几页，从1开始
     * @param pageSize 每页多少条
     * @param filter   查询语法，参考https://www.runoob.com/mongodb/mongodb-query.html
     *                 举例如下：查询40~70岁，且姓金或鲍的人
     * {
     *     "age": {
     *         "$gt": 40,
     *         "$lt": 70
     *     },
     *     "$or": [
     *         {
     *             "driver": {
     *                 "$regex": "金"
     *             }
     *         },
     *         {
     *             "driver": {
     *                 "$regex": "鲍"
     *             }
     *         }
     *     ]
     * }
     * @return 符合条件的记录数
     */
    JSONObject getPageData(String table, int pageNum, int pageSize, JSONObject filter);
}
