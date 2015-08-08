package com.huhmoon.apparely.retrofitapisubmodule.models;

import java.net.URL;
import java.util.List;

/**
 * Created by ssureymoon on 8/8/15.
 */
public class FoodResponse {
    private ResponseData data;
    public ResponseData getResponseData() {
        return data;
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
}