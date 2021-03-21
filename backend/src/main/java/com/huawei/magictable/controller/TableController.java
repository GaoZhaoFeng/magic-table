/***********************************************************
 * @Description : 获取用户自定义表的数据
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/21 21:25
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.controller;

import com.alibaba.fastjson.JSONObject;
import com.huawei.magictable.enums.ResultEnum;
import com.huawei.magictable.service.TableService;
import com.huawei.magictable.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Table APIs")
@RequestMapping("/table")
public class TableController {
    @Autowired
    TableService tableService;

    @PostMapping("/page_data")
    @ApiOperation("获取用户信息")
    ResultVO<JSONObject> getPageData(@RequestParam String table, @RequestParam int pageNum,
                                     @RequestParam int pageSize, @RequestBody JSONObject filter) {
        JSONObject pageData = tableService.getPageData(table, pageNum, pageSize, filter);
        ResultEnum getPageDataSuccess = ResultEnum.GET_PAGE_DATA_SUCCESS;
        return new ResultVO<>(getPageDataSuccess.getCode(), getPageDataSuccess.getMessage(), pageData);
    }
}
