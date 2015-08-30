/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: cv
 * $Id:  AdminController.java 2015-01-27 15:38:37 $
 */

package cn.thinkjoy.mubs.controller;

import cn.thinkjoy.cloudstack.context.CloudContextFactory;
import cn.thinkjoy.common.domain.UserDomain;
import cn.thinkjoy.common.domain.view.BizData4Page;
import cn.thinkjoy.common.managerui.controller.AbstractAdminController;
import cn.thinkjoy.common.managerui.domain.Role;
import cn.thinkjoy.common.managerui.domain.RoleUser;
import cn.thinkjoy.common.managerui.domain.User;
import cn.thinkjoy.common.managerui.service.IRoleService;
import cn.thinkjoy.common.managerui.service.IRoleUserService;
import cn.thinkjoy.common.service.IPageService;
import cn.thinkjoy.common.utils.UserContext;
import cn.thinkjoy.dto.AssignDTO;
import cn.thinkjoy.dto.AssignDetailDTO;
import cn.thinkjoy.dto.RoleDTO;
import cn.thinkjoy.dto.UserDTO;
import cn.thinkjoy.ucm.facade.IUserManagementFacade;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 注意：具体的业务系统将##mubs###替换成appName
 */


@Controller
@RequestMapping(value = "/admin/mubs")
public class UserController extends AbstractAdminController {

    @Autowired
    private IUserManagementFacade iUserManagementFacade;

    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IRoleUserService iRoleUserService;


    /**
     * 这个字段需要在
     */
    private final static String appKey = CloudContextFactory.getCloudContext().getApplicationName();
    private final static String prodcut_code = CloudContextFactory.getCloudContext().getProductCode();


    /**
     * 页面主请求
     *
     * @param request
     * @param response
     * @return 返回菜单数据、表格描述元数据、当前主描述  如本页面为org
     */
    @RequestMapping(value = "/user")
    public ModelAndView renderMainView(HttpServletRequest request, HttpServletResponse response, ModelAndView view) {


        return doRenderMainView(request, response);
    }

    /**
     * 获取所有的组织信息
     *
     * @return
     */
    @RequestMapping(value = "/users")
    @ResponseBody
    public BizData4Page findAllAdmins(HttpServletRequest request, HttpServletResponse response) {
        List<UserDTO> assignUsers = Lists.newArrayList();

        List<User> users = iUserManagementFacade.getUsersByAppName(appKey, prodcut_code);

        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setPassword(user.getPassword());
            userDTO.setUsername(user.getName());
            userDTO.setId(String.valueOf(user.getId()));
            assignUsers.add(userDTO);

        }
        BizData4Page page = new BizData4Page();
        page.setRecords(assignUsers.size());
        page.setRows(assignUsers);
        page.setTotal(assignUsers.size());

        return page;
    }


    /**
     * 重写save/update用户
     *
     * @param request
     * @param response
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/commonsave/user")
    public ModelAndView save(HttpServletRequest request,
                             HttpServletResponse response, UserDTO userDTO) {

        if (StringUtils.isBlank(userDTO.getId())
                || "_empty".equals(userDTO.getId())) {
            userDTO.setId("0");
        }

        UserDomain currentUser = UserContext.getCurrentUser();

        User user = new User();
        user.setCreateDate(System.currentTimeMillis());
        user.setCreator(currentUser.getId());
        user.setName(userDTO.getUsername());
        user.setId(Long.valueOf(userDTO.getId()));
        user.setPassword(userDTO.getPassword());
        user.setStatus(1);


        if (user.getId() > 0) {
            /**
             * 修改用户，只修改密码
             */
            iUserManagementFacade.changeUserPassword(user.getId(), "",
                    user.getPassword());
        } else {

            /**
             * 插入新用户
             */
            user.setId(0L);
            User u = iUserManagementFacade.createNewUser(userDTO.getUsername(),
                    userDTO.getPassword());
            if (u != null) {
                iUserManagementFacade.authorization(u.getId(), appKey, prodcut_code, true);
            }

        }
        return doRenderMainView(request, response);
    }

    /**
     * 重写删除用户
     *
     * @param request
     * @param response
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/commondel/user")
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response, UserDTO userDTO) {

        User user = iUserManagementFacade.getUserById(Long.valueOf(userDTO.getId()));
        iUserManagementFacade.deleteUser(Long.valueOf(userDTO.getId()));

        return doRenderMainView(request, response);
    }


    /**
     * 获取所有角色资源
     *
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "/user-getAllResources")
    @ResponseBody
    public List<RoleDTO> getAllResource(HttpServletRequest request,
                                        HttpServletResponse response) {

        String userId = request.getParameter("userId");
        List<Role> roles = iRoleService.findAll();
        List<RoleDTO> roleDTOs = Lists.newArrayList();

        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setParentResourceId(role.getId());
            roleDTO.setResourceId(role.getId());
            roleDTO.setResourceName(role.getName());

            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("roleId", role.getId());

            RoleUser roleUser = iRoleUserService.queryOne(map);
            if (roleUser != null) {
                roleDTO.setUserId(Long.valueOf(roleUser.getUserId().toString()));
            }
            roleDTOs.add(roleDTO);

        }
        return roleDTOs;

    }


    /**
     * 给某一个用户分配角色
     *
     * @param request
     * @param response
     * @param assign
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/user-assign")
    public Object assign(HttpServletRequest request,
                         HttpServletResponse response, AssignDTO assign) {
        String result = null;
        Map<String, Object> map = new HashMap<>();
        map.put("userId", assign.getObjId());

        /**
         * 先删除该用户的所有角色
         */
        iRoleUserService.deleteByCondition(map);

        if (assign != null && StringUtils.isNotBlank(assign.getResources())) {
            List<AssignDetailDTO> details = JSON.parseArray(
                    assign.getResources(), AssignDetailDTO.class);
            for (AssignDetailDTO detail : details) {
                RoleUser roleUser = new RoleUser();
                roleUser.setCreateDate(System.currentTimeMillis());
                roleUser.setCreator(UserContext.getCurrentUser().getId());
                roleUser.setRoleId(detail.getResourceId());
                roleUser.setUserId(assign.getObjId());
                iRoleUserService.insert(roleUser);
            }
            result = "分配成功";

        }

        return result;
    }


    @Override
    protected IPageService getMainService() {
        return null;
    }

    @Override
    protected String getBizSys() {
        return "mubs";
    }

    @Override
    protected String getMainObjName() {
        return "user";
    }

    @Override
    protected String getViewTitle() {
        return "用户管理";
    }

    @Override
    protected String getParentTitle() {
        return "系统管理";
    }

    @Override
    public boolean getEnableDataPerm() {
        return true;
    }
}
