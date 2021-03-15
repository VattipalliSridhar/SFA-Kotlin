package com.apps.sfaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViolationModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Violations")
    @Expose
    private List<Violation> violations = null;

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

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }




    public class Violation {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("ViolationName")
        @Expose
        private String violationName;
        @SerializedName("ViolationId")
        @Expose
        private String violationId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getViolationName() {
            return violationName;
        }

        public void setViolationName(String violationName) {
            this.violationName = violationName;
        }

        public String getViolationId() {
            return violationId;
        }

        public void setViolationId(String violationId) {
            this.violationId = violationId;
        }


    }


}
