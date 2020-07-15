package com.sunnyz.iiwebapi.base;

import com.github.pagehelper.PageRowBounds;
import com.sunnyz.iiwebapi.auth.JwtUser;
import com.sunnyz.iiwebapi.util.EnumService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class BaseController {

    @Autowired
    SqlSession sqlSession;

    @Autowired
    EnumService enumService;

    @GetMapping("/hello")
    public String index() {
        return "hello iiweb! ";
    }

    @RequestMapping(value = "/{namespace}/{statement}/paging")
    public AjaxResponse getEntryByParams(@PathVariable("namespace") String namespace, @PathVariable("statement") String statement,
                                         @RequestParam Map<String, Object> params, HttpServletRequest request, Authentication auth) {
        //region common filter
        params.put("domain", request.getHeader("domain"));
        params.put("userId", ((JwtUser) auth.getPrincipal()).getUserId());
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
        enumService.processEnumFields(rows);
        PageDto result = new PageDto<>(page, rowBounds.getTotal(), rows);
        return AjaxResponse.success(result);
    }

    @RequestMapping(value = "/{namespace}/{statement}/list")
    public AjaxResponse getList(@PathVariable("namespace") String namespace, @PathVariable("statement") String statement,
                                @RequestParam Map<String, Object> params, HttpServletRequest request, Authentication auth) {
        //region common filter
        params.put("domain", request.getHeader("domain"));
        params.put("userId", ((JwtUser) auth.getPrincipal()).getUserId());
        //endregion

        List rows = sqlSession.selectList(namespace + "." + statement, params);
        enumService.processEnumFields(rows);
        return AjaxResponse.success(rows);
    }
}
