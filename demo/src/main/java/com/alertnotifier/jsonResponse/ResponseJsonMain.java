package com.alertnotifier.jsonResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ResponseJsonMain {

    public String status;
    public Data data = new Data();

    @JsonIgnore
    public String errors;
}
