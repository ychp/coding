package com.ychp.spider.parser;

import com.google.common.cache.AbstractCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ychp.spider.enums.ScanType;
import com.ychp.spider.model.Rule;
import com.ychp.spider.model.SpiderData;
import com.ychp.spider.utils.HttpRequest;
import com.ychp.spider.utils.ParserUtils;
import lombok.Getter;
import lombok.Setter;
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
public abstract class Parser<T extends SpiderData> {

    @Getter
    @Setter
    protected String configPrex;

    public Parser(){
        initConfigPrex();
    }

    protected abstract void initConfigPrex();

    protected Rule initRule(Map<String, String> ruleValues){
        if(ruleValues == null){
            ruleValues = Maps.newHashMap();
        }
        if(ruleValues.isEmpty()) {
            Properties prop = new Properties();
            InputStream in = Object.class.getResourceAsStream("/rules.properties");
            try {
                if (in != null) {
                    prop.load(in);
                    ruleValues.put("url", prop.getProperty("spider.rule.url-" + configPrex).trim());
                    ruleValues.put("keywords", prop.getProperty("spider.rule.keyword-" + configPrex).trim());
                    ruleValues.put("videoTag", prop.getProperty("spider.rule.video-" + configPrex).trim());
                    ruleValues.put("imageTag", prop.getProperty("spider.rule.image-" + configPrex).trim());
                    ruleValues.put("textTag", prop.getProperty("spider.rule.text-" + configPrex).trim());
                    ruleValues.put("subTag", prop.getProperty("spider.rule.subTag-" + configPrex).trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Rule rule = ParserUtils.getRule(ruleValues);
        return rule;
    }

    public List<Node> generateTag(String html, Rule rule){
        Map<String, List<Node>> tagMaps = generateMaps(html);
        return generateTagContent(tagMaps, rule);
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

    public List<Node> generateTagContent(Map<String,List<Node>> tagMaps, Rule rule){
        List<Node> result = Lists.newArrayList();
        List<String> tag = rule.getTags();
        for(String tagType : tag){
            if(tagMaps.get(tagType) != null) {
                result.addAll(tagMaps.get(tagType));
            }
        }
        return result;
    }

    public List<Map<String, Object>> getDatas(String html, Rule rule){
        List<Node> tags = generateTag(html, rule);
        List<Map<String, Object>> results = Lists.newArrayList();
        for(Node tag : tags){
            results.add(getOneResult(tag, rule));
        }
        return results;
    }

    public Map<String, Object> getOneResult(Node node, Rule rule){
        Map<String, Object> result = Maps.newHashMap();
        result.put("videos", getVideos(node, rule));
        result.put("images", getImages(node, rule));
        result.put("text", getText(node, rule));
        result.put("subTags", getSubTags(node, rule));
        result.put("dataRef", rule.getUrlRegx());
        return result;
    }

    public List<Map<String,String>> getVideos(Node node, Rule rule){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> video = rule.getVideoTag();
        if(containType(node, video)){
            Map<String,String> item = Maps.newHashMap();
            item.put("content", node.attr("source"));
            item.put("url", node.attr("source"));
            result.add(item);
        }

        return result;
    }

    public List<Map<String,String>> getImages(Node node, Rule rule){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> image = rule.getImageTag();
        if(containType(node, image)){
            Map<String,String> item = Maps.newHashMap();
            item.put("content", node.attr("src"));
            item.put("url", node.attr("src"));
            result.add(item);
        }
        return result;
    }

    public List<Map<String,String>> getText(Node node, Rule rule){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> text = rule.getTextTag();
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
                if(item.get("url").matches("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?")
                        || item.get("url").matches("(/[a-zA-Z0-9\\&%_\\./-~-]*)[?]{0,1}")) {
                    result.add(item);
                }
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

    public List<Map<String,String>> getSubTags(Node node, Rule rule){
        List<Map<String,String>> result = Lists.newArrayList();
        List<String> tags = rule.getSubTag();
        if(containType(node, tags)){
            Map<String,String> item = Maps.newHashMap();
            String textContent = node.outerHtml();
            if(containKeyWord(rule.getKeyWord(), textContent)) {
                item.put("content", textContent);
                item.put("url", node.attr("src"));
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

    public List<T> makeResult(List<Map<String, Object>> datas, Rule rule){
        List<T> result = Lists.newArrayList();
        for(Map<String, Object> item : datas){
            result.addAll(makeVideoResult((List<Map<String,String>>)item.get("videos"), rule));
            result.addAll(makeImageResult((List<Map<String,String>>)item.get("images"), rule));
            result.addAll(makeTextResult((List<Map<String,String>>)item.get("text"), rule));
            result.addAll(makeSubTagResult((List<Map<String,String>>)item.get("subTags"), rule));
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

    public List<T> makeVideoResult(List<Map<String,String>> datas, Rule rule){
        List<T> result = Lists.newArrayList();
        if(datas != null){
            T data;
            for(Map<String,String> item : datas) {
                data = (T) new SpiderData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.VIDEO.getValue());
                data.setContent(item.get("content"));
                data.setUrl(item.get("url"));
                data.setSource(rule.getUrlRegx());
                result.add(data);
            }
        }
        return result;
    }

    public List<T> makeImageResult(List<Map<String,String>> datas, Rule rule){
        List<T> result = Lists.newArrayList();
        if(datas != null){
            T data;
            for(Map<String,String> item : datas){
                data = (T) new SpiderData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.IMAGE.getValue());
                data.setContent(item.get("content"));
                data.setUrl(item.get("url"));
                data.setSource(rule.getUrlRegx());
                result.add(data);
            }
        }
        return result;
    }

    public List<T> makeTextResult(List<Map<String,String>> datas, Rule rule){
        List<T> result = Lists.newArrayList();
        if(datas != null) {
            T data;
            for (Map<String,String> item : datas) {
                data = (T) new SpiderData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.TEXT.getValue());
                data.setContent(item.get("content"));
                data.setUrl(item.get("url"));
                data.setSource(rule.getUrlRegx());
                result.add(data);
            }
        }
        return result;
    }

    public List<T> makeSubTagResult(List<Map<String,String>> datas, Rule rule){
        List<T> result = Lists.newArrayList();
        if(datas != null) {
            T data;
            for (Map<String,String> item : datas) {
                data = (T) new SpiderData();
                data.setKeyword(rule.getKeyWords());
                data.setType(ScanType.TAG.getValue());
                data.setContent(item.get("content"));
                data.setUrl(item.get("url"));
                data.setSource(rule.getUrlRegx());
                result.add(data);
            }
        }
        return result;
    }

    public List<T> parseContext(Map<String, String> ruleValues){
        Rule rule = initRule(ruleValues);
        String url = rule.getUrlRegx();
        String html = "";
        if(url.startsWith("https")){
            html = HttpRequest.getBySSL(url, "");
        }else {
            html = HttpRequest.sendGet(url, "");
        }
        html = removeUselessContent(html);
//        System.out.println(html);
        List<Map<String, Object>> datas = getDatas(html, rule);
        return makeResult(datas, rule);
    }

    public List<T> parseContext(){
        return parseContext(null);
    }

    protected String removeUselessContent(String html){
        if(!html.startsWith("<html") || !html.startsWith("<HTML")){
            if(html.indexOf("<html") != -1) {
                html = html.substring(html.indexOf("<html"));
            }
            if(html.indexOf("<HTML") != -1) {
                html = html.substring(html.indexOf("<HTML"));
            }
        }
        return html;
    }

    public abstract List<T>  spider(Map<String, String> ruleValues);

}
