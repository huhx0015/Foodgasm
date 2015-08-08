package com.huhmoon.apparely.data;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGFoodModel {

    @Expose
    public String title;
    @Expose
    public String preview;

    public FGFoodModel(String title, String preview){
        this.title = title;
        this.preview = preview;
    }

    public String getFoodName(){
        return title;
    }
    public String getFoodUrl(){
        return preview;
    }

    public static class FoodResponse {
        private ResponseData data;

        public ResponseData getResponseData() {
            return data;
        }
    }

    public static class ResponseData {
        private List<ResponseChildData> children;
        public List<ResponseChildData> getResponseChildData() {
            return children;
        }
    }

    public static class ResponseChildData {
        private ChildData data;
        public ChildData getChildData() {
            return data;
        }
    }

    public static class ChildData {

        private String title;
        private Preview preview;
        public String getTitle() {
            return title;
        }
        public Preview getPreview() {
            return preview;
        }

    }

    public static class Preview {
        private List<Image> images;
        public List<Image> getImages() {
            return images;
        }
    }

    public static class Image {
        private List<Resolutions> resolutions;
        public List<Resolutions> getResolutions() {
            return resolutions;
        }
    }
    public static class Resolutions {
        private String url;
        public String getUrl() {
            return url;
        }
    }

    public static class FoodDeserializer implements JsonDeserializer<FoodResponse> {
        @Override
        public FoodResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException
        {
            return new Gson().fromJson(je, FoodResponse.class);
        }
    }



    public static List<FGFoodModel> parseFoodModel(FoodResponse model){
        List<FGFoodModel> foodModelList = new ArrayList<FGFoodModel>();
        String title;
        String url;
        List<ResponseChildData> children = model.getResponseData().getResponseChildData();
        for(ResponseChildData child: children){
            title = child.getChildData().getTitle();
            List<Image> images = child.getChildData().getPreview().getImages();
            Image image = images.get(0);
            try {
                url = image.getResolutions().get(3).getUrl().replaceAll("amp;", "");
            }catch (Exception e) {
                url = image.getResolutions().get(2).getUrl().replaceAll("amp;", "");
            }

            foodModelList.add(new FGFoodModel(title, url));
        }
        return foodModelList;
    }
}
