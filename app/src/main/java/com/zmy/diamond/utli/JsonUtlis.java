package com.zmy.diamond.utli;


import com.zmy.diamond.utli.bean.JsonBean_GaoDeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 在这里解析一下无法通过GSON来解析的json数据
 * Created by zhangmengyun on 2018/6/28.
 */

public class JsonUtlis {


    /**
     * 手动解析高德数据
     *
     * @param jsonStr
     * @return
     */
    public static JsonBean_GaoDeMap fromJsonGaoDe(String jsonStr) {

        JsonBean_GaoDeMap jsonBean = null;


        try {
            jsonBean = new JsonBean_GaoDeMap();
            JSONObject json = new JSONObject(jsonStr);

            jsonBean.setStatus(json.optInt("status"));
            jsonBean.setCount(json.optInt("count"));
            jsonBean.setInfo(json.optString("info"));
            jsonBean.setInfocode(json.optString("infocode"));
            JSONObject suggestionObj = json.optJSONObject("suggestion");
            JsonBean_GaoDeMap.SuggestionBean suggestion = new JsonBean_GaoDeMap.SuggestionBean();

            List<String> keywordsList = new ArrayList<>();
            JSONArray keywordsArray = suggestionObj.optJSONArray("keywords");
            for (int i = 0; i < keywordsArray.length(); i++) {
                keywordsList.add(keywordsArray.optString(i));
            }
            suggestion.setKeywords(keywordsList);

            List<String> citiesList = new ArrayList<>();
            JSONArray citiesArray = suggestionObj.optJSONArray("cities");
            for (int i = 0; i < citiesArray.length(); i++) {
                citiesList.add(citiesArray.optString(i));
            }
            suggestion.setCities(citiesList);


            jsonBean.setSuggestion(suggestion);


            List<JsonBean_GaoDeMap.PoisBean> poisBeansList = new ArrayList<>();
            JSONArray poisArray = json.optJSONArray("pois");
            for (int i = 0; i < poisArray.length(); i++) {
                JSONObject poisObj = poisArray.optJSONObject(i);
                JsonBean_GaoDeMap.PoisBean poisBean = new JsonBean_GaoDeMap.PoisBean();
                poisBean.setId(poisObj.optString("id"));
                poisBean.setName(poisObj.optString("name"));
                poisBean.setTag(poisObj.optString("tag"));
                poisBean.setType(poisObj.optString("type"));
                poisBean.setTypecode(poisObj.optString("typecode"));
                poisBean.setBiz_type(poisObj.optString("biz_type"));
                poisBean.setAddress(poisObj.optString("address"));
                poisBean.setLocation(poisObj.optString("location"));
                poisBean.setTel(poisObj.optString("tel"));
                poisBean.setPostcode(poisObj.optString("postcode"));
                poisBean.setWebsite(poisObj.optString("website"));
                poisBean.setEmail(poisObj.optString("email"));
                poisBean.setPcode(poisObj.optString("pcode"));
                poisBean.setPname(poisObj.optString("pname"));
                poisBean.setCitycode(poisObj.optString("citycode"));
                poisBean.setCityname(poisObj.optString("cityname"));
                poisBean.setAdcode(poisObj.optString("adcode"));
                poisBean.setAdname(poisObj.optString("adname"));
                poisBean.setImportance(poisObj.optString("importance"));
                poisBean.setShopid(poisObj.optString("shopid"));
                poisBean.setShopinfo(poisObj.optString("shopinfo"));
                poisBean.setPoiweight(poisObj.optString("poiweight"));
                poisBean.setGridcode(poisObj.optString("gridcode"));
                poisBean.setDistance(poisObj.optString("distance"));
                poisBean.setNavi_poiid(poisObj.optString("navi_poiid"));
                poisBean.setEntr_location(poisObj.optString("entr_location"));
                poisBean.setBusiness_area(poisObj.optString("business_area"));
                poisBean.setExit_location(poisObj.optString("exit_location"));
                poisBean.setMatch(poisObj.optString("match"));
                poisBean.setRecommend(poisObj.optString("recommend"));
                poisBean.setTimestamp(poisObj.optString("timestamp"));
                poisBean.setAlias(poisObj.optString("alias"));
                poisBean.setIndoor_map(poisObj.optString("indoor_map"));
//                poisBean.setIndoor_data(poisObj.optString("indoor_data"));
                poisBean.setGroupbuy_num(poisObj.optString("groupbuy_num"));
                poisBean.setDiscount_num(poisObj.optString("discount_num"));
//                poisBean.setBiz_ext(poisObj.optString("biz_ext"));
                poisBean.setEvent(poisObj.optString("event"));
                poisBean.setChildren(poisObj.optString("children"));
                poisBean.setPhotos(poisObj.optString("photos"));


                poisBeansList.add(poisBean);
            }

            jsonBean.setPois(poisBeansList);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonBean;

    }

}
