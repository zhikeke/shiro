package com.shiro.parm;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 10:45
 */
@Getter
@Setter
public class TestVo {


    @NotBlank
    private String msg;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Integer id;

}
