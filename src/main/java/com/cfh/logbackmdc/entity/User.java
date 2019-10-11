package com.cfh.logbackmdc.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName User
 * @Description:
 * @Author: bughua
 * @CreateDate: 2019/10/11 11:00
 */
@Data
public class User {
    @NotBlank(message = "用户名不能为空")
    private String userName;
}
