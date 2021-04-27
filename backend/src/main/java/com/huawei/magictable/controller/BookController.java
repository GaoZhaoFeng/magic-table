package com.huawei.magictable.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.huawei.magictable.enums.ResultEnum;
import com.huawei.magictable.service.BookService;
import com.huawei.magictable.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Book APIS")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/insertBook")
    @ApiOperation("插入一本书籍信息")
    ResultVO<JSONObject> insertBookByUrl(@RequestBody @ApiParam(name="url",value="实现了从当当网爬取书籍数据的能力，例如输入http://product.dangdang.com/29201117.html",required=true)String url) {
        ResultVO<JSONObject> resultVO;
        JSONObject insertBook = bookService.insertBookByURl(url);
        ResultEnum postBookInfoSuccess = ResultEnum.POST_BOOK_INFO_SUCCESS;
        resultVO = new ResultVO<>(postBookInfoSuccess.getCode(), postBookInfoSuccess.getMessage(), insertBook);
        return resultVO;
    }
    @PostMapping("/insertBooks")
    @ApiOperation("批量插入书籍信息")
    ResultVO<JSONObject> insertBooksByUrl(@RequestBody @ApiParam(name="url",value="实现了从当当网爬取书籍数据的能力，例如输入http://category.dangdang.com/cp01.21.03.01.00.00.html",required=true)String url) {
        ResultVO<JSONObject> resultVO;
        JSONObject insertBook = bookService.insertBooksByURl(url);
        ResultEnum postBookInfoSuccess = ResultEnum.POST_BOOK_INFO_SUCCESS;
        resultVO = new ResultVO<>(postBookInfoSuccess.getCode(), postBookInfoSuccess.getMessage(), insertBook);
        return resultVO;
    }
    @GetMapping("/listBooks")
    @ApiOperation("获取书籍信息")
    ResultVO<JSONObject> getBooksData(){
        ResultVO<JSONObject> resultVO;
        JSONObject queryAllBooks = bookService.queryAllBooks();
        ResultEnum postBookInfoSuccess = ResultEnum.GET_BOOK_INFO_SUCCESS;
        resultVO = new ResultVO<>(postBookInfoSuccess.getCode(), postBookInfoSuccess.getMessage(), queryAllBooks);
        return resultVO;
    }
    @PostMapping("/listBooks")
    @ApiOperation("获取符合条件的书籍信息")
    ResultVO<JSONObject> getBookDataByFilter(@RequestBody @ApiParam(name="url",value="类似[{\"property\":\"xxx\",\"operator\":\"=\",\"value\":\"xxx\"}]",required=true)String fifter,String size,String page){

        JsonObject queryAllBooks = bookService.queryBookByFilter();
        ResultEnum postBookInfoSuccess = ResultEnum.GET_BOOK_INFO_SUCCESS;

        return  new ResultVO<JSONObject>(postBookInfoSuccess.getCode(), postBookInfoSuccess.getMessage(), queryAllBooks);;
    }
}
