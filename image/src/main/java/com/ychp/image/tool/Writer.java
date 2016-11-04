package com.ychp.image.tool;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/23
 */
public abstract class Writer {

    public abstract String writeImage(String url, String subPath, String name);

    public abstract Boolean deleteImage(String url);
}
