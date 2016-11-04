package com.ychp.image.tool;

import com.ychp.image.utils.HttpUtil;
import com.ychp.image.ImageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/23
 */
@Component
public class DefaultWriter extends Writer {

    @Autowired
    private ImageProperties imageProperties;

    @Override
    public String writeImage(String url, String subPath, String name) {
        Boolean isSuccess = HttpUtil.saveImage(url, imageProperties.getPath() + File.separator + subPath, name);
        if(isSuccess){
            return imageProperties.getImageUrl() + "/" + subPath + "/" +name;
        }
        return null;
    }

    @Override
    public Boolean deleteImage(String url) {
        String path = url.replace(imageProperties.getImageUrl(), imageProperties.getPath());
        File file = new File(path);
        if(file.exists()){
            return file.delete();
        }
        return false;
    }
}
