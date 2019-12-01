package com.jeremie.dynamic.service.sign;

public class SignImpl implements SignInterface {

    @Override
    public String sign(String myInfo) {
        //提交资料操作
        submitInfo(myInfo);
        //审核
        audit();
        //获取结果
        return getResult();
    }

    /**
     * 提交资料操作
     *
     * @param myInfo 我的信息
     */
    private void submitInfo(String myInfo) {
        //提交资料
        System.out.println("提交资料");
    }

    /**
     * 审核
     */
    private void audit() {
        //审核
        System.out.println("提交审核");
    }

    /**
     * 获取审核结果
     *
     * @return 审核结果
     */
    private String getResult() {
        //获取结果
        System.out.println("获取结果");
        return "结果";
    }
}
