package com.apps.sfaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatusModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("jawan_name")
    @Expose
    private String jawanName;
    @SerializedName("jawan_mobile")
    @Expose
    private String jawanMobile;
    @SerializedName("binlist")
    @Expose
    private List<Binlist> binlist = null;
    @SerializedName("TotalBins")
    @Expose
    private Integer totalBins;
    @SerializedName("TotalCleanedToilets")
    @Expose
    private Integer totalCleanedToilets;
    @SerializedName("TotalNotCleanedToilets")
    @Expose
    private Integer totalNotCleanedToilets;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJawanName() {
        return jawanName;
    }

    public void setJawanName(String jawanName) {
        this.jawanName = jawanName;
    }

    public String getJawanMobile() {
        return jawanMobile;
    }

    public void setJawanMobile(String jawanMobile) {
        this.jawanMobile = jawanMobile;
    }

    public List<Binlist> getBinlist() {
        return binlist;
    }

    public void setBinlist(List<Binlist> binlist) {
        this.binlist = binlist;
    }

    public Integer getTotalBins() {
        return totalBins;
    }

    public void setTotalBins(Integer totalBins) {
        this.totalBins = totalBins;
    }

    public Integer getTotalCleanedToilets() {
        return totalCleanedToilets;
    }

    public void setTotalCleanedToilets(Integer totalCleanedToilets) {
        this.totalCleanedToilets = totalCleanedToilets;
    }

    public Integer getTotalNotCleanedToilets() {
        return totalNotCleanedToilets;
    }

    public void setTotalNotCleanedToilets(Integer totalNotCleanedToilets) {
        this.totalNotCleanedToilets = totalNotCleanedToilets;
    }


    public class Binlist {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("bin_name")
        @Expose
        private String binName;
        @SerializedName("area")
        @Expose
        private String area;
        @SerializedName("circlename")
        @Expose
        private String circlename;
        @SerializedName("WardName")
        @Expose
        private String wardName;
        @SerializedName("datetime")
        @Expose
        private String datetime;
        @SerializedName("cleanStatus")
        @Expose
        private String cleanStatus;
        @SerializedName("selChecklist")
        @Expose
        private String selChecklist;
        @SerializedName("cat_type")
        @Expose
        private String catType;
        @SerializedName("ScanedTime")
        @Expose
        private String scanedTime;
        @SerializedName("ToiletName")
        @Expose
        private String toiletName;
        @SerializedName("isToiletCleaned")
        @Expose
        private String isToiletCleaned;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBinName() {
            return binName;
        }

        public void setBinName(String binName) {
            this.binName = binName;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCirclename() {
            return circlename;
        }

        public void setCirclename(String circlename) {
            this.circlename = circlename;
        }

        public String getWardName() {
            return wardName;
        }

        public void setWardName(String wardName) {
            this.wardName = wardName;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getCleanStatus() {
            return cleanStatus;
        }

        public void setCleanStatus(String cleanStatus) {
            this.cleanStatus = cleanStatus;
        }

        public String getSelChecklist() {
            return selChecklist;
        }

        public void setSelChecklist(String selChecklist) {
            this.selChecklist = selChecklist;
        }

        public String getCatType() {
            return catType;
        }

        public void setCatType(String catType) {
            this.catType = catType;
        }

        public String getScanedTime() {
            return scanedTime;
        }

        public void setScanedTime(String scanedTime) {
            this.scanedTime = scanedTime;
        }

        public String getToiletName() {
            return toiletName;
        }

        public void setToiletName(String toiletName) {
            this.toiletName = toiletName;
        }

        public String getIsToiletCleaned() {
            return isToiletCleaned;
        }

        public void setIsToiletCleaned(String isToiletCleaned) {
            this.isToiletCleaned = isToiletCleaned;
        }

    }


}
