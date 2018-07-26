package com.bluz.demo.es;

public abstract class AbstractIndexPreBuildProcessor implements IndexPreBuildProcessor{
    private IndexPreBuildProcessor successProcessor;
    private IndexPreBuildProcessor errorProcessor;

    public void setSuccessProcessor(IndexPreBuildProcessor successProcessor) {
        this.successProcessor = successProcessor;
    }

    public void setErrorProcessor(IndexPreBuildProcessor errorProcessor) {
        this.errorProcessor = errorProcessor;
    }

    public Boolean execute() throws Exception{
        Boolean result = doExecute();
        if(result && successProcessor!=null){
            successProcessor.execute();
        }else if(!result && errorProcessor!=null){
            errorProcessor.execute();
        }
        return true;
    }

    abstract Boolean doExecute() throws Exception;
}
