package org.example.radiation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.radiation.commom.R;
import org.example.radiation.entity.User;
import org.example.radiation.service.UserService;
import org.example.radiation.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 移动端用户请求发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        // 获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            // 生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            // 调用腾讯云提供的短信服务API完成发送短信
            // String[] phoneNumberSet = {phone};
            // String[] templateParamSet = {code, "5"};
            // SMSUtils.sendMessage("迪斯科虾饺公众号","1713910", phoneNumberSet, templateParamSet);

            // 需要将生成的验证码保存到Sesssion
            session.setAttribute(phone, code);

            return R.success("手机验证码短信发送成功");
        }

        return R.error("短信发送失败");
    }


    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("info:{}", map.toString());

        // 获取手机号
        String phone = map.get("phone").toString();

        // 获取验证码
        String code = map.get("code").toString();

        // 从session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        // 比对页面提交验证码与Session中保存的验证码
        if(codeInSession == null || !codeInSession.equals(code)){
            // 比对失败或验证码为空
            return R.error("登录失败");
        }

        // 如果比对成功，说明登录成功
        session.removeAttribute(phone);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);

        User user = userService.getOne(queryWrapper);

        if(user == null){
            // 判断手机号是否为新用户（是否在数据库user表中），如果是则自动完成注册
            user = new User();
            user.setName(phone);
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user", user.getId());
        return R.success(user);
    }

    /**
     * 用户退出登录
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        log.info("用户登出...");
        request.getSession().removeAttribute("user");
        return R.success("登出成功");
    }
}
