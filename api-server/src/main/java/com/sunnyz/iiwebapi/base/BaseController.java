package com.sunnyz.iiwebapi.base;

import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class BaseController {

    @Autowired
    SqlSession sqlSession;

    @GetMapping("/hello")
    public String index() {
        return "welcome to iiweb ~~~ ";
    }

    @RequestMapping(value = "/{namespace}/{statement}/paging")
    public AjaxResponse getEntryByParams(@PathVariable("namespace") String namespace, @PathVariable("statement") String statement,
                                         @RequestParam Map<String, Object> params, HttpServletRequest request, Object auth) {
        //region common filter

        //params.put("AAA", request.getHeader("AAAA"));
        //params.put("userId", String.valueOf(((User) auth.getPrincipal()).getId()));

        //endregion

        //region page filter

        Integer page = 1;
        if (params.containsKey("page")) {
            page = Integer.valueOf(params.get("page").toString());
        }
        Integer size = 15;
        if (params.containsKey("size")) {
            size = Integer.valueOf(params.get("size").toString());
        }
        PageRowBounds rowBounds = new PageRowBounds((page - 1) * size, size);

        //endregion

        List rows = sqlSession.selectList(namespace + "." + statement, params, rowBounds);
        // this.enumService.processEnumFields(rows);
        PageDto result = new PageDto<>(page, rowBounds.getTotal(), rows);
        return AjaxResponse.success(result);
    }

    @RequestMapping(value = "/{namespace}/{statement}/list")
    public AjaxResponse getList(@PathVariable("namespace") String namespace, @PathVariable("statement") String statement,
                                @RequestParam Map<String, Object> params, HttpServletRequest request, Object auth) {

        List rows = sqlSession.selectList(namespace + "." + statement, params);
        //this.enumService.processEnumFields(rows);
        return AjaxResponse.success(rows);
    }
}
