package com.shiro.beans;

import lombok.*;

import java.util.Set;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 9:54
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String subject;

    private String message;

    private Set<String> receivers;
}
