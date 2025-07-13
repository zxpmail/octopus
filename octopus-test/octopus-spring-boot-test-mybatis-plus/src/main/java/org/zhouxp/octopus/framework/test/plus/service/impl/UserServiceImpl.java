package org.zhouxp.octopus.framework.test.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zhouxp.octopus.framework.test.plus.dao.UserMapper;
import org.zhouxp.octopus.framework.test.plus.model.User;
import org.zhouxp.octopus.framework.test.plus.service.UserService;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 18:28:52
 *
 * @author zhouxp
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
