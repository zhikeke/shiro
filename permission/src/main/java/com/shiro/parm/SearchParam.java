package com.shiro.parm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/12 10:01
 */
@Getter
@Setter
@ToString
public class SearchParam {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private String fromTime;   // yyyy-MM-dd HH:mm:ss

    private String toTime;  // yyyy-MM-dd HH:mm:ss

}
