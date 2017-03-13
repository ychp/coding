package com.ychp.utils.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/3/10
 */
@Data
public class InfoDto implements Serializable {

    private static final long serialVersionUID = -6846607563475594757L;
    private String access_url;
    private String authority;
    private String biz_attr;
    private Long ctime;
    private int filelen;
    private int filesize;
    private Long mtime;
    private String name;
    private String sha;
    private String source_url;
}
