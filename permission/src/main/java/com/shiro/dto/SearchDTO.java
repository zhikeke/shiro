package com.shiro.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/12 10:04
 */
@Getter
@Setter
@ToString
public class SearchDTO {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private Date fromTime;   // yyyy-MM-dd HH:mm:ss

    private Date toTime;  // yyyy-MM-dd HH:mm:ss

}
