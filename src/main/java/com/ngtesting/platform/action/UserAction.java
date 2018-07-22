package com.ngtesting.platform.action;

import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.model.TstOrg;
import com.ngtesting.platform.model.TstProjectAccessHistory;
import com.ngtesting.platform.model.TstUser;
import com.ngtesting.platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = Constant.API_PATH_CLIENT + "/user")
public class UserAction {
    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    SysPrivilegeService sysPrivilegeService;
    @Autowired
    OrgRolePrivilegeService orgRolePrivilegeService;
    @Autowired
    CasePropertyService casePropertyService;
    @Autowired
    ProjectPrivilegeService projectPrivilegeService;

    @PostMapping(value = "getProfile")
    @ResponseBody
    public Map<String, Object> getProfile(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        TstUser user = (TstUser) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
        Integer orgId = user.getDefaultOrgId();
        Integer prjId = user.getDefaultPrjId();

        Integer orgIdNew = json.getInteger("orgId");
        Integer prjIdNew = json.getInteger("prjId");

        if (orgIdNew != null && orgIdNew.longValue() != orgId.longValue()) { // org不能为空
            userService.setDefaultOrg(user, orgId);
        }
        if (prjIdNew != null && (prjId == null || prjIdNew.longValue() != prjId.longValue())) { // prj可能为空
            projectService.viewPers(prjIdNew, user);
        }

        Integer userId = user.getId();

        Map<String, Boolean> sysPrivileges = sysPrivilegeService.listByUser(userId);
        ret.put("sysPrivileges", sysPrivileges);

        List<TstOrg> orgs = orgService.listByUser(userId);
        ret.put("myOrgs", orgs);

        Map<String, Boolean> orgPrivileges = orgRolePrivilegeService.listByUser(user.getId(), orgId);
        ret.put("orgPrivileges", orgPrivileges);

        Map<String,Map<String,String>> casePropertyMap = casePropertyService.getMap(orgId);
        ret.put("casePropertyMap", casePropertyMap);

        List<TstProjectAccessHistory> recentProjects = projectService.listRecentProject(orgId, userId);
        ret.put("recentProjects", recentProjects);

//        user.setDefaultPrjId(recentProjects.size() > 0?recentProjects.get(0).getPrjId(): null);

        Map<String, Boolean> prjPrivileges = projectPrivilegeService.listByUser(userId, prjId, orgId);
        ret.put("prjPrivileges", prjPrivileges);

        ret.put("profile", user);
        ret.put("code", Constant.RespCode.SUCCESS.getCode());

        return ret;
    }

    @ResponseBody
    @PostMapping("/query")
    public Object query(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    int pageSize){
        return userService.query(pageNum, pageSize);
    }

    @ResponseBody
    @RequestMapping("/get")
    public Object get(@RequestBody Integer id) {
        Map<String, Object> ret = new HashMap();
        TstUser po = userService.get(id);

        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        ret.put("data", po);
        return ret;
    }

}
