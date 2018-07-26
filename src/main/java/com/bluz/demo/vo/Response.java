package com.bluz.demo.vo;

public class Response {
    public static final String SUCCESS="success";
    public static final String ERROR="error";

    private String state;
    private String msg;

    public String getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public static class Builder{
        private Response response=new Response();
        public static Response.Builder newBuilder(){
            return new Response.Builder();
        }
        public Response.Builder success(){
            response.state=SUCCESS;
            return this;
        }
        public Response.Builder error(){
            response.state=ERROR;
            return this;
        }
        public Response.Builder msg(String msg){
            response.msg=msg;
            return this;
        }
        public Response build(){
            return response;
        }
    }
}
