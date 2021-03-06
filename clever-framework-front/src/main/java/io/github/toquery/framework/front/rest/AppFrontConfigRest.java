package io.github.toquery.framework.front.rest;

import com.google.common.collect.Maps;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/app/front")
public class AppFrontConfigRest {

    @Autowired
    private AppProperties appProperties;

    @GetMapping("/config")
    public ResponseParam frontConfig() {
        return this.frontConfigRoleModel();
    }

    @GetMapping("/config/role-model")
    public ResponseParam frontConfigRoleModel() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("roleModel", appProperties.getRoleModel());
        return ResponseParam.builder().content(map).build();
    }
}
