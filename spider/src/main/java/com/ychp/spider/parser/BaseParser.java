package com.ychp.spider.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ychp.spider.enums.ScanType;
import com.ychp.spider.model.BaseData;
import com.ychp.spider.model.Rule;
import com.ychp.spider.utils.HttpRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/12
 */
public abstract class BaseParser<T extends BaseData> {

    @Setter
    protected Rule rule;

    @Getter
    @Setter
    protected String configPrex;

    protected List<String> defaultTag = Lists.newArrayList("div");

    protected List<String> defaultVideo = Lists.newArrayList("video", "flash", "vdo");

    protected List<String> defaultImage = Lists.newArrayList("img");

    protected List<String> defaultText = Lists.newArrayList("div");

    protected List<String> defaultSubTag = Lists.newArrayList("div");

    public BaseParser(){
        initConfigPrex();
        initRule();
    }

    protected abstract void initConfigPrex();

    protected void initRule(){
        Properties prop = new Properties();
        InputStream in = Object.class.getResourceAsStream("/rules.properties");
        try {
            if(in != null){
                prop.load(in);
                rule = new Rule();
                rule.setUrlRegx(prop.getProperty("spider.rule.url-" + configPrex).trim());
                String keyStr = prop.getProperty("spider.rule.keyword-" + configPrex);
                rule.setKeyWords(keyStr);
                if(!StringUtils.isEmpty(keyStr)){
                    List<String> tagTypes = Lists.newArrayList(keyStr.trim().split(","));
                    rule.setKeyWord(tagTypes);
                }
                String tagTypeStr = prop.getProperty("spider.rule.tagType-" + configPrex);
                if(!StringUtils.isEmpty(tagTypeStr)){
                    List<String> tagTypes = Lists.newArrayList(tagTypeStr.trim().split(","));
                    rule.setTagType(tagTypes);
                }
                String videoStr = prop.getProperty("spider.rule.video-" + configPrex);
                if(!StringUtils.isEmpty(videoStr)){
                    List<String> tagTypes = Lists.newArrayList(videoStr.trim().split(","));
                    rule.setVideoTag(tagTypes);
                }
                String imageStr = prop.getProperty("spider.rule.image-" + configPrex);
                if(!StringUtils.isEmpty(imageStr)){
                    List<String> tagTypes = Lists.newArrayList(imageStr.trim().split(","));
                    rule.setImageTag(tagTypes);
                }
                String textStr = prop.getProperty("spider.rule.text-" + configPrex);
                if(!StringUtils.isEmpty(textStr)){
                    List<String> tagTypes = Lists.newArrayList(textStr.trim().split(","));
                    rule.setTextTag(tagTypes);
                }
                String subTag = prop.getProperty("spider.rule.subTag-" + configPrex);
                if(!StringUtils.isEmpty(subTag)){
                    List<String> tagTypes = Lists.newArrayList(subTag.trim().split(","));
                    rule.setSubTag(tagTypes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Node> generateTag(String html){
        Map<String, List<Node>> tagMaps = generateMaps(html);
        return generateTagContent(tagMaps);
    }

    protected Map<String,List<Node>> generateMaps(String html){
        Map<String, List<Node>> result = Maps.newHashMap();
        List<Node> tags;
        Document doc = Jsoup.parse(html);

        List<Node> allTags = Lists.newArrayList();
        putAllTags(allTags, doc.childNodes().get(0));

        for(Node tag : allTags){
            tags = result.getOrDefault(tag.nodeName(), Lists.<Node>newArrayList());
            tags.add(tag);
            result.put(tag.nodeName(), tags);
        }

        return result;
    }

    protected void putAllTags(List<Node> allTags, Node node){
        if(node.nodeName().indexOf("root") != -1){
            for(Node child : node.childNodes()){
                putAllTags(allTags, child);
            }
        } else if(node.childNodeSize() > 0){
            allTags.add(node);
            for(Node child : node.childNodes()){
                putAllTags(allTags, child);
            }
        } else {
            allTags.add(node);
        }
    }

    public List<Node> generateTagContent(Map<String,List<Node>> tagMaps){
        List<Node> result = Lists.newArrayList();
        List<String> tag = defaultTag;
        if(!rule.getTagType().isEmpty()) {
            tag = rule.getTagType();
        }
        for(String tagType : tag){
            if(tagMaps.get(tagType) != null) {
                result.addAll(tagMaps.get(tagType));
            }
        }
        return result;
    }

    public List<Map<String, Object>> getDatas(String html){
        List<Node> tags = generateTag(html);
        List<Map<String, Object>> results = Lists.newArrayList();
        for(Node tag : tags){
            results.add(getOneResult(tag));
        }
        return results;
    }

    public Map<String, Object> getOneResult(Node node){
        Map<String, Object> result = Maps.newHashMap();
        result.put("videos", getVideos(node));
        result.put("images", getImages(node));
        result.put("text", getText(node));
        result.put("subTags", getSubTags(node));
        result.put("dataRef", rule.getUrlRegx());
        return result;
    }

    public List<Map<String,String>> getVideos(Node node){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> video = defaultVideo;
        if(!rule.getVideoTag().isEmpty()){
            video = rule.getVideoTag();
        }
        if(containType(node, video)){
            Map<String,String> item = Maps.newHashMap();
            item.put("content", node.attr("source"));
            item.put("url", node.attr("source"));
            result.add(item);
        }

        return result;
    }

    public List<Map<String,String>> getImages(Node node){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> image = defaultImage;
        if(!rule.getImageTag().isEmpty()){
            image = rule.getImageTag();
        }
        if(containType(node, image)){
            Map<String,String> item = Maps.newHashMap();
            item.put("content", node.attr("src"));
            item.put("url", node.attr("src"));
            result.add(item);
        }
        return result;
    }

    public List<Map<String,String>> getText(Node node){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> text = defaultText;
        if(!rule.getTextTag().isEmpty()){
            text = rule.getTextTag();
        }
        if(containType(node, text)){
            Map<String,String> item = Maps.newHashMap();
            String textContent = node.outerHtml();
            if(node.childNodeSize() > 0) {
                textContent = node.childNode(0).outerHtml();
            }
            if(containKeyWord(rule.getKeyWord(), textContent)){
                item.put("content", textContent);
                if (node.hasAttr("href")) {
                    item.put("url", node.attr("href"));
                }
                result.add(item);
            }
        }
        return result;
    }

    protected boolean containKeyWord(List<String> keyWord, String textContent){
        if(keyWord.isEmpty()){
            return true;
        }

        for(String key : keyWord){
            if(textContent.contains(key)){
                return true;
            }
        }
        return false;
    }

    public List<Map<String,String>> getSubTags(Node node){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> tags = defaultSubTag;
        if(!rule.getSubTag().isEmpty()){
            tags = rule.getSubTag();
        }
        if(containType(node, tags)){
            Map<String,String> item = Maps.newHashMap();
            String textContent = node.outerHtml();
            if(containKeyWord(rule.getKeyWord(), textContent)) {
                item.put("content", textContent);
                result.add(item);
            }
        }
        return result;
    }


    protected boolean containType(Node node, List<String> regs){
        String tagName;
        tagName = node.nodeName().toLowerCase();
        for (String reg : regs){
            if(tagName.matches(reg) || tagName.indexOf(reg) != -1){
                return true;
            }
        }
        return false;
    }

    public List<T> makeResult(List<Map<String, Object>> datas){
        List<T> result = Lists.newArrayList();
        for(Map<String, Object> item : datas){
            result.addAll(makeVideoResult((List<Map<String,String>>)item.get("videos")));
            result.addAll(makeImageResult((List<Map<String,String>>)item.get("images")));
            result.addAll(makeTextResult((List<Map<String,String>>)item.get("text")));
            result.addAll(makeSubTagResult((List<Map<String,String>>)item.get("subTags")));
            String dataRef = (String)item.get("dataRef");
            setDataRef(result, dataRef);
        }
        return result;
    }

    private void setDataRef(List<T> result, String dataRef) {
        for(T t : result){
            t.setDataRef(dataRef);
        }
    }

    public List<T> makeVideoResult(List<Map<String,String>> datas){
        List<T> result = Lists.newArrayList();
        if(datas != null){
            T data;
            for(Map<String,String> item : datas) {
                data = (T) new BaseData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.VIDEO.getValue());
                data.setContent(item.get("content"));
                data.setUrl(item.get("url"));
                result.add(data);
            }
        }
        return result;
    }

    public List<T> makeImageResult(List<Map<String,String>> datas){
        List<T> result = Lists.newArrayList();
        if(datas != null){
            T data;
            for(Map<String,String> item : datas){
                data = (T) new BaseData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.IMAGE.getValue());
                data.setContent(item.get("content"));
                data.setUrl(item.get("url"));
                result.add(data);
            }
        }
        return result;
    }

    public List<T> makeTextResult(List<Map<String,String>> datas){
        List<T> result = Lists.newArrayList();
        if(datas != null) {
            T data;
            for (Map<String,String> item : datas) {
                data = (T) new BaseData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.TEXT.getValue());
                data.setContent(item.get("content"));
                data.setUrl(item.get("url"));
                result.add(data);
            }
        }
        return result;
    }

    public List<T> makeSubTagResult(List<Map<String,String>> datas){
        List<T> result = Lists.newArrayList();
        if(datas != null) {
            T data;
            for (Map<String,String> item : datas) {
                data = (T) new BaseData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.TAG.getValue());
                data.setContent(item.get("content"));
                result.add(data);
            }
        }
        return result;
    }

    public List<T> parseContext(){
        String url = rule.getUrlRegx();
        if(url.startsWith("https")){
            url = "http" + url.substring(5);
        }
        String html = HttpRequest.sendGet(url, "");
        html = removeUselessContent(html);
        System.out.println(html);
        List<Map<String, Object>> datas = getDatas(html);
        return makeResult(datas);
    }

    protected String removeUselessContent(String html){
        if(!html.startsWith("<html") || !html.startsWith("<HTML")){
            if(html.indexOf("<html") != -1) {
                html = html.substring(html.indexOf("<html"));
            }
            if(html.indexOf("<HTML") != -1) {
                html = html.substring(html.indexOf("<html"));
            }
        }
        return html;
    }

    public static void main(String[] args){

    }
}
