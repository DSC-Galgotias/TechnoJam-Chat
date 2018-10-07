package tech.honeysharma.techbmechat.Models;

public class Request {

    String request_type;



    public Request(){}



    public Request(String request_type){



        this.request_type = request_type;

    }





    public String getRequest_type() {

        return request_type;

    }



    public void setRequest_type(String request_type) {

        this.request_type = request_type;

    }
}
